package server.http.handlers;

import java.io.InputStream;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpCoreContext;
import server.http.HttpFileHandler;

/**
 * A Http Handler for handling all requests of static files
 */
public class StaticContentHttpHandler {

  /**
   * Handles requests for static files, that is files with URI '/static/.*'. If the file does not
   * exist in the static folder a 404 File Not Found Http response is returned
   *
   * @param httpRequest The Http request for the static file
   * @return A Http response containing the requested file or a 404 File Not Found status code.
   */
  public static HttpResponse handleRequest(HttpRequest httpRequest) {
    HttpResponse response = new DefaultHttpResponseFactory()
        .newHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, new HttpCoreContext());

    // Get the uri and get a stream for the file
    String uri = httpRequest.getRequestLine().getUri();
    InputStream fileContent = new HttpFileHandler().getFile(uri);

    // Check if the file exists
    if (fileContent != null) {
      BasicHttpEntity httpEntity = new BasicHttpEntity();

      // Set file info
      httpEntity.setContent(fileContent);
      httpEntity.setContentLength(new HttpFileHandler().length(uri));
      httpEntity.setContentType(HttpFileHandler.translateContentType(uri));

      response.setEntity(httpEntity);
    } else {
      response.setStatusCode(404);
    }

    return response;
  }

}
