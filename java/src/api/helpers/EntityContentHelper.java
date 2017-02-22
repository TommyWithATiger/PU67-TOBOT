package api.helpers;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.util.EntityUtils;

public class EntityContentHelper {

  public static String checkAndGetEntityContent(HttpRequest httpRequest)
      throws APIBadRequestException, APIErrorException {
    if (!(httpRequest instanceof HttpEntityEnclosingRequest)) {
      throw new APIBadRequestException("No login data");
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
