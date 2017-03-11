package api.handlers.user;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.isLoggedIn;

import api.exceptions.APIRequestForbiddenException;
import data.dao.UserDAO;
import data.user.User;
import data.user.UserTypeConverter;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIGetUserInfoHandler {

  /**
   * An API handler for a user to get it's user information. Requires the user to be logged in.
   *
   * @param httpRequest the request to handle
   * @return A JSON object with the following variables:
   *        username (String): the username
   *        token (String): the user token
   *        userType (String): The user type
   *        email (String): the user email
   */
  public static String getUserInfo(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    if (!isLoggedIn(httpRequest)) {
      throw new APIRequestForbiddenException("User must be logged in to get info");
    }

    String username = httpRequest.getFirstHeader("X-Username").getValue();
    // Will never be null due to login check above
    User user = UserDAO.getInstance().findUserByUsername(username);

    JSONObject response = new JSONObject();

    response.put("username", user.getUsername());
    response.put("token", user.getSessionToken());
    response.put("userType", UserTypeConverter.userTypeToString(user.getUserType()));
    response.put("email", user.getEmail());

    return response.toString();
  }

}
