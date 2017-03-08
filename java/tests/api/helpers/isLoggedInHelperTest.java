package api.helpers;

import static api.helpers.isLoggedInHelper.isLoggedIn;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.user.User;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Test;

public class isLoggedInHelperTest extends BaseTest {

  @Test
  public void testValidLogin() {
    User user = new User("user", "valid@email.com", "qwerty");
    user.create();

    user.createSessionToken();
    user.update();

    HttpRequest httpRequest = new BasicHttpRequest("GET", "URL");
    httpRequest.addHeader("X-Username", user.getUsername());
    httpRequest.addHeader("Authorization", "Bearer " + user.getSessionToken());

    assertTrue(isLoggedIn(httpRequest));
  }

  @Test
  public void testMissingHeaders() {
    assertFalse(isLoggedIn(new BasicHttpRequest("GET", "URL")));
  }

  @Test
  public void testMissingUsernameHeader() {
    User user = new User("user", "valid@email.com", "qwerty");
    user.create();

    user.createSessionToken();
    user.update();

    HttpRequest httpRequest = new BasicHttpRequest("GET", "URL");
    httpRequest.addHeader("Authorization", "Bearer " + user.getSessionToken());

    assertFalse(isLoggedIn(httpRequest));
  }

  @Test
  public void testMissingAuthorizationHeader() {
    User user = new User("user", "valid@email.com", "qwerty");
    user.create();

    user.createSessionToken();
    user.update();

    HttpRequest httpRequest = new BasicHttpRequest("GET", "URL");
    httpRequest.addHeader("X-Username", user.getUsername());

    assertFalse(isLoggedIn(httpRequest));
  }

  @Test
  public void testInvalidLogin() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "URL");
    httpRequest.addHeader("X-Username", "admin");
    httpRequest.addHeader("Authorization", "Bearer: adshjasdkhk");

    assertFalse(isLoggedIn(httpRequest));
  }

}
