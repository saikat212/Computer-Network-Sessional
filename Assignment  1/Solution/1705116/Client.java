import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        try
        {
            socket.connect(new InetSocketAddress("www.google.com",80));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Client started....");
        System.out.println(socket.getLocalAddress().getHostAddress());
        String ipAddress=socket.getLocalAddress().getHostAddress();
        NetworkConnection nc =new NetworkConnection(ipAddress,14999);

        ArrayList<String> inbox=new ArrayList<String>();


        Thread readerThread=new Thread(new Reader(nc,inbox));
        Thread writerThread = new Thread(new Writer(nc,inbox));
        readerThread.start();
        writerThread.start();
        try
        {
            readerThread.join();
            writerThread.join();
        }catch (Exception e)
        {
            System.out.println("Thread exited");
        }

    }
}
