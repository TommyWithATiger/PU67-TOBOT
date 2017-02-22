package api.helpers;

import data.DataAccessObjects.UserDAO;
import data.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class isLoggedInHelper {

  public static boolean isLoggedIn(HttpRequest httpRequest, JSONObject jsonObject) {

    if (!jsonObject.has("username") || !httpRequest.containsHeader("Authentication")) {
      return false;
    }

    String userName = jsonObject.getString("username");
    String token = httpRequest.getFirstHeader("Authentication").getValue().substring(7);

    User user = UserDAO.getInstance().findUserByUsername(userName);

    return user != null && user.checkUserSessionToken(token);
  }

}
