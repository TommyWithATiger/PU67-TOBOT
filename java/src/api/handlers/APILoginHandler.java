package api.handlers;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class APILoginHandler {

  public static String handleLoginRequest(HttpRequest httpRequest) {
    if (!(httpRequest instanceof HttpEntityEnclosingRequest)) {
      throw new APIBadRequestException("No login data");
    }

    HttpEntity httpEntity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
    String requestContent;

    try {
      requestContent = EntityUtils.toString(httpEntity);
    } catch (IOException e) {
      throw new APIErrorException("Could not read entity");
    }

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
