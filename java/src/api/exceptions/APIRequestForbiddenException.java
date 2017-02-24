package api.exceptions;

/**
 * A unchecked exception for when an API request cannot be fulfilled due to the user not having the
 * right permissions
 */
public class APIRequestForbiddenException extends RuntimeException {

  public APIRequestForbiddenException(String exceptionMessage) {
    super(exceptionMessage);
  }

}
