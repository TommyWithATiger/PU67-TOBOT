package api.handlers.rating;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getArgumentsInURL;
import static api.helpers.isLoggedInHelper.isLoggedIn;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import data.DataAccessObjects.RatingDAO;
import data.DataAccessObjects.TopicDAO;
import data.DataAccessObjects.UserDAO;
import data.Topic;
import data.User;
import data.rating.Rating;
import data.rating.RatingConverter;
import data.rating.RatingKey;
import java.util.HashMap;
import org.apache.http.HttpRequest;
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
    checkRequestMethod("GET", httpRequest);

    // Must be logged in
    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User is not logged in, cannot find ratings");
    }

    HashMap<String, String> uriArguments = getArgumentsInURL(httpRequest);

    // Require title
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

    Rating rating = RatingDAO.getInstance()
        .findRatingByRatingKey(new RatingKey(user.getId(), topicID));

    if (rating == null) {
      throw new APIBadRequestException("No rating for the given subject");
    }

    JSONObject response = new JSONObject();
    response.put("rating", RatingConverter.convertEnumToFullRatingName(rating.getRatingEnum()));
    return response.toString();
  }

}
