package api.handlers.subject;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
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
    return joinSubject(httpRequest, Subject::isParticipant, Subject::addParticipant);
  }

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
    return joinSubject(httpRequest, Subject::isEditor, Subject::addEditor);
  }

/**
 * A helper method to join a user to a subject. Requires the user to be logged in and the following
 * data:
 *        subjectID (int): the id of the subject
 *
 * @param httpRequest The request to handle
 * @return A JSON string with two variables:
 *        was-joined (boolean): indicates whether the user was already joined to the subject
 *        is-joined (boolean): indicates whether the user is currently joined to the subject
 */
  private static String joinSubject(HttpRequest httpRequest, BiPredicate<Subject, User> hasJoinedFunc, BiConsumer<Subject, User> joinFunc) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    User user  = getUserFromRequest(httpRequest, ", cannot add relate user to subject");

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

    return response.toString();
  }

}
