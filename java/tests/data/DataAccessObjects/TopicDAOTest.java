package data.DataAccessObjects;

import base.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TopicDAOTest extends BaseTest {


  @Test
  public void placeholder(){

  }
  /*Non functional at the moment

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
    topic.delete();
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
    Topic result = topicDAO.findSingleTopicsByTitle("Math");
    assertEquals(result, topic);
  }

  @Test
  public void findTopicsByTitle() {
    Topic topic = new Topic("Math", "From algebra to calculus.");
    topicDAO.merge(topic);
    List<Topic> result = topicDAO.findTopicsByTitle("Math");
    assertTrue(result.contains(topic));
  }
  */
}
