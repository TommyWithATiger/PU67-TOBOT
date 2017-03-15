package api.helpers;

import api.exceptions.APIRequestForbiddenException;
import data.dao.UserDAO;
import data.user.User;
import org.apache.http.HttpRequest;

public class isLoggedInHelper {

  /**
   * A helper method for checking if the user is logged in
   *
   * @param httpRequest The request
   * @param exceptionSuffix a suffix to the base exception message
   * @throws APIRequestForbiddenException if the header is missing X-Username or Authorization, or if
   * the user does not exist, or if the users's session is expired.
   * @return The User that made the request
   */
  public static User getUserFromRequest(HttpRequest httpRequest, String exceptionSuffix) {
    String base = "User is not logged in";

    // Must have username in the data and the Authentication header set
    if (!httpRequest.containsHeader("X-Username") || !httpRequest.containsHeader("Authorization")) {
      throw new APIRequestForbiddenException(base + exceptionSuffix);
    }

    String userName = httpRequest.getFirstHeader("X-Username").getValue();
    // The header is on the form "Bearer <token>"
    String token = httpRequest.getFirstHeader("Authorization").getValue().substring(7);

    // User must exist
    User user = UserDAO.getInstance().findUserByUsername(userName);

    if (user == null) {
      throw new APIRequestForbiddenException(base + exceptionSuffix);
    }

    boolean sessionOK = user.checkUserSessionToken(token);
    // If the user has an expired session token it will be deleted from the database
    user.update();

    if (!sessionOK){
      throw new APIRequestForbiddenException(base + exceptionSuffix);
    }

    return user;
  }
}
