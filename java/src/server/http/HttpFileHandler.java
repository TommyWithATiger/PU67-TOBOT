package server.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class HttpFileHandler {

  private static String basePath = System.getProperty("user.dir") + "/../web/";
  private static HashMap<String, String> contentTranslations = populateTranslations();

  public InputStream getFile(String serverPath) {
    String path = clean(serverPath);
    try {
      return new FileInputStream(basePath + path);
    } catch (FileNotFoundException ignored) {
      return null;
    }
  }

  public long length(String serverPath) {
    String path = clean(serverPath);
    return new File(basePath + path).length();
  }

  public static String translateContentType(String filename) {
    String fileType = filename.substring(filename.lastIndexOf(".") + 1);
    if (contentTranslations.containsKey(fileType)) {
      return contentTranslations.get(fileType);
    }
    return "text/plain";
  }

  private static HashMap<String, String> populateTranslations() {
    HashMap<String, String> translationTable = new HashMap<>();

    translationTable.put("html", "text/html");
    translationTable.put("js", "application/javascript");
    translationTable.put("json", "application/json");
    translationTable.put("css", "text/css");

    return translationTable;
  }

  private static String clean(String serverPath) {
    return serverPath.replaceAll("\\.\\./", "");
  }

}
