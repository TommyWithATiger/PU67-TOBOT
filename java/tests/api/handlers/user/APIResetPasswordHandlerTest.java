package api.handlers.user;

import static api.handlers.user.APIResetPasswordHandler.resetPassword;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
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

public class APIResetPasswordHandlerTest extends BaseTest {

  private User user;
  private String resetToken;

  @Before
  public void setup() {
    user = new User("Username", "email@email.com", "password");
    user.create();

    resetToken = user.generatePasswordResetToken();
    user.update();
  }

  @Test(expected = APIBadMethodException.class)
  public void testResetPasswordWrongMethod() {
    HttpRequest httpRequest = buildRequestContent("reset/", "GET", resetToken, "password1",
        user.getEmail());
    resetPassword(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testResetPasswordNoContent() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "reset/");
    resetPassword(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testResetPasswordNoFieldsSet() {
    HttpRequest httpRequest = buildRequestContent("reset/", "POST", "{}");
    resetPassword(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testResetPasswordNoUserForEmail() {
    HttpRequest httpRequest = buildRequestContent("reset/", "POST", resetToken, "password1",
        "email1@email.com");
    resetPassword(httpRequest);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testResetPasswordResetTokenExpired() {
    user.removeResetToken();
    user.update();
    HttpRequest httpRequest = buildRequestContent("reset/", "POST", resetToken, "password1",
        "email@email.com");
    resetPassword(httpRequest);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testResetPasswordResetTokenIncorrect() {
    HttpRequest httpRequest = buildRequestContent("reset/", "POST", "asdHHGSEQWUY12y3dhjgsa127SAD",
        "password1", "email@email.com");
    resetPassword(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testResetPasswordNewPasswordInvalid() {
    HttpRequest httpRequest = buildRequestContent("reset/", "POST", resetToken, "o",
        "email@email.com");
    resetPassword(httpRequest);
  }

  @Test
  public void testResetPassword() {
    HttpRequest httpRequest = buildRequestContent("reset/", "POST", resetToken, "password1",
        "email@email.com");
    String response = resetPassword(httpRequest);

    User user = UserDAO.getInstance().findUserByUsername(this.user.getUsername());

    assertEquals("{\"changed-password\":true}", response);
    assertTrue(user.checkPassword("password1"));
    assertFalse(user.checkPassword("password"));
  }

  private HttpRequest buildRequest(String url, String method) {
    return new BasicHttpEntityEnclosingRequest(method, url);
  }

  private HttpRequest buildRequestContent(String url, String method, String resetToken,
      String password, String email) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("resetToken", resetToken);
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