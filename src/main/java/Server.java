import exceptions.WrongPortException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static private final ExecutorService threadPool = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        Integer port = null;
        try{
            port = Integer.parseInt(args[0]);
            if(port < 1024 || port > 65535) {
                throw new WrongPortException("Incoming port number is wrong");
            }
        }catch (NumberFormatException e){
            System.err.println("Port is not a number");
            e.printStackTrace();
        }
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket finalClientSocket = socket.accept();
                threadPool.submit(() -> Protocol.receiveFile(finalClientSocket));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
