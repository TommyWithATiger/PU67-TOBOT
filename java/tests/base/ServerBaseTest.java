package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.junit.After;
import org.junit.Before;
import server.connection.SocketHandler;

public class ServerBaseTest extends BaseTest{

  private SocketHandler socketHandler;

  protected Socket socket;
  protected PrintWriter printWriter;
  protected DefaultHttpResponseParser responseParser;
  protected SessionInputBufferImpl sessionInputBuffer;

  @Before
  public void setupSockets() throws IOException {
    setupSocketHandler();
    setupSocket();
  }

  private void setupSocket() throws IOException {
    socket = new Socket();
    socket.connect(new InetSocketAddress("localhost", SocketHandler.PORT));

    printWriter = new PrintWriter(socket.getOutputStream());

    sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8192);
    sessionInputBuffer.bind(socket.getInputStream());
    responseParser = new DefaultHttpResponseParser(sessionInputBuffer);
  }

  private void setupSocketHandler(){
    socketHandler = new SocketHandler();
    socketHandler.start();
  }

  @After
  public void tearDown() throws IOException {
    socketHandler.stopServer();
    socket.close();
  }

}
