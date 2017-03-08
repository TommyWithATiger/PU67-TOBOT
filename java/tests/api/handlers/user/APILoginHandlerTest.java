package api.handlers.user;

import static api.handlers.user.APILoginHandler.handleLoginRequest;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.dao.UserDAO;
import data.user.User;
import java.io.ByteArrayInputStream;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APILoginHandlerTest extends BaseTest {

  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User("username", "user@email.com", "password");
    user.create();
  }

  @Test(expected = APIBadMethodException.class)
  public void testHandleLoginRequestWrongMethod() {
    HttpRequest httpRequest = buildRequestContent("login/url", "GET");
    handleLoginRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLoginRequestNoContent() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "login/url");
    handleLoginRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLoginRequestFieldsNotSet() {
    HttpRequest httpRequest = buildRequestContent("login/url", "POST", "{\"test\":\"test\"}");
    handleLoginRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLoginRequestUsernameNotValid() {
    HttpRequest httpRequest = buildRequestContent("login/url", "POST",
        "{\"username\":\"test\",\"password\":\"test\"}");
    handleLoginRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLoginRequestWrongPassword() {
    HttpRequest httpRequest = buildRequestContent("login/url", "POST",
        "{\"username\":\"username\",\"password\":\"test\"}");
    handleLoginRequest(httpRequest);
  }

  @Test
  public void testHandleLoginRequestValid() {
    HttpRequest httpRequest = buildRequestContent("login/url", "POST");
    String response = handleLoginRequest(httpRequest);
    user = UserDAO.getInstance().findUserByUsername(user.getUsername());
    assertTrue(user.getSessionToken() != null);
    assertEquals("{\"username\":\"username\",\"token\":\"" + user.getSessionToken() + "\"}",
        response);
  }

  @Test
  public void testHandleLoginRequestAlreadyHasSessionToken() {
    user.createSessionToken();
    user.update();
    String sessionToken = user.getSessionToken();
    HttpRequest httpRequest = buildRequestContent("login/url", "POST");
    String response = handleLoginRequest(httpRequest);
    user = UserDAO.getInstance().findUserByUsername(user.getUsername());
    assertEquals(sessionToken, user.getSessionToken());
    assertEquals("{\"username\":\"username\",\"token\":\"" + user.getSessionToken() + "\"}",
        response);
  }

  private HttpRequest buildRequest(String url, String method) {
    return new BasicHttpEntityEnclosingRequest(method, url);
  }

  private HttpRequest buildRequestContent(String url, String method) {
    JSONObject content = new JSONObject();
    content.put("username", user.getUsername());
    content.put("password", "password");

    return buildRequestContent(url, method, content.toString());
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