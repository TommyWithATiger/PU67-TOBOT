package api.handlers.subject;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.Subject;
import data.dao.SubjectDAO;
import data.user.User;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIJoinSubjectHandler {

  /**
   * A API handler method for setting a user as a participant in a subject. This requires the user
   * to be logged in  and the following data:
   *        subjectID (int): the id of the subject
   *
   * @param httpRequest The request to handle
   * @return A JSON string with two variables:
   *        was-joined (boolean): indicates whether the user had already joined the subject
   *        is-joined (boolean): indicates whether the user has joined the subject
   */
  public static String joinSubjectParticipantHandler(HttpRequest httpRequest) {
    return joinSubject(httpRequest, Subject::isParticipant, Subject::addParticipant).toString();
  }

  /**
   * An API handler for checking if the user has joined the given subject. This requires the user to be logged in
   * and the following data:
   *      subjectID (int): the id of the subject
   *
   * @param httpRequest The request to handle
   * @return A JSON string with one variable:
   *      joined (boolean): indicates whether the user has joined the subject
   */
  public static String hasJoinedSubject(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    User user = getUserFromRequest(httpRequest, ", cannot check if unlogged in user has joined subject");


    Integer subjectID = getIntegerURIField(httpRequest, "id");

    Subject subject = SubjectDAO.getInstance().findById(subjectID);

    JSONObject response = new JSONObject();
    response.put("joined", subject.isParticipant(user));
    return response.toString();
  }

  /**
   * A API handler method for removing a user as an participant in a subject. This requires the user
   * to be logged in  and the following data:
   *        subjectID (int): the id of the subject
   *
   * @param httpRequest The request to handle
   * @return A JSON string with two variables:
   *        was-joined (boolean): indicates whether the user was already a Participant of the subject
   *        is-joined (boolean): indicates whether the user currently is a Participant of the subject
   */
  public static String leaveSubjectParticipantHandler(HttpRequest httpRequest) {
    return negate(joinSubject(httpRequest, ((subject, user) -> !subject.isParticipant(user)),
        Subject::removeParticipant)).toString();
  }

  /**
   * A helper method to join a user to a subject. Requires the user to be logged in and the following

  /**
   * A API handler method for setting a user as an editor in a subject. This requires the user
   * to be logged in  and the following data:
   *        subjectID (int): the id of the subject
   *
   * @param httpRequest The request to handle
   * @return A JSON string with two variables:
   *        was-joined (boolean): indicates whether the user was already an Editor of the subject
   *        is-joined (boolean): indicates whether the user currently is an Editor of the subject
   */
  public static String joinSubjectEditorHandler(HttpRequest httpRequest) {
    return joinSubject(httpRequest, Subject::isEditorNoAdminCheck, Subject::addEditor).toString();
  }

  /**
   * A API handler method for removing a user as an editor in a subject. This requires the user
   * to be logged in  and the following data:
   *        subjectID (int): the id of the subject
   *
   * @param httpRequest The request to handle
   * @return A JSON string with two variables:
   *        was-joined (boolean): indicates whether the user was already an Editor of the subject
   *        is-joined (boolean): indicates whether the user currently is an Editor of the subject
   */
  public static String leaveSubjectEditorHandler(HttpRequest httpRequest) {
    return negate(joinSubject(httpRequest, ((subject, user) -> !subject.isEditorNoAdminCheck(user)),
        Subject::removeEditor)).toString();
  }

  /**
   * A helper method to join a user to a subject. Requires the user to be logged in and the following
   * data:
   *        subjectID (int): the id of the subject
   *
   * @param httpRequest The request to handle
   * @param hasJoinedFunc a function tell whether a subject and a user is related
   * @param joinFunc a function to relate a subject and a user
   * @return A JSON object with two variables:
   *        was-joined (boolean): indicates whether the user was already joined to the subject,
   * according to hasJoinedFunc
   *        is-joined (boolean): indicates whether the user is currently joined to the subject,
   * according to hasJoinedFunc
   */
  private static JSONObject joinSubject(HttpRequest httpRequest, BiPredicate<Subject, User> hasJoinedFunc, BiConsumer<Subject, User> joinFunc) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    User user  = getUserFromRequest(httpRequest, ", cannot leave/join subject");

    Subject subject = SubjectDAO.getInstance().findById(getJSONField(httpRequest, Integer.class, "subjectID"));

    if (subject == null) {
      throw new APIBadRequestException("Subject id does not exist");
    }

    JSONObject response = new JSONObject();

    boolean joined = hasJoinedFunc.test(subject, user);
    response.put("was-joined", joined);

    // Relate them if they are not already related
    if (!joined) {
      joinFunc.accept(subject, user);
      subject.update();
    }

    response.put("is-joined", hasJoinedFunc.test(subject, user));

    return response;
  }

  /**
   * A helper method to negate fields set in joinSubject()
   *
   * @param response, the JSONObject with the fields to be negated
   * @return A JSON object with two variables, was-joined and is-joined, with value negated/inverted
   * from what it originally was
   */
  private static JSONObject negate(JSONObject response){
    response.put("was-joined", !response.getBoolean("was-joined"));
    response.put("is-joined", !response.getBoolean("is-joined"));
    return response;
  }

}
