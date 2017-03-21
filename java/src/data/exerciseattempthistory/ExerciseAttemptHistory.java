package data.exerciseattempthistory;

import data.AbstractBaseEntity;
import data.dao.ExerciseAttemptHistoryDAO;
import data.exercise.Exercise;
import data.user.User;
import java.util.Date;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllExerciseAttemptHistoriesForUser",
        query = "SELECT eah FROM ExerciseAttemptHistory eah WHERE eah.user = :user"),
    @NamedQuery(name = "findAllExerciseAttemptHistoriesForExercise",
        query = "SELECT eah FROM ExerciseAttemptHistory eah WHERE eah.exercise = :exercise"),
})
@Table
public class ExerciseAttemptHistory extends AbstractBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date attemptDate;

  private boolean success;

  @ManyToOne
  private User user;

  @ManyToOne
  private Exercise exercise;

  protected ExerciseAttemptHistory(){
  }

  public ExerciseAttemptHistory(User user, Exercise exercise, boolean wasSuccessful){
    this.user = user;
    this.exercise = exercise;
    this.success = wasSuccessful;
    attemptDate = new Date();
  }

  public Integer getId() {
    return id;
  }

  public Date getAttemptDate() {
    return attemptDate;
  }

  public boolean wasSuccess() {
    return success;
  }

  public User getUser() {
    return user;
  }

  public Exercise getExercise() {
    return exercise;
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
