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
   * Finds all exercises in the database
   *
   * @return List of all Exercise objects in the database
   */
  @Override
  public List<Exercise> findAll() {
    return super.find("findAllExercises");
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
