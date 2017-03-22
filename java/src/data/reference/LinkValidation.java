package data.reference;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;

public class LinkValidation {

  public static boolean validateLink(Reference reference, String link)
      throws IOException, IllegalArgumentException, HttpException {
    if (reference.getReferenceType() == ReferenceType.VIDEO) {
      return validateVideoLink(link);
    }

    if (link.matches(
        "(https?://(?:www\\.|(?!www))[^\\s.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})")) {
      return testLinkAccessible(link);
    }
    throw new MalformedURLException();
  }

  public static boolean validateVideoLink(String link)
      throws IllegalArgumentException, IOException, HttpException {
    if (link.matches(
        "^((?:https?:)?//)?((?:www)\\.)?((?:youtube\\.com|youtu.be))(/(?:[\\w\\-]+\\?v=|embed/|v/)?)([\\w\\-]+)(\\S+)?$")) {
      return testLinkAccessible(link);
    }
    throw new IllegalArgumentException("Video URL has to be from Youtube");
  }

  public static boolean testLinkAccessible(String link) throws IOException, HttpException {
    if (!link.contains("http")) {
      link = "http://" + link;
    }
    URL url = new URL(link);
    Socket socket = new Socket();
    if (link.charAt(4) == 's') {
      socket = SSLSocketFactory.getDefault().createSocket(url.getHost(), 443);
    } else {
      socket.connect(new InetSocketAddress(url.getHost(), 80));
    }

    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

    SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(
        new HttpTransportMetricsImpl(), 8192);
    sessionInputBuffer.bind(socket.getInputStream());
    DefaultHttpResponseParser responseParser = new DefaultHttpResponseParser(sessionInputBuffer);

    String request = url.getPath();
    if (url.getQuery() != null) {
      request += "?" + url.getQuery();
    }
    if (request.length() == 0) {
      request = "/";
    }
    printWriter.write("GET " + request + " HTTP/1.1\r\n");
    printWriter.write("Host: " + url.getHost() + "\r\n\r\n");
    printWriter.flush();

    HttpResponse httpResponse = responseParser.parse();
    List validCodes = Arrays.asList(200, 302, 300, 301, 303, 304, 307, 308);
    int code = httpResponse.getStatusLine().getStatusCode();
    socket.close();

    if (!validCodes.contains(code)) {
      throw new IllegalArgumentException("Link is not accessible.");
    }

    return true;
  }
}
