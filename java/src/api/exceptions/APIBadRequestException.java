package api.exceptions;

public class APIBadRequestException extends RuntimeException {

  public APIBadRequestException(String exceptionMessage){
    super(exceptionMessage);
  }

}
