package api.handlers.relators;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;

import api.exceptions.APIBadRequestException;
import data.dao.SubjectDAO;
import data.Subject;
import data.Topic;
import java.util.Collection;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class APIGetRelatedTopicsSubjectHandler {

  /**
   * A API handler for handling requests for getting topics related to a subject
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "related-topics" which is an JSON array where each
   * topic follows the structure of the topic.createAbout method
   */
  public static String getRelatedTopicsSubjectID(HttpRequest httpRequest){
    checkRequestMethod("GET", httpRequest);

    Integer subjectID = getIntegerURIField(httpRequest, "id");

    Subject subject = SubjectDAO.getInstance().findById(subjectID);

    if (subject == null) {
      throw new APIBadRequestException("No subject with that id exists");
    }

    Collection<Topic> relatedTopics = subject.getTopics();

    JSONObject response = new JSONObject();
    JSONArray topics = new JSONArray();
    relatedTopics.forEach(topic -> topics.put(topic.createAbout()));
    response.put("related_topics", topics);

    return response.toString();
  }

}
