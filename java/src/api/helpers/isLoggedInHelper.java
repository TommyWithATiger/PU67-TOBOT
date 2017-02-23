package api.helpers;

import data.DataAccessObjects.UserDAO;
import data.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class isLoggedInHelper {

  /**
   * A helper method for checking if the user is logged in
   * @param httpRequest The request
   * @param jsonObject The data object of the request
   * @return A boolean indicating if the user is logged in
   */
  public static boolean isLoggedIn(HttpRequest httpRequest, JSONObject jsonObject) {

    // Must have username in the data and the Authentication header set
    if (!jsonObject.has("username") || !httpRequest.containsHeader("Authentication")) {
      return false;
    }

    String userName = jsonObject.getString("username");
    // The header is on the form "Bearer <token>"
    String token = httpRequest.getFirstHeader("Authentication").getValue().substring(7);

    // User must exist
    User user = UserDAO.getInstance().findUserByUsername(userName);

    if (user == null) {
      return false;
    }

    boolean returnValue = user.checkUserSessionToken(token);

    // If the user has an expired session token it will be deleted from the database
    user.update();

    return returnValue;
  }

}
