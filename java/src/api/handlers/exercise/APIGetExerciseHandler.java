package api.handlers.exercise;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.Topic;
import data.dao.ExerciseDAO;
import data.dao.TopicDAO;
import data.exercise.Exercise;
import data.user.User;
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

  /**
   * An API handler for getting the next exercises to do in a topic. Requires the user to be logged
   * in and the following data:
   *        topicID (Integer): id of the topic the exercise is related to
   *        limit (Integer, optional (defaults to 1)): the maximum number of exercises to get
   *
   * @param httpRequest The request to handle
   * @return A JSON object containing a variable "exercises" which is an array of JSON objects on the
   * form of exercise.createAbout method for each exercise
   */
  public static String getNextExercises(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    User user = getUserFromRequest(httpRequest, ", cannot create a new exercise");

    Topic topic = TopicDAO.getInstance().findById(getJSONField(httpRequest, Integer.class, "topicID"));
    if (topic == null) {
      throw new APIBadRequestException("No topic with the given id");
    }

    Integer limit;
    try{
      limit = getJSONField(httpRequest, Integer.class, "limit");
    } catch (APIBadRequestException are){
      limit = 1;
    }

    List<Exercise> exercises = ExerciseDAO.getInstance().getNextExercises(user, topic, limit);

    if (exercises == null) {
      throw new APIBadRequestException("Could not find exercises for the topic with the given id");
    }

    JSONObject response = new JSONObject();
    response.put("exercises", new JSONArray(exercises.stream().map(Exercise::createAbout).collect(
        Collectors.toList())));

    return response.toString();
  }

}
