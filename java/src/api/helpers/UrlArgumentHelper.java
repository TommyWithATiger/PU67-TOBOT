package api.helpers;

import api.exceptions.APIBadRequestException;
import java.util.HashMap;
import org.apache.http.HttpRequest;

public class UrlArgumentHelper {

  public static HashMap<String, String> getArgumentsInURL(HttpRequest httpRequest) {
    HashMap<String, String> arguments = new HashMap<>();

    String uri = httpRequest.getRequestLine().getUri();

    String argumentString;
    try {
      argumentString = uri.substring(uri.indexOf("?") + 1);
    } catch (IndexOutOfBoundsException ioobe) {
      throw new APIBadRequestException("No arguments in url");
    }

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

}
