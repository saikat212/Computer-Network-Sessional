import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(14999);
        System.out.println("Server Socket Created...");
        System.out.println(InetAddress.getLocalHost());

        HashMap<String, Information> clientList = new HashMap<String, Information>();

        HashMap<String, ArrayList<FileInformation>> FileInfoDatabase=new HashMap<String,ArrayList<FileInformation>>();
        while (true) {
            Socket socket = serverSocket.accept();
            NetworkConnection nc = new NetworkConnection(socket);
            new Thread(new ServerOperationHelper(clientList,nc,FileInfoDatabase)).start();
        }

    }
}

