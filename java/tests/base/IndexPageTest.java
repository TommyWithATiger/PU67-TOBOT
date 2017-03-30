package base;

import static junit.framework.TestCase.fail;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import server.http.HttpFileHandler;

public class IndexPageTest extends ServerBaseTest {

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

  @After
  public void after() {
    if (deleteFile != null) {
      deleteFile.delete();
    }
  }


}
