package data.dao;

import data.Subject;
import data.dao.util.FieldTuple;
import data.Topic;
import data.user.User;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class TopicDAO extends AbstractBaseDAO<Topic, Integer> {

  private static TopicDAO instance;

  /**
   * Instantiates the TopicDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  public TopicDAO(EntityManagerFactory emFactory) {
    super(Topic.class, emFactory);
  }

  /**
   * Get all database occurrences of Subject
   *
   * @return List of all Subjects
   */
  public List<Topic> findAll() {
    return super.find("findAllTopics");
  }

  /**
   * Finds topics that matches the given title
   *
   * @param title, the title to query for
   * @return List of Topic objects that match the title
   */
  public List<Topic> findTopicsByTitle(String title) {
    return super.find("findTopicsByTitle", new FieldTuple("title", title));
  }

  /**
   * Finds Ratings related to a Subject and a User, sorted by rating (number of stars).
   * Only ratings for topics related to the subject are returned.
   *
   * @param subject, the related subject object
   * @param user, the related user object
   * @return List of Rating objects
   */
  public List<Topic> findTopicBySubjectUserSortedByRating(Subject subject, User user) {
    return super.find("findTopicBySubjectUserSortedByRating",
        new FieldTuple("subjectID", subject.getId()),
        new FieldTuple("userID", user.getId()));
  }


  /**
   * Returns the static TopicDAO instance
   *
   * @return TopicDAO, null if not instantiated
   */
  public static TopicDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static TopicDAO instance,
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !TopicDAO.emFactory.isOpen()) {
      instance = new TopicDAO(emFactory);
    }
  }
}
