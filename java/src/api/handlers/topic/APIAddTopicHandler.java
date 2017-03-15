package api.handlers.topic;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromHeader;

import data.Topic;
import org.apache.http.HttpRequest;

public class APIAddTopicHandler {

  /**
   * An API handler for adding topics. Requires the user to be logged in and the following data:
   *        title (String): the topic title
   *        description (String): the topic description
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the data from topic.createAbout
   */
  public static String handleAddTopicRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    getUserFromHeader(httpRequest, ", cannot create a new topic");

    String title = getJSONField(httpRequest, String.class, "title");
    String description = getJSONField(httpRequest, String.class, "description");

    Topic topic = new Topic(title, description);
    topic.create();

    return topic.createAbout().toString();
  }
}
