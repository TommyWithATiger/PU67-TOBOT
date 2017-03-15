package api.handlers.user;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.dao.UserDAO;
import data.user.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILoggedInCheckHandler {

  /**
   * A handler for handling request checking if the current login data is valid. Requires the data:
   *        username (String): the username
   *        token (String): the session token
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the following variables:
   *        logged_in (boolean): indicates if the user logged in
   */
  public static String handleLoggedInCheckRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    User user = UserDAO.getInstance().findUserByUsername(getJSONField(httpRequest, String.class, "username"));
    String token = getJSONField(httpRequest, String.class, "token");

    if (user == null) {
      throw new APIBadRequestException("User does not exist");
    }

    boolean userLoggedIn = user.checkUserSessionToken(token);

    user.update();

    JSONObject loginCheckResponse = new JSONObject();
    loginCheckResponse.put("logged_in", userLoggedIn);

    return loginCheckResponse.toString();
  }

}
