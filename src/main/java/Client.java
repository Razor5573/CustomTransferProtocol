import exceptions.WrongPortException;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Integer port = null;
        try{
            port = Integer.parseInt(args[0]);
            if(port < 1024 || port > 65535) {
                throw new WrongPortException("Incoming port number is wrong");
            }
        }
        catch (NumberFormatException e){
            System.err.println("Port is not a number");
            e.printStackTrace();
        }

        String path = args[1];
        String DNSorIP = args[2];
        File file = new File(path);

        try (Socket socket = new Socket(DNSorIP, port)){
            Protocol.sendFile(socket, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
