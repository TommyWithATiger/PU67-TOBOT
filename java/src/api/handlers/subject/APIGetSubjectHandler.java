package api.handlers.subject;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getArgumentsInURL;

import api.exceptions.APIBadRequestException;
import data.dao.SubjectDAO;
import data.Subject;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
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

    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    // Require id
    if (!uriArguments.containsKey("id")) {
      throw new APIBadRequestException("id must be given");
    }

    int subjectID;

    // Subject id must be integer
    try {
      subjectID = Integer.parseInt(uriArguments.get("id"));
    } catch (NumberFormatException nfe) {
      throw new APIBadRequestException("id must be integer");
    }

    Subject subject = SubjectDAO.getInstance().findById(subjectID);

    if (subject == null) {
      throw new APIBadRequestException("No subject with the given id");
    }

    JSONObject response = subject.createAbout();
    return response.toString();
  }

  /**
   * An API handler for finding subjects by title. Require the following data:
   *        title (String): the subject title to search for
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "subjects" which is an array of JSON objects on the
   * from of the createAboutSubject method below for each subject
   */
  public static String getSubjectsByTitle(HttpRequest httpRequest) {
    checkRequestMethod("GET", httpRequest);

    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    // Require title
    if (!uriArguments.containsKey("title")) {
      throw new APIBadRequestException("title must be given");
    }

    String subjectTitle = uriArguments.get("title");

    List<Subject> subjects = SubjectDAO.getInstance().findSubjectsByTitle(subjectTitle);
    subjects.sort(Comparator.comparing(Subject::getTitle));

    JSONObject response = new JSONObject();
    JSONArray subjectArray = new JSONArray();
    subjects.forEach(subject -> subjectArray.put(subject.createAbout()));
    response.put("subjects", subjectArray);

    return response.toString();
  }

  /**
   * An API handler for finding all subjects
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "subjects" which is an array of JSON objects on the
   * from of the createAboutSubject method below for each subject
   */
  public static String getAllSubjects(HttpRequest httpRequest){
    checkRequestMethod("GET", httpRequest);

    List<Subject> subjects = SubjectDAO.getInstance().findAll();
    subjects.sort(Comparator.comparing(Subject::getTitle));

    JSONObject response = new JSONObject();
    JSONArray subjectArray = new JSONArray();
    subjects.forEach(subject -> subjectArray.put(subject.createAbout()));
    response.put("subjects", subjectArray);

    return response.toString();
  }

}
