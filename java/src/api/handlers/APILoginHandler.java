package api.handlers;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
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

    JSONObject loginResponse = new JSONObject();
    loginResponse.put("username", username);
    loginResponse.put("token", password);

    return loginResponse.toString();
  }



}
