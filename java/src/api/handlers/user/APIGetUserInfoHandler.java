package api.handlers.user;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import data.user.User;
import org.apache.http.HttpRequest;

public class APIGetUserInfoHandler {

  /**
   * An API handler for a user to get it's user information. Requires the user to be logged in.
   *
   * @param httpRequest the request to handle
   * @return A JSON object with the data from user.createAbout
   */
  public static String getUserInfo(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);
    User user = getUserFromRequest(httpRequest, ", cannot get user info");
    return user.createAbout().toString();
  }

}
