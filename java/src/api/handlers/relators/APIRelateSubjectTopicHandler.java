package api.handlers.relators;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.isLoggedIn;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import data.DataAccessObjects.SubjectDAO;
import data.DataAccessObjects.TopicDAO;
import data.Subject;
import data.Topic;
import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class APIRelateSubjectTopicHandler {

  public static String relateSubjectTopicHandler(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!isLoggedIn(httpRequest, jsonObject)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot create a new subject");
    }

    if (!jsonObject.has("subjectID") || !jsonObject.has("topicID")) {
      throw new APIBadRequestException("Request does not have the required data");
    }

    Integer subjectID, topicID;
    try {
      subjectID = jsonObject.getInt("subjectID");
      topicID = jsonObject.getInt("topicID");
    } catch (JSONException je){
      throw new APIBadRequestException("Topic or subject ID is not integer");
    }

    Subject subject = SubjectDAO.getInstance().findById(subjectID);
    Topic topic = TopicDAO.getInstance().findById(topicID);

    if (subject == null || topic == null) {
      throw new APIBadRequestException("One of the id's does not exists");
    }

    JSONObject response = new JSONObject();
    response.put("already-related", subject.hasTopic(topic));
    if (!subject.hasTopic(topic)) {
      topic.addToSubject(subject);
      topic.update();
      subject.update();
    }

    response.put("is-related", subject.hasTopic(topic));

    return response.toString();
  }

}
