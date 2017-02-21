package http;

import static junit.framework.TestCase.assertEquals;

import base.ServerBaseTest;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.junit.Test;

public class HTTPHandlerTest extends ServerBaseTest {

  @Test
  public void testRedirectOnInvalidUri() throws IOException, HttpException {
    printWriter.write("GET /averylongurithatwillneveractuallybeused.txt HTTP/1.1\r\n\r\n");
    printWriter.flush();

    HttpResponse httpResponse = responseParser.parse();
    assertEquals("HTTP/1.1 301 Moved Permanently", httpResponse.getStatusLine().toString());
    assertEquals("/index.html", httpResponse.getFirstHeader("Location").getValue());
  }

}
