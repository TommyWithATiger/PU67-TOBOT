package api.handlers.relators;

import static api.handlers.topic.APIGetTopicHandler.createAboutTopic;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getArgumentsInURL;
import static api.helpers.isLoggedInHelper.isLoggedIn;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import data.DataAccessObjects.SubjectDAO;
import data.Subject;
import data.Topic;
import java.util.Collection;
import java.util.HashMap;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class APIGetRelatedTopicsSubjectHandler {

  /**
   * A API handler for handling requests for getting topics related to a subject
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "related-topics" which is an JSON array where each
   * topic follows the structure of the createAboutTopic method
   */
  public static String getRelatedTopicsSubjectID(HttpRequest httpRequest){
    checkRequestMethod("GET", httpRequest);

    // Must be logged in
    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot find ratings");
    }

    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    // Require id
    if (!uriArguments.containsKey("id")) {
      throw new APIBadRequestException("id must be given");
    }


    // id must be integer
    int subjectID;
    try {
      subjectID = Integer.parseInt(uriArguments.get("id"));
    } catch (NumberFormatException nfe) {
      throw new APIBadRequestException("id must be integer");
    }

    Subject subject = SubjectDAO.getInstance().findById(subjectID);

    if (subject == null) {
      throw new APIBadRequestException("No subject with that id exists");
    }

    Collection<Topic> relatedTopics = subject.getTopics();

    JSONObject response = new JSONObject();
    JSONArray topics = new JSONArray();
    relatedTopics.forEach(topic -> topics.put(createAboutTopic(topic)));
    response.put("related-topics", topics);

    return response.toString();
  }

}
