package api.exceptions;

/**
 * An unchecked exception for when an API request results in a server error
 */
public class APIErrorException extends RuntimeException {

  public APIErrorException(String exceptionMessage){
    super(exceptionMessage);
  }

}
