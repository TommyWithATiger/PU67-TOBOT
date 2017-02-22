package api.handlers;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
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

    JSONObject loginCheckResponse = new JSONObject();
    loginCheckResponse.put("logged_in", "true");

    return loginCheckResponse.toString();
  }

}
