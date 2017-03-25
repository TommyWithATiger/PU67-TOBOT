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

  private String saveBase64Image(String base64) {
    try {
      return makeSaveRequest(base64.substring(base64.indexOf(",") + 1));
    } catch (IOException | HttpException e) {
      throw new APIBadRequestException("Could not handle the image");
    }
  }

  private String makeSaveRequest(String base64Image) throws IOException, HttpException {
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress("cdn.tobot.hummel.io", 8080));

    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

    SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8192);
    sessionInputBuffer.bind(socket.getInputStream());
    DefaultHttpResponseParser responseParser = new DefaultHttpResponseParser(sessionInputBuffer);

    printWriter.write("POST /save.php HTTP/1.1\r\nContent-Type: application/x-www-form-urlencoded\r\nHost: cdn.tobot.hummel.io:8080\nContent-Length: " + (6 + base64Image.length()) + "\r\n\r\nimage=" + base64Image + "\r\n\r\n");
    printWriter.flush();
    HttpResponse httpResponse = responseParser.parse();
    if (sessionInputBuffer.available() == 0){
      throw new HttpException("Could not save image");
    }
    String url = sessionInputBuffer.readLine();

    return BASEURL + url;
  }

}
