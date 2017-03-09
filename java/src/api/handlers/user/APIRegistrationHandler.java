package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.user.UserDataValidator;
import data.user.UserDataValidator.IsValidToken;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIRegistrationHandler {

  /**
   * An API handler for checking registration data, which returns information about what is not
   * valid. Requires the following fields to be set:
   *    username (String): The username to register
   *    password (String): The password to register
   *    email (String): The email to register
   *
   * @param httpRequest The request to handle
   * @return A JSON string containing the following variables:
   *    username-valid (Boolean): Indicates if the username is valid
   *    username-message (String) [if username-valid is 'false']: A message about why the username is not valid
   *    password-valid (Boolean): Indicates if the password is valid
   *    password-message (String) [if password-valid is 'false']: A message about why the password is not valid
   *    email-valid (Boolean): Indicates if the email is valid
   *    email-message (String) [if email-valid is 'false']: A message about why the email is not valid
   *
   */
  public static String checkRegistrationData(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!jsonObject.has("username") || !jsonObject.has("password") || !jsonObject.has("email")) {
      throw new APIBadRequestException("Request must have the required fields set");
    }

    JSONObject response = new JSONObject();

    // Username check
    IsValidToken usernameValid = UserDataValidator.checkUsername(jsonObject.getString("username"));
    response.put("username-valid", usernameValid.isValid());
    if (!usernameValid.isValid()) {
      response.put("username-message", usernameValid.getMessage());
    }

    // Password check
    IsValidToken passwordValid = UserDataValidator.checkUsername(jsonObject.getString("password"));
    response.put("password-valid", passwordValid.isValid());
    if (!passwordValid.isValid()) {
      response.put("password-message", passwordValid.getMessage());
    }

    // Password check
    IsValidToken emailValid = UserDataValidator.checkUsername(jsonObject.getString("email"));
    response.put("email-valid", emailValid.isValid());
    if (!emailValid.isValid()) {
      response.put("email-message", emailValid.getMessage());
    }

    return response.toString();
  }

}
