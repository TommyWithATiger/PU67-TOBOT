package api.handlers.user;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadRequestException;
import data.user.User;
import data.user.UserDataValidator;
import data.user.UserDataValidator.IsValidToken;
import data.user.UserType;
import data.user.UserTypeConverter;
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
   *    username_valid (Boolean): Indicates if the username is valid
   *    username_message (String) [if username_valid is 'false']: A message about why the username is not valid
   *    password_valid (Boolean): Indicates if the password is valid
   *    password_message (String) [if password_valid is 'false']: A message about why the password is not valid
   *    email_valid (Boolean): Indicates if the email is valid
   *    email_message (String) [if email_valid is 'false']: A message about why the email is not valid
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
    response.put("username_valid", usernameValid.isValid());
    if (!usernameValid.isValid()) {
      response.put("username_message", usernameValid.getMessage());
    }

    // Password check
    IsValidToken passwordValid = UserDataValidator.checkPassword(jsonObject.getString("password"));
    response.put("password_valid", passwordValid.isValid());
    if (!passwordValid.isValid()) {
      response.put("password_message", passwordValid.getMessage());
    }

    // Password check
    IsValidToken emailValid = UserDataValidator.checkEmail(jsonObject.getString("email"));
    response.put("email_valid", emailValid.isValid());
    if (!emailValid.isValid()) {
      response.put("email_message", emailValid.getMessage());
    }

    return response.toString();
  }

  /**
   * An API handler for registering a user. Requires the following fields to be set:
   *    username (String): The username to register
   *    password (String): The password to register
   *    email (String): The email to register
   *    user_type (String): The user type
   *
   * @param httpRequest The request to handle
   * @return A JSON string containing the following variables
   *    registered (Boolean): Indicating if the user is registered
   */
  public static String registerUser(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    String requestContent = checkAndGetEntityContent(httpRequest);

    JSONObject jsonObject = checkAndGetJSON(requestContent);

    if (!jsonObject.has("username") || !jsonObject.has("password") || !jsonObject.has("email")
        || !jsonObject.has("user_type")) {
      throw new APIBadRequestException("Request must have the required fields set");
    }

    String username = jsonObject.getString("username");
    if (!UserDataValidator.checkUsername(username).isValid()) {
      throw new APIBadRequestException("Username not valid");
    }

    String password = jsonObject.getString("password");
    if (!UserDataValidator.checkPassword(password).isValid()) {
      throw new APIBadRequestException("Password is not valid");
    }

    String email = jsonObject.getString("email");
    if (!UserDataValidator.checkEmail(email).isValid()) {
      throw new APIBadRequestException("Email is not valid");
    }

    UserType userType;
    String userTypeString = jsonObject.getString("user_type");
    try {
      userType = UserTypeConverter.stringToUserType(userTypeString);
      if (userType == UserType.ADMIN) {
        throw new APIBadRequestException("Cannot register admin");
      }
    } catch (IllegalArgumentException iae) {
      throw new APIBadRequestException("User-type is not valid");
    }

    User user = new User(username, email, password, userType);
    user.create();

    JSONObject response = new JSONObject();

    response.put("registered", true);

    return response.toString();
  }

}
