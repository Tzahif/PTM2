package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Job implements Runnable {
    Socket client;
    private ArrayList<String> gameBoard;
    private ClientHandler ch;
    private int jobPriority;

    public Job(Socket client, ArrayList<String> gameBoard, ClientHandler ch, int jobPriority) {
        super();
        this.client = client;
        this.gameBoard = gameBoard;
        this.ch = ch;
        this.jobPriority = jobPriority;
    }

    public int getJobPriority() {
        return jobPriority;
    }

    @Override
    public void run() {
        try {
            ch.handle(this.gameBoard, this.client.getOutputStream());
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

