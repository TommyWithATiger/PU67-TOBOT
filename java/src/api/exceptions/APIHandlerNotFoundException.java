package api.exceptions;

/**
 * A checked exception for when the API delegator does not have a handler for the given API request
 */
public class APIHandlerNotFoundException extends Exception {

  public APIHandlerNotFoundException(String exceptionMessage) {
    super(exceptionMessage);
  }

}
