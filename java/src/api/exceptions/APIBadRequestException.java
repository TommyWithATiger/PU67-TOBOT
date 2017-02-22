package api.exceptions;

/**
 * An unchecked exceptions for when an API request is malformed
 */
public class APIBadRequestException extends RuntimeException {

  public APIBadRequestException(String exceptionMessage){
    super(exceptionMessage);
  }

}
