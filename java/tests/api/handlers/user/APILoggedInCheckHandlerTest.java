package api.handlers.user;

import static api.handlers.user.APILoggedInCheckHandler.handleLoggedInCheckRequest;
import static org.junit.Assert.assertEquals;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.user.User;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APILoggedInCheckHandlerTest extends BaseTest {

  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User("username", "user@email.com", "password");
    user.create();

    user.createSessionToken();
    user.update();
  }

  @Test(expected = APIBadMethodException.class)
  public void testHandleLoggedInCheckRequestWrongMethod() {
    HttpRequest request = buildRequestContent("check/url", "GET");
    handleLoggedInCheckRequest(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLoggedInCheckRequestNoContent() {
    HttpRequest request = new BasicHttpRequest("POST", "check/url");
    handleLoggedInCheckRequest(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLoggedInCheckRequestFieldsNotSet() {
    HttpRequest request = buildRequestContent("check/url", "POST", "{}");
    handleLoggedInCheckRequest(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleLoggedInCheckRequestUserDoesNotExist() {
    HttpRequest request = buildRequestContent("check/url", "POST",
        "{\"username\":\"notValid\",\"token\":\"ignored\"}");
    handleLoggedInCheckRequest(request);
  }

  @Test
  public void testHandleLoggedInCheck() {
    HttpRequest request = buildRequestContent("check/url", "POST");
    String response = handleLoggedInCheckRequest(request);
    assertEquals("{\"logged_in\":true}", response);
  }

  @Test
  public void testHandleLoggedInCheckNotLoggedIn() {
    HttpRequest request = buildRequestContent("check/url", "POST");
    user.logout();
    user.update();
    String response = handleLoggedInCheckRequest(request);
    assertEquals("{\"logged_in\":false}", response);
  }

  private HttpRequest buildRequestContent(String url, String method) {
    JSONObject content = new JSONObject();
    content.put("username", user.getUsername());
    content.put("token", user.getSessionToken());

    return buildRequestContent(url, method, content.toString());
  }

}