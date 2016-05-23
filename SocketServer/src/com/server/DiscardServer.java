package com.server;

import com.server.bean.ClientInformationPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * Discards any incoming data.
 */
public class DiscardServer
{
    private int port;
    private String host;

    public DiscardServer(String host, int port)
    {
        this.port = port;
        this.host = host;
    }

    public void run() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            //构造函数传递要解码成的类型
                            ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(ClientInformationPacket.ClientProc.getDefaultInstance()));
                            //编码用
                            ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());

                            ch.pipeline().addLast(new DiscardServerHandlerIn());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(host, port).sync(); // (7)
            f.channel().closeFuture().sync();
        } finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}