package data.DataAccessObjects;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import base.BaseTest;
import data.Topic;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TopicDAOTest extends BaseTest {

  @Mock
  private TopicDAO topicDAO;
  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Before
  public void init() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
    this.topicDAO = new TopicDAO(entityManagerFactory);
  }

  @Test
  public void persist() {
    Topic topic = new Topic("Math", "From algebra to calculus.");

    topicDAO.persist(topic);

    assertNotNull(topicDAO.findById(topic.getId()));
  }

  @Test
  public void remove() {
    Topic topic = new Topic("Math", "From algebra to calculus.");

    topicDAO.persist(topic);
    assertNotNull(topicDAO.findById(topic.getId()));

    topicDAO.remove(topic);
    assertNull(topicDAO.findById(topic.getId()));
  }

  @Test
  public void findById() {
    Topic topic = new Topic("Math", "From algebra to calculus.");

    topicDAO.persist(topic);

    assertNotNull(topicDAO.findById(topic.getId()));
  }

  @Test
  public void findSingleTopicByTitle() {
    Topic topic = new Topic("Math", "From algebra to calculus.");
    topicDAO.persist(topic);

    assertEquals(topicDAO.findSingleTopicByTitle("Math"), topic);
  }

}
