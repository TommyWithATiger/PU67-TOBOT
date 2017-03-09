package api.handlers.relators;

import static api.handlers.relators.APIGetRelatedTopicsWithRatingCountHandler.getTopicWithRatingCountSubjectID;
import static org.junit.Assert.assertEquals;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.Subject;
import data.Topic;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.user.User;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Before;
import org.junit.Test;

public class APIGetRelatedTopicsWithRatingCountHandlerTest extends BaseTest {

  private User user1;
  private User user2;
  private User user3;
  private User user4;
  private Topic topic1;
  private Topic topic2;
  private Subject subject;

  @Before
  public void setup() {
    user1 = new User("username1", "user1@email.com", "password");
    user2 = new User("username2", "user2@email.com", "password");
    user3 = new User("username3", "user3@email.com", "password");
    user4 = new User("username4", "user4@email.com", "password");
    user1.createSessionToken();
    user2.createSessionToken();
    user3.createSessionToken();
    user4.createSessionToken();
    user1.create();
    user2.create();
    user3.create();
    user4.create();

    topic1 = new Topic("Test topic 1", "Description");
    topic2 = new Topic("Test topic 2", "Description");
    topic1.create();
    topic2.create();

    subject = new Subject("Test subject", "Description");
    subject.addTopic(topic1);
    subject.addTopic(topic2);
    subject.addParticipant(user1);
    subject.addParticipant(user2);
    subject.addParticipant(user3);
    subject.create();
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetTopicWithRatingCountSubjectIDWrongMethod() {
    HttpRequest request = buildRequest("get/relation/count/url?id=" + String.valueOf(subject.getId()),
        "POST");
    getTopicWithRatingCountSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicWithRatingCountSubjectIDNoArguments() {
    HttpRequest request = buildRequest("get/relation/count/url", "GET");
    getTopicWithRatingCountSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicWithRatingCountSubjectIDNoIDArgument() {
    HttpRequest request = buildRequest("get/relation/count/url?test=2", "GET");
    getTopicWithRatingCountSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicWithRatingCountSubjectIDIDNotInteger() {
    HttpRequest request = buildRequest("get/relation/count/url?id=asdf", "GET");
    getTopicWithRatingCountSubjectID(request);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetTopicWithRatingCountSubjectIDIDNotValid() {
    HttpRequest request = buildRequest("get/relation/count/url?id=-12", "GET");
    getTopicWithRatingCountSubjectID(request);
  }

  @Test
  public void testGetTopicWithRatingCountSubjectIDNoRatings() {
    HttpRequest request = buildRequest("get/relation/count/url?id=" + String.valueOf(subject.getId()),
        "GET");
    String response = getTopicWithRatingCountSubjectID(request);
    assertEquals(
        "{\"related_topics\":[{\"description\":\"Description\",\"id\":" + String
        .valueOf(topic1.getId()) + ",\"title\":\"Test topic 1\",\"ratingCount\":[3,0,0,0,0,0]},"
                                 +    "{\"description\":\"Description\",\"id\":" + String
        .valueOf(topic2.getId()) + ",\"title\":\"Test topic 2\",\"ratingCount\":[3,0,0,0,0,0]}]}", response);
  }

  @Test
  public void testGetTopicWithRatingCountSubjectIDRatings() {
    Rating rating11 = new Rating(user1.getId(), topic1.getId(), RatingEnum.GOOD);
    Rating rating12 = new Rating(user1.getId(), topic2.getId(), RatingEnum.POOR);

    Rating rating21 = new Rating(user2.getId(), topic1.getId(), RatingEnum.GOOD);
    Rating rating22 = new Rating(user2.getId(), topic2.getId(), RatingEnum.SUPERB);

    // rating31 does not exist
    Rating rating32 = new Rating(user3.getId(), topic2.getId(), RatingEnum.OK);

    Rating rating41 = new Rating(user4.getId(), topic1.getId(), RatingEnum.NONE);
    Rating rating42 = new Rating(user4.getId(), topic2.getId(), RatingEnum.OK);

    rating11.create();
    rating12.create();
    rating21.create();
    rating22.create();
    rating32.create();
    rating41.create();
    rating42.create();

    HttpRequest request = buildRequest("get/relation/count/url?id=" + String.valueOf(subject.getId()),
        "GET");
    String response = getTopicWithRatingCountSubjectID(request);
    assertEquals(
        "{\"related_topics\":[{\"description\":\"Description\",\"id\":" + String
            .valueOf(topic1.getId()) + ",\"title\":\"Test topic 1\",\"ratingCount\":[1,0,0,0,2,0]},"
            +    "{\"description\":\"Description\",\"id\":" + String
            .valueOf(topic2.getId()) + ",\"title\":\"Test topic 2\",\"ratingCount\":[0,0,1,1,0,1]}]}", response);
  }

  private HttpRequest buildRequest(String url, String method) {
    return new BasicHttpRequest(method, url);
  }
}
