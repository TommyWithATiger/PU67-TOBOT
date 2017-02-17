package api.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler {

  private ServerSocket serverSocket;
  public static int PORT = 5032;

  public SocketHandler() {
    try {
      serverSocket = new ServerSocket(PORT);
      handle();
    } catch (IOException exception) {
      System.out.println(
          "Could not bind the server to port " + PORT + "!\n Please make sure the port is free");
      exception.printStackTrace();
      System.exit(1);
    }
  }

  private void handle() {
    while (!serverSocket.isClosed()){
      try {
        Socket client = serverSocket.accept();
        ServerThread clientThread = new ServerThread(client);
        clientThread.start();
      } catch (IOException e) {
        System.out.println("Connection error");
        e.printStackTrace();
      }
    }
  }

}
