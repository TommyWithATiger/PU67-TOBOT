package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILogoutHandler {

  public static String handleLogoutRequest(HttpRequest httpRequest){
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = new JSONObject(requestContent);

    if (!jsonObject.has("username") || !jsonObject.has("token")){
      throw new APIBadRequestException("Logout data not complete");
    }

    String username = jsonObject.getString("username");
    String password = jsonObject.getString("password");

    JSONObject logoutResponse = new JSONObject();
    logoutResponse.put("logged_out", "true");

    return logoutResponse.toString();
  }

}
