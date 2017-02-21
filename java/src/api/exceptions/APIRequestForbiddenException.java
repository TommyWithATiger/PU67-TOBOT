package api.exceptions;

/**
 * A checked exception for when an API request cannot be fulfilled due to the user not having the
 * right permissions
 */
public class APIRequestForbiddenException extends Exception {

  public APIRequestForbiddenException(String exceptionMessage) {
    super(exceptionMessage);
  }

}
