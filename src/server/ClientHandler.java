package server;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public interface ClientHandler {
    public void handle(ArrayList<String> gameBoard, OutputStream out);
}
