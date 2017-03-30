package api.handlers.topic;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;
import static api.helpers.UrlArgumentHelper.getURIField;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import data.Subject;
import data.dao.SubjectDAO;
import data.dao.TopicDAO;
import data.Topic;
import data.user.User;
import java.util.List;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIGetTopicHandler {

  /**
   * An API handler for getting information about a topic given its id. Require the following data:
   *        id (int): the topic id
   *
   * @param httpRequest The request to handle
   * @return A JSON object on the form of the createAboutTopic method.
   */
  public static String getTopicByID(HttpRequest httpRequest){
    checkRequestMethod("GET", httpRequest);

    Integer topicID = getIntegerURIField(httpRequest, "id");

    Topic topic = TopicDAO.getInstance().findById(topicID);

    if (topic == null){
      throw new APIBadRequestException("No topic with the given id");
    }

    return topic.createAbout().toString();
  }

  /**
   * An API handler for getting topics by title. Require the following data:
   *        title (String): the topic title
   *
   * @param httpRequest The request to handle
   * @return A JSON object with the variable topics which is an array of JSON objects on the form of
   * the createAboutTopic method for each of the topics.
   */
  public static String getTopicsByTitle(HttpRequest httpRequest){
    checkRequestMethod("GET", httpRequest);

    String title = getURIField(httpRequest, "title");

    List<Topic> topics = TopicDAO.getInstance().findTopicsByTitle(title);

    JSONObject response = new JSONObject();
    JSONArray topicArray = new JSONArray();
    topics.forEach(topic -> topicArray.put(topic.createAbout()));
    response.put("topics", topicArray);
    return response.toString();
  }

  /**
   * An API handler for finding all topics
   *
   * @param httpRequest The request to handle
   * @return A JSON object with the variable topics which is an array of JSON objects on the form of
   * the createAboutTopic method for each of the topics.
   */
  public static String getAllTopics(HttpRequest httpRequest){
    checkRequestMethod("GET", httpRequest);

    List<Topic> topics = TopicDAO.getInstance().findAll();

    JSONObject response = new JSONObject();
    JSONArray topicArray = new JSONArray();
    topics.forEach(topic -> topicArray.put(topic.createAbout()));
    response.put("topics", topicArray);
    return response.toString();
  }

  /**
   * An API handler for getting all topics for the user, sorted by lowest rating, in a subject.
   * Does not return topics that are not rated (no stars). Requires the user to be logged in.
   *
   * @param httpRequest The request to handle
   * @return A JSON object with the variable topics which is an array of JSON objects on the form of
   * the topic.createAbout method for each of the topics.
   */
  public static String getTopicsBySubjectSortedByRating(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    User user = getUserFromRequest(httpRequest, ", cannot find topics");

    Subject subject = SubjectDAO.getInstance().findById(getJSONField(httpRequest, Integer.class, "subjectID"));
    if (subject == null) {
      throw new APIBadRequestException("no subject with the given id exists");
    }

    List<Topic> topics = TopicDAO.getInstance().findTopicBySubjectUserSortedByRating(subject, user);

    JSONObject response = new JSONObject();
    JSONArray topicArray = new JSONArray(topics.stream().map(Topic::createAbout).toArray());
    response.put("topics", topicArray);

    return response.toString();
  }

}
