package api.handlers.exercise;

import static api.helpers.JSONCheckerHelper.getJSONArray;
import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.Topic;
import data.dao.ExerciseRatingDAO;
import data.dao.TopicDAO;
import data.exercise.Exercise;
import data.exerciserating.ExerciseRatingEnum;
import data.user.User;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpRequest;

public class APIAddExerciseHandler {

  /**
   * An API handler for adding exercises consisting of text only. Requires the user to be logged in
   * and the following data:
   *        title (String): the title the exercise
   *        text (String): the text of the exercise
   *        difficulty (String): the difficulty of the exercise, as rated by the uploader of the
   *  exercise. Valid values are "Easy", "Medium",  "Hard", or "Unknown".
   *        topicIDs (Array of integers): id's of the topic's the exercise is related to
   *        solution (String, optional): the solution of the exercise
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the data from exercise.createAbout()
   */
  public static String handleAddTextOnlyExercise(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    User user = getUserFromRequest(httpRequest, ", cannot create a new exercise");

    String title = getJSONField(httpRequest, String.class, "title");
    String text = getJSONField(httpRequest, String.class, "text");

    String solution;
    try{
      solution = getJSONField(httpRequest, String.class, "solution");
    } catch (APIBadRequestException are){
      solution = null;
    }

    ExerciseRatingEnum difficulty;
    try{
      difficulty = ExerciseRatingEnum.valueOf(getJSONField(httpRequest, String.class, "difficulty"));
    } catch (IllegalArgumentException iae){
      throw new APIBadRequestException("Invalid difficulty");
    }

    List<Topic> topics = getJSONArray(httpRequest, Integer.class, "topicIDs").stream()
        .map(id -> TopicDAO.getInstance().findById(id)).collect(Collectors.toList());

    boolean hadNulls = topics.removeAll(Collections.singleton(null));
    if(hadNulls){
      throw new APIBadRequestException("One of the topics does not exist");
    }

    Exercise exercise = new Exercise(title, text, solution);
    exercise.create();
    exercise.addToTopics(topics);

    ExerciseRatingDAO.getInstance().createOrUpdate(user, exercise, difficulty);

    return exercise.createAbout().toString();
  }

}
