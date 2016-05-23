import com.server.DiscardServer;

/**
 * Created by xiaoding on 2016/5/17.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            new DiscardServer("192.168.1.92", 54321).run();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
