package api.handlers.topic;

import static api.handlers.topic.APIAddTopicHandler.handleAddTopicRequest;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.dao.TopicDAO;
import data.Topic;
import data.user.User;
import java.io.ByteArrayInputStream;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APIAddTopicHandlerTest extends BaseTest {

  private User user;

  @Before
  public void setUp() {
    user = new User("username", "user@email.com", "password");
    user.create();

    user.createSessionToken();
    user.update();
  }

  @Test(expected = APIBadMethodException.class)
  public void testHandleAddTopicRequestWrongMethod() {
    HttpRequest httpRequest = buildRequestContent("topic/url", "GET", true);
    handleAddTopicRequest(httpRequest);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testHandleAddTopicRequestNotLoggedIn() {
    HttpRequest httpRequest = buildRequestContent("topic/url", "POST", false);
    handleAddTopicRequest(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testHandleAddTopicRequestFieldsNotSet() {
    HttpRequest httpRequest = buildRequestContent("topic/url", "POST", true, "{}");
    handleAddTopicRequest(httpRequest);
  }

  @Test
  public void testHandleAddTopicRequest() {
    HttpRequest httpRequest = buildRequestContent("topic/url", "POST", true);
    String response = handleAddTopicRequest(httpRequest);
    Topic topic = TopicDAO.getInstance().findTopicsByTitle("Test title").get(0);
    assertNotNull(topic);
    assertEquals("Test description", topic.getDescription());
    assertEquals("{\"description\":\"Test description\",\"id\":" + topic.getId()
        + ",\"title\":\"Test title\"}", response);
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