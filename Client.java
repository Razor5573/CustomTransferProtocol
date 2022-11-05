import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        String path = "C:\\Users\\nikit\\Downloads\\Telegram Desktop\\film1_v2.mp4";
        String DNSorIP = "10.3.130.167";
        File file = new File(path);
        Socket socket = Creator.createSocket(DNSorIP, port);
        Protocol.sendFile(socket, file);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
