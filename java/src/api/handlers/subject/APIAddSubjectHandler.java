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

  public static String handleAddSubject(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!isLoggedIn(httpRequest, jsonObject)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot create a new subject");
    }

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
