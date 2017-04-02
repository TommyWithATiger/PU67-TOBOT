package pdf.dom.images;

import api.exceptions.APIBadRequestException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Saves given images to an external host and changes the content of the image nodes to links of
 * the given image instead. Works as a thread so that the images can be saved in parallel saving
 * a lot of uploading time
 */
public class ImageSaver extends Thread {

  public static final String BASEURL = "http://cdn.tobot.hummel.io:8080/images/";
  private String base64;
  private Node imageNode;

  public ImageSaver(String base64, Node imageNode) {
    this.base64 = base64;
    this.imageNode = imageNode;
  }

  @Override
  public void run() {
    ((Element) imageNode).setAttribute("src", saveBase64Image(base64));
  }

  /**
   * Saves a base 64 image
   *
   * @param base64 The base64 string of the image
   * @return The url of the image on the image host
   */
  private String saveBase64Image(String base64) {
    try {
      return makeSaveRequest(base64.substring(base64.indexOf(",") + 1));
    } catch (IOException | HttpException e) {
      throw new APIBadRequestException("Could not handle the image");
    }
  }

  /**
   * Makes a request to save the given image to the external host
   *
   * @param image The given image
   * @return A URL for the image on the external host
   * @throws IOException Problems writing to the socket
   * @throws HttpException No content in response
   */
  private String makeSaveRequest(String image) throws IOException, HttpException {
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress("cdn.tobot.hummel.io", 8080));

    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

    SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8192);
    sessionInputBuffer.bind(socket.getInputStream());
    DefaultHttpResponseParser responseParser = new DefaultHttpResponseParser(sessionInputBuffer);

    printWriter.write("POST /save.php HTTP/1.1\r\nContent-Type: application/x-www-form-urlencoded\r\nHost: cdn.tobot.hummel.io:8080\nContent-Length: " + (6 + image.length()) + "\r\n\r\nimage=" + image + "\r\n\r\n");
    printWriter.flush();
    HttpResponse httpResponse = responseParser.parse();
    if (sessionInputBuffer.available() == 0){
      throw new HttpException("Could not save image");
    }
    String url = sessionInputBuffer.readLine();

    return BASEURL + url;
  }

}
