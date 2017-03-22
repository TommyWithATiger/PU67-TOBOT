package api.handlers.exercise;

import static api.handlers.exercise.APIGetExerciseHandler.getExerciseByID;
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
  private Exercise exercise3;

  private Topic topic1;
  private Topic topic2;

  @Before
  public void setup() {
    exercise1 = new Exercise("title1", "text1");
    exercise2 = new Exercise("title2", "text2");
    exercise3 = new Exercise("title3", "text3");
    exercise1.create();
    exercise2.create();
    exercise3.create();
    topic1 = new Topic("title1", "description");
    topic2 = new Topic("title2", "description");
    topic1.addExercise(exercise1);
    topic1.addExercise(exercise2);
    topic2.addExercise(exercise3);
    topic1.create();
    topic2.create();
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
}
