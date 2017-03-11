package api.helpers;

import api.exceptions.APIBadRequestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

  public static List<String> getURIFields(HttpRequest httpRequest, String... fields)
      throws APIBadRequestException {
    List<String> ret = new ArrayList<>();

    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    for (String field : fields) {
      if (!uriArguments.containsKey(field)) {
        throw new APIBadRequestException(field + " must be given");
      }
      ret.add(uriArguments.get(field));
    }
    return ret;
  }

  public static List<Integer> getIntegerURIFields(HttpRequest httpRequest, String... fields)
      throws APIBadRequestException {
    List<Integer> ret = new ArrayList<>();

    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    for (String field : fields) {
      if (!uriArguments.containsKey(field)) {
        throw new APIBadRequestException(field + " must be given");
      }
      try {
        ret.add(Integer.valueOf(uriArguments.get(field)));
      } catch (NumberFormatException nfe) {
        throw new APIBadRequestException(field + " must be int");
      }
    }
    return ret;
  }

}
