package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.DataAccessObjects.UserDAO;
import data.user.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILogoutHandler {

  /**
   * An API handler for handling logout requests. Require the following data:
   *        username (String): The username
   *        token (String): The session token
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the following variable
   *        logged_out (boolean): A variable indicating if the user has been logged out
   */
  public static String handleLogoutRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!jsonObject.has("username") || !jsonObject.has("token")) {
      throw new APIBadRequestException("Logout data not complete");
    }

    String username = jsonObject.getString("username");
    String token = jsonObject.getString("token");

    User user = UserDAO.getInstance().findUserByUsername(username);

    if (user == null) {
      throw new APIBadRequestException("User does not exist");
    }

    if (token.equals(user.getSessionToken())) {
      user.logout();
    }

    user.update();

    JSONObject logoutResponse = new JSONObject();
    logoutResponse.put("logged_out", String.valueOf(user.getSessionToken() == null));

    return logoutResponse.toString();
  }

}
