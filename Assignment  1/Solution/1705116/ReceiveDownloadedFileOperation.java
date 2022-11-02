import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiveDownloadedFileOperation {
    String receivedFileName;
    NetworkConnection netConnection;
    int chunckSize;
    long uploadFileSize;
    SendReceive sendReceiveOperation;

    public ReceiveDownloadedFileOperation(String rf, NetworkConnection nc, int cz, long ufs)
    {
        receivedFileName=rf;
        netConnection=nc;
        chunckSize=cz;
        uploadFileSize=ufs;
        sendReceiveOperation=new SendReceive(netConnection);

    }

    public boolean ReceiveDownloadedFile()
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

        // try {
        // size = netConnection.ois.readLong();
        //} catch (IOException e) {
        // e.printStackTrace();
        //  }
        //size = 2*1024;

        byte[] buffer = new byte[chunckSize];

        // System.out.println("filesize:");
        //System.out.println(uploadFileSize);

        //System.out.println("chunksize:");
        //System.out.println(chunckSize);
        while (true)
        {
            if(netConnection.socket.isConnected())
            {
                try {

                    // System.out.println("buffer_length:");
                    //System.out.println(buffer.length);

                    //System.out.println("size:");
                    // System.out.println(size);

                    // System.out.println("bytes: ");
                    // System.out.println(bytes);
                    int value= (int)Math.min(buffer.length,size);
                    //System.out.println("value: ");
                    // System.out.println(value);

                    if (size > 0 && (bytes = netConnection.ois.read(buffer,0,value)) != -1)
                    {


                        fileOutputStream.write(buffer,0,bytes);

                        size -= bytes;
                        totalReceived+=bytes;

                        partialReceive+=bytes;



                        //System.out.println("TotalReceived: ");
                        //System.out.println(totalReceived);



                        if(partialReceive==chunckSize)
                        {
                            cc++;
                            partialReceive=0;

                           // String str =String.valueOf(cc);
                            //String ts=String.valueOf(totalReceived);
                           // String us=String.valueOf(uploadFileSize);
                            //sendReceiveOperation.SendMessage("Successfully Received Chunk : "+str+" : (Received/FileSize)::"+ts+"/"+us+"\n");

                        }
                        // if get timeout then delete all chunck


                    }
                    else
                    {
                        // System.out.println("yes");

                        if(totalReceived==uploadFileSize)

                        {
                            String str =String.valueOf(cc+1);
                            String ts=String.valueOf(totalReceived);
                            String us=String.valueOf(uploadFileSize);
                            //sendReceiveOperation.SendMessage("Successfully Received Last Chunk : "+str+" : (Received/FileSize)::"+ts+"/"+us+"\n");
                            //sendReceiveOperation.SendMessage("Successfully File Uploaded "+"\n");
                        }
                        else
                        {
                            //sendReceiveOperation.SendMessage("Error message: Failure to Upload File \n");
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
