package api.handlers.user;

import static api.helpers.JSONCheckerHelper.getJSONFields;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.dao.UserDAO;
import data.user.User;
import java.util.List;
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

    List<String> fields = getJSONFields(httpRequest, String.class, "username", "token");

    User user = UserDAO.getInstance().findUserByUsername(fields.get(0));
    String token = fields.get(1);

    if (user == null) {
      throw new APIBadRequestException("User does not exist");
    }

    if (token.equals(user.getSessionToken())) {
      user.logout();
    }

    user.update();

    JSONObject logoutResponse = new JSONObject();
    logoutResponse.put("logged_out", user.getSessionToken() == null);

    return logoutResponse.toString();
  }

}
