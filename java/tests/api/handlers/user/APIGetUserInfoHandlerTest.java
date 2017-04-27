package api.handlers.user;

import static api.handlers.user.APIGetUserInfoHandler.getUserInfo;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.user.User;
import org.apache.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

public class APIGetUserInfoHandlerTest extends BaseTest {

  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User("Username", "user@email.com", "password");
    user.create();

    user.createSessionToken();
    user.update();
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetUserInfoBadMethod() {
    HttpRequest httpRequest = buildRequest("user/info", "GET", user, true);
    getUserInfo(httpRequest);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testGetUserInfoNoLogin() {
    HttpRequest httpRequest = buildRequest("user/info", "POST", user, false);
    getUserInfo(httpRequest);
  }

  @Test
  public void testGetUserInfo() {
    HttpRequest httpRequest = buildRequest("user/info", "POST", user, true);
    String response = getUserInfo(httpRequest);
    assertEquals(
        "{\"userType\":\"Student\",\"email\":\"user@email.com\",\"username\":\"Username\",\"token\":\""
            + user.getSessionToken() + "\"}", response);
  }

}