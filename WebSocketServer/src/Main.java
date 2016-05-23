import com.xiaoding.gameServer.GameServer;
import org.java_websocket.WebSocketImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by xiaoding on 2016/5/20.
 */

public class Main
{
    public static void main(String[] args) throws Exception
    {
        WebSocketImpl.DEBUG = false;
        int port = 8887; // 843 flash policy port
        GameServer s = new GameServer(port);
        s.start();


        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        {
            String in = sysin.readLine();
            if (in.equals("exit"))
            {
                s.stop();
                break;
            } else if (in.equals("restart"))
            {
                s.stop();
                s.start();
                break;
            }
        }
    }
}
