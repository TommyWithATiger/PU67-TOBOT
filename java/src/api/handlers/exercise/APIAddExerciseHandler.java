package api.handlers.exercise;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.Topic;
import data.dao.ExerciseRatingDAO;
import data.dao.TopicDAO;
import data.exercise.Exercise;
import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingEnum;
import data.exerciserating.ExerciseRatingKey;
import data.user.User;
import org.apache.http.HttpRequest;

public class APIAddExerciseHandler {

  /**
   * An API handler for adding exercises consisting of text only. Requires the user to be logged in
   * and the following data:
   *        title (String): the title the exercise
   *        text (String): the text of the exercise
   *        difficulty (String): the difficulty of the exercise, as rated by the uploader of the
   *  exercise. Valid values are "Easy", "Medium",  "Hard", or "Unknown".
   *        solution (String): the solution of the exercise
   *        topicID (Integer): id of the topic the exercise is related to
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
    String solution = getJSONField(httpRequest, String.class, "solution");

    ExerciseRatingEnum difficulty;
    try{
      difficulty = ExerciseRatingEnum.valueOf(getJSONField(httpRequest, String.class, "difficulty"));
    } catch (IllegalArgumentException iae){
      throw new APIBadRequestException("Invalid difficulty");
    }

    Topic topic = TopicDAO.getInstance().findById(getJSONField(httpRequest, Integer.class, "topicID"));
    if(topic == null){
      throw new APIBadRequestException("Topic does not exist");
    }

    Exercise exercise = new Exercise(title, text, solution);
    exercise.create();
    topic.addExercise(exercise);
    topic.update();

    ExerciseRating exerciseRating = ExerciseRatingDAO.getInstance().findById(new ExerciseRatingKey(user, exercise));
    if(exerciseRating == null){
      exerciseRating = new ExerciseRating(user, exercise, difficulty);
      exerciseRating.create();
    } else {
      exerciseRating.setExerciseRating(difficulty);
      exerciseRating.update();
    }

    return exercise.createAbout().toString();
  }

}
