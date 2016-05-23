package com.server;

/**
 * Created by xiaoding on 2016/5/17.
 */

import com.server.bean.ClientInformationPacket;
import com.server.bean.ServerInformationPacket;
import com.server.response.ResponseManager;
import com.server.utils.DES;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandlerIn extends ChannelInboundHandlerAdapter
{
    private String secretKey = "abcdefghijklmnopqrstuvwxyz123456";

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        ClientInformationPacket.ClientProc clientProc = (ClientInformationPacket.ClientProc) msg;

        if (clientProc.hasUser())
        {
            ClientInformationPacket.ClientProc.CUser user = clientProc.getUser();
            System.out.println(user.getUserName());
            System.out.println(user.getUserPwd());


            ServerInformationPacket.ServerProc.SUser sUser = ResponseManager.responseUser();
            int contentLen = sUser.getSerializedSize();

            final ByteBuf data = ctx.alloc().buffer(contentLen + 12); // (2)
            data.resetReaderIndex();
            data.writeInt(2);//协议类型
            data.writeInt(10001);//协议编号
            data.writeInt(contentLen);//内容长度

            data.writeBytes(sUser.toByteArray());


            ctx.writeAndFlush(data);

        } else if (clientProc.hasChatInfo())
        {
            ClientInformationPacket.ClientProc.CChatInfo chatInfo = clientProc.getChatInfo();
            System.out.println(chatInfo.getType());
            System.out.println(chatInfo.getWords());


            ServerInformationPacket.ServerProc.SChatInfo sChatInfo = ResponseManager.responseChatInfo();
            int contentLen = sChatInfo.getSerializedSize();

            final ByteBuf data = ctx.alloc().buffer(contentLen + 12); // (2)
            data.resetReaderIndex();
            data.writeInt(2);//协议类型
            data.writeInt(10002);//协议编号
            data.writeInt(contentLen);//内容长度
            data.writeBytes(sChatInfo.toByteArray());
            ctx.writeAndFlush(data);
        }
    }

    private void parseData(byte[] data, ChannelHandlerContext ctx, ByteBuf in)
    {
        in.resetReaderIndex();
        int seek = in.readerIndex();
        int proc = in.readInt();
        seek = in.readerIndex();

        if (proc == 1002)//登陆报文
        {
            int len = in.readShort();
            byte[] userNameByte = new byte[len];
            seek = in.readerIndex();
            in.readBytes(userNameByte, 0, len);

            String userName = new String(userNameByte);
            System.out.println("userName: " + userName);

            System.out.println("sexType: " + in.readByte());

            System.out.println("age: " + in.readShort());
        }

        ByteBuf time = ctx.alloc().buffer(4); //为ByteBuf分配四个字节
        int echo = 0;
        time.writeInt(echo);
        ctx.writeAndFlush(time); // (3)
    }

    public String readUTF8(ByteBuf in, byte[] data)
    {
        return null;
    }

}