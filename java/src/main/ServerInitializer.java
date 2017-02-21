package main;

import server.connection.SocketHandler;
import server.http.HTTPHandler;

public class ServerInitializer {

  public static void main(String[] args) throws InterruptedException {
    System.setProperty("javax.xml.accessExternalDTD", "all");

    // Setup calls
    HTTPHandler.populateRegistry();
    
    // Place all setup before this call, this will run forever
    SocketHandler server = new SocketHandler();
    server.start();

    Thread.sleep(Long.MAX_VALUE);

    server.stopServer();
  }
}
