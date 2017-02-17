package api.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

  private Socket clientSocket;
  private BufferedReader inputReader;
  private PrintWriter printWriter;

  public ServerThread(Socket clientSocket) throws IOException {
    this.clientSocket = clientSocket;
    inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    printWriter = new PrintWriter(clientSocket.getOutputStream());
  }

  @Override
  public void run() {
    while(!clientSocket.isClosed()){
      
    }
  }

}
