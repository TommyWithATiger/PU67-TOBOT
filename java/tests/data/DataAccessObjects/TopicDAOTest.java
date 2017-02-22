package data.DataAccessObjects;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import base.BaseTest;
import data.Topic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TopicDAOTest extends BaseTest {

  @Mock
  private TopicDAO topicDAO;
  @Mock
  private EntityManagerFactory entityManagerFactory;
  @Mock
  private EntityManager entityManager;
  @Mock
  private EntityTransaction entityTransaction;

  @Before
  public void init() {
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(entityTransaction);
    //this.topicDAO = new TopicDAO(entityManagerFactory);
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

    topic = topicDAO.merge(topic);
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
    Topic result = topicDAO.findSingleTopicByTitle("Math");
    assertEquals(result, topic);
  }

  @Test
  public void findTopicsByTitle() {
    Topic topic = new Topic("Math", "From algebra to calculus.");
    topicDAO.merge(topic);
    List<Topic> result = topicDAO.findTopicByTitle("Math");
    assertTrue(result.contains(topic));
  }

}
