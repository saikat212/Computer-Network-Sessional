import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SendFileOperation  {
    String path;
    NetworkConnection netConnection;
    int chunckSize;
    SendReceive sendReceiveOperation;

    public SendFileOperation(String pathname,NetworkConnection nc,int cz)
    {
        path=pathname;
        netConnection=nc;
        chunckSize=cz;
        sendReceiveOperation=new SendReceive(netConnection);

    }

    public void  SendFile()
    {

        int bytes =0;
        File file =new File(path);
        String acknowledgementMessage;



        //break file into chunks

        byte[] buffer=new byte[chunckSize];


        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true)
        {
            if(netConnection.socket.isConnected())
            {
                try {
                    //System.out.println("bytes: ");
                    // System.out.println(bytes);
                    if (((bytes = fileInputStream.read(buffer))!=-1))
                    {

                        netConnection.oos.write(buffer,0,bytes);
                        netConnection.oos.flush();

                        acknowledgementMessage=sendReceiveOperation.ReceiveMessage();

                        //netConnection.socket.setSoTimeout(30000);

                       // if(acknowledgementMessage.equals(null))
                       // {
                            //System.out.println("Server response time out error");
                        //}
                        System.out.println(acknowledgementMessage);



                    }
                    else
                    {

                        acknowledgementMessage=sendReceiveOperation.ReceiveMessage();
                        System.out.println(acknowledgementMessage);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("File Transmission Cancel");
            }

        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
