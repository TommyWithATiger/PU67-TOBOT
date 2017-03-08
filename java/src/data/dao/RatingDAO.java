package data.dao;

import data.dao.util.FieldTuple;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingKey;
import java.util.List;
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
   * Finds all ratings in the database
   *
   * @return List of Rating objects
   */
  public List<Rating> findAll() {
    return super.find("findAllRatings");
  }

  /**
   * Finds Ratings related to the user
   *
   * @param user, the related user object
   * @return List of Rating objects
   */
  public List<Rating> findRatingByUser(User user) {
    return super.find("findRatingByUser",
        new FieldTuple("userID", user.getId()));
  }

  /**
   * Finds Ratings related to a topic
   *
   * @param topic, the related topic object
   * @return List of Rating objects
   */
  public List<Rating> findRatingByTopic(Topic topic) {
    return super.find("findRatingByTopic",
        new FieldTuple("topicID", topic.getId()));
  }

  /**
   * Returns the static SubjectDAO instance
   *
   * @return RatingDAO, null if not instantiated
   */
  public static RatingDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static RatingDAO instance
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !RatingDAO.emFactory.isOpen()) {
      instance = new RatingDAO(emFactory);
    }
  }

}
