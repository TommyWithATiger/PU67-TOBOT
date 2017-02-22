package data.DataAccessObjects;

import data.Topic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class TopicDAO extends DAOBase<Topic, Integer> {

  public TopicDAO(EntityManagerFactory emFactory) {
    super(Topic.class, emFactory);
  }

  public List<Topic> findTopicByTitle(String title) {
    EntityManager em = emFactory.createEntityManager();
    EntityTransaction et = em.getTransaction();
    et.begin();
    TypedQuery<Topic> query = em.createNamedQuery("findTopicByTitle", Topic.class);
    query.setParameter("title", title);
    List<Topic> result = query.getResultList();
    et.commit();
    em.close();
    return result;
  }

  public Topic findSingleTopicByTitle(String title) {
    EntityManager em = emFactory.createEntityManager();
    EntityTransaction et = em.getTransaction();
    et.begin();
    TypedQuery<Topic> query = em.createNamedQuery("findTopicByTitle", Topic.class);
    query.setParameter("title", title);
    Topic result = query.getResultList().get(0);
    et.commit();
    em.close();
    return result;
  }

}
