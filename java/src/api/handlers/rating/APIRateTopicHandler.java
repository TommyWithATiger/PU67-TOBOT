package api.handlers.rating;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.JSONCheckerHelper.getJSONFields;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserPost;

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

    String requestContent = checkAndGetEntityContent(httpRequest);
    JSONObject jsonObject = checkAndGetJSON(requestContent);

    User user = getUserPost(httpRequest, ", cannot create a new subject");

    String ratingValue = getJSONFields(jsonObject, String.class, "rating").get(0);
    RatingEnum ratingEnum;
    try {
      ratingEnum = RatingConverter.convertFullRatingNameToEnum(ratingValue);
    } catch (IllegalArgumentException iae) {
      throw new APIBadRequestException("rating is not a valid rating");
    }

    Integer topicID = getJSONFields(jsonObject, Integer.class, "topicID").get(0);

    // Check topic exists
    Topic topic = TopicDAO.getInstance().findById(topicID);
    if (topic == null) {
      throw new APIBadRequestException("No topic with the given id");
    }

    // Either update an old rating or create a new one
    Rating rating = RatingDAO.getInstance().findById(new RatingKey(user.getId(), topic.getId()));
    if (rating == null) {
      rating = new Rating(user.getId(), topicID, ratingEnum);
      rating.create();
    } else {
      rating.setRating(ratingEnum);
      rating.update();
    }

    JSONObject response = new JSONObject();
    response.put("topicID", topicID);
    response.put("rating", ratingValue);
    return response.toString();
  }

}
