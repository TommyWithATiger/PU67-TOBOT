package api.handlers.rating;

import static api.handlers.rating.APIGetTopicRatingHandler.getTopicRatingByTopicID;
import static api.handlers.rating.APIGetTopicRatingHandler.getTopicRatings;
import static api.handlers.rating.APIGetTopicRatingHandler.getTopicsWithRatings;
import static org.junit.Assert.assertEquals;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import api.exceptions.APIRequestForbiddenException;
import base.BaseTest;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingEnum;
import org.apache.http.HttpRequest;
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

    rating = new Rating(user.getId(), topic.getId(), RatingEnum.Good);
    rating.create();
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testGetTopicRatingByIDNoLogin() {
    HttpRequest httpRequest = buildRequest("topic/url?id=12", "POST", user, false);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicRatingByIDWrongMethod() {
    HttpRequest httpRequest = buildRequest("topic/url?id=12", "GET", user, true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDNoIDInURL() {
    HttpRequest httpRequest = buildRequest("topic/url?test=hei", "POST", user, true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDIDNotValid() {
    HttpRequest httpRequest = buildRequest("topic/url?id=-21", "POST", user, true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDIDNotNumber() {
    HttpRequest httpRequest = buildRequest("topic/url?id=hei", "POST", user, true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicRatingByIDNoRatingSet() {
    rating.delete();

    HttpRequest httpRequest = buildRequest("topic/url?id=" + String.valueOf(topic.getId()),
        "POST", user, true);
    getTopicRatingByTopicID(httpRequest);
  }

  @Test
  public void testGetTopicRatingByIDValid() {
    HttpRequest httpRequest = buildRequest("topic/url?id=" + String.valueOf(topic.getId()),
        "POST", user, true);
    String response = getTopicRatingByTopicID(httpRequest);
    assertEquals(
        "{\"rating\":\"" + rating.getRating().toString() + "\"}",
        response);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testGetTopicRatingsNoLogin() {
    HttpRequest httpRequest = buildRequest("topic/url", "POST", user, false);
    getTopicRatings(httpRequest);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicRatingsWrongMethod() {
    HttpRequest httpRequest = buildRequest("topic/url", "GET", user, true);
    getTopicRatings(httpRequest);
  }

  @Test
  public void testGetTopicRatings() {
    HttpRequest httpRequest = buildRequest("topic/url", "POST", user, true);
    String response = getTopicRatings(httpRequest);
    assertEquals(
        "{\"ratings\":[{\"topicID\":" + String.valueOf(topic.getId()) + ",\"rating\":\"Good\"}]}",
        response);
  }

  @Test
  public void testGetTopicRatingsEmpty() {
    rating.delete();
    HttpRequest httpRequest = buildRequest("topic/url", "POST", user, true);
    String response = getTopicRatings(httpRequest);
    assertEquals("{\"ratings\":[]}", response);
  }

  @Test
  public void testGetTopicRatingsSeveral() {
    Topic topic2 = new Topic("Test topic 2", "Description");
    topic2.create();
    Rating rating2 = new Rating(user.getId(), topic2.getId(), RatingEnum.Poor);
    rating2.create();

    HttpRequest httpRequest = buildRequest("topic/url", "POST", user, true);
    String response = getTopicRatings(httpRequest);
    assertEquals("{\"ratings\":[{\"topicID\":" + String.valueOf(topic.getId())
        + ",\"rating\":\"Good\"},{\"topicID\":" + String.valueOf(topic2.getId())
        + ",\"rating\":\"Poor\"}]}", response);
  }

  @Test(expected = APIRequestForbiddenException.class)
  public void testGetTopicsWithRatingsNoLogin() {
    HttpRequest httpRequest = buildRequest("topic/rating/url", "POST", user, false);
    getTopicsWithRatings(httpRequest);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicWithRatingsWrongMethod() {
    HttpRequest httpRequest = buildRequest("topic/rating/url", "GET", user, true);
    getTopicsWithRatings(httpRequest);
  }

  @Test
  public void testGetTopicsWithRatings() {
    HttpRequest httpRequest = buildRequest("topic/rating/url", "POST", user, true);
    String response = getTopicsWithRatings(httpRequest);
    assertEquals(
        "{\"topics\":[{\"rating\":\"Good\",\"description\":\"Description\",\"has-rating\":true,\"id\":"
            + String.valueOf(topic.getId()) + ",\"title\":\"Test topic\"}]}", response);
  }

  @Test
  public void testGetTopicsWithRatingsNoRating() {
    rating.delete();
    HttpRequest httpRequest = buildRequest("topic/rating/url", "POST", user, true);
    String response = getTopicsWithRatings(httpRequest);
    assertEquals(
        "{\"topics\":[{\"description\":\"Description\",\"has-rating\":false,\"id\":"
            + String.valueOf(topic.getId()) + ",\"title\":\"Test topic\"}]}", response);
  }

  @Test
  public void testGetTopicWithRatingsSeveralTopics() {
    Topic topic2 = new Topic("Test topic 2", "Description");
    topic2.create();
    Rating rating2 = new Rating(user.getId(), topic2.getId(), RatingEnum.Poor);
    rating2.create();

    HttpRequest httpRequest = buildRequest("topic/rating/url", "POST", user, true);
    String response = getTopicsWithRatings(httpRequest);
    assertEquals(
        "{\"topics\":[{\"rating\":\"Good\",\"description\":\"Description\",\"has-rating\":true,\"id\":"
            + String.valueOf(topic.getId())
            + ",\"title\":\"Test topic\"},{\"rating\":\"Poor\",\"description\":\"Description\",\"has-rating\":true,\"id\":"
            + String.valueOf(topic2.getId()) + ",\"title\":\"Test topic 2\"}]}", response);
  }

}