package data.dao;

import data.exercise.Exercise;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class ExerciseDAO extends AbstractBaseDAO<Exercise, Integer> {

  private static ExerciseDAO instance;

  /**
   * Instantiates the ExerciseDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  private ExerciseDAO(EntityManagerFactory emFactory) {
    super(Exercise.class, emFactory);
  }

  /**
   * Returns the static ExerciseDAO instance
   *
   * @return RatingDAO, null if not instantiated
   */
  public static ExerciseDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static ExerciseDAO instance
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !ExerciseDAO.emFactory.isOpen()) {
      instance = new ExerciseDAO(emFactory);
    }
  }

}
