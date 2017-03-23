package data.exerciserating;

import data.exercise.Exercise;
import data.user.User;
import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class ExerciseRatingKey implements Serializable {

  private Integer userID;
  private Integer exerciseID;

  public ExerciseRatingKey() {
  }

  /**
   * Instantiates a ExerciseRatingKey object
   *
   * @param userID, the id of the User that made the rating
   * @param exerciseID, the id of the exercise that is being rated
   */
  public ExerciseRatingKey(Integer userID, Integer exerciseID) {
    this.userID = userID;
    this.exerciseID = exerciseID;
  }

  /**
   * Instantiates a ExerciseRatingKey object
   *
   * @param user, the User that made the rating
   * @param exercise, the exercise that is being rated
   */
  public ExerciseRatingKey(User user, Exercise exercise) {
    this(user.getId(), exercise.getId());
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Integer getExerciseID() {
    return exerciseID;
  }

  public void setExerciseID(Integer exerciseID) {
    this.exerciseID = exerciseID;
  }

  @Override
  public boolean equals(Object other) {
    if (userID == null || exerciseID == null) {
      return super.equals(other);
    } else if (other instanceof ExerciseRatingKey) {
      ExerciseRatingKey otherRatingKey = (ExerciseRatingKey) other;
      return userID.equals(otherRatingKey.userID) && exerciseID.equals(otherRatingKey.exerciseID);
    }
    return false;
  }

  @Override
  public int hashCode() {
    // http://stackoverflow.com/a/11742634/1428218
    return 31 * userID + exerciseID;
  }
}
