package api.helpers;

import api.exceptions.APIBadMethodException;
import org.apache.http.HttpRequest;

public class RequestMethodHelper {

  public static void checkRequestMethod(String method, HttpRequest httpRequest)
      throws APIBadMethodException {
    if (!method.equals(httpRequest.getRequestLine().getMethod())) {
      throw new APIBadMethodException(
          "Expected request method: " + method + ". Found: " + httpRequest.getRequestLine()
              .getMethod());
    }
  }

}
