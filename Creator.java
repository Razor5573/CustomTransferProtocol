import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Creator {
    static Socket socket;
    static ServerSocket serverSocket;
    public static Socket createSocket(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    public static ServerSocket createServerSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }
}
