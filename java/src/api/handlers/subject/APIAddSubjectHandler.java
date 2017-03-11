package api.handlers.subject;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.JSONCheckerHelper.requireJSONFields;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserPost;

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

    // User must be logged in
    getUserPost(httpRequest, ", cannot create a new subject");

    requireJSONFields(jsonObject, "title", "institution", "subjectCode", "description");

    String title = jsonObject.getString("title");
    String institution = jsonObject.getString("institution");
    String subjectCode = jsonObject.getString("subjectCode");
    String description = jsonObject.getString("description");

    Subject subject = new Subject(title, institution, subjectCode, description);
    subject.create();

    return subject.createAbout().toString();
  }

}
