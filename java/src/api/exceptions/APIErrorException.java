package api.exceptions;

public class APIErrorException extends RuntimeException {

  public APIErrorException(String exceptionMessage){
    super(exceptionMessage);
  }

}
