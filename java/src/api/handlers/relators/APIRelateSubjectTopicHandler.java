package api.handlers.relators;

import static api.helpers.JSONCheckerHelper.getJSONFields;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromHeader;

import api.exceptions.APIBadRequestException;
import data.dao.SubjectDAO;
import data.dao.TopicDAO;
import data.Subject;
import data.Topic;
import java.util.List;
import org.apache.http.HttpRequest;
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

    // User must be logged in
    getUserFromHeader(httpRequest, ", cannot create a new subject");

    List<Integer> fields = getJSONFields(httpRequest, Integer.class,
        "subjectID", "topicID");

    Subject subject = SubjectDAO.getInstance().findById(fields.get(0));
    Topic topic = TopicDAO.getInstance().findById(fields.get(1));

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
