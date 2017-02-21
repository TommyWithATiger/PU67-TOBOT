package api;

import api.exceptions.APIHandlerNotFoundException;
import java.util.HashMap;
import java.util.function.Function;
import org.apache.http.HttpRequest;

/**
 * A class for delegating API requests to the appropriate API request handlers
 */
public class APIDelegator {

  private static HashMap<String, Function<HttpRequest, String>> handlerRegistry = populateRegistry();

  /**
   * Delegates the APIRequest to the proper handler
   *
   * @param APIRequest The HttpRequest which contains the request to the API
   * @return A string version of a JSON response to the APIRequest.
   * @throws APIHandlerNotFoundException An error returned when there is no API handler for the
   * given request
   */
  public static String delegate(HttpRequest APIRequest) throws APIHandlerNotFoundException {
    String uri = APIRequest.getRequestLine().getUri().substring(4);
    for (String uriRegex : handlerRegistry.keySet()) {
      if (uri.matches(uriRegex)) {
        return handlerRegistry.get(uriRegex).apply(APIRequest);
      }
    }

    throw new APIHandlerNotFoundException("Could not find a handler that matches the API call");
  }

  /**
   * @return a populated HashMap of regex strings and their connected API handlers
   */
  private static HashMap<String, Function<HttpRequest, String>> populateRegistry() {
    HashMap<String, Function<HttpRequest, String>> handlerRegistry = new HashMap<>();

    // Api handlers should be registered here

    return handlerRegistry;
  }

}
