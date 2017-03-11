package api.handlers.subject;

import static api.helpers.JSONCheckerHelper.getJSONFields;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromHeader;

import data.Subject;
import java.util.List;
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
    getUserFromHeader(httpRequest, ", cannot create a new subject");

    List<String> fields = getJSONFields(httpRequest, String.class,
        "title", "institution", "subjectCode", "description");

    String title = fields.get(0);
    String institution = fields.get(1);
    String subjectCode = fields.get(2);
    String description = fields.get(3);

    Subject subject = new Subject(title, institution, subjectCode, description);
    subject.create();

    return subject.createAbout().toString();
  }

}
