package api;

import api.exceptions.APIHandlerNotFoundException;
import api.handlers.relators.APIRelateSubjectTopicHandler;
import api.handlers.subject.APIAddSubjectHandler;
import api.handlers.subject.APIGetSubjectHandler;
import api.handlers.topic.APIAddTopicHandler;
import api.handlers.topic.APIGetTopicHandler;
import api.handlers.user.APILoggedInCheckHandler;
import api.handlers.user.APILoginHandler;
import api.handlers.user.APILogoutHandler;
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
    String uri = APIRequest.getRequestLine().getUri().substring(8);
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

    // User login logic
    handlerRegistry.put("user\\/login", APILoginHandler::handleLoginRequest);
    handlerRegistry.put("user\\/logout", APILogoutHandler::handleLogoutRequest);
    handlerRegistry.put("user\\/check", APILoggedInCheckHandler::handleLoggedInCheckRequest);

    // Subject and topics
    handlerRegistry.put("topic\\/create", APIAddTopicHandler::handleAddTopicRequest);
    handlerRegistry.put("subject\\/create", APIAddSubjectHandler::handleAddSubject);
    handlerRegistry
        .put("subject\\/topic\\/relate", APIRelateSubjectTopicHandler::relateSubjectTopicHandler);

    handlerRegistry.put("subject\\/get\\/\\?id=.*", APIGetSubjectHandler::getSubjectByID);
    handlerRegistry.put("subject\\/get\\/\\?title=.*", APIGetSubjectHandler::getSubjectsByTitle);

    handlerRegistry.put("topic\\/get\\/\\?id=.*", APIGetTopicHandler::getTopicByID);
    handlerRegistry.put("topic\\/get\\/\\?title=.*", APIGetTopicHandler::getTopicsByTitle);

    return handlerRegistry;
  }

}
