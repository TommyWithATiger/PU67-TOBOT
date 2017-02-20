package main;

import server.connection.SocketHandler;
import server.http.HTTPHandler;

public class ServerInitializer {

  public static void main(String[] args) {
    System.setProperty("javax.xml.accessExternalDTD", "all");

    // Setup calls
    HTTPHandler.populateRegistry();

    // Place all setup before this call, this will run forever
    new SocketHandler();
  }
}
