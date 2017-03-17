package api.handlers.rating;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.dao.RatingDAO;
import data.dao.TopicDAO;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingConverter;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIRateTopicHandler {

  /**
   * An API handler for a user to rate its knowledge in a subject. If a rating exists it is updated
   * instead. Requires the user to be logged in, and the following data:
   *        topicID (int): the topic id
   *        rating (String): the rating of the topic, has 5 valid values
   *                            None, Poor, Ok, Good, Superb
   *
   * @param httpRequest the request to handle
   * @return A JSON object with the following variables:
   *        topicID (int): the topic id
   *        rating (String): the rating of the topic, has 5 valid values
   *                            None, Poor, Ok, Good, Superb
   */
  public static String rateTopic(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    User user = getUserFromRequest(httpRequest, ", cannot create a new subject");

    String ratingValue = getJSONField(httpRequest, String.class, "rating");
    RatingEnum ratingEnum;
    try {
      ratingEnum = RatingConverter.convertFullRatingNameToEnum(ratingValue);
    } catch (IllegalArgumentException iae) {
      throw new APIBadRequestException("rating is not a valid rating");
    }

    // Check topic exists
    Topic topic = TopicDAO.getInstance().findById(getJSONField(httpRequest, Integer.class, "topicID"));
    if (topic == null) {
      throw new APIBadRequestException("No topic with the given id");
    }

    // Either update an old rating or create a new one
    Rating rating = RatingDAO.getInstance().findById(new RatingKey(user.getId(), topic.getId()));
    if (rating == null) {
      rating = new Rating(user.getId(), topic.getId(), ratingEnum);
      rating.create();
    } else {
      rating.setRating(ratingEnum);
      rating.update();
    }

    JSONObject response = new JSONObject();
    response.put("topicID", topic.getId());
    response.put("rating", ratingValue);
    return response.toString();
  }

}
