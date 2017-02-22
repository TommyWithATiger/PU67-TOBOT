package data.DataAccessObjects;

import data.rating.Rating;
import data.rating.RatingKey;
import javax.persistence.EntityManagerFactory;

public class RatingDAO extends AbstractBaseDAO<Rating, RatingKey> {

  protected static RatingDAO instance;

  /**
   * Instantiates the SubjectDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  public RatingDAO(EntityManagerFactory emFactory) {
    super(Rating.class, emFactory);
  }

  /**
   * Returns the static SubjectDAO instance
   *
   * @return SubjectDAO, null if not instantiated
   */
  public static RatingDAO getInstance() {
    return instance;
  }

  /**
   * Returns the static SubjectDAO instance,
   * Creates one if null
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   * @return the Static SubjectDAO instance for this server instance
   */
  public static RatingDAO getInstance(EntityManagerFactory emFactory) {
    if (instance == null) {
      instance = new RatingDAO(emFactory);
    }
    return instance;
  }

}
