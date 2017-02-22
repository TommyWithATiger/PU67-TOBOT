package api.exceptions;

public class APIBadMethodException extends RuntimeException {

  public APIBadMethodException(String exceptionMessage){
    super(exceptionMessage);
  }

}
