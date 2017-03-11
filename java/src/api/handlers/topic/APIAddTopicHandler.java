package api.handlers.topic;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserPost;

import api.exceptions.APIBadRequestException;
import data.Topic;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIAddTopicHandler {

  /**
   * An API handler for adding topics. Requires the user to be logged in and the following data:
   *        title (String): the topic title
   *        description (String): the topic description
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the following data:
   *        title (String): the topic title
   *        description (String): the topic description
   *        id (int): the topic id
   */
  public static String handleAddTopicRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    // User must be logged in
    getUserPost(httpRequest, ", cannot create a new topic");

    // Require title and description
    if (!jsonObject.has("title") || !jsonObject.has("description")) {
      throw new APIBadRequestException("Topic information is not complete");
    }

    String title = jsonObject.getString("title");
    String description = jsonObject.getString("description");

    Topic topic = new Topic(title, description);
    topic.create();

    return topic.createAbout().toString();
  }

}
