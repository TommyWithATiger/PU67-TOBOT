package server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.apache.commons.io.IOUtils;
import sun.nio.ch.IOUtil;

/**
 * The main server class
 */
public class SocketHandler extends Thread {

  private ServerSocket serverSocket;
  // The port the server will run at
  public static int PORT = 5032;

  public SocketHandler() {
    try {
      // Bind the socket
      serverSocket = new ServerSocket(PORT);
      serverSocket.setReceiveBufferSize(33554432);
    } catch (IOException exception) {
      // If the server cannot bind to the port, it will shutdown
      System.out.println(
          "Could not bind the server to port " + PORT + "!\n Please make sure the port is free");
      exception.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * The run method of the thread, will run the server until it is closed
   */
  @Override
  public void run() {
    while (!serverSocket.isClosed()) {
      try {
        // Accept all new connections and start a new thread for them
        Socket client = serverSocket.accept();
        client.setReceiveBufferSize(33554432);
        client.setSendBufferSize(33554432);
        ServerThread clientThread = new ServerThread(client);
        clientThread.start();
      } catch (SocketException se) {
        // Will occur when the socket in which the server accepts incoming requests is closed
        System.out.println("Server closed");
      } catch (IOException e) {
        // A connection error when connecting to a client
        System.out.println("Connection error");
        e.printStackTrace();
      }
    }
  }

  /**
   * Tries to stop the server by closing the socket. This will stop the main loop in the run method
   */
  public void stopServer() {
    try {
      serverSocket.close();
    } catch (IOException e) {
      System.out.println("Could not stop server");
      e.printStackTrace();
    }
  }

}
