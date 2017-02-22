package data.DataAccessObjects;

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
   * Finds topics that matches the given title
   *
   * @param title, the title to query for
   * @return List of Topic objects that match the title
   */
  public List<Topic> findTopicByTitle(String title) {
    return super.find("findTopicByTitle", "title", title);
  }

  /**
   * Finds one topic that matches the given title
   *
   * @param title, the title to query for
   * @return A Topic object that matches the title
   */
  public Topic findSingleTopicByTitle(String title) {
    List<Topic> results = findTopicByTitle(title);
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
   * Returns the static TopicDAO instance,
   * Creates one if null
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   * @return the Static TopicDAO instance for this server instance
   */
  public static TopicDAO getInstance(EntityManagerFactory emFactory) {
    if (instance == null) {
      instance = new TopicDAO(emFactory);
    }
    return instance;
  }
}
