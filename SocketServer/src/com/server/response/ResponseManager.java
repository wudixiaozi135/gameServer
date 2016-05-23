package com.server.response;

import com.server.bean.ServerInformationPacket;

/**
 * Created by xiaoding on 2016/5/18.
 */
public class ResponseManager
{
    /*
    * 响应聊天信息
    * */
    public static ServerInformationPacket.ServerProc.SChatInfo responseChatInfo()
    {
        ServerInformationPacket.ServerProc.SChatInfo proc = ServerInformationPacket.ServerProc.SChatInfo.newBuilder()
                .setResult("0")
                .setType("1")
                .setWords("server")
                .build();
        return proc;
    }

    /*
    * 响应用户信息
    * */
    public static ServerInformationPacket.ServerProc.SUser responseUser()
    {
        ServerInformationPacket.ServerProc.SUser proc = ServerInformationPacket.ServerProc.SUser.newBuilder()
                .setResult("0")
                .build();
        return proc;
    }
}
