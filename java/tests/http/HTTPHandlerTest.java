package http;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

import base.IndexPageTest;
import base.ServerBaseTest;
import java.io.File;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.http.HttpFileHandler;

public class HTTPHandlerTest extends IndexPageTest {

  @Test
  public void testRedirectOnInvalidUri() throws IOException, HttpException {
    printWriter.write("GET /averylongurithatwillneveractuallybeused.txt HTTP/1.1\r\n\r\n");
    printWriter.flush();

    HttpResponse httpResponse = responseParser.parse();
    assertEquals("HTTP/1.1 200 OK", httpResponse.getStatusLine().toString());
  }


}
