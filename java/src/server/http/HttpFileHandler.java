package server.http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class HttpFileHandler {

  private static String basePath = System.getProperty("user.dir") + "/../web/";

  public InputStream getFile(String serverPath) {
    String path = clean(serverPath);
    try {
      return new FileInputStream(basePath + path);
    } catch (FileNotFoundException ignored) {
      return null;
    }
  }

  private static String clean(String serverPath) {
    return serverPath.replaceAll("\\.\\./", "");
  }
  
}
