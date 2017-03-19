package api.handlers.exercise;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;

import api.exceptions.APIBadRequestException;
import data.Topic;
import data.dao.ExerciseDAO;
import data.dao.TopicDAO;
import data.exercise.Exercise;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class APIGetExerciseHandler {

  /**
   * An API handler for getting information about a exercise from the exercise id. Requires the
   * id of the exercise.
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing the variables in exercise.createAbout()
   */
  public static String getExerciseByID(HttpRequest httpRequest) {
    checkRequestMethod("GET", httpRequest);

    Exercise exercise = ExerciseDAO.getInstance().findById(getIntegerURIField(httpRequest, "id"));

    if (exercise == null) {
      throw new APIBadRequestException("No exercise with the given id");
    }

    return exercise.createAbout().toString();
  }

  /**
   * An API handler for getting all exercises related to a topic. Requires the id of the topic.
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "exercises" which is an array of JSON objects on the
   * form of exercise.createAbout method for each exercise
   */
  public static String getExercisesByTopic(HttpRequest httpRequest) {
    checkRequestMethod("GET", httpRequest);

    Topic topic = TopicDAO.getInstance().findById(getIntegerURIField(httpRequest, "topic"));

    if (topic == null) {
      throw new APIBadRequestException("No topic with the given id");
    }

    List<Exercise> exercises = ExerciseDAO.getInstance().findExerciseByTopic(topic);

    if (exercises == null) {
      throw new APIBadRequestException("No exercise for the topic with the given id");
    }

    JSONObject response = new JSONObject();
    response.put("exercises", new JSONArray(exercises.stream().map(Exercise::createAbout).collect(
        Collectors.toList())));
    return response.toString();
  }

}
