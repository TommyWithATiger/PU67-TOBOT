package api.handlers.user;

import static api.helpers.JSONCheckerHelper.getJSONFields;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.dao.UserDAO;
import data.user.User;
import java.util.List;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APILoggedInCheckHandler {

  /**
   * A handler for handling request checking if the current login data is valid. Requires the data:
   *        username (String): the username
   *        token (String): the session token
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the following variables:
   *        logged_in (boolean): indicates if the user logged in
   */
  public static String handleLoggedInCheckRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    List<String> fields = getJSONFields(httpRequest, String.class, "username", "token");

    User user = UserDAO.getInstance().findUserByUsername(fields.get(0));
    String token = fields.get(1);

    if (user == null) {
      throw new APIBadRequestException("User does not exist");
    }

    boolean userLoggedIn = user.checkUserSessionToken(token);

    user.update();

    JSONObject loginCheckResponse = new JSONObject();
    loginCheckResponse.put("logged_in", userLoggedIn);

    return loginCheckResponse.toString();
  }

}
