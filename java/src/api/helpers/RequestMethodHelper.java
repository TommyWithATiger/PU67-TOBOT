package api.helpers;

import api.exceptions.APIBadMethodException;
import org.apache.http.HttpRequest;

public class RequestMethodHelper {

  /**
   * A helper method for checking if the http request uses the given method
   * @param method The method required
   * @param httpRequest The http request
   * @throws APIBadMethodException Thrown if the method is not correct
   */
  public static void checkRequestMethod(String method, HttpRequest httpRequest)
      throws APIBadMethodException {
    if (!method.equals(httpRequest.getRequestLine().getMethod())) {
      throw new APIBadMethodException(
          "Expected request method: " + method + ". Found: " + httpRequest.getRequestLine()
              .getMethod());
    }
  }

}
