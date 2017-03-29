package api.helpers;

import api.exceptions.APIBadRequestException;
import java.util.HashMap;
import org.apache.http.HttpRequest;

public class UrlArgumentHelper {

  /**
   * A helper method for getting arguments from the URL of a http request
   *
   * @param httpRequest The HttpRequest to get URL arguments from
   * @return A HashMap of the arguments
   * @throws APIBadRequestException An exception indicating that there is no arguments in the url
   */
  public static HashMap<String, String> getArgumentsInURL(HttpRequest httpRequest)
      throws APIBadRequestException {
    HashMap<String, String> arguments = new HashMap<>();

    String uri = httpRequest.getRequestLine().getUri();

    if (!uri.contains("?")) {
      throw new APIBadRequestException("No arguments in url");
    }
    String argumentString = uri.substring(uri.indexOf("?") + 1);

    argumentString = argumentString.replace("%20", " ");
    String[] argumentStrings = argumentString.split("&");

    for (String argument : argumentStrings) {
      String[] splitArgument = argument.split("=");
      if (splitArgument.length != 2) {
        continue;
      }
      arguments.put(splitArgument[0], splitArgument[1]);
    }

    return arguments;
  }

  /**
   * A helper method for getting an url argument as a String
   *
   * @param httpRequest The HttpRequest to get URL argument from
   * @return The value of the argument
   * @throws APIBadRequestException An exception indicating required argument is not present in the
   * request
   */
  public static String getURIField(HttpRequest httpRequest, String field)
      throws APIBadRequestException {
    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    if (!uriArguments.containsKey(field)) {
      throw new APIBadRequestException(field + " must be given");
    }
    return uriArguments.get(field);
  }

  /**
   * A helper method for getting an url argument as an Integer
   *
   * @param httpRequest The HttpRequest to get URL argument from
   * @return The value of the argument
   * @throws APIBadRequestException An exception indicating required argument is not present in the
   * request
   */
  public static Integer getIntegerURIField(HttpRequest httpRequest, String field)
      throws APIBadRequestException {
      try {
        return Integer.valueOf(getURIField(httpRequest, field));
      } catch (NumberFormatException nfe) {
        throw new APIBadRequestException(field + " must be int");
      }
  }

}
