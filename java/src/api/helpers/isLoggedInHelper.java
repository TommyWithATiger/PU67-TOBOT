package api.helpers;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;

import data.DataAccessObjects.UserDAO;
import data.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class isLoggedInHelper {

  public static boolean isLoggedIn(HttpRequest httpRequest) {
    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!jsonObject.has("username") || !httpRequest.containsHeader("Authentication")) {
      return false;
    }

    String userName = jsonObject.getString("username");
    String token = httpRequest.getFirstHeader("Authentication").getValue().substring(7);

    User user = UserDAO.getInstance().findUserByUsername(userName);

    return user != null && user.checkUserSessionToken(token);
  }

}
