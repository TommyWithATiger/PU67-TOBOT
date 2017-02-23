package api.handlers.subject;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.DataAccessObjects.SubjectDAO;
import data.Subject;
import java.util.List;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIGetSubjectHandler {

  /**
   * An API handler for getting information about a subject from the subject id. Requires the
   * following data:
   *        id (int): the subject id
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing the following variables
   *        id (int): the subject id
   *        title (String): the subject title
   *        description (String): the subject description
   *        institution (String): the institution that arranges the subject
   *        subjectCode (String): the subject code
   */
  public static String getSubjectByID(HttpRequest httpRequest) {
    checkRequestMethod("GET", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    // Require id
    if (!jsonObject.has("id")) {
      throw new APIBadRequestException("id must be given");
    }

    int subjectID;

    // Subject id must be integer
    try {
      subjectID = jsonObject.getInt("id");
    } catch (JSONException je) {
      throw new APIBadRequestException("JSON must have an integer id");
    }

    Subject subject = SubjectDAO.getInstance().findById(subjectID);

    if (subject == null) {
      throw new APIBadRequestException("No subject with the given id");
    }

    JSONObject response = createAboutSubject(subject);

    return response.toString();
  }

  /**
   * An API handler for finding subjects by title. Require the following data:
   *        title (String): the subject title to search for
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "subjects" which is an array of JSON objects on the
   * form of the createAboutSubject method below for each subject
   */
  public static String getSubjectsByTitle(HttpRequest httpRequest) {
    checkRequestMethod("GET", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    // Require title
    if (!jsonObject.has("title")) {
      throw new APIBadRequestException("title must be given");
    }

    String subjectTitle = jsonObject.getString("title");

    List<Subject> subjects = SubjectDAO.getInstance().findSubjectByTitle(subjectTitle);

    JSONObject response = new JSONObject();
    JSONArray subjectArray = new JSONArray();
    subjects.forEach(subject -> subjectArray.put(createAboutSubject(subject)));
    response.put("subjects", subjectArray);

    return response.toString();
  }

  /**
   * Creates a JSON object from a subject
   *
   * @param subject The subject from which to create the JSON object
   * @return A JSON object with the following data
   *        id (int): the subject id
   *        title (String): the subject title
   *        description (String): the subject description
   *        institution (String): the institution that arranges the subject
   *        subjectCode (String): the subject code
   */
  private static JSONObject createAboutSubject(Subject subject) {
    JSONObject aboutSubject = new JSONObject();
    aboutSubject.put("id", subject.getId());
    aboutSubject.put("title", subject.getTitle());
    aboutSubject.put("description", subject.getDescription());
    aboutSubject.put("institution", subject.getInstitution());
    aboutSubject.put("subjectCode", subject.getSubjectCode());
    return aboutSubject;
  }

}
