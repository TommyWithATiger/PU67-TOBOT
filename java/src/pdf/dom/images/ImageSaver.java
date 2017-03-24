package pdf.dom.images;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;

public class ImageSaver {

  public static final String BASEURL = "cdn.tobot.hummel.io:8080/images/";

  public static String saveBase64Image(String base64) {
    try {
      return makeSaveRequest(base64.substring(base64.indexOf(",") + 1));
    } catch (IOException | HttpException e) {
      return null;
    }
  }

  private static String makeSaveRequest(String base64Image) throws IOException, HttpException {
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
