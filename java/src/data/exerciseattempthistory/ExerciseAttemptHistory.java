package data.exerciseattempthistory;

import data.dao.ExerciseAttemptHistoryDAO;
import data.exercise.Exercise;
import data.user.User;
import java.util.Date;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllExerciseAttemptHistoriesForUser",
        query = "SELECT eah FROM ExerciseAttemptHistory eah WHERE eah.id.userID = :userID"),
    @NamedQuery(name = "findAllExerciseAttemptHistoriesForExercise",
        query = "SELECT eah FROM ExerciseAttemptHistory eah WHERE eah.id.exerciseID = :exerciseID"),
})
@Table
public class ExerciseAttemptHistory {

  private static final int CONSECUTIVE_COMPLETIONS_TO_RETIRE = 6;

  @EmbeddedId
  private ExerciseAttemptHistoryKey id;

  private int attempts = 0;
  private int consecutiveSuccessAttempts = 0;

  private boolean isRetired = false;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastAttemptDate;

  protected ExerciseAttemptHistory(){
    id = new ExerciseAttemptHistoryKey();
  }

  public ExerciseAttemptHistory(ExerciseAttemptHistoryKey id){
    this.id = id;
  }

  public ExerciseAttemptHistory(User user, Exercise exercise){
    this(new ExerciseAttemptHistoryKey(user, exercise));
  }

  /**
   * Adds the Exercise to the database
   */
  public void create() {
    ExerciseAttemptHistoryDAO.getInstance().persist(this);
  }

  /**
   * Removes the Exercise from the database
   */
  public void delete() {
    ExerciseAttemptHistoryDAO.getInstance().remove(this);
  }

  /**
   * Updates the Exercise's database entry
   */
  public void update() {
    ExerciseAttemptHistoryDAO.getInstance().merge(this);
  }

  public boolean isRetired() {
    return isRetired;
  }

  /**
   * Registers an attempt on the related exercise by the related user.
   *
   * @param wasSuccess true if the user was successful in its attempt to do the exercise
   */
  public void registerAttempt(boolean wasSuccess){
    attempts += 1;
    lastAttemptDate = new Date();
    if (wasSuccess){
      consecutiveSuccessAttempts += 1;
      if (consecutiveSuccessAttempts >= CONSECUTIVE_COMPLETIONS_TO_RETIRE){
        isRetired = true;
      }
    } else{
      consecutiveSuccessAttempts = 0;
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof ExerciseAttemptHistory)) {
      return false;
    }
    ExerciseAttemptHistory otherEAH = (ExerciseAttemptHistory) other;
    return otherEAH.id.equals(this.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

}
