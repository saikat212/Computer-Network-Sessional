import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

public class Reader implements Runnable {

    public NetworkConnection netConnection;
    SendReceive sendReceiveOperation;
    String msg;
    ArrayList<String> inboxMessage;

    public Reader(NetworkConnection nc, ArrayList<String> inb) {
        netConnection = nc;
        sendReceiveOperation=new SendReceive(netConnection);
        inboxMessage=inb;

    }


    @Override
    public void run() {
        while (true) {

            msg =sendReceiveOperation.ReceiveMessage();
            System.out.println(msg);
            String command[] =msg.split("\\$");

            if(msg.equalsIgnoreCase("request"))
            {
               String str =sendReceiveOperation.ReceiveMessage();
               inboxMessage.add(str);
            }
            if(msg.equalsIgnoreCase("logout"))
            {

                System.out.println(msg);
                try {
                    netConnection.socket.close();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(command[0].equalsIgnoreCase("upload"))
            {
                String uploadingInfo=msg;

                String chuckSize_str=command[1];
                String uploadedFileName=command[2];


                int chunksize=Integer.parseInt(chuckSize_str);
                String uploadConfirmation=sendReceiveOperation.ReceiveMessage();
                System.out.println(uploadConfirmation);
                String path="C:\\Users\\SAIKAT\\IdeaProjects\\FileManager\\src"+"\\"+uploadedFileName;

                if(uploadConfirmation.equalsIgnoreCase("File Uploading Confirmation Message : Success"))
                {

                    SendFileOperation sendFileOperation=new SendFileOperation(path,netConnection,chunksize);
                    sendFileOperation.SendFile();

                }
                else if(uploadConfirmation.toLowerCase().contains("File Uploading Confirmation Message : Failed"))
                {

                }
            }
            if(msg.toLowerCase().contains("download"))
            {


                String uploadedFilename=command[0];
                String csz_str=command[1];
                int csz =Integer.parseInt(csz_str);
                String fsz_str=command[2];
                long fsz=Long.parseLong(fsz_str);
                String sId=command[3];





                //String str = sendReceiveOperation.ReceiveMessage();
                //System.out.println(str);

                //ReceiveFileOperation receiveFileOperation=new ReceiveFileOperation(uploadedFilename,netConnection,csz,fsz);
                ReceiveDownloadedFileOperation receiveDownloadedFileOperation=new ReceiveDownloadedFileOperation(uploadedFilename,netConnection,csz,fsz);

                if(receiveDownloadedFileOperation.ReceiveDownloadedFile())
                {
                    //FileInformation fileInformation=new FileInformation(fileID,uploadedFilename,fileType);
                    //fileInformationArrayList.add(fileInformation);
                    //FileInfoDatabase.put(studentId,fileInformationArrayList);

                    String sourcePath ="C:\\Users\\SAIKAT\\IdeaProjects\\FileManager"+"\\"+uploadedFilename;
                    String targetPath =null;

                    targetPath = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager"+"\\DownloadByStudent"+"\\"+uploadedFilename;



                    Path temp = null;
                    try {
                        temp = Files.move(Paths.get(sourcePath), Paths.get(targetPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(temp != null)
                    {
                        System.out.println("File Downloaded successfully");
                    }
                    else
                    {
                        System.out.println("Failed to move the file");
                    }
                }
                else
                {
                    System.out.println("File Transmission Cancel");
                }

            }




            //Object obj=netConnection.read();
            //msg = (String)obj;
            //System.out.println(msg);
            //if (msg.toLowerCase().contains("logout"))
            //{
            // try {
            //  netConnection.socket.close();
            //  return;
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            //}
        }
    }
}

