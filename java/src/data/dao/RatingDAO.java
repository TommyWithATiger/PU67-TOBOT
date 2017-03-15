package data.dao;

import data.Subject;
import data.dao.util.FieldTuple;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingKey;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class RatingDAO extends AbstractBaseDAO<Rating, RatingKey> {

  private static RatingDAO instance;

  /**
   * Instantiates the RatingDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  private RatingDAO(EntityManagerFactory emFactory) {
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
   * Finds Ratings related to a Subject and a Topic. Only ratings from users that are participating
   * in the Subject are returned
   *
   * @param subject, the related subject object
   * @param topic, the related topic object
   * @return List of Rating objects
   */
  public List<Rating> findParticipatingRatingBySubjectTopic(Subject subject, Topic topic) {
    return super.find("findParticipatingRatingBySubjectTopic",
        new FieldTuple("subjectID", subject.getId()),
        new FieldTuple("topicID", topic.getId()));
  }

  /**
   * Returns the static RatingDAO instance
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
