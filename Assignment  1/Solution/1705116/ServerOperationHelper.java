import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.nio.file.Files;
import java.nio.file.*;

public class ServerOperationHelper implements Runnable {
    int MAX_BUFFER_SIZE = 20 * 1024;
    int MIN_CHUNK_SIZE = 1024;
    int MAX_CHUNK_SIZE = 10 * 1024;

    HashMap<String, Information> clientList;
    HashMap<String, ArrayList<FileInformation>> FileInfoDatabase;

    NetworkConnection nc;

    public ServerOperationHelper(HashMap<String, Information> clist, NetworkConnection nConnection, HashMap<String, ArrayList<FileInformation>> FID) {
        clientList = clist;
        nc = nConnection;
        FileInfoDatabase = FID;
    }


    @Override
    public void run() {
        SendReceive sendReceiveOperation = new SendReceive(nc);


        while (true) {


            String msg = sendReceiveOperation.ReceiveMessage();


            if (msg.toLowerCase().contains("login")) {
                String words[] = msg.split("\\$");
                String studentId = words[1];
                if (clientList.containsKey(studentId)) {
                    nc.write("Login denied");
                } else {
                    nc.write("Login Successful with ID: " + studentId);
                    Information newClientInfo = new Information(studentId, nc);
                    clientList.put(studentId, newClientInfo);

                    ArrayList<FileInformation> fileInformationArrayList = new ArrayList<FileInformation>();
                    int fileIDLastDigit = 1;


                    String path = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + studentId;
                    File f1 = new File(path);

                    if (f1.mkdir()) {
                        System.out.println("Folder Created for ID : " + studentId);
                    } else {
                        System.out.println("Folder not created . ");
                    }
                    path = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + studentId + "\\" + "private";
                    File f2 = new File(path);

                    if (f2.mkdir()) {
                        System.out.println("Private Folder Created for ID : " + studentId);
                    } else {
                        System.out.println("Private Folder not created . ");
                    }

                    path = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + studentId + "\\" + "public";
                    File f3 = new File(path);

                    if (f3.mkdir()) {
                        System.out.println("Private Folder Created for ID : " + studentId);
                    } else {
                        System.out.println("Public Folder not created . ");
                    }


                    while (true) {


                        msg = sendReceiveOperation.ReceiveMessage();
                        System.out.println(msg);

                        if (msg.toLowerCase().contains("response")) {
                            String command[] = msg.split("\\$");
                            String whoRequested = command[1];
                            String requestId = command[2];
                            String filenameAgainstRequest = command[3];

                            for (Map.Entry<String, Information> entry : clientList.entrySet()) {
                                String key = entry.getKey();
                                Information info = entry.getValue();

                                if (!(info.netConnection.socket.isClosed())) {
                                    if (key.equals(whoRequested)) {

                                        SendReceive broadcast = new SendReceive(info.netConnection);
                                        String str = "Response  message against file request\n################\n";
                                        str += "File Request Response :\nStudentId : " + studentId + "\nRequestId: " + requestId + "\nFile name : " + filenameAgainstRequest + "\n";

                                        broadcast.SendMessage(str);
                                        break;


                                    }

                                }

                            }


                            String uploadedFilename = filenameAgainstRequest;
                            String fileType = "public";



                            String fileSize_str = sendReceiveOperation.ReceiveMessage();
                            long uploadedFileSize = Long.parseLong(fileSize_str);


                            int chunkSize = (int) (MIN_CHUNK_SIZE + (MAX_CHUNK_SIZE - MIN_CHUNK_SIZE + 1) * Math.random());


                            String lastDigit = String.valueOf(fileIDLastDigit);
                            String fileID = studentId + "_" + lastDigit;
                            fileIDLastDigit++;



                            String chunkSize_str = String.valueOf(chunkSize);
                            String uploadingInfo = "upload$" + chunkSize_str + "$" + uploadedFilename;

                            if (true) {
                                sendReceiveOperation.SendMessage(uploadingInfo);
                                sendReceiveOperation.SendMessage("File Uploading Confirmation Message : Success");
                            } else {
                                sendReceiveOperation.SendMessage("File Uploading Confirmation Message : Failed");
                            }




                            ReceiveFileOperation receiveFileOperation = new ReceiveFileOperation(uploadedFilename, nc, chunkSize, uploadedFileSize);

                            if (receiveFileOperation.ReceiveFile()) {
                                FileInformation fileInformation = new FileInformation(fileID, uploadedFilename, fileType);
                                fileInformationArrayList.add(fileInformation);
                                FileInfoDatabase.put(studentId, fileInformationArrayList);

                                String sourcePath = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + uploadedFilename;
                                String targetPath = null;
                                if (fileType.toLowerCase().contains("private")) {
                                    targetPath = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + studentId + "\\" + "private" + "\\" + uploadedFilename;
                                } else if (fileType.toLowerCase().contains("public")) {
                                    targetPath = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + studentId + "\\" + "public" + "\\" + uploadedFilename;
                                }


                                Path temp = null;
                                try {
                                    temp = Files.move(Paths.get(sourcePath), Paths.get(targetPath));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (temp != null) {
                                    System.out.println("File renamed and moved successfully");
                                } else {
                                    System.out.println("Failed to move the file");
                                }
                            } else {
                                System.out.println("File Transmission Cancel");
                            }


                        }

                        if (msg.toLowerCase().contains("request")) {
                            String command[] = msg.split("\\$");
                            String requestId = command[1];
                            String fileDescription = command[2];


                            for (Map.Entry<String, Information> entry : clientList.entrySet()) {
                                String key = entry.getKey();
                                Information info = entry.getValue();

                                if (!(info.netConnection.socket.isClosed())) {
                                    if (key.equals(studentId)) {

                                    } else {

                                        SendReceive broadcast = new SendReceive(info.netConnection);
                                        String str = "Unread inbox message\n################\n";
                                        str += "File Request :\nStudentId : " + studentId + "\nrequestId: " + requestId + "\nfile description: " + fileDescription + "\n";
                                        broadcast.SendMessage("request"); //don"t erase
                                        broadcast.SendMessage(str);
                                    }


                                }


                            }


                        }

                        if (msg.toLowerCase().contains("download")) {
                            String command[] = msg.split("\\$");
                            String whichStudentId = "Id not found";
                            String whichFileName = "filename not found";
                            String whichFileType = "filetype not found";
                            String whichFileId = command[1];
                            boolean isFind = false;


                            for (Map.Entry<String, ArrayList<FileInformation>> entry : FileInfoDatabase.entrySet()) {
                                if (!(isFind)) {
                                    String key = entry.getKey();
                                    ArrayList<FileInformation> arr = entry.getValue();

                                    for (FileInformation FI : arr) {

                                        if (FI.fileID.toLowerCase().contains(whichFileId)) {
                                            whichStudentId = key;
                                            whichFileName = FI.fileName;
                                            whichFileType = FI.fileType;
                                            isFind = true;
                                            break;
                                        }

                                    }
                                } else {
                                    break;
                                }

                            }
                            String csz = String.valueOf(MAX_BUFFER_SIZE);


                            String pathname = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + whichStudentId + "\\" + whichFileType + "\\" + whichFileName;

                            File fx = new File(pathname);
                            long fsz = fx.length();
                            String fsz_str = String.valueOf(fsz);

                            String str = whichFileName + "$" + csz + "$" + fsz_str + "$" + whichStudentId + "$" + "download";
                            sendReceiveOperation.SendMessage(str);



                            SendDownloadedFileOperation sendDownloadedFileOperation = new SendDownloadedFileOperation(pathname, nc, MAX_BUFFER_SIZE);
                            sendDownloadedFileOperation.SendDownloadedFile();




                        }

                        if (msg.toLowerCase().contains("upload")) {
                            String command[] = msg.split("\\$");
                            String uploadedFilename = command[1];
                            String fileType = command[2];

                            System.out.println("filename: " + uploadedFilename);


                            String fileSize_str = sendReceiveOperation.ReceiveMessage();
                            long uploadedFileSize = Long.parseLong(fileSize_str);
                            System.out.println("uploadedFileSize" + fileSize_str);
                            System.out.println(uploadedFileSize);


                            int chunkSize = (int) (MIN_CHUNK_SIZE + (MAX_CHUNK_SIZE - MIN_CHUNK_SIZE + 1) * Math.random());
                            System.out.println("chunksize : " + chunkSize);

                            String lastDigit = String.valueOf(fileIDLastDigit);
                            String fileID = studentId + "_" + lastDigit;
                            fileIDLastDigit++;


                            String chunkSize_str = String.valueOf(chunkSize);
                            String uploadingInfo = "upload$" + chunkSize_str + "$" + uploadedFilename;

                            if (true) {
                                sendReceiveOperation.SendMessage(uploadingInfo);
                                sendReceiveOperation.SendMessage("File Uploading Confirmation Message : Success");
                            } else {
                                sendReceiveOperation.SendMessage("File Uploading Confirmation Message : Failed");
                            }




                            ReceiveFileOperation receiveFileOperation = new ReceiveFileOperation(uploadedFilename, nc, chunkSize, uploadedFileSize);

                            if (receiveFileOperation.ReceiveFile()) {
                                FileInformation fileInformation = new FileInformation(fileID, uploadedFilename, fileType);
                                fileInformationArrayList.add(fileInformation);
                                FileInfoDatabase.put(studentId, fileInformationArrayList);

                                String sourcePath = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + uploadedFilename;
                                String targetPath = null;
                                if (fileType.toLowerCase().contains("private")) {
                                    targetPath = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + studentId + "\\" + "private" + "\\" + uploadedFilename;
                                } else if (fileType.toLowerCase().contains("public")) {
                                    targetPath = "C:\\Users\\SAIKAT\\IdeaProjects\\FileManager" + "\\" + studentId + "\\" + "public" + "\\" + uploadedFilename;
                                }


                                Path temp = null;
                                try {
                                    temp = Files.move(Paths.get(sourcePath), Paths.get(targetPath));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (temp != null) {
                                    System.out.println("File renamed and moved successfully");
                                } else {
                                    System.out.println("Failed to move the file");
                                }
                            } else {
                                System.out.println("File Transmission Cancel");
                            }


                        }

                        if (msg.toLowerCase().contains("view myfile")) {
                            String fileList = new String("(Student ID : " + studentId + ") all files list..\n" + "####################\n");

                            for (Map.Entry<String, ArrayList<FileInformation>> entry : FileInfoDatabase.entrySet()) {
                                String key = entry.getKey();

                                if (key.toLowerCase().contains(studentId)) {
                                    ArrayList<FileInformation> arr = entry.getValue();
                                    for (FileInformation FI : arr) {
                                        fileList = fileList + FI.fileID + "  " + FI.fileName + "  " + FI.fileType + "\n";
                                    }
                                }


                            }

                            System.out.println(fileList);
                            System.out.println("server file list ");
                            sendReceiveOperation.SendMessage(fileList);
                        }

                        if (msg.toLowerCase().contains("view othersfile")) {
                            String fileList = new String("All students all public  files list..\n" + "####################\n");
                            fileList = fileList + "StudentID --- FileID --- FileName --- FileType\n";

                            for (Map.Entry<String, ArrayList<FileInformation>> entry : FileInfoDatabase.entrySet()) {
                                String key = entry.getKey();

                                if (!(key.toLowerCase().contains(studentId))) {
                                    ArrayList<FileInformation> arr = entry.getValue();
                                    for (FileInformation FI : arr) {
                                        if (FI.fileType.equalsIgnoreCase("public")) {
                                            fileList = fileList + key + "  " + FI.fileID + "  " + FI.fileName + "  " + FI.fileType + "\n";
                                        }

                                    }
                                }


                            }

                            System.out.println(fileList);
                            System.out.println("server file list ");
                            sendReceiveOperation.SendMessage(fileList);
                        }


                        if (msg.toLowerCase().contains("list lookup")) {
                            String listMsg = new String("Lists of all clients..\n");
                            listMsg = new String(listMsg + "online : \n");
                            for (Map.Entry<String, Information> entry : clientList.entrySet()) {
                                String key = entry.getKey();
                                Information info = entry.getValue();

                                if (!(info.netConnection.socket.isClosed())) {
                                    listMsg = new String(listMsg + key + "\n");

                                }


                            }
                            listMsg = new String(listMsg + "offline : \n");
                            for (Map.Entry<String, Information> entry : clientList.entrySet()) {
                                String key = entry.getKey();
                                Information info = entry.getValue();

                                if (info.netConnection.socket.isClosed()) {
                                    listMsg = new String(listMsg + key + "\n");

                                }


                            }

                            sendReceiveOperation.SendMessage(listMsg);


                        }

                        if (msg.toLowerCase().contains("logout")) {
                            String s = "logout";

                            sendReceiveOperation.SendMessage(s);
                            Thread t = new Thread();
                            try {
                                t.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try {
                                newClientInfo.netConnection.socket.close();
                                clientList.replace(studentId, newClientInfo);
                                return;

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }


                }


            } else {
                System.out.println("you should login at first..");
            }


        }
    }
}

