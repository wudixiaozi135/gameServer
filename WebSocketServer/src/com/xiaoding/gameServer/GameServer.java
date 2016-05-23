package com.xiaoding.gameServer;

import com.xiaoding.data.NetPacket;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class GameServer extends WebSocketServer
{

    public GameServer(int port) throws UnknownHostException
    {
        super(new InetSocketAddress(port));
    }

    public GameServer(InetSocketAddress address)
    {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake)
    {
//        this.sendToAll("new connection: " + handshake.getResourceDescriptor());
//        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message)
    {
        System.out.println(message.getInt());
        System.out.println(NetPacket.readUTF8(message));


        NetPacket packet = new NetPacket();
        packet.writeUTF8("中国");
        packet.limit();

        ByteBuffer byteBuffer = packet.getByteBuffer();
        this.sendToAll(byteBuffer);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {
//        this.sendToAll(conn + " has left the room!");
        System.out.println(conn + " has left the room!");
    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {
        ex.printStackTrace();
        if (conn != null)
        {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }


    public void sendToAll(ByteBuffer byteBuf)
    {
        Collection<WebSocket> con = connections();
        synchronized (con)
        {
            for (WebSocket c : con)
            {
                c.send(byteBuf);
            }
        }
    }
}