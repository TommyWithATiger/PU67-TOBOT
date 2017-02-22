package api.exceptions;

/**
 * An unchecked exception for bad method in an API request
 */
public class APIBadMethodException extends RuntimeException {

  public APIBadMethodException(String exceptionMessage){
    super(exceptionMessage);
  }

}
