package data.DataAccessObjects;

import data.Topic;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class TopicDAO extends DAOBase<Topic, Integer> {

  public TopicDAO(EntityManagerFactory emFactory) {
    super(Topic.class, emFactory);
  }

  public List<Topic> findtopicByTitle(String title) {
    TypedQuery<Topic> query = entityManager.createNamedQuery("findTopicByTitle", Topic.class);
    return query.getResultList();
  }

  public Topic findSingleTopicByTitle(String title) {
    TypedQuery<Topic> query = entityManager.createNamedQuery("findTopicByTitle", Topic.class);
    query.setParameter("title", title);
    return query.getSingleResult();
  }

}
