package data;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import base.BaseTest;
import javax.persistence.EntityManagerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TopicTest extends BaseTest {

  @Mock
  private EntityManagerFactory entityManagerFactory;


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
  public void testGetters() {
    Topic topic = new Topic("Math", "From algebra to calculus.", 1);

    assertEquals(0, topic.getId());
    assertEquals("Math", topic.getTitle());
    assertNotSame("English", topic.getTitle());
    assertEquals("From algebra to calculus.", topic.getDescription());
    assertNotSame("Piano lessons.", topic.getDescription());
    assertEquals(1, topic.getParentId());
    assertNotSame(3, topic.getParentId());
  }

  @Test
  public void testSetters() {
    Topic topic = new Topic("Math", "From algebra to calculus.", 1);

    topic.setTitle("English");
    topic.setDescription("blah, blah");
    topic.setParentId(3);

    assertNotSame("Math", topic.getTitle());
    assertEquals("English", topic.getTitle());
    assertNotSame("From algebra to calculus.", topic.getDescription());
    assertEquals("blah, blah", topic.getDescription());
    assertEquals(3, topic.getParentId());
    assertNotSame(1, topic.getParentId());
  }


}
