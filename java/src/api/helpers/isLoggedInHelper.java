package api.helpers;

import data.DataAccessObjects.UserDAO;
import data.User;
import org.apache.http.HttpRequest;

public class isLoggedInHelper {

  /**
   * A helper method for checking if the user is logged in
   * @param httpRequest The request
   * @return A boolean indicating if the user is logged in
   */
  public static boolean isLoggedIn(HttpRequest httpRequest) {

    // Must have username in the data and the Authentication header set
    if (!httpRequest.containsHeader("X-Username") || !httpRequest.containsHeader("Authentication")) {
      return false;
    }

    String userName = httpRequest.getFirstHeader("X-Username").getValue();
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
