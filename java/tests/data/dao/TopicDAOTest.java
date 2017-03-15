package data.dao;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Topic;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TopicDAOTest extends BaseTest{

  private static Topic topic1;
  private static Topic topic2;
  private static Topic topic3;

  @Before
  public void populate(){
    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic1.create();
    topic2 = new Topic("Philosophy 101", "Think and argue");
    topic2.create();
    topic3 = new Topic("Philosophy 102", "Think and argue some more");
    topic3.create();
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
  public void testFindTopicsByTitle() throws Exception {
    List progTopics = TopicDAO.getInstance().findTopicsByTitle("Programming");
    assertTrue(progTopics.contains(topic1));
    assertEquals(1, progTopics.size());

    List philTopics = TopicDAO.getInstance().findTopicsByTitle("Philosophy");
    assertTrue(philTopics.contains(topic2));
    assertTrue(philTopics.contains(topic3));
    assertEquals(2, philTopics.size());
  }

}

