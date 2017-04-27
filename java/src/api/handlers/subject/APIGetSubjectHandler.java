package api.handlers.subject;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getArgumentsInURL;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.Subject;
import data.dao.SubjectDAO;
import data.user.User;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    Integer subjectID = getIntegerURIField(httpRequest, "id");

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
   * form of the subject.createAbout method for each subject
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
   * form of the subject.createAbout method for each subject
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

  /**
   * An API handler for getting all subjects that a user is participating in
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "subjects" which is an array of JSON objects on the
   * from of the subject.createAbout method for each subject
   */
  public static String getParticipatingSubjects(HttpRequest httpRequest){
    return getMemberSubjects(httpRequest, user -> SubjectDAO.getInstance().findSubjectsByParticipant(user));
  }

  /**
   * An API handler for getting all subjects that a user is an editor for
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "subjects" which is an array of JSON objects on the
   * from of the subject.createAbout method for each subject
   */
  public static String getEditorSubjects(HttpRequest httpRequest){
    return getMemberSubjects(httpRequest, user -> SubjectDAO.getInstance().findSubjectsByEditor(user));
  }

  /**
   * An API handler for getting all subjects that a user is a member of either as a participant
   * or as an editor
   *
   * @param httpRequest The request to handle
   * @param getSubjectsFunc a function to get the relevant subjects for this user
   * @return A JSON object containing a variable "subjects" which is an array of JSON objects on the
   * from of the subject.createAbout method for each subject
   */
  private static String getMemberSubjects(HttpRequest httpRequest,
      Function<User, List<Subject>> getSubjectsFunc){
    checkRequestMethod("POST", httpRequest);

    User user = getUserFromRequest(httpRequest, ", cannot get subjects");

    List<Subject> subjects = getSubjectsFunc.apply(user);

    JSONObject response = new JSONObject();
    JSONArray subjectArray = new JSONArray(subjects.stream().map(Subject::createAbout).collect(
        Collectors.toList()));
    response.put("subjects", subjectArray);

    return response.toString();
  }

}
