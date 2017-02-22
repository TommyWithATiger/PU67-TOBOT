package data;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import base.BaseTest;
import data.DataAccessObjects.TopicDAO;
import javax.persistence.EntityManagerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TopicTest extends BaseTest {

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private TopicDAO topicDAO;

  @Test
  public void testMakeTopic() {
    Topic topic1 = new Topic();
    topic1.setTitle("Math");
    topic1.setDescription("From algebra to calculus.");
    Topic topic2 = new Topic("English", "blah, blah");
    Topic topic3 = new Topic("Music", "Piano lessons", 1);

    assertNotNull(topic1);
    assertEquals("Math", topic1.getTitle());
    assertEquals("From algebra to calculus.", topic1.getDescription());

    assertNotNull(topic2);
    assertEquals("English", topic2.getTitle());
    assertEquals("blah, blah", topic2.getDescription());

    assertNotNull(topic2);
    assertEquals("Music", topic3.getTitle());
    assertEquals("Piano lessons", topic3.getDescription());
    assertEquals(1, topic3.getParentId());

  }

  @Test
  public void testGetSetTitle() {
    Topic topic = new Topic("Math", "From algebra to calculus.", 1);


    assertEquals("Math", topic.getTitle());
    assertNotSame("English", topic.getTitle());

    topic.setTitle("English");

    assertNotSame("Math", topic.getTitle());
    assertEquals("English", topic.getTitle());
  }

  @Test
  public void testGetSetDescription() {
    Topic topic = new Topic("Math", "From algebra to calculus.", 1);

    assertEquals("From algebra to calculus.", topic.getDescription());
    assertNotSame("Piano lessons.", topic.getDescription());

    topic.setDescription("blah, blah");

    assertNotSame("From algebra to calculus.", topic.getDescription());
    assertEquals("blah, blah", topic.getDescription());
  }

  @Test
  public void testGetSetParentID(){
    Topic topic = new Topic("Math", "From algebra to calculus.", 1);

    assertEquals(1, topic.getParentId());
    assertNotSame(3, topic.getParentId());

    assertTrue(topic.setParentId(3));
    //assertFalse(topic.setParentId(231)); //TODO check not existing parent id
    assertFalse(topic.setParentId(topic.getId()));

  }

  @Test
  public void testGetID() {
    Topic topic = new Topic("Math", "From algebra to calculus.", 1);

    assertEquals(0, topic.getId());

    topic.setParentId(3);

    assertEquals(3, topic.getParentId());
    assertNotSame(1, topic.getParentId());
  }


}
