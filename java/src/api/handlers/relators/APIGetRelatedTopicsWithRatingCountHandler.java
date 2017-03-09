package api.handlers.relators;

import static api.handlers.topic.APIGetTopicHandler.createAboutTopic;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getArgumentsInURL;

import api.exceptions.APIBadRequestException;
import data.Subject;
import data.Topic;
import data.dao.RatingDAO;
import data.dao.SubjectDAO;
import data.rating.Rating;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.IntStream;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class APIGetRelatedTopicsWithRatingCountHandler {

  /**
   * A API handler for handling requests for getting topics related to a subject, with information
   * about the frequencies of ratings from participating users.
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "related-topics" which is an JSON array where each
   * topic follows the structure of the createAboutTopicWithRatingCount method
   */
  public static String getTopicWithRatingCountSubjectID(HttpRequest httpRequest){
    checkRequestMethod("GET", httpRequest);

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
    relatedTopics.forEach(topic -> topics.put(createAboutTopicWithRatingCount(topic, subject)));
    response.put("related_topics", topics);

    return response.toString();
  }

  /**
   * Creates a JSON object with information about a topic
   *
   * @param topic The topic to create a JSON object about
   * @return A JSON object with all the data in createAboutTopic.
   *        Also has an additional ratingCount field, which holds an array of frequencies of each
   *        rating. ratingCount[1] = 5 means five participants have rated the topic as 1 star, etc.
   */
  public static JSONObject createAboutTopicWithRatingCount(Topic topic, Subject subject){
    JSONObject aboutTopic = createAboutTopic(topic);

    int[] starFrequency = new int[] {0, 0, 0, 0, 0, 0};
    Collection<Rating> ratings = RatingDAO.getInstance()
        .findParticipatingRatingBySubjectTopic(subject, topic);
    if (ratings == null){
      // no participants have rated themselves, so everyone is at 0 stars
      starFrequency[0] = subject.getParticipants().size();
    }
    else{
      ratings.forEach(rating -> starFrequency[rating.getIntRating()] += 1);
      starFrequency[0] = subject.getParticipants().size() - IntStream.of(starFrequency).sum();
    }
    aboutTopic.put("ratingCount", new JSONArray(starFrequency));
    return aboutTopic;
  }

}

