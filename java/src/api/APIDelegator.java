package api;

import api.exceptions.APIHandlerNotFoundException;
import api.handlers.pdf.APIUploadAndSplitPDF;
import api.handlers.exercise.APIRegisterExerciseAttemptHandler;
import api.handlers.exercise.APIAddExerciseHandler;
import api.handlers.exercise.APIGetExerciseHandler;
import api.handlers.rating.APIGetTopicRatingHandler;
import api.handlers.rating.APIRateTopicHandler;
import api.handlers.reference.APIAddReferenceHandler;
import api.handlers.reference.APIGetReferenceHandler;
import api.handlers.relators.APIGetRelatedTopicsSubjectHandler;
import api.handlers.relators.APIGetRelatedTopicsWithRatingCountHandler;
import api.handlers.relators.APIRelateSubjectTopicHandler;
import api.handlers.subject.APIAddSubjectHandler;
import api.handlers.subject.APIGetSubjectHandler;
import api.handlers.subject.APIJoinSubjectHandler;
import api.handlers.topic.APIAddTopicHandler;
import api.handlers.topic.APIGetTopicHandler;
import api.handlers.user.APIGetUserInfoHandler;
import api.handlers.user.APILoggedInCheckHandler;
import api.handlers.user.APILoginHandler;
import api.handlers.user.APILogoutHandler;
import api.handlers.user.APIRegistrationHandler;
import api.handlers.user.APIResetPasswordHandler;
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
    handlerRegistry.put("user\\/info", APIGetUserInfoHandler::getUserInfo);

    // User registration
    handlerRegistry.put("user\\/registration\\/check", APIRegistrationHandler::checkRegistrationData);
    handlerRegistry.put("user\\/registration", APIRegistrationHandler::registerUser);

    // Password reset
    handlerRegistry.put("user\\/reset\\/request", APIResetPasswordHandler::requestPasswordReset);
    handlerRegistry.put("user\\/reset", APIResetPasswordHandler::resetPassword);

    // Subject and topics
    handlerRegistry.put("topic\\/create", APIAddTopicHandler::handleAddTopicRequest);
    handlerRegistry.put("subject\\/create", APIAddSubjectHandler::handleAddSubject);
    handlerRegistry
        .put("subject\\/topic\\/relate", APIRelateSubjectTopicHandler::relateSubjectTopicHandler);
    handlerRegistry
            .put("subject\\/topic\\/unrelate", APIRelateSubjectTopicHandler::unrelateSubjectTopicHandler);

    handlerRegistry.put("subject\\/related\\/\\?id=.*",
        APIGetRelatedTopicsSubjectHandler::getRelatedTopicsSubjectID);

    handlerRegistry.put("subject\\/related\\/count\\/\\?id=.*",
        APIGetRelatedTopicsWithRatingCountHandler::getTopicWithRatingCountSubjectID);

    handlerRegistry.put("subject\\/get\\/\\?id=.*", APIGetSubjectHandler::getSubjectByID);
    handlerRegistry.put("subject\\/get\\/\\?title=.*", APIGetSubjectHandler::getSubjectsByTitle);
    handlerRegistry.put("subject\\/get", APIGetSubjectHandler::getAllSubjects);
    handlerRegistry.put("subject\\/get/participant", APIGetSubjectHandler::getParticipatingSubjects);
    handlerRegistry.put("subject\\/get/editor", APIGetSubjectHandler::getEditorSubjects);

    handlerRegistry.put("subject\\/join/participant", APIJoinSubjectHandler::joinSubjectParticipantHandler);
    handlerRegistry.put("subject\\/join/editor", APIJoinSubjectHandler::joinSubjectEditorHandler);

    handlerRegistry.put("subject\\/leave/participant", APIJoinSubjectHandler::leaveSubjectParticipantHandler);
    handlerRegistry.put("subject\\/leave/editor", APIJoinSubjectHandler::leaveSubjectEditorHandler);

    handlerRegistry.put("topic\\/get\\/\\?id=.*", APIGetTopicHandler::getTopicByID);
    handlerRegistry.put("topic\\/get\\/\\?title=.*", APIGetTopicHandler::getTopicsByTitle);
    handlerRegistry.put("topic\\/get", APIGetTopicHandler::getAllTopics);
    handlerRegistry.put("topic\\/rating\\/get", APIGetTopicRatingHandler::getTopicsWithRatings);

    handlerRegistry.put("topic\\/subject\\/ordered",
        APIGetTopicHandler::getTopicsBySubjectSortedByRating);
    // might want change this to order by a more advanced method in the future

    // Ratings
    handlerRegistry.put("rating\\/rate", APIRateTopicHandler::rateTopic);
    handlerRegistry
        .put("rating\\/get\\/\\?id=.*", APIGetTopicRatingHandler::getTopicRatingByTopicID);
    handlerRegistry.put("rating\\/get", APIGetTopicRatingHandler::getTopicRatings);

    // PDF
    handlerRegistry.put("pdf\\/split", APIUploadAndSplitPDF::uploadAndSplitPDF);
    
    // Exercises
    handlerRegistry.put("exercise\\/get\\/\\?id=.*", APIGetExerciseHandler::getExerciseByID);
    handlerRegistry.put("exercise\\/get\\/\\?topic=.*", APIGetExerciseHandler::getExercisesByTopic);
    handlerRegistry.put("exercise\\/create", APIAddExerciseHandler::handleAddTextOnlyExercise);
    handlerRegistry.put("exercise\\/register", APIRegisterExerciseAttemptHandler::handleRegisterExercise);
    handlerRegistry.put("exercise\\/next", APIGetExerciseHandler::getNextExercises);

    // References
    handlerRegistry
        .put("reference\\/get\\/\\?id=.*", APIGetReferenceHandler::getReferenceById);
    handlerRegistry
        .put("reference\\/get\\/\\?topic=.*", APIGetReferenceHandler::getReferencesByTopic);
    handlerRegistry.put("reference\\/create", APIAddReferenceHandler::handleAddReferenceRequest);

    return handlerRegistry;
  }

}
