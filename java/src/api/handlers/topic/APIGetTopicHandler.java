package api.handlers.topic;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.DataAccessObjects.TopicDAO;
import data.Topic;
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

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!jsonObject.has("id")) {
      throw new APIBadRequestException("id must be given");
    }

    int topicID;

    // Subject id must be integer
    try {
      topicID = jsonObject.getInt("id");
    } catch (JSONException je) {
      throw new APIBadRequestException("JSON must have an integer id");
    }

    Topic topic = TopicDAO.getInstance().findById(topicID);

    if (topic == null){
      throw new APIBadRequestException("No topic with the given id");
    }

    return createAboutTopic(topic).toString();
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

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    // Require title
    if (!jsonObject.has("title")) {
      throw new APIBadRequestException("title must be given");
    }

    String title = jsonObject.getString("title");

    List<Topic> topics = TopicDAO.getInstance().findTopicByTitle(title);

    JSONObject response = new JSONObject();
    JSONArray topicArray = new JSONArray();
    topics.forEach(topic -> topicArray.put(createAboutTopic(topic)));
    response.put("topics", topicArray);
    return response.toString();
  }

  /**
   * Creates a JSON object with information about a topic
   *
   * @param topic The topic to create a JSON object about
   * @return A JSON object with the following data:
   *        id (int): the topic id
   *        title (String): the topic title
   *        description (String): the topic description
   */
  private static JSONObject createAboutTopic(Topic topic){
    JSONObject aboutTopic = new JSONObject();
    aboutTopic.put("id", topic.getId());
    aboutTopic.put("title", topic.getTitle());
    aboutTopic.put("description", topic.getDescription());
    return aboutTopic;
  }

}
