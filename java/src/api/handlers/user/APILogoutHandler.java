package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.DataAccessObjects.UserDAO;
import data.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILogoutHandler {

  public static String handleLogoutRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = new JSONObject(requestContent);

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
