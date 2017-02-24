package api.exceptions;

/**
 * A unchecked exception for when the API delegator does not have a handler for the given API request
 */
public class APIHandlerNotFoundException extends RuntimeException{

  public APIHandlerNotFoundException(String exceptionMessage) {
    super(exceptionMessage);
  }

}
