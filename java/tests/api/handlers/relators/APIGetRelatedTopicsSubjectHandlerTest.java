package api.handlers.relators;

import static api.handlers.relators.APIGetRelatedTopicsSubjectHandler.getRelatedTopicsSubjectID;
import static org.junit.Assert.assertEquals;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.Subject;
import data.Topic;
import data.User;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Before;
import org.junit.Test;

public class APIGetRelatedTopicsSubjectHandlerTest extends BaseTest {

  private User user;
  private Topic topic;
  private Subject subject;

  @Before
  public void setup() {
    user = new User("username", "user@email.com", "password");
    user.create();
    user.createSessionToken();
    user.update();

    topic = new Topic("Test topic", "Description");
    topic.create();

    subject = new Subject("Test subject", "Description");
    subject.create();

    subject.addTopic(topic);
    subject.update();
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetRelatedTopicsSubjectIDWrongMethod() {
    HttpRequest request = buildRequest("get/relation/url?id=" + String.valueOf(subject.getId()),
        "POST");
    getRelatedTopicsSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetRelatedTopicsSubjectIDNoArguments() {
    HttpRequest request = buildRequest("get/relation/url", "GET");
    getRelatedTopicsSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetRelatedTopicsSubjectIDNoIDArgument() {
    HttpRequest request = buildRequest("get/relation/url?test=2", "GET");
    getRelatedTopicsSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetRelatedTopicsSubjectIDIDNotInteger() {
    HttpRequest request = buildRequest("get/relation/url?id=asdf", "GET");
    getRelatedTopicsSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetRelatedTopicsSubjectIDIDNotValid() {
    HttpRequest request = buildRequest("get/relation/url?id=-12", "GET");
    getRelatedTopicsSubjectID(request);
  }

  @Test
  public void testGetRelatedTopicsSubjectID() {
    HttpRequest request = buildRequest("get/relation/url?id=" + String.valueOf(subject.getId()),
        "GET");
    String response = getRelatedTopicsSubjectID(request);
    assertEquals("{\"related_topics\":[{\"description\":\"Description\",\"id\":" + String
        .valueOf(topic.getId()) + ",\"title\":\"Test topic\"}]}", response);
  }

  private HttpRequest buildRequest(String url, String method) {
    return new BasicHttpRequest(method, url);
  }
}