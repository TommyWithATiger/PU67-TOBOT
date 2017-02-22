package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.DataAccessObjects.UserDAO;
import data.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILoggedInCheckHandler {

  public static String handleLoggedInCheckRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = new JSONObject(requestContent);

    if (!jsonObject.has("username") || !jsonObject.has("token")) {
      throw new APIBadRequestException("Login check data not complete");
    }

    String username = jsonObject.getString("username");
    String token = jsonObject.getString("token");

    User user = UserDAO.getInstance().findUserByUsername(username);
    if (user == null) {
      throw new APIBadRequestException("User does not exist");
    }

    boolean userLoggedIn = user.checkUserSessionToken(token);

    user.update();

    JSONObject loginCheckResponse = new JSONObject();
    loginCheckResponse.put("logged_in", String.valueOf(userLoggedIn));

    return loginCheckResponse.toString();
  }

}
