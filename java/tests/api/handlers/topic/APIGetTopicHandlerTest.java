package api.handlers.topic;

import static api.handlers.topic.APIGetTopicHandler.createAboutTopic;
import static api.handlers.topic.APIGetTopicHandler.getAllTopics;
import static api.handlers.topic.APIGetTopicHandler.getTopicByID;
import static api.handlers.topic.APIGetTopicHandler.getTopicsByTitle;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.Topic;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APIGetTopicHandlerTest extends BaseTest {

  private Topic topic;

  @Before
  public void setUp() throws Exception {
    topic = new Topic("Test title", "Test description");
    topic.create();
  }

  @Test
  public void testCreateAboutTopic() {
    JSONObject data = createAboutTopic(topic);
    assertEquals(topic.getId(), data.getInt("id"));
    assertEquals(topic.getTitle(), data.getString("title"));
    assertEquals(topic.getDescription(), data.getString("description"));
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetAllTopicsWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "topics/all/url");
    getAllTopics(httpRequest);
  }

  @Test
  public void testGetAlLTopics() {
    Topic topic2 = new Topic("Test topic 2", "Something");
    topic2.create();

    HttpRequest httpRequest = new BasicHttpRequest("GET", "topics/all/url");
    String response = getAllTopics(httpRequest);
    assertEquals("{\"topics\":[{\"description\":\"Test description\",\"id\":" + topic.getId()
        + ",\"title\":\"Test title\"},{\"description\":\"Something\",\"id\":" + topic2.getId()
        + ",\"title\":\"Test topic 2\"}]}", response);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicsByTitleWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "topics/title/url");
    getTopicsByTitle(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicsByTitleNoUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topics/title/url");
    getTopicsByTitle(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicsByTitleWrongUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topics/title/url?test=test");
    getTopicsByTitle(httpRequest);
  }

  @Test
  public void testGetTopicsByTitle() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topics/title/url?title=Test title");
    String response = getTopicsByTitle(httpRequest);
    assertEquals("{\"topics\":[{\"description\":\"Test description\",\"id\":" + topic.getId()
        + ",\"title\":\"Test title\"}]}", response);
  }

  @Test
  public void testGetTopicsByTitleNonApplicable() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topics/title/url?title=abcd");
    String response = getTopicsByTitle(httpRequest);
    assertEquals("{\"topics\":[]}", response);
  }

  @Test
  public void testGetTopicsByTitlePartialTitle() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topics/title/url?title=Test");
    String response = getTopicsByTitle(httpRequest);
    assertEquals("{\"topics\":[{\"description\":\"Test description\",\"id\":" + topic.getId()
        + ",\"title\":\"Test title\"}]}", response);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicByIDWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "topic/id/url");
    getTopicByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicByIDNoUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topic/id/url");
    getTopicByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicByIDWrongUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topic/id/url?test=test");
    getTopicByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicByIDIDNotInteger() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topic/id/url?id=test");
    getTopicByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicByIDIDNotValid() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topic/id/url?id=-12");
    getTopicByID(httpRequest);
  }

  @Test
  public void testGetTopicByID() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "topic/id/url?id=" + topic.getId());
    String response = getTopicByID(httpRequest);
    assertEquals("{\"description\":\"Test description\",\"id\":" + topic.getId()
        + ",\"title\":\"Test title\"}", response);
  }

}