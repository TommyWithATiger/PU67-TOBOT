package api.helpers;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.util.EntityUtils;

public class EntityContentHelper {

  /**
   * A helper for getting the content out of a http request
   *
   * @param httpRequest The http request to get the content from
   * @return A string of the content in the http request
   * @throws APIBadRequestException Thrown if there is no data
   * @throws APIErrorException Thrown if there is a IOException while reading the content
   */
  public static String checkAndGetEntityContent(HttpRequest httpRequest)
      throws APIBadRequestException, APIErrorException {
    if (!(httpRequest instanceof HttpEntityEnclosingRequest)) {
      throw new APIBadRequestException("No data");
    }

    HttpEntity httpEntity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
    String requestContent;

    try {
      requestContent = EntityUtils.toString(httpEntity);
    } catch (IOException e) {
      throw new APIErrorException("Could not read entity");
    }

    return requestContent;
  }

}
