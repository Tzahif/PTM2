package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class WorkerRunnable implements Runnable{

    private Socket clientSocket = null;
    private PriorityJobScheduler pjs;
    private ClientHandler ch;

    public WorkerRunnable(Socket clientSocket, PriorityJobScheduler pjs, ClientHandler ch) {
        this.clientSocket = clientSocket;
        this.pjs = pjs;
        this.ch = ch;
    }

    public void run() {
        try {
            ArrayList<String> gameBoard = buildBoard(this.clientSocket.getInputStream());
            int priority = (gameBoard.size() - 1) * gameBoard.get(0).length();
            pjs.addJob(new Job(this.clientSocket, gameBoard, ch, priority));
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }

    private ArrayList<String> buildBoard(InputStream in) {
        BufferedReader buff = new BufferedReader(new InputStreamReader((in)));
        ArrayList<String> gameBoard = new ArrayList<>();
        String line = "";
        try {
            gameBoard.add(buff.readLine());
            while (!(line.equals("done"))) {
                line = buff.readLine();
                gameBoard.add(line);
            }
            //in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameBoard;
    }
}
