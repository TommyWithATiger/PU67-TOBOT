package data.exerciserating;

import data.dao.ExerciseRatingDAO;
import data.exercise.Exercise;
import data.user.User;
import javax.persistence.*;

@Entity
@NamedQueries({
})
@Table
public class ExerciseRating {

  @EmbeddedId
  private ExerciseRatingKey id;

  @Enumerated(value = EnumType.STRING)
  @Convert(converter = ExerciseRatingConverter.class)
  private ExerciseRatingEnum rating;


  public ExerciseRating() {
    super();
    id = new ExerciseRatingKey();
  }

  /**
   * Instantiates a ExerciseRating object
   *
   * @param id, the ExerciseRatingKey that determines the user and exercise this rating is for
   * @param rating, a rating
   */
  public ExerciseRating(ExerciseRatingKey id, ExerciseRatingEnum rating) {
    this();
    this.id = id;
    setExerciseRating(rating);
  }

  /**
   * Instantiates a ExerciseRating object
   *
   * @param userID, the id of the User that made the rating
   * @param exerciseID, the id of the exercise that is being rated
   * @param rating, a rating
   */
  public ExerciseRating(Integer userID, Integer exerciseID, ExerciseRatingEnum rating) {
    this(new ExerciseRatingKey(userID, exerciseID), rating);
  }

  /**
   * Instantiates a ExerciseRating object
   *
   * @param user, the User that made the rating
   * @param exercise, the exercise that is being rated
   * @param rating, a rating
   */
  public ExerciseRating(User user, Exercise exercise, ExerciseRatingEnum rating) {
    this(user.getId(), exercise.getId(), rating);
  }

  /**
   * Get the Id of the user that made the rating
   *
   * @return the Id of the user
   */
  public Integer getUserID() {
    return id.getUserID();
  }

  /**
   * Set the Id of the user that made the rating
   *
   * @param userID the id of the user
   */
  public void setUserID(Integer userID) {
    id.setUserID(userID);
  }

  /**
   * Get the Id of the exercise that was rated
   *
   * @return the Id of the exercise
   */
  public Integer getExerciseID() {
    return id.getExerciseID();
  }

  /**
   * Set the Id of the exercise that was rated
   *
   * @param exerciseID the id of the exercise
   */
  public void setExerciseID(Integer exerciseID) {
    id.setExerciseID(exerciseID);
  }

  /**
   * Get the rating
   *
   * @return the rating
   */
  public ExerciseRatingEnum getRating() {
    return rating;
  }

  /**
   * Get the rating as an integer from 1 to 3
   *
   * @return the integer representing rating
   */
  public int getIntRating() {
    return rating.value();
  }

  /**
   * Set the rating
   *
   * @param ratingEnum the rating
   */
  public void setExerciseRating(ExerciseRatingEnum ratingEnum) {
    this.rating = ratingEnum;
  }

  /**
   * Adds the ExerciseRating to the database
   */
  public void create() {
    ExerciseRatingDAO.getInstance().persist(this);
  }

  /**
   * Removes the ExerciseRating from the database
   */
  public void delete() {
    ExerciseRatingDAO.getInstance().remove(this);
  }

  /**
   * Updates the ExerciseRating's database entry
   */
  public void update() {
    ExerciseRatingDAO.getInstance().merge(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ExerciseRating) {
      return id.equals(((ExerciseRating) other).id);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
