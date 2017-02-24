package server.http.handlers;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpCoreContext;
import server.http.HttpFileHandler;

/**
 * A Http Handler for the index page
 */
public class IndexPageHTTPHandler {

  /**
   * Handles requests for the index page.
   *
   * @param httpRequest The given Http request
   * @return A Http response with the index.html file as content, if the fill exists in the /web/
   * folder. Else it returns a 404 File Not Found Http response
   */
  public static HttpResponse handleRequest(HttpRequest httpRequest) {
    HttpResponse response = new DefaultHttpResponseFactory()
        .newHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, new HttpCoreContext());

    // Check if the index.html exists
    BasicHttpEntity httpEntity = new BasicHttpEntity();
    httpEntity.setContent(new HttpFileHandler().getFile("index.html"));
    if (httpEntity.getContent() != null) {
      response.setEntity(httpEntity);

      // Set file information
      httpEntity.setContentType(HttpFileHandler.translateContentType("index.html"));
      httpEntity.setContentLength(new HttpFileHandler().length("index.html"));
    } else {
      response.setStatusCode(404);
    }

    return response;
  }

}
