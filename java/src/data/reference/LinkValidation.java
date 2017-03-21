package data.reference;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import server.connection.SocketHandler;

public class LinkValidation {

  public static boolean validateLink(Reference reference, String link) throws MalformedURLException, IllegalArgumentException {
    if (reference.getReferenceType() == ReferenceType.VIDEO) return validateVideoLink(reference, link);

    String[] schemes = {"http", "https"};
    UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_2_SLASHES);
    if (link.matches("(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})")) {
    //if (urlValidator.isValid(link)) {
      return true;
    }
    throw new MalformedURLException();
  }

  public static boolean validateVideoLink(Reference reference, String link) throws IllegalArgumentException{
    if (link.matches("^((?:https?:)?\\/\\/)?((?:www)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$")) {
      return true;
    }
    throw new IllegalArgumentException("Video URL has to be from Youtube");
  }

  public static boolean testLinkAccessible(String link) throws IOException, HttpException {
    URL url = new URL(link);
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress(url.getHost(), SocketHandler.PORT));

    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

    SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8192);
    sessionInputBuffer.bind(socket.getInputStream());
    DefaultHttpResponseParser responseParser = new DefaultHttpResponseParser(sessionInputBuffer);

    printWriter.write("GET " + url.getPath() + "?" + url.getQuery() + " HTTP/1.1\r\n\r\n");
    printWriter.flush();

    HttpResponse httpResponse = responseParser.parse();
    boolean responseOK = httpResponse.getStatusLine().toString().equals("HTTP/1.1 200 OK");
    socket.close();

    if (!responseOK) {
      throw new IllegalArgumentException("Link is not accessible.");
    }
    return true;
  }
}
