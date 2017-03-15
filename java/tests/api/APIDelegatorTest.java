package api;

import static api.APIDelegator.delegate;

import api.exceptions.APIHandlerNotFoundException;
import base.BaseTest;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Test;

public class APIDelegatorTest extends BaseTest {

  @Test(expected = APIHandlerNotFoundException.class)
  public void testInvalidAPIHandlerURL() {
    HttpRequest httpRequest = new BasicHttpRequest("GET",
        "/api/v1/aqwertyuidkjhagvcbncakjgfhhkjfhvabdshkjhfv/jadsfklhdjahjsfgkeqwewqui");
    delegate(httpRequest);
  }

}