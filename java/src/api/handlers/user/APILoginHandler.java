package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.dao.UserDAO;
import data.user.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILoginHandler {

  /**
   * An API handler method for handling login of a user. Requires the following data:
   *        username (String): the username
   *        password (String): the password
   *
   * @param httpRequest The request to handle
   * @return A JSON string containing the following data
   *        username (String): the username
   *        token (String): the session token
   */
  public static String handleLoginRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    // Require username and password
    if (!jsonObject.has("username") || !jsonObject.has("password")) {
      throw new APIBadRequestException("Login data not complete");
    }

    String username = jsonObject.getString("username");
    String password = jsonObject.getString("password");

    User user = UserDAO.getInstance().findUserByUsername(username);
    if (user == null) {
      throw new APIBadRequestException("User not found");
    }

    if (!user.checkPassword(password)) {
      throw new APIBadRequestException("Password incorrect");
    }

    if (user.getSessionToken() == null) {
      user.createSessionToken();
    } else{
      user.generateSessionTokenExpireDate();
    }

    String token = user.getSessionToken();

    user.update();

    JSONObject loginResponse = new JSONObject();
    loginResponse.put("username", username);
    loginResponse.put("token", token);

    return loginResponse.toString();
  }



}
