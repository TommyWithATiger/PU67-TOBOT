package api.helpers;

import static api.helpers.RequestMethodHelper.checkRequestMethod;

import api.exceptions.APIBadMethodException;
import base.BaseTest;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Test;

public class RequestMethodHelperTest extends BaseTest {

  @Test
  public void testValidRequestMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "URL");
    checkRequestMethod("GET", httpRequest);
  }

  @Test(expected = APIBadMethodException.class)
  public void testInvalidRequestMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "URL");
    checkRequestMethod("DELETE", httpRequest);
  }

}