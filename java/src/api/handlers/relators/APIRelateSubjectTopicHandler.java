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

  /**
   * A API handler method for relating a subject and a topic together. This require the user to be logged in
   * and the following data:
   *        subjectID (int): the id of the subject
   *        topicID (int): the id of the topic
   *
   * @param httpRequest The request to handle
   * @return A JSON string with two variables:
   *        already-related (boolean): indicates whether the subject and topic was already related
   *        is-related (boolean): indicates whether the subject and topic is related now
   */
  public static String relateSubjectTopicHandler(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    // User must be logged in
    if (!isLoggedIn(httpRequest, jsonObject)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot create a new subject");
    }

    // Require subjectID and topicID
    if (!jsonObject.has("subjectID") || !jsonObject.has("topicID")) {
      throw new APIBadRequestException("Request does not have the required data");
    }

    // subjectID and topicID must be integers
    Integer subjectID, topicID;
    try {
      subjectID = jsonObject.getInt("subjectID");
      topicID = jsonObject.getInt("topicID");
    } catch (JSONException je) {
      throw new APIBadRequestException("Topic or subject ID is not integer");
    }

    Subject subject = SubjectDAO.getInstance().findById(subjectID);
    Topic topic = TopicDAO.getInstance().findById(topicID);

    if (subject == null || topic == null) {
      throw new APIBadRequestException("One of the id's does not exists");
    }

    JSONObject response = new JSONObject();
    response.put("already-related", subject.hasTopic(topic));

    // Relate them if they are not already related
    if (!subject.hasTopic(topic)) {
      topic.addToSubject(subject);
      topic.update();
      subject.update();
    }

    response.put("is-related", subject.hasTopic(topic));

    return response.toString();
  }

}
