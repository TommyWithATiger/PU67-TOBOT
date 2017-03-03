package data;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import base.BaseTest;
import data.DataAccessObjects.TopicDAO;
import java.util.List;
import org.junit.Test;

public class TopicTest extends BaseTest {

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
    assertFalse(topic.setParentId(topic.getId()));

  }

  @Test
  public void testGetID() {
    Topic topic = new Topic("Math", "From algebra to calculus.");

    assertEquals(0, topic.getId());
  }

  @Test
  public void addToRemoveFromSubject() {
    Topic topic = new Topic("Math", "From algebra to calculus.");
    Subject subject = new Subject("Math 101", "intro course to basic math.");

    topic.addToSubject(subject);
    assertTrue(subject.hasTopic(topic));

    topic.removeFromSubject(subject);
    assertFalse(subject.hasTopic(topic));
  }

  @Test
  public void testCreate() throws Exception {
    Topic topic = new Topic("topic", "The best topic");

    topic.create();

    Topic test = TopicDAO.getInstance().findById(topic.getId());

    assertEquals(topic, test);
  }

  @Test
  public void testDelete() throws Exception {
    Topic topic = new Topic("topic", "The best topic");
    topic.create();
    Topic test = TopicDAO.getInstance().findById(topic.getId());
    assertEquals(topic, test);

    topic.delete();
    test = TopicDAO.getInstance().findById(topic.getId());
    assertEquals(null, test);
  }

  @Test
  public void testUpdate() throws Exception {
    Topic topic = new Topic("topic", "The best topic");

    topic.create();

    topic.setTitle("other topic");
    topic.update();

    List<Topic> results = TopicDAO.getInstance().findTopicsByTitle("other topic");

    assertTrue(results.contains(topic));

    assertEquals(topic.hashCode(), topic.getId());
  }

  @Test
  public void testComparison() {
    Topic topic = new Topic("topic", "The best topic");
    topic.create();
    Topic topic2 = new Topic("topic2", "The second best topic");
    topic2.create();

    assertNotSame(topic, topic2);
    assertEquals(topic, TopicDAO.getInstance().findById(topic.getId()));
    assertFalse(topic.equals(5));
    assertFalse(topic.equals(null));
  }

}
