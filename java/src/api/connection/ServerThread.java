package api.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.impl.io.DefaultHttpRequestParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;

public class ServerThread extends Thread {

  private Socket clientSocket;
  private PrintWriter printWriter;
  private DefaultHttpRequestParser requestParser;

  public ServerThread(Socket clientSocket) throws IOException {
    this.clientSocket = clientSocket;
    SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(
        new HttpTransportMetricsImpl(), 8192);
    sessionInputBuffer.bind(clientSocket.getInputStream());
    requestParser = new DefaultHttpRequestParser(sessionInputBuffer);
    printWriter = new PrintWriter(clientSocket.getOutputStream());
  }

  @Override
  public void run() {
    while (!clientSocket.isClosed()) {
      try {
        HttpRequest request = requestParser.parse();
      } catch (IOException | HttpException e) {
        e.printStackTrace();
      }
    }
  }

}
