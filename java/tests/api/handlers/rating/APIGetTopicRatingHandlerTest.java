package api.handlers.rating;

import static api.handlers.rating.APIGetTopicRatingHandler.getTopicRatingByTopicID;
import static api.handlers.rating.APIGetTopicRatingHandler.getTopicRatings;
import static org.junit.Assert.assertEquals;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.Topic;
import data.User;
import data.rating.Rating;
import data.rating.RatingConverter;
import data.rating.RatingEnum;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Before;
import org.junit.Test;

public class APIGetTopicRatingHandlerTest extends BaseTest {

  private User user;
  private Topic topic;
  private Rating rating;

  @Before
  public void setup() {
    user = new User("username", "user@email.com", "password");
    user.create();

    user.createSessionToken();
    user.update();

    topic = new Topic("Test topic", "Description");
    topic.create();

    rating = new Rating(user.getId(), topic.getId(), RatingEnum.GOOD);
    rating.create();
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testGetTopicRatingByIDNoLogin() {
    HttpRequest httpRequest = buildRequest("topic/url?id=12", "POST", false);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicRatingByIDWrongMethod() {
    HttpRequest httpRequest = buildRequest("topic/url?id=12", "GET", true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDNoIDInURL() {
    HttpRequest httpRequest = buildRequest("topic/url?test=hei", "POST", true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDIDNotValid() {
    HttpRequest httpRequest = buildRequest("topic/url?id=-21", "POST", true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDIDNotNumber() {
    HttpRequest httpRequest = buildRequest("topic/url?id=hei", "POST", true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDNoRatingSet() {
    rating.delete();

    HttpRequest httpRequest = buildRequest("topic/url?id=" + String.valueOf(topic.getId()), "POST",
        true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test
  public void testGetTopicRatingByIDValid() {
    HttpRequest httpRequest = buildRequest("topic/url?id=" + String.valueOf(topic.getId()), "POST",
        true);
    String response = getTopicRatingByTopicID(httpRequest);
    assertEquals(
        "{\"rating\":\"" + RatingConverter.convertEnumToFullRatingName(rating.getRating()) + "\"}",
        response);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testGetTopicRatingsNoLogin() {
    HttpRequest httpRequest = buildRequest("topic/url", "POST", false);
    getTopicRatings(httpRequest);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicRatingsWrongMethod() {
    HttpRequest httpRequest = buildRequest("topic/url", "GET", true);
    getTopicRatings(httpRequest);
  }

  @Test
  public void testGetTopicRatings() {
    HttpRequest httpRequest = buildRequest("topic/url", "POST", true);
    String response = getTopicRatings(httpRequest);
    assertEquals(
        "{\"ratings\":[{\"topicID\":" + String.valueOf(topic.getId()) + ",\"rating\":\"Good\"}]}",
        response);
  }

  @Test
  public void testGetTopicRatingsEmpty() {
    rating.delete();
    HttpRequest httpRequest = buildRequest("topic/url", "POST", true);
    String response = getTopicRatings(httpRequest);
    assertEquals("{\"ratings\":[]}", response);
  }

  @Test
  public void testGetTopicRatingsSeveral() {
    Topic topic2 = new Topic("Test topic 2", "Description");
    topic2.create();
    Rating rating2 = new Rating(user.getId(), topic2.getId(), RatingEnum.POOR);
    rating2.create();

    HttpRequest httpRequest = buildRequest("topic/url", "POST", true);
    String response = getTopicRatings(httpRequest);
    assertEquals("{\"ratings\":[{\"topicID\":" + String.valueOf(topic.getId())
        + ",\"rating\":\"Good\"},{\"topicID\":" + String.valueOf(topic2.getId())
        + ",\"rating\":\"Poor\"}]}", response);
  }

  private HttpRequest buildRequest(String url, String method, boolean setLogin) {
    HttpRequest httpRequest = new BasicHttpRequest(method, url);
    if (setLogin) {
      httpRequest.setHeader(new BasicHeader("X-Username", user.getUsername()));
      httpRequest.setHeader(new BasicHeader("Authorization", "Bearer " + user.getSessionToken()));
    }
    return httpRequest;
  }

}