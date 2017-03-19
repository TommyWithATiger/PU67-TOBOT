package data.dao;

import data.Topic;
import data.dao.util.FieldTuple;
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

  public List<Exercise> findExerciseByTopic(Topic topic) {
    return super.find("findExerciseByTopic",
        new FieldTuple("topic", topic));
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
