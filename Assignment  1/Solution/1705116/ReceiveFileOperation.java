import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiveFileOperation {
    String receivedFileName;
    NetworkConnection netConnection;
    int chunckSize;
    long uploadFileSize;
    SendReceive sendReceiveOperation;
    int MAX_BUFFER_SIZE = 20 * 1024;

    public ReceiveFileOperation(String rf,NetworkConnection nc,int cz,long ufs)
    {
        receivedFileName=rf;
        netConnection=nc;
        chunckSize=cz;
        uploadFileSize=ufs;
        sendReceiveOperation=new SendReceive(netConnection);

    }

    public boolean ReceiveFile()
    {
        int bytes =0;
        long size=uploadFileSize;
        FileOutputStream fileOutputStream=null;
        int cc=0;
        long totalReceived=0;
        int partialReceive=0;

        try {
            fileOutputStream = new FileOutputStream(receivedFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        byte[] buffer = new byte[MAX_BUFFER_SIZE];

        while (true)
        {
            if(netConnection.socket.isConnected())
            {
                try {


                    //n=chunksize % 1024
                    //x=(n)*1024
                    //rest= 1*(chunksize - x)
                    //




                    int value= (int)Math.min(buffer.length,size);

                    if (size > 0 && (bytes = netConnection.ois.read(buffer,0,value)) != -1)
                    {


                        fileOutputStream.write(buffer,0,bytes);



                        size -= bytes;
                        totalReceived+=bytes;

                        partialReceive+=bytes;






                        if(partialReceive==chunckSize)
                        {
                            cc++;
                            partialReceive=0;

                            String str =String.valueOf(cc);
                            String ts=String.valueOf(totalReceived);
                            String us=String.valueOf(uploadFileSize);
                            sendReceiveOperation.SendMessage("Successfully Received Chunk : "+str+" : (Received/FileSize)::"+ts+"/"+us+"\n");

                        }
                       // if(sendReceiveOperation.ReceiveMessage().equalsIgnoreCase("timeout"))
                        //{
                          //  System.out.println("Transmission Failed for Timeout");
                        //}


                    }
                    else
                    {

                        if(totalReceived==uploadFileSize)

                        {
                            String str =String.valueOf(cc+1);
                            String ts=String.valueOf(totalReceived);
                            String us=String.valueOf(uploadFileSize);
                            sendReceiveOperation.SendMessage("Successfully Received Last Chunk : "+str+" : (Received/FileSize)::"+ts+"/"+us+"\n");
                            sendReceiveOperation.SendMessage("Successfully File Uploaded "+"\n");
                        }
                        else
                        {
                            sendReceiveOperation.SendMessage("Error message: Failure to Upload File \n");
                            //delete chunk
                            return false;
                        }
                        break;

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                return false;

            }

        }


        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return  true;

    }
}
