package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer implements Server
{
    private static int POOL_SIZE = 2;
    private static int QUEUE_SIZE = 100;
    private static int SERVER_THREAD_POOL_SIZE = 10;

    private ServerSocket server;
    private int port;
    private volatile boolean stop;
    private PriorityJobScheduler pjs = new PriorityJobScheduler(POOL_SIZE, QUEUE_SIZE);
    private ExecutorService serverThreadPool = Executors.newFixedThreadPool(SERVER_THREAD_POOL_SIZE);

    public MyServer(int port)
    {
        this.port = port;
        stop = false;
    }
    // "127.0.0.1" / "localhost"
    private void runServer(ClientHandler ch) throws IOException {
        while (!stop) {
            try {
                Socket client = this.server.accept(); // blocking call
                this.serverThreadPool.execute(new WorkerRunnable(client, this.pjs, ch));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverThreadPool.shutdown();
        server.close();
    }

    public void start(ClientHandler ch) {
        try {
            this.server = new ServerSocket(this.port);
            //this.server.setSoTimeout(300);
        } catch (IOException e) {

             e.printStackTrace();
        }

        System.out.println("Listening on port " + port);

        new Thread(()->{try {
            runServer(ch);
        }catch (Exception e){
            e.printStackTrace();
        }
        }).start();
    }
    public void stop()
    {
        stop=true;
    }

    public static void main(String[] args) {
        MyServer myServer = new MyServer(6400);
        myServer.start(new MyClientHandler());
    }
}
