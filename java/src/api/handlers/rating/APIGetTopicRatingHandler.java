package api.handlers.rating;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;
import static api.helpers.isLoggedInHelper.getUserFromHeader;

import api.exceptions.APIBadRequestException;
import data.dao.RatingDAO;
import data.dao.TopicDAO;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingConverter;
import data.rating.RatingKey;
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

    User user = getUserFromHeader(httpRequest, ", cannot find ratings");

    Integer topicID = getIntegerURIField(httpRequest, "id");

    Topic topic = TopicDAO.getInstance().findById(topicID);

    if (topic == null) {
      throw new APIBadRequestException("no topic with the given id exists");
    }

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

    User user = getUserFromHeader(httpRequest, ", cannot find ratings");

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
   * where each JSON object represents a single rating. These JSON objects have all the variables
   * from topic.createAbout, and:
   *        has-rating (Boolean): Indicates if there is a rating for the given topic
   *        rating (String)[If 'has-rating' is 'true']: the rating of the topic, has 5 valid values
   *                            None, Poor, Ok, Good, Superb
   */
  public static String getTopicsWithRatings(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    User user = getUserFromHeader(httpRequest, ", cannot find ratings");

    List<Topic> topics = TopicDAO.getInstance().findAll();
    List<Rating> ratingsUser = RatingDAO.getInstance().findRatingByUser(user);

    JSONObject response = new JSONObject();
    JSONArray topicArray = new JSONArray();
    topics.forEach(topic -> {
      JSONObject aboutTopic = topic.createAbout();
      Optional<Rating> ratingTopic = ratingsUser.stream()
          .filter((rating) -> rating.getTopicID() == topic.getId()).findFirst();
      aboutTopic.put("has-rating", ratingTopic.isPresent());
      ratingTopic.ifPresent(rating -> aboutTopic.put("rating",
          RatingConverter.convertEnumToFullRatingName(rating.getRating())));
      topicArray.put(aboutTopic);
    });
    response.put("topics", topicArray);

    return response.toString();
  }

}
