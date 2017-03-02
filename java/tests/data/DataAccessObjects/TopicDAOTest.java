package data.DataAccessObjects;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Topic;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class TopicDAOTest extends BaseTest{

  private static Topic topic1;
  private static Topic topic2;
  private static Topic topic3;

  @BeforeClass
  public static void populate(){
    EntityManager em = entityManagerFactory.createEntityManager();

    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic2 = new Topic("Philosophy 101", "Think and argue");
    topic3 = new Topic("Philosophy 102", "Think and argue some more");

    em.getTransaction().begin();
    em.persist(topic1);
    em.persist(topic2);
    em.persist(topic3);
    em.getTransaction().commit();

    em.close();
  }

  @Override
  public void cleanDB(){
    // Do not clean.
    // This allows populate to be @BeforeClass only, which speeds the tests up by a lot.
  }

  @Test
  public void testFindAll() throws Exception {
    List result = TopicDAO.getInstance().findAll();
    assertTrue(result.contains(topic1));
    assertTrue(result.contains(topic2));
    assertTrue(result.contains(topic3));
    assertEquals(3, result.size());
  }

  @Test
  public void testFindTopicsByTitle1() throws Exception {
    List progTopics = TopicDAO.getInstance().findTopicsByTitle("Programming");
    assertTrue(progTopics.contains(topic1));
    assertEquals(1, progTopics.size());
  }

  @Test
  public void testFindTopicsByTitle2() throws Exception {
    List philTopics = TopicDAO.getInstance().findTopicsByTitle("Philosophy");
    assertTrue(philTopics.contains(topic2));
    assertTrue(philTopics.contains(topic3));
    assertEquals(2, philTopics.size());
  }

}

