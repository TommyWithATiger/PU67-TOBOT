package api.handlers.rating;

import static api.handlers.topic.APIGetTopicHandler.createAboutTopic;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getArgumentsInURL;
import static api.helpers.isLoggedInHelper.isLoggedIn;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import data.dao.RatingDAO;
import data.dao.TopicDAO;
import data.dao.UserDAO;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingConverter;
import data.rating.RatingKey;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class APIGetTopicRatingHandler {

  /**
   * An API handler for getting the users rating of a given topic by the topic id. Requires the user
   * to be logged in.
   *
   * @param httpRequest The request to handle
   * @return A JSON object with the following variable:
   *        rating (String): the rating of the topic, has 5 valid values
   *                            None, Poor, Ok, Good, Superb
   */
  public static String getTopicRatingByTopicID(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // Must be logged in
    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot find ratings");
    }

    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    // Require id
    if (!uriArguments.containsKey("id")) {
      throw new APIBadRequestException("id must be given");
    }

    int topicID;
    try {
      topicID = Integer.parseInt(uriArguments.get("id"));
    } catch (NumberFormatException nfe) {
      throw new APIBadRequestException("id must be integer");
    }

    Topic topic = TopicDAO.getInstance().findById(topicID);

    if (topic == null) {
      throw new APIBadRequestException("no topic with the given id exists");
    }

    String username = httpRequest.getFirstHeader("X-Username").getValue();
    // Will never be null due to login check above
    User user = UserDAO.getInstance().findUserByUsername(username);

    Rating rating = RatingDAO.getInstance().findById(new RatingKey(user.getId(), topicID));

    if (rating == null) {
      throw new APIBadRequestException("No rating for the given subject");
    }

    JSONObject response = new JSONObject();
    response.put("rating", RatingConverter.convertEnumToFullRatingName(rating.getRating()));
    return response.toString();
  }

  /**
   * An API handler for getting all topic ratings for a user
   *
   * @param httpRequest The request to handle
   * @return A JSON object consisting of a variable "ratings" which is an array of JSON objects,
   * where each JSON object represents a single rating. These JSON objects have the following variables:
   *        rating (String): the rating of the topic, has 5 valid values
   *                            None, Poor, Ok, Good, Superb
   *        topicID (int): the topic id
   */
  public static String getTopicRatings(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot find ratings");
    }

    String username = httpRequest.getFirstHeader("X-Username").getValue();
    // Will never be null due to login check above
    User user = UserDAO.getInstance().findUserByUsername(username);

    List<Rating> ratings = RatingDAO.getInstance().findRatingByUser(user);

    JSONObject response = new JSONObject();
    JSONArray ratingArray = new JSONArray();
    ratings.forEach(rating -> {
      JSONObject ratingJSON = new JSONObject();
      ratingJSON.put("rating", RatingConverter.convertEnumToFullRatingName(rating.getRating()));
      ratingJSON.put("topicID", rating.getTopicID());
      ratingArray.put(ratingJSON);
    });
    response.put("ratings", ratingArray);

    return response.toString();
  }

  /**
   * An API handler for getting all topics with rating if the rating exists
   *
   * @param httpRequest The request to handle
   * @return A JSON object consisting of a variable "ratings" which is an array of JSON objects,
   * where each JSON object represents a single rating. These JSON objects have the following variables:
   *        id (int): topicID of the topic
   *        title (String): Title of the topic
   *        description (String): Description of the topic
   *        has-rating (Boolean): Indicates if there is a rating for the given topic
   *        rating (String)[If 'has-rating' is 'true']: the rating of the topic, has 5 valid values
   *                            None, Poor, Ok, Good, Superb
   */
  public static String getTopicsWithRatings(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot find ratings");
    }

    String username = httpRequest.getFirstHeader("X-Username").getValue();
    // Will never be null due to login check above
    User user = UserDAO.getInstance().findUserByUsername(username);

    List<Topic> topics = TopicDAO.getInstance().findAll();
    List<Rating> ratingsUser = RatingDAO.getInstance().findRatingByUser(user);

    JSONObject response = new JSONObject();
    JSONArray topicArray = new JSONArray();
    topics.forEach(topic -> {
      JSONObject aboutTopic = createAboutTopic(topic);
      Optional<Rating> ratingTopic = ratingsUser.stream()
          .filter((rating) -> rating.getTopicID() == topic.getId()).findFirst();
      aboutTopic.put("has-rating", ratingTopic.isPresent());
      ratingTopic.ifPresent(rating -> aboutTopic.put("rating",
          RatingConverter.convertEnumToFullRatingName(rating.getRating())));
    });
    response.put("topics", topicArray);

    return response.toString();
  }

}
