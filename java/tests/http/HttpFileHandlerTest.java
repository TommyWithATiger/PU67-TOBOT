package http;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.http.HttpFileHandler;

public class HttpFileHandlerTest {

  private HttpFileHandler fileHandler;
  private String baseServerPath;
  private File file;
  private String filePath;
  private byte[] fileContent;

  @Before
  public void setup() throws IOException {
    fileContent = new byte[]{1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 1, 1, 1, 2, 2, 3, 3, 4, 5};
    fileHandler = new HttpFileHandler();
    baseServerPath = System.getProperty("user.dir") + "/../web/";
    filePath = "abcdefghijklmnopqrstuvwxyz.txt";
    Files.createFile(Paths.get(baseServerPath + filePath));
    Files.write(Paths.get(baseServerPath + filePath), fileContent);
    file = new File(baseServerPath + filePath);
  }

  @Test
  public void testFindsCorrectFile() throws IOException {
    byte[] fileHandlerOutput = IOUtils.readFully(fileHandler.getFile(filePath), fileContent.length);
    assertTrue(Arrays.equals(fileHandlerOutput, fileContent));
  }

  @Test
  public void testDoesNotFindInvalidFile() {
    assertNull(fileHandler.getFile("aqwnbxnvjkadsijvuytzxtyrrteaghjqghwvbn.pdf"));
  }

  @Test
  public void testFindLengthFile() {
    assertEquals(file.length(), fileHandler.length(filePath));
  }

  @Test
  public void testGetValidContentType() {
    assertEquals("text/html", HttpFileHandler.translateContentType("asdjkfahsd.html"));
    assertEquals("application/javascript", HttpFileHandler.translateContentType("helper.js"));
    assertEquals("application/json", HttpFileHandler.translateContentType("somethingUseful.json"));
    assertEquals("text/css", HttpFileHandler.translateContentType("/static/helper/style.js.css"));
  }

  @Test
  public void testGetNonExistentContentType(){
    assertEquals("text/plain", HttpFileHandler.translateContentType("/helper/file.php"));
  }

  @After
  public void tearDown() throws IOException {
    Files.delete(file.toPath());
  }

}
