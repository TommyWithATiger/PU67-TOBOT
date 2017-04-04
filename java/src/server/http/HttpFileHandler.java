package server.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * A helper class for handling file requests to the server. Requires the web folder and java folder
 * to lay in the same folder
 */
public class HttpFileHandler {

  public static String basePath = System.getProperty("user.dir") + "/../web/dist/";
  private static HashMap<String, String> contentTranslations = populateTranslations();

  /**
   * @param serverPath The filepath for the requested file on the server
   * @return A InputStream for the requested file or null if the file does not exist
   */
  public InputStream getFile(String serverPath) {
    // Make the path safe
    String path = clean(serverPath);
    try {
      return new FileInputStream(basePath + path);
    } catch (FileNotFoundException ignored) {
      return null;
    }
  }

  /**
   * @param serverPath The filepath on the server for the file to find the length of
   * @return A long value indicating the length of the given file, -1 if it does not exist
   */
  public long length(String serverPath) {
    String path = clean(serverPath);
    File file = new File(basePath + path);
    if (file.exists()) {
      return file.length();
    }
    return -1;
  }

  /**
   * Translates a filename to the Http Content-Type header for the file
   *
   * @param filename The filename of the file, may include the path
   * @return A Http Content-Type value, if the file type does not exist in the system 'text/plain'
   */
  public static String translateContentType(String filename) {
    String fileType = filename.substring(filename.lastIndexOf(".") + 1);
    if (contentTranslations.containsKey(fileType)) {
      return contentTranslations.get(fileType);
    }
    return "text/plain";
  }

  /**
   * @return Returns a HashMap populated with file endings to Content-Type values translations
   */
  private static HashMap<String, String> populateTranslations() {
    HashMap<String, String> translationTable = new HashMap<>();

    // Register filename to Content-Type value translation
    translationTable.put("html", "text/html");
    translationTable.put("js", "application/javascript");
    translationTable.put("json", "application/json");
    translationTable.put("css", "text/css");

    return translationTable;
  }

  /**
   * Cleans a serverPath so that it is safe and users cannot access files outside the server folder
   *
   * @param serverPath The server path to clean
   * @return A cleaned server path
   */
  private static String clean(String serverPath) {
    return serverPath.replaceAll("\\.\\./", "");
  }

}
