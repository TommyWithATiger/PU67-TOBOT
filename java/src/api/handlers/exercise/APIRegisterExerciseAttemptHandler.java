package api.handlers.exercise;

import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.dao.ExerciseAttemptHistoryDAO;
import data.dao.ExerciseDAO;
import data.dao.ExerciseRatingDAO;
import data.exercise.Exercise;
import data.exerciseattempthistory.ExerciseAttemptHistory;
import data.exerciseattempthistory.ExerciseAttemptHistoryKey;
import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingEnum;
import data.exerciserating.ExerciseRatingKey;
import data.user.User;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIRegisterExerciseAttemptHandler {

  /**
   * An API handler for registering an attempt on an exercises. Requires the user to be logged in
   * and the following data:
   *        exerciseID (Integer): the id of the exercise
   *        difficulty (String): the difficulty of the exercise. Valid values are "Easy", "Medium",
   * "Hard". "Unknown" is not valid.
   *        success (Boolean): whether the user completed the exercise successfully
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the boolean variable "exercise-registered" that indicates whether
   * exercise was successfully registered
   */
  public static String handleRegisterExercise(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    User user = getUserFromRequest(httpRequest, ", cannot register attempt");

    Boolean success = getJSONField(httpRequest, Boolean.class, "success");
    Exercise exercise = ExerciseDAO.getInstance().findById(getJSONField(httpRequest, Integer.class, "exerciseID"));

    if(exercise == null){
      throw new APIBadRequestException("Exercise does not exist");
    }

    ExerciseRatingEnum difficulty;
    try{
      difficulty = ExerciseRatingEnum.valueOf(getJSONField(httpRequest, String.class, "difficulty"));
    } catch (IllegalArgumentException iae){
      throw new APIBadRequestException("Invalid difficulty");
    }
    if(difficulty == ExerciseRatingEnum.Unknown){
      throw new APIBadRequestException("'Unknown' is not a valid difficulty");
    }

    ExerciseAttemptHistory eah = ExerciseAttemptHistoryDAO.getInstance().findById(new ExerciseAttemptHistoryKey(user, exercise));
    if(eah == null){
      eah = new ExerciseAttemptHistory(user, exercise);
      eah.registerAttempt(success);
      eah.create();
    } else {
      eah.registerAttempt(success);
      eah.update();
    }

    ExerciseRating exerciseRating = ExerciseRatingDAO.getInstance().findById(new ExerciseRatingKey(user, exercise));
    if(exerciseRating == null){
      exerciseRating = new ExerciseRating(user, exercise, difficulty);
      exerciseRating.create();
    } else {
      exerciseRating.setExerciseRating(difficulty);
      exerciseRating.update();
    }

    JSONObject response = new JSONObject();
    response.put("exercise-registered", true);

    return response.toString();
  }

}
