package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import data.dao.UserDAO;
import data.dao.util.FieldTuple;
import data.user.User;
import data.user.UserDataValidator;
import data.user.UserMailRecovery;
import java.util.List;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIResetPasswordHandler {

  /**
   * An API handler for resetting a password given a reset token and the email of the given account.
   * Requires the following data to be set:
   *    password (String): the new password
   *    resetToken (String): the reset token
   *    email (String): the email of the user
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the following variables
   *    changed_password (Boolean): indicates if the password was reset
   */
  public static String resetPassword(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!jsonObject.has("password") || !jsonObject.has("resetToken") || !jsonObject.has("email")) {
      throw new APIBadRequestException("Request must have the required fields set");
    }

    String password = jsonObject.getString("password");
    String resetToken = jsonObject.getString("resetToken");
    String email = jsonObject.getString("email");

    List<User> users = UserDAO.getInstance()
        .find("findUsersByEmail", new FieldTuple("email", email));
    if (users.isEmpty()) {
      throw new APIBadRequestException("Email not valid");
    }
    User user = users.get(0);

    // Check if the user has a token
    if (!user.hasResetToken()) {
      throw new APIRequestForbiddenException("Reset gone");
    }

    // Check if token is valid
    if (!user.checkPasswordResetToken(resetToken)) {
      throw new APIRequestForbiddenException("Token is not correct");
    }

    // Check if new password is valid
    if (!UserDataValidator.checkPassword(password).isValid()) {
      throw new APIBadRequestException("New password is not valid");
    }

    user.setPassword(password);
    // Remove token from further use
    user.removeResetToken();
    // Make sure all devices logged in as the user are kicked out when the password is changed
    user.logout();
    user.update();

    JSONObject response = new JSONObject();
    response.put("changed_password", true);

    return response.toString();
  }

  /**
   * An API handler for requesting a password reset. If successful it will send an reset email to
   * the user. Require the following variables:
   *    email (String): the users email
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the following variables:
   *    success (Boolean): indicates if the reset request was successful
   */
  public static String requestPasswordReset(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!jsonObject.has("email")) {
      throw new APIBadRequestException("Request must have email set");
    }

    String email = jsonObject.getString("email");
    List<User> users = UserDAO.getInstance()
        .find("findUsersByEmail", new FieldTuple("email", email));
    if (users.isEmpty()) {
      throw new APIBadRequestException("Email not valid");
    }
    User user = users.get(0);

    UserMailRecovery.getInstance().sendRecoveryMail(user);

    JSONObject response = new JSONObject();
    response.put("success", true);
    return response.toString();
  }


}
