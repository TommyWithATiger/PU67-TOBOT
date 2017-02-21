package http.handlers;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import base.ServerBaseTest;
import server.connection.SocketHandler;
import server.http.HTTPHandler;
import server.http.HttpFileHandler;

public class IndexPageHTTPHandlerTest extends ServerBaseTest {

  private Socket socket;
  private PrintWriter printWriter;
  private DefaultHttpResponseParser responseParser;
  private SessionInputBufferImpl sessionInputBuffer;

  @Before
  public void setupSocket() throws IOException {
    socket = new Socket();
    socket.connect(new InetSocketAddress("localhost", SocketHandler.PORT));

    printWriter = new PrintWriter(socket.getOutputStream());

    sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8192);
    sessionInputBuffer.bind(socket.getInputStream());
    responseParser = new DefaultHttpResponseParser(sessionInputBuffer);

    HTTPHandler.populateRegistry();
  }

  @Test
  public void testIndexRequest() throws IOException, HttpException, InterruptedException {
    printWriter.write("GET /index.html HTTP/1.1\r\n\r\n");
    printWriter.flush();

    // If the test fail at the comparision of the two content byte arrays try increasing the sleep time to make sure all data is received from the server.
    Thread.sleep(20);

    HttpResponse httpResponse = responseParser.parse();
    assertEquals("HTTP/1.1 200 OK", httpResponse.getStatusLine().toString());
    assertEquals("text/html", httpResponse.getFirstHeader("Content-Type").getValue());

    byte[] responseContent = new byte[sessionInputBuffer.length()];
    sessionInputBuffer.read(responseContent);

    byte[] htmlContent = IOUtils.readFully(new HttpFileHandler().getFile("index.html"),
        (int) new HttpFileHandler().length("index.html"));

    assertTrue(Arrays.equals(responseContent, htmlContent));

  }

  @After
  public void tearDown() throws IOException {
    socket.close();
  }

}
