package data.dao;

import data.exercise.Exercise;
import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingEnum;
import data.exerciserating.ExerciseRatingKey;
import data.user.User;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class ExerciseRatingDAO extends AbstractBaseDAO<ExerciseRating, ExerciseRatingKey> {

  private static ExerciseRatingDAO instance;

  /**
   * Updates an ExerciseRating if it exists, if not creates it.
   *
   * @param user, the user that made the rating
   * @param exercise, the exercise being rated
   * @param difficulty, the difficulty of the exercise
   */
  public ExerciseRating createOrUpdate(User user, Exercise exercise, ExerciseRatingEnum difficulty){
    ExerciseRating exerciseRating = findById(new ExerciseRatingKey(user, exercise));
    if(exerciseRating == null){
      exerciseRating = new ExerciseRating(user, exercise, difficulty);
      exerciseRating.create();
    } else {
      exerciseRating.setExerciseRating(difficulty);
      exerciseRating.update();
    }
    return exerciseRating;
  }

  /**
   * Instantiates the ExerciseRatingDAO object
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  public ExerciseRatingDAO(EntityManagerFactory emFactory) {
    super(ExerciseRating.class, emFactory);
  }

  /**
   * Returns the static ExerciseRatingDAO instance
   *
   * @return ExerciseRatingDAO, null if not instantiated
   */
  public static ExerciseRatingDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static ExerciseRatingDAO instance
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !ExerciseRatingDAO.emFactory.isOpen()) {
      instance = new ExerciseRatingDAO(emFactory);
    }
  }

}
