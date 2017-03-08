package api.handlers.user;

import static api.handlers.user.APILogoutHandler.handleLogoutRequest;
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

public class APILogoutHandlerTest extends BaseTest {

  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User("username", "user@email.com", "password");
    user.create();

    user.createSessionToken();
    user.update();
  }

  @Test(expected = APIBadMethodException.class)
  public void testHandleLogoutRequestWrongMethod() {
    HttpRequest httpRequest = buildRequestContent("logout/url", "GET");
    handleLogoutRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLogoutRequestNoContent() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "logout/url");
    handleLogoutRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLogoutRequestFieldsNotSet() {
    HttpRequest httpRequest = buildRequestContent("logout/url", "POST", "{\"test\":\"test\"}");
    handleLogoutRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLogoutRequestUsernameNotValid() {
    HttpRequest httpRequest = buildRequestContent("logout/url", "POST",
        "{\"username\":\"test\",\"token\":\"test\"}");
    handleLogoutRequest(httpRequest);
  }

  @Test
  public void testHandleLogoutRequestWrongToken() {
    HttpRequest httpRequest = buildRequestContent("logout/url", "POST",
        "{\"username\":\"username\",\"token\":\"test\"}");
    String response = handleLogoutRequest(httpRequest);
    assertTrue(user.getSessionToken() != null);
    assertEquals("{\"logged_out\":false}", response);
  }

  @Test
  public void testHandleLogoutRequestValid() {
    HttpRequest httpRequest = buildRequestContent("logout/url", "POST");
    String response = handleLogoutRequest(httpRequest);
    user = UserDAO.getInstance().findUserByUsername(user.getUsername());
    assertTrue(user.getSessionToken() == null);
    assertEquals("{\"logged_out\":true}", response);
  }

  @Test
  public void testHandleLogoutRequestAlreadyLoggedOut() {
    HttpRequest httpRequest = buildRequestContent("logout/url", "POST");

    user.logout();
    user.update();

    String response = handleLogoutRequest(httpRequest);
    user = UserDAO.getInstance().findUserByUsername(user.getUsername());
    assertTrue(user.getSessionToken() == null);
    assertEquals("{\"logged_out\":true}", response);
  }

  private HttpRequest buildRequest(String url, String method) {
    return new BasicHttpEntityEnclosingRequest(method, url);
  }

  private HttpRequest buildRequestContent(String url, String method) {
    JSONObject content = new JSONObject();
    content.put("username", user.getUsername());
    content.put("token", user.getSessionToken());

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