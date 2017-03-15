package api.handlers.user;

import static api.handlers.user.APIRegistrationHandler.checkRegistrationData;
import static api.handlers.user.APIRegistrationHandler.registerUser;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.dao.UserDAO;
import data.user.User;
import data.user.UserType;
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
    HttpRequest httpRequest = buildRequestContentRegistrationCheck("registration/check", "GET",
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
    HttpRequest httpRequest = buildRequestContentRegistrationCheck("registration/check", "POST",
        "username", "password", "user@email.com");
    String response = checkRegistrationData(httpRequest);
    assertEquals("{\"username_valid\":true,\"email_valid\":true,\"password_valid\":true}",
        response);
  }

  @Test
  public void testCheckRegistrationDataFieldsNotValid() {
    HttpRequest httpRequest = buildRequestContentRegistrationCheck("registration/check", "POST",
        "uu",
        "pp", "user");
    String response = checkRegistrationData(httpRequest);
    assertEquals(
        "{\"username_valid\":false,\"email_message\":\"Email address is not in a valid format\",\"password_message\":\"Passwords must be of length 8 or longer\",\"email_valid\":false,\"password_valid\":false,\"username_message\":\"Username must have a length of 3 - 32 symbols\"}",
        response);
  }

  @Test(expected = APIBadMethodException.class)
  public void testRegisterUserWrongMethod() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration", "GET", "username",
        "password", "user@email.com", "Student");
    registerUser(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRegisterUserNoContent() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "registration");
    registerUser(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRegisterUserNoFieldsSet() {
    HttpRequest httpRequest = buildRequestContent("registration", "POST", "{}");
    registerUser(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRegisterUserUsernameNotValid() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration", "POST", "aa",
        "password", "user@email.com", "Student");
    registerUser(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRegisterUserPasswordNotValid() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration", "POST", "username",
        "a", "user@email.com", "Student");
    registerUser(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRegisterUserEmailNotValid() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration", "POST", "username",
        "password", "email", "Student");
    registerUser(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRegisterUserUserTypeNotValid() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration", "POST", "username",
        "password", "user@email.com", "notavalidusertype");
    registerUser(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRegisterUserUserTypeAdmin() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration", "POST", "username",
        "password", "user@email.com", "Admin");
    registerUser(httpRequest);
  }

  @Test
  public void testRegisterUser() {
    HttpRequest httpRequest = buildRequestContentRegistration("registration", "POST", "username",
        "password", "user@email.com", "Student");
    String response = registerUser(httpRequest);
    assertEquals("{\"registered\":true}", response);
    User user = UserDAO.getInstance().findUserByUsername("username");
    assertNotNull(user);
    assertEquals("user@email.com", user.getEmail());
    assertTrue(user.checkPassword("password"));
    assertEquals(UserType.STUDENT, user.getUserType());
  }

  private HttpRequest buildRequest(String url, String method) {
    return new BasicHttpEntityEnclosingRequest(method, url);
  }

  private HttpRequest buildRequestContentRegistrationCheck(String url, String method,
      String username, String password, String email) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("username", username);
    jsonObject.put("password", password);
    jsonObject.put("email", email);
    return buildRequestContent(url, method, jsonObject.toString());
  }

  private HttpRequest buildRequestContentRegistration(String url, String method, String username,
      String password, String email, String userType) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("username", username);
    jsonObject.put("password", password);
    jsonObject.put("email", email);
    jsonObject.put("user_type", userType);
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