package data.user;

import data.dao.UserDAO;
import data.dao.util.FieldTuple;
import java.util.List;
import org.apache.commons.validator.routines.EmailValidator;

public class UserDataValidator {

  /**
   * A class for validation tokens, keeping track of a message and a boolean value indicating if the
   * token is valid
   */
  public static class isValidToken {

    private String message;
    private boolean valid;

    private isValidToken(String message, boolean validValue) {
      this(validValue);
      this.message = message;
    }

    private isValidToken(boolean validValue) {
      this.valid = validValue;
    }

    /**
     * @return If the token is valid
     */
    public boolean isValid() {
      return valid;
    }

    /**
     * @return The token message
     */
    public String getMessage() {
      return message;
    }
  }

  /**
   * Checks if the given username is valid and available
   *
   * @param username The username to check
   * @return A isValidToken indicating if the username is valid and available, if not it has an
   * error message
   */
  public static isValidToken checkUsername(String username) {
    if (username.length() > 2 && username.length() < 33) {
      return new isValidToken("Username must have a length of 3 - 32 symbols", false);
    }

    User user = UserDAO.getInstance().findUserByUsername(username);
    if (user == null) {
      return new isValidToken(true);
    }
    return new isValidToken("Username already taken", false);
  }

  /**
   * Checks if the given email is valid and available
   *
   * @param email The email to check
   * @return A isValidToken indicating if the username is valid and available, if not it has an
   * error message
   */
  public static isValidToken checkEmail(String email) {
    if (!EmailValidator.getInstance().isValid(email)) {
      return new isValidToken("Email address is not in a valid format", false);
    }
    List<User> results = UserDAO.getInstance()
        .find("findUsersByEmail", new FieldTuple("email", email));
    if (results.isEmpty()) {
      return new isValidToken(true);
    }
    return new isValidToken("An account with that email address already exists", false);
  }

  /**
   * Checks if the given password is valid
   *
   * @param password The password to check
   * @return A isValidToken indicating if the username is valid, if not it has an error message
   */
  public static isValidToken checkPassword(String password) {
    if (password.length() < 8) {
      return new isValidToken("Passwords must be of length 8 or longer", false);
    }
    return new isValidToken(true);
  }

}
