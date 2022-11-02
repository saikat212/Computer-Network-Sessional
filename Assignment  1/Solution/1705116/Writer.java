import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Writer implements Runnable {
    public  NetworkConnection netConnection;

    ArrayList<String> inboxMessage;

    public Writer(NetworkConnection nc, ArrayList<String> inb)
    {

        netConnection=nc;
        inboxMessage=inb;

    }


    @Override
    public void run() {

        SendReceive sendReceiveOperation=new SendReceive(netConnection);


        while (true)
        {
            String allCommand="############################################\n1. login$uniqueID (for login)\n2. list lookup (to see connected all)\n3. view myfile\n4. view othersfile\n5. upload$fileName$fileType\n6. download$fileID\n7. request$requestId$Description\n8.response$WhorequestId$requestId$fileName\n9. view message\n10. logout\n##########################################\n";
            System.out.println(allCommand);





            Scanner in =new Scanner(System.in);

            String s=in.nextLine();

            if(s.toLowerCase().contains("response"))
            {
                 //response$whichStuentId$requestId$filename
                sendReceiveOperation.SendMessage(s);

                String words[] =s.split("\\$");
                String fname=words[3];




                String path="C:\\Users\\SAIKAT\\IdeaProjects\\FileManager\\src"+"\\"+fname;


                File uploadedFile =new File(path);
                long fileSize=uploadedFile.length();


                System.out.println("File Uploading Request....");
                String str=String.valueOf(fileSize);


                sendReceiveOperation.SendMessage(str);


            }
            if(s.toLowerCase().contains("request"))
            {
                sendReceiveOperation.SendMessage(s); // request$requestId$description



            }
            if(s.toLowerCase().contains("view message"))
            {

                if(inboxMessage.isEmpty())
                {
                    System.out.println("No unread message ");
                }
                else
                {
                    for(int i=0;i<inboxMessage.size();i++)
                    {
                        System.out.println(inboxMessage.get(i));

                    }
                    for(int i=0;i<inboxMessage.size();i++)
                    {
                        inboxMessage.remove(i);

                    }
                }


            }

            if(s.toLowerCase().contains("logout"))
            {
                sendReceiveOperation.SendMessage(s);


            }


            if(s.toLowerCase().contains("list lookup"))
            {
                sendReceiveOperation.SendMessage(s);


            }

            if(s.toLowerCase().contains("download"))
            {
                sendReceiveOperation.SendMessage(s);  // download$fileID

            }
            if(s.toLowerCase().contains("view othersfile"))
            {
                sendReceiveOperation.SendMessage(s);

            }

            if(s.toLowerCase().contains("view myfile"))
            {
                sendReceiveOperation.SendMessage(s);

            }

            if(s.toLowerCase().contains("login"))
            {
                sendReceiveOperation.SendMessage(s);

            }

            if(s.toLowerCase().contains("upload"))
            {

                String words[] =s.split("\\$");
                String path="C:\\Users\\SAIKAT\\IdeaProjects\\FileManager\\src"+"\\"+words[1];
                String filename=words[1];

                File uploadedFile =new File(path);
                long fileSize=uploadedFile.length();



                System.out.println("File Uploading Request....");
                String str=String.valueOf(fileSize);

                sendReceiveOperation.SendMessage(s);

                sendReceiveOperation.SendMessage(str);


            }

            else
            {


            }


        }

    }
}
