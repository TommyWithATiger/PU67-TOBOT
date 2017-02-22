package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.DataAccessObjects.UserDAO;
import data.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILoginHandler {

  public static String handleLoginRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = new JSONObject(requestContent);

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
    }

    String token = user.getSessionToken();
    user.generateSessionTokenExpireDate();

    JSONObject loginResponse = new JSONObject();
    loginResponse.put("username", username);
    loginResponse.put("token", token);

    return loginResponse.toString();
  }



}
