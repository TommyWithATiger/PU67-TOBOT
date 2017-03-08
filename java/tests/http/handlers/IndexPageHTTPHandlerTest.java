package http.handlers;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import base.ServerBaseTest;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.junit.Test;
import server.http.HttpFileHandler;

public class IndexPageHTTPHandlerTest extends ServerBaseTest {

  @Test
  public void testIndexRequest() throws IOException, HttpException, InterruptedException {
    printWriter.write("GET /index.html HTTP/1.1\r\n\r\n");
    printWriter.flush();

    // If the test fail at the comparision of the two content byte arrays try increasing the sleep time to make sure all data is received from the server.
    Thread.sleep(200);

    HttpResponse httpResponse = responseParser.parse();
    assertEquals("HTTP/1.1 200 OK", httpResponse.getStatusLine().toString());
    assertEquals("text/html", httpResponse.getFirstHeader("Content-Type").getValue());

    byte[] responseContent = new byte[sessionInputBuffer.length()];
    sessionInputBuffer.read(responseContent);

    byte[] htmlContent = IOUtils.readFully(new HttpFileHandler().getFile("index.html"),
        (int) new HttpFileHandler().length("index.html"));

    assertTrue(Arrays.equals(responseContent, htmlContent));

  }

}
