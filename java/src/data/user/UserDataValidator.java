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
  public static class IsValidToken {

    private String message;
    private boolean valid;

    private IsValidToken(String message, boolean validValue) {
      this(validValue);
      this.message = message;
    }

    private IsValidToken(boolean validValue) {
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
   * @return A IsValidToken indicating if the username is valid and available, if not it has an
   * error message
   */
  public static IsValidToken checkUsername(String username) {
    if (username.length() < 3 || username.length() > 32) {
      return new IsValidToken("Username must have a length of 3 - 32 symbols", false);
    }

    User user = UserDAO.getInstance().findUserByUsername(username);
    if (user == null) {
      return new IsValidToken(true);
    }
    return new IsValidToken("Username already taken", false);
  }

  /**
   * Checks if the given email is valid and available
   *
   * @param email The email to check
   * @return A IsValidToken indicating if the username is valid and available, if not it has an
   * error message
   */
  public static IsValidToken checkEmail(String email) {
    if (!EmailValidator.getInstance().isValid(email)) {
      return new IsValidToken("Email address is not in a valid format", false);
    }
    List<User> results = UserDAO.getInstance()
        .find("findUsersByEmail", new FieldTuple("email", email));
    if (results.isEmpty()) {
      return new IsValidToken(true);
    }
    return new IsValidToken("An account with that email address already exists", false);
  }

  /**
   * Checks if the given password is valid
   *
   * @param password The password to check
   * @return A IsValidToken indicating if the username is valid, if not it has an error message
   */
  public static IsValidToken checkPassword(String password) {
    if (password.length() < 8) {
      return new IsValidToken("Passwords must be of length 8 or longer", false);
    }
    return new IsValidToken(true);
  }

}
