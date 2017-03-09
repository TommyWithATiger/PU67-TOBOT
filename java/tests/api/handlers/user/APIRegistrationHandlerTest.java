package api.handlers.user;

import static api.handlers.user.APIRegistrationHandler.checkRegistrationData;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import java.io.ByteArrayInputStream;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.json.JSONObject;
import org.junit.Test;

public class APIRegistrationHandlerTest extends BaseTest {

  @Test(expected = APIBadMethodException.class)
  public void testCheckRegistrationDataWrongMethod() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration/check", "GET",
        "username", "password", "user@email.com");
    checkRegistrationData(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testCheckRegistrationDataNoData() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "registration/check");
    checkRegistrationData(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testCheckRegistrationDataNoFieldsSet() {
    HttpRequest httpRequest = buildRequestContent("registration/check", "POST", "{}");
    checkRegistrationData(httpRequest);
  }

  @Test
  public void testCheckRegistrationData() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration/check", "POST",
        "username", "password", "user@email.com");
    String response = checkRegistrationData(httpRequest);
    assertEquals("{\"password-valid\":true,\"username-valid\":true,\"email-valid\":true}",
        response);
  }

  @Test
  public void testCheckRegistrationDataFieldsNotValid() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration/check", "POST", "uu",
        "pp", "user");
    String response = checkRegistrationData(httpRequest);
    assertEquals(
        "{\"username-message\":\"Username must have a length of 3 - 32 symbols\",\"password-valid\":false,\"username-valid\":false,\"password-message\":\"Passwords must be of length 8 or longer\",\"email-valid\":false,\"email-message\":\"Email address is not in a valid format\"}",
        response);
  }

  private HttpRequest buildRequest(String url, String method) {
    return new BasicHttpEntityEnclosingRequest(method, url);
  }

  private HttpRequest buildRequestContentRegistration(String url, String method, String username,
      String password, String email) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("username", username);
    jsonObject.put("password", password);
    jsonObject.put("email", email);
    return buildRequestContent(url, method, jsonObject.toString());
  }

  private HttpRequest buildRequestContent(String url, String method, String content) {
    BasicHttpEntityEnclosingRequest httpRequest = (BasicHttpEntityEnclosingRequest) buildRequest(
        url, method);

    BasicHttpEntity httpEntity = new BasicHttpEntity();

    httpEntity.setContent(new ByteArrayInputStream(content.getBytes()));

    httpRequest.setEntity(httpEntity);

    return httpRequest;
  }

}