package data.dao;

import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingKey;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class ExerciseRatingDAO extends AbstractBaseDAO<ExerciseRating, ExerciseRatingKey> {

  private static ExerciseRatingDAO instance;

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
