package main;

import api.connection.SocketHandler;

public class ServerInitializer {

  public static void main(String[] args) {
    System.setProperty("javax.xml.accessExternalDTD", "all");

    // Should include all setup files for the project

    new SocketHandler();
  }
}
