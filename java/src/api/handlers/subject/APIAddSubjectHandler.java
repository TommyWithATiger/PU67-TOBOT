package api.handlers.subject;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import data.Subject;
import org.apache.http.HttpRequest;

public class APIAddSubjectHandler {

  /**
   * An API handler for adding subjects. Requires the user to be logged in and the following data:
   *        title (String): the title the subject
   *        institution (String): the institution of the subject
   *        subjectCode (String): the subject code
   *        description (String): the description of the subject
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the data from Subject.createAbout
   */
  public static String handleAddSubject(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    getUserFromRequest(httpRequest, ", cannot create a new subject");

    String title = getJSONField(httpRequest, String.class, "title");
    String institution = getJSONField(httpRequest, String.class, "institution");
    String subjectCode = getJSONField(httpRequest, String.class, "subjectCode");
    String description = getJSONField(httpRequest, String.class, "description");

    Subject subject = new Subject(title, institution, subjectCode, description);
    subject.create();

    return subject.createAbout().toString();
  }

}
