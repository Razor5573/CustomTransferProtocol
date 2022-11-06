import exceptions.FileCheckSumException;
import exceptions.FileNameSizeException;
import exceptions.FileSizeException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Protocol {
    public static void sendFile(Socket socket, File file) {
        String fileName = file.getName();

        if(fileName.getBytes().length > 4096){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new FileNameSizeException("File can not be named that HUGE name!");
        }

        long fileNameSize = fileName.length();
        byte[] byteFileNameSize = new byte[Long.BYTES];
        int byteFileNameSizeLength = byteFileNameSize.length;

        for (int i = 0; i < byteFileNameSizeLength; i++) {                            //long to byte
            byteFileNameSize[byteFileNameSizeLength - i - 1] = (byte) (fileNameSize & 0xFF);
            fileNameSize >>= 8;
        }

        long fileSize = file.length();
        byte[] byteFileSize = new byte[Long.BYTES];
        int byteFileSizeLength = byteFileSize.length;
        for (int i = 0; i < byteFileSizeLength; i++) {                            //long to byte
            byteFileSize[byteFileSizeLength - i - 1] = (byte) (fileSize & 0xFF);
            fileSize >>= 8;
        }

        if(fileSize > Math.pow(2, 40)){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new FileSizeException("More than 1 terabyte? God, no!");
        }

        try {
            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(byteFileNameSize);
            outputStream.write(fileName.getBytes(StandardCharsets.UTF_8));
            outputStream.write(byteFileSize);
            outputStream.write(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void receiveFile(Socket socket) {
        byte[] fileSizeBuffer = new byte[8];
        byte[] fileNameSizeBuffer = new byte[8];
        byte[] fileBuffer = new byte[4096];
        long fileSize = 0;
        long fileNameSize = 0;

        Counter counter = new Counter(fileBuffer.length);
        counter.run();
        try {
            InputStream inputStream = socket.getInputStream();

            inputStream.read(fileNameSizeBuffer);
            for (byte b : fileNameSizeBuffer) {
                fileNameSize = (fileNameSize << 8) + (b & 0xFF);           //from bytes to long
            }

            byte[] fileNameBuffer = new byte[(int) fileNameSize];
            inputStream.read(fileNameBuffer);

            String fileName = new String(fileNameBuffer, StandardCharsets.UTF_8);
            File uploadsDir = new File("uploads/");
            uploadsDir.mkdir();
            File uploadedFile = new File(uploadsDir + "/" + fileName);

            boolean createFileStatus = uploadedFile.createNewFile();
            int i = 1;
            while(!createFileStatus){
                uploadedFile = new File(uploadsDir + "/" +  "(" + i + ")" + fileName);
                createFileStatus = uploadedFile.createNewFile();
                i++;
            }

            FileOutputStream uploadedFileStream = new FileOutputStream(uploadedFile);

            inputStream.read(fileSizeBuffer);

            for (byte b : fileSizeBuffer) {
                fileSize = (fileSize << 8) + (b & 0xFF);           //from bytes to long
            }

            int status = 0;

            while(status != -1){
                status = inputStream.read(fileBuffer);
                if(status != -1){
                    uploadedFileStream.write(fileBuffer, 0, status);
                }
                counter.setBufferLength(status);
            }
            long uploadedFileSize = uploadedFile.length();
            if(uploadedFileSize != fileSize){
                socket.close();
                throw new FileCheckSumException("The checksum does not match the original file size," +
                        " something went wrong during the upload process");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
