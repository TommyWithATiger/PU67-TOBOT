package api.handlers.rating;

import static api.handlers.rating.APIRateTopicHandler.rateTopic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.dao.RatingDAO;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingConverter;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import java.io.ByteArrayInputStream;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class APIRateTopicHandlerTest extends BaseTest {

  private User user;
  private Topic topic;

  @Before
  public void setup() {
    user = new User("username", "user@email.com", "password");
    user.create();
    user.createSessionToken();
    user.update();

    topic = new Topic("Test topic", "Description");
    topic.create();
  }

  @Test(expected = APIBadMethodException.class)
  public void testRateTopicWrongMethod() {
    HttpRequest httpRequest = buildRequestContent("rate/url", "GET", true);
    rateTopic(httpRequest);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testRateTopicNotLoggedIn() {
    HttpRequest httpRequest = buildRequestContent("rate/url", "POST", false);
    rateTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRateTopicFieldsNotSet() {
    HttpRequest httpRequest = buildRequestContent("rate/url", "POST", true, "{}");
    rateTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRateTopicTopicIDNotInteger() {
    HttpRequest httpRequest = buildRequestContent("rate/url", "POST", true,
        "{\"topicID\":test,\"rating\":\"Good\"}");
    rateTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRateTopicTopicIDNotCorrect() {
    HttpRequest httpRequest = buildRequestContent("rate/url", "POST", true,
        "{\"topicID\":-12,\"rating\":\"Good\"}");
    rateTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testRateTopicRatingNotValid() {
    HttpRequest httpRequest = buildRequestContent("rate/url", "POST", true,
        "{\"topicID\":" + String.valueOf(topic.getId()) + ",\"rating\":\"asdfhkjjgsfdjklhg\"}");
    rateTopic(httpRequest);
  }

  @Test
  public void testRateTopic() {
    HttpRequest httpRequest = buildRequestContent("rate/url", "POST", true);
    String response = rateTopic(httpRequest);
    assertEquals("{\"topicID\":" + String.valueOf(topic.getId()) + ",\"rating\":\"Good\"}",
        response);
    Rating rating = RatingDAO.getInstance()
        .findById(new RatingKey(user.getId(), topic.getId()));
    assertNotNull(rating);
    assertEquals(RatingEnum.GOOD, rating.getRating());
  }

  @Test
  public void testRateTopicRatingExists() {
    Rating rating = new Rating(user.getId(), topic.getId(), RatingEnum.GOOD);
    rating.create();

    HttpRequest httpRequest = buildRequestContent("rate/url", "POST", true,
        "{\"topicID\":" + String.valueOf(topic.getId()) + ",\"rating\":\"Poor\"}");
    String response = rateTopic(httpRequest);
    assertEquals("{\"topicID\":" + String.valueOf(topic.getId()) + ",\"rating\":\"Poor\"}",
        response);
    Rating ratingResult = RatingDAO.getInstance()
        .findById(new RatingKey(user.getId(), topic.getId()));
    assertNotNull(ratingResult);
    assertEquals(RatingEnum.POOR, ratingResult.getRating());
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
    content.put("topicID", topic.getId());
    content.put("rating", RatingConverter.convertEnumToFullRatingName(RatingEnum.GOOD));

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