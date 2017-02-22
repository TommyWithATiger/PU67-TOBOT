package server.http.handlers;

import api.APIDelegator;
import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import api.exceptions.APIHandlerNotFoundException;
import api.exceptions.APIRequestForbiddenException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpCoreContext;

/**
 * A HttpHandler for the API
 */
public class APIHttpHandler {

  /**
   * Handles a HTTP request for the API. Will try to call the APIDelegator to get a response JSON
   * message If the APIDelegator throws an exception a Http response is returned with the
   * appropriate Http status code
   *
   * @param httpRequest The Http request made
   * @return A HttpResponse to the API request
   */
  public static HttpResponse handleRequest(HttpRequest httpRequest) {

    HttpResponse httpResponse = new DefaultHttpResponseFactory()
        .newHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, new HttpCoreContext());

    // Try to create a response to the request
    try {
      BasicHttpEntity httpEntity = new BasicHttpEntity();
      httpEntity.setContent(new ByteArrayInputStream(APIDelegator.delegate(httpRequest).getBytes(
          StandardCharsets.UTF_8)));
      httpResponse.setEntity(httpEntity);
    } catch (APIHandlerNotFoundException e) {
      // Cannot find a API handler to handle the request, the requested resource must not exist
      httpResponse.setStatusCode(404);
    } catch (APIErrorException e) {
      httpResponse.setStatusCode(500);
    } catch (APIBadRequestException e) {
      httpResponse.setStatusCode(400);
    } catch (APIBadMethodException e) {
      httpResponse.setStatusCode(405);
    } catch (APIRequestForbiddenException e) {
      httpResponse.setStatusCode(403);
    }

    return httpResponse;
  }

}
