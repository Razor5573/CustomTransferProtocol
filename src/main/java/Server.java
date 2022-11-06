import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static private final ExecutorService threadPool = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        while(true){
            try (ServerSocket socket = new ServerSocket(port)){
                Socket finalClientSocket = socket.accept();
                threadPool.submit(() -> Protocol.receiveFile(finalClientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
