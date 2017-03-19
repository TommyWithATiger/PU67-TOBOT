package data.dao;

import data.exerciseattempthistory.ExerciseAttemptHistory;
import data.exerciseattempthistory.ExerciseAttemptHistoryKey;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class ExerciseAttemptHistoryDAO extends AbstractBaseDAO<ExerciseAttemptHistory, ExerciseAttemptHistoryKey> {

  private static ExerciseAttemptHistoryDAO instance;

  /**
   * Instantiates the ExerciseDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  private ExerciseAttemptHistoryDAO(EntityManagerFactory emFactory) {
    super(ExerciseAttemptHistory.class, emFactory);
  }

  /**
   * Returns the static ExerciseDAO instance
   *
   * @return RatingDAO, null if not instantiated
   */
  public static ExerciseAttemptHistoryDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static ExerciseDAO instance
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !ExerciseAttemptHistoryDAO.emFactory.isOpen()) {
      instance = new ExerciseAttemptHistoryDAO(emFactory);
    }
  }

}
