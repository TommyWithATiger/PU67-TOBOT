package api.helpers;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
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

    String requestContent;
    try {
      // Have to store the entity as a BufferedHttpEntity, because otherwise this method could only
      // be called once, for each HttpRequest, since it would consume the normal (not  repeatable)
      // HttpEntity.
      BufferedHttpEntity httpEntity = new BufferedHttpEntity(((HttpEntityEnclosingRequest) httpRequest).getEntity());
      ((HttpEntityEnclosingRequest) httpRequest).setEntity(httpEntity);

      requestContent = EntityUtils.toString(httpEntity);
    } catch (IOException e) {
      throw new APIErrorException("Could not read entity");
    }

    return requestContent;
  }

}
