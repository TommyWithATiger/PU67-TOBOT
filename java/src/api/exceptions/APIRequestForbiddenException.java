package api.exceptions;

public class APIRequestForbiddenException extends Exception {

  public APIRequestForbiddenException(String exceptionMessage){
    super(exceptionMessage);
  }

}
