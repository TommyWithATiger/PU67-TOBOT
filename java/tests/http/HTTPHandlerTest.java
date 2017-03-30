package http;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

import base.ServerBaseTest;
import java.io.File;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.http.HttpFileHandler;

public class HTTPHandlerTest extends ServerBaseTest {

  private File deleteFile;

  @Before
  public void before() {
    File file = new File(HttpFileHandler.basePath + "index.html");
    if (!file.exists()) {
      File parent = file.getParentFile();
      if (!parent.exists()) {
        deleteFile = parent;
        while (!deleteFile.getParentFile().exists()) {
          deleteFile = deleteFile.getParentFile();
        }
        if (!parent.mkdirs()) {
          fail("Could not create required folder structure");
        }
      }


      try {
        file.createNewFile();
        deleteFile = file;
      } catch (IOException e) {
        fail("Could not create file needed for test");
      }
    }
  }

  @Test
  public void testRedirectOnInvalidUri() throws IOException, HttpException {
    printWriter.write("GET /averylongurithatwillneveractuallybeused.txt HTTP/1.1\r\n\r\n");
    printWriter.flush();

    HttpResponse httpResponse = responseParser.parse();
    assertEquals("HTTP/1.1 200 OK", httpResponse.getStatusLine().toString());
  }

  @After
  public void after() {
    if (deleteFile != null) {
      deleteFile.delete();
    }
  }


}
