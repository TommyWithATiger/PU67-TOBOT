package api.handlers.exercise;

import static api.handlers.exercise.APIGetExerciseHandler.getExerciseByID;
import static api.handlers.exercise.APIGetExerciseHandler.getExercisesByTopic;
import static junit.framework.TestCase.assertEquals;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.Topic;
import data.exercise.Exercise;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Before;
import org.junit.Test;

public class APIGetExerciseHandlerTest extends BaseTest {

  private Exercise exercise1;
  private Exercise exercise2;

  private Topic topic;

  @Before
  public void setup() {
    exercise1 = new Exercise("title1", "text1");
    exercise2 = new Exercise("title2", "text2");
    exercise1.create();
    exercise2.create();

    topic = new Topic("title1", "description");
    topic.addExercise(exercise1);
    topic.addExercise(exercise2);
    topic.create();
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetExerciseByIDWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "exercise/url?id=12");
    getExerciseByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByIDNoIDInURL() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?test=21");
    getExerciseByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByIDIDNotValid() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?id=-21");
    getExerciseByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByIDIDNotNumber() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?id=lol");
    getExerciseByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByIDNotInDatabase() {
    int id = exercise1.getId();
    exercise1.delete();
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?id=" + String.valueOf(id));
    getExerciseByID(httpRequest);
  }

  @Test
  public void testGetExerciseByIDValid() {
    HttpRequest httpRequest = new BasicHttpRequest("GET",
        "exercise/url?id=" + String.valueOf(exercise1.getId()));
    String response = getExerciseByID(httpRequest);
    assertEquals("{\"id\":" + exercise1.getId() + ",\"text\":\"" + exercise1.getText() +
        "\",\"title\":\"" + exercise1.getTitle() + "\"}", response);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetExercisesByTopicWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "exercise/url?topic=12");
    getExercisesByTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByTopicNoTopicInURL() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?test=21");
    getExercisesByTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByTopicTopicNotValid() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?topic=-21");
    getExercisesByTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByTopicTopicIDNotNumber() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?topic=lol");
    getExercisesByTopic(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetExerciseByTopicNotInDatabase() {
    int id = topic.getId();
    topic.delete();
    HttpRequest httpRequest = new BasicHttpRequest("GET", "exercise/url?topic=" + String.valueOf(id));
    getExercisesByTopic(httpRequest);
  }

  @Test
  public void testGetExerciseByTopicValid() {
    HttpRequest httpRequest = new BasicHttpRequest("GET",
        "exercise/url?topic=" + String.valueOf(topic.getId()));
    String response = getExercisesByTopic(httpRequest);
    assertEquals("{\"exercises\":" +
            "[{\"id\":" + exercise1.getId() + ",\"text\":\"" + exercise1.getText() +
            "\",\"title\":\"" + exercise1.getTitle() + "\"}," +
            "{\"id\":" + exercise2.getId() + ",\"text\":\"" + exercise2.getText() +
            "\",\"title\":\"" + exercise2.getTitle() + "\"}]}",
        response);
  }
}
