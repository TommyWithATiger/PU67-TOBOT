package api.handlers.subject;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.isLoggedIn;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import data.Subject;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIAddSubjectHandler {

  /**
   * An API handler for adding subjects. Requires the user to be logged in and the following data:
   *        title (String): the title the subject
   *        institution (String): the institution of the subject
   *        subjectCode (String): the subject code
   *        description (String): the description of the subject
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the following data:
   *        title (String): the subject title
   *        institution (String): the institution that has the subject
   *        subjectCode (String): the subject code
   *        description (String): the subject description
   *        id (int): the subject id
   */
  public static String handleAddSubject(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    // Must be logged in
    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot create a new subject");
    }

    // Require title, institution, subjectCode and description
    if (!jsonObject.has("title") || !jsonObject.has("institution") || !jsonObject.has("subjectCode")
        || !jsonObject.has("description")) {
      throw new APIBadRequestException("Request does not have the required data");
    }

    String title = jsonObject.getString("title");
    String institution = jsonObject.getString("institution");
    String subjectCode = jsonObject.getString("subjectCode");
    String description = jsonObject.getString("description");

    Subject subject = new Subject(title, institution, subjectCode, description);
    subject.create();

    JSONObject response = new JSONObject();
    response.put("title", title);
    response.put("institution", institution);
    response.put("subjectCode", subjectCode);
    response.put("description", description);
    response.put("id", subject.getId());

    return response.toString();
  }

}
