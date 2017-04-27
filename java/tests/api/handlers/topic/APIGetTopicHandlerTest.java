package api.handlers.topic;

import static api.handlers.topic.APIGetTopicHandler.getAllTopics;
import static api.handlers.topic.APIGetTopicHandler.getTopicByID;
import static api.handlers.topic.APIGetTopicHandler.getTopicsBySubjectSortedByRating;
import static api.handlers.topic.APIGetTopicHandler.getTopicsByTitle;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.Subject;
import data.Topic;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.user.User;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APIGetTopicHandlerTest extends BaseTest {

  private Topic topic;
  private User user;
  private Subject subject;

  @Before
  public void setUp() throws Exception {
    user = new User("username", "user@email.com", "password");
    user.create();
    user.createSessionToken();
    user.update();

    topic = new Topic("Test title", "Test description");
    topic.create();

    Rating rating = new Rating(user.getId(), topic.getId(), RatingEnum.Ok);
    rating.create();

    subject = new Subject("Test title", "Test subject");
    subject.create();
    subject.addParticipant(user);
    subject.addTopic(topic);
    subject.update();
  }

  @Test
  public void testCreateAboutTopic() {
    JSONObject data = topic.createAbout();
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

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicsBySubjectSortedByRatingWrongMethod() {
    HttpRequest httpRequest = buildRequestContent("topic/subject/ordered", "GET", true);
    getTopicsBySubjectSortedByRating(httpRequest);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testGetTopicsBySubjectSortedByRatingNotLoggedIn() {
    HttpRequest httpRequest = buildRequestContent("topic/subject/ordered", "POST", false);
    getTopicsBySubjectSortedByRating(httpRequest);
  }


  @Test(expected = APIBadRequestException.class)
  public void testGetTopicsBySubjectSortedByRatingFieldsNotSet() {
    HttpRequest httpRequest = buildRequestContent("topic/subject/ordered", "POST", user, true,
        "{}");
    getTopicsBySubjectSortedByRating(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicsBySubjectSortedByRatingSubjectIDNotInteger() {
    HttpRequest httpRequest = buildRequestContent("topic/subject/ordered", "POST", user, true,
        "{\"subjectID\":\"lol\"");
    getTopicsBySubjectSortedByRating(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicsBySubjectSortedByRatingSubjectIDNotCorrect() {
    HttpRequest httpRequest = buildRequestContent("topic/subject/ordered", "POST", user, true,
        "{\"subjectID\":-1");
    getTopicsBySubjectSortedByRating(httpRequest);
  }

  @Test
  public void testGetTopicsBySubjectSortedByRating() {
    HttpRequest httpRequest = buildRequestContent("topic/subject/ordered", "POST", true);
    String response = getTopicsBySubjectSortedByRating(httpRequest);
    assertEquals("{\"topics\":[" + topic.createAbout().toString() + "]}",
        response);
  }

  private HttpRequest buildRequestContent(String url, String method, boolean setLoggedIn) {
    JSONObject content = new JSONObject();
    content.put("subjectID", subject.getId());

    return buildRequestContent("topic/subject/ordered", method, user, setLoggedIn, content.toString());
  }

}
