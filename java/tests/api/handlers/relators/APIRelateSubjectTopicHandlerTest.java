package api.handlers.relators;

import static api.handlers.relators.APIRelateSubjectTopicHandler.relateSubjectTopicHandler;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.dao.SubjectDAO;
import data.Subject;
import data.Topic;
import data.user.User;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APIRelateSubjectTopicHandlerTest extends BaseTest {

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
    subject.addEditor(user);
    subject.create();
  }

  @Test(expected = APIBadMethodException.class)
  public void testRelateSubjectTopicWrongMethod() {
    HttpRequest request = buildRequestContent("relate/url", "GET", true);
    relateSubjectTopicHandler(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRelateSubjectTopicNoContent() {
    HttpRequest request = new BasicHttpRequest("POST", "relate/url");
    request.addHeader("X-Username", user.getUsername());
    request.addHeader("Authorization", "Bearer " + user.getSessionToken());
    relateSubjectTopicHandler(request);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testRelateSubjectTopicNotLoggedIn() {
    HttpRequest request = buildRequestContent("relate/url", "POST", false);
    relateSubjectTopicHandler(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRelateSubjectTopicIDNotSet() {
    HttpRequest request = buildRequestContent("relate/url", "POST", user, true, "{}");
    relateSubjectTopicHandler(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRelateSubjectTopicTopicIDNotInt() {
    HttpRequest request = buildRequestContent("relate/url", "POST", user, true,
        "{\"subjectID\":" + String.valueOf(subject.getId()) + ",\"topicID\":\"test\"}");
    relateSubjectTopicHandler(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRelateSubjectTopicSubjectIDNotInt() {
    HttpRequest request = buildRequestContent("relate/url", "POST", user, true,
        "{\"topicID\":" + String.valueOf(topic.getId()) + ",\"subjectID\":\"test\"}");
    relateSubjectTopicHandler(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRelateSubjectTopicTopicIDNotValid() {
    HttpRequest request = buildRequestContent("relate/url", "POST", user, true,
        "{\"subjectID\":" + String.valueOf(subject.getId()) + ",\"topicID\":-12}");
    relateSubjectTopicHandler(request);
  }


  @Test(expected = APIBadRequestException.class)
  public void testRelateSubjectTopicSubjectIDNotValid() {
    HttpRequest request = buildRequestContent("relate/url", "POST", user, true,
        "{\"topicID\":" + String.valueOf(topic.getId()) + ",\"subjectID\":-12}");
    relateSubjectTopicHandler(request);
  }

  @Test
  public void testRelateSubjectTopic() {
    HttpRequest request = buildRequestContent("relate/url", "POST", true);
    String content = relateSubjectTopicHandler(request);
    assertEquals("{\"is-related\":true,\"already-related\":false}", content);
    subject = SubjectDAO.getInstance().findById(subject.getId());
    assertTrue(subject.hasTopic(topic));
  }

  @Test
  public void testRelateSubjectTopicAlreadyRelated() {
    topic.addToSubject(subject);
    topic.update();
    subject.update();

    HttpRequest request = buildRequestContent("relate/url", "POST", true);
    String content = relateSubjectTopicHandler(request);
    assertEquals("{\"is-related\":true,\"already-related\":true}", content);
    assertTrue(subject.hasTopic(topic));
  }


  private HttpRequest buildRequestContent(String url, String method, boolean setLoggedIn) {
    JSONObject content = new JSONObject();
    content.put("topicID", topic.getId());
    content.put("subjectID", subject.getId());

    return buildRequestContent(url, method, user, setLoggedIn, content.toString());
  }

}