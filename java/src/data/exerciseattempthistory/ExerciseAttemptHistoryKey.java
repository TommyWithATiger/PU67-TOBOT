package data.exerciseattempthistory;

import data.exercise.Exercise;
import data.user.User;
import java.io.Serializable;
import javax.persistence.*;


@Embeddable
public class ExerciseAttemptHistoryKey implements Serializable {

  private Integer userID;
  private Integer exerciseID;

  protected ExerciseAttemptHistoryKey() {
  }

  public ExerciseAttemptHistoryKey(User user, Exercise exercise) {
    this.userID = user.getId();
    this.exerciseID = exercise.getId();
  }

  @Override
  public boolean equals(Object other){
    if(userID == null || exerciseID == null){
      return super.equals(other);
    }
    else if(other instanceof ExerciseAttemptHistoryKey){
      ExerciseAttemptHistoryKey otherEAHK = (ExerciseAttemptHistoryKey) other;
      return userID.equals(otherEAHK.userID) && exerciseID.equals(otherEAHK.exerciseID);
    }
    return false;
  }

  @Override
  public int hashCode(){
    // http://stackoverflow.com/a/11742634/1428218
    return 31 * userID + exerciseID;
  }

}
