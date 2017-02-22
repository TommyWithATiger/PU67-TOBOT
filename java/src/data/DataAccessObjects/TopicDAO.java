package data.DataAccessObjects;

import data.Topic;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class TopicDAO extends AbstractBaseDAO<Topic, Integer> {

  public TopicDAO(EntityManagerFactory emFactory) {
    super(Topic.class, emFactory);
  }

  protected static TopicDAO instance;

  public List<Topic> findTopicByTitle(String title) {
    return super.find("findTopicByTitle","title", title);
  }

  public Topic findSingleTopicByTitle(String title) {
    List<Topic> results = findTopicByTitle(title);
    if (results != null) {
      return results.get(0);
    }
    return null;
    //Fixme handle no result exception here
  }

  public static TopicDAO getInstance() {
    return instance;
  }

  public static TopicDAO getInstance(EntityManagerFactory emFactory) {
    if (instance == null) {
      instance = new TopicDAO(emFactory);
    }
    return instance;
  }
}
