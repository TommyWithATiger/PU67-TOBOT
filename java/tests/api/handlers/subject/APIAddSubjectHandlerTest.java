package api.handlers.subject;

import static api.handlers.subject.APIAddSubjectHandler.handleAddSubject;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.DataAccessObjects.SubjectDAO;
import data.Subject;
import data.User;
import java.io.ByteArrayInputStream;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APIAddSubjectHandlerTest extends BaseTest {

  private User user;

  @Before
  public void setUp() {
    user = new User("username", "user@email.com", "password");
    user.create();

    user.createSessionToken();
    user.update();
  }

  @Test(expected = APIBadMethodException.class)
  public void testHandleAddSubjectWrongMethod() {
    HttpRequest httpRequest = buildRequestContent("subject/url", "GET", true);
    handleAddSubject(httpRequest);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testHandleAddSubjectNotLoggedIn() {
    HttpRequest httpRequest = buildRequestContent("subject/url", "POST", false);
    handleAddSubject(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleAddSubjectFieldsNotSet() {
    HttpRequest httpRequest = buildRequestContent("subject/url", "POST", true, "{}");
    handleAddSubject(httpRequest);
  }

  @Test
  public void testHandleAddSubject() {
    HttpRequest httpRequest = buildRequestContent("subject/url", "POST", true);
    String response = handleAddSubject(httpRequest);
    Subject subject = SubjectDAO.getInstance().findSubjectsByTitle("Test title").get(0);
    assertNotNull(subject);
    assertEquals("Test description", subject.getDescription());
    assertEquals("IDI-NTNU", subject.getInstitution());
    assertEquals("TDT4100", subject.getSubjectCode());
    assertEquals("{\"institution\":\"IDI-NTNU\",\"description\":\"Test description\",\"id\":"
        + subject.getId() + ",\"title\":\"Test title\",\"subjectCode\":\"TDT4100\"}", response);
  }

  private HttpRequest buildRequest(String url, String method, boolean setLoggedIn) {
    BasicHttpEntityEnclosingRequest httpRequest = new BasicHttpEntityEnclosingRequest(method, url);
    if (setLoggedIn) {
      httpRequest.addHeader("X-Username", user.getUsername());
      httpRequest.addHeader("Authorization", "Bearer " + user.getSessionToken());
    }
    return httpRequest;
  }

  private HttpRequest buildRequestContent(String url, String method, boolean setLoggedIn) {
    JSONObject content = new JSONObject();
    content.put("title", "Test title");
    content.put("description", "Test description");
    content.put("institution", "IDI-NTNU");
    content.put("subjectCode", "TDT4100");

    return buildRequestContent(url, method, setLoggedIn, content.toString());
  }

  private HttpRequest buildRequestContent(String url, String method, boolean setLoggedIn,
      String content) {
    BasicHttpEntityEnclosingRequest httpRequest = (BasicHttpEntityEnclosingRequest) buildRequest(
        url, method, setLoggedIn);

    BasicHttpEntity httpEntity = new BasicHttpEntity();

    httpEntity.setContent(new ByteArrayInputStream(content.getBytes()));

    httpRequest.setEntity(httpEntity);

    return httpRequest;
  }

}