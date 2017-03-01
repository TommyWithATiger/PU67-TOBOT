package data.DataAccessObjects;

import data.DataAccessObjects.util.FieldTuple;
import data.Topic;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class TopicDAO extends AbstractBaseDAO<Topic, Integer> {

  protected static TopicDAO instance;

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
   * Finds one topic that matches the given title
   *
   * @param title, the title to query for
   * @return A Topic object that matches the title
   */
  public Topic findSingleTopicsByTitle(String title) {
    List<Topic> results = findTopicsByTitle(title);
    if (results != null) {
      return results.get(0);
    }
    return null;
    //Fixme handle no result exception here
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
    if (instance == null) {
      instance = new TopicDAO(emFactory);
    }
  }
}
