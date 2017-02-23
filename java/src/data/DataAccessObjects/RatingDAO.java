package data.DataAccessObjects;

import data.DataAccessObjects.util.FieldTuple;
import data.Topic;
import data.User;
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
   * Finds a Rating related to the ratingKey (tuple of userId and topicId)
   *
   * @param ratingKey, a RatingKey object
   * @return A Rating object, null if none is found
   */
  public Rating findRatingByRatingKey(RatingKey ratingKey) {
    List<Rating> result = super.find("findRatingByRatingKey",
        new FieldTuple("userID", ratingKey.getUserID()),
        new FieldTuple("topicID", ratingKey.getTopicID()));
    if (!result.isEmpty()) {
      return result.get(0);
    }
    return null;
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
