package server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketHandler extends Thread {

  private ServerSocket serverSocket;
  public static int PORT = 5032;

  public SocketHandler() {
    try {
      serverSocket = new ServerSocket(PORT);
    } catch (IOException exception) {
      System.out.println(
          "Could not bind the server to port " + PORT + "!\n Please make sure the port is free");
      exception.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public void run() {
    while (!serverSocket.isClosed()) {
      try {
        Socket client = serverSocket.accept();
        ServerThread clientThread = new ServerThread(client);
        clientThread.start();
      } catch (SocketException se){
        System.out.println("Server closed");
      } catch (IOException e) {
        System.out.println("Connection error");
        e.printStackTrace();
      }
    }
  }

  public void stopServer() {
    try {
      serverSocket.close();
    } catch (IOException e) {
      System.out.println("Could not stop server");
      e.printStackTrace();
    }
  }

}
