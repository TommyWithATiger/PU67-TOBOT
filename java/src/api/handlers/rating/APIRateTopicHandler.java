package api.handlers.rating;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
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
import data.rating.RatingEnum;
import data.rating.RatingKey;
import org.apache.http.HttpRequest;
import org.json.JSONException;
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

    // The user must be logged in
    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot create a new subject");
    }

    // Require topic id
    if (!jsonObject.has("topicID") || !jsonObject.has("rating")) {
      throw new APIBadRequestException("topicID must be set");
    }


    // Check that there is a valid rating
    String ratingValue = jsonObject.getString("rating");
    RatingEnum ratingEnum;
    try {
      ratingEnum = RatingConverter.convertFullRatingNameToEnum(ratingValue);
    } catch (IllegalArgumentException iae) {
      throw new APIBadRequestException("rating is not a valid rating");
    }

    // Check topicID is integer
    int topicID;
    try {
      topicID = jsonObject.getInt("topicID");
    } catch (JSONException je){
      throw new APIBadRequestException("topicID must be integer");
    }

    // Check topic exists
    Topic topic = TopicDAO.getInstance().findById(topicID);
    if (topic == null) {
      throw new APIBadRequestException("No topic with the given id");
    }

    String username = httpRequest.getFirstHeader("X-Username").getValue();
    // Will never be null due to login check above
    User user = UserDAO.getInstance().findUserByUsername(username);

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
