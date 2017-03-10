package data.dao;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Subject;
import data.Topic;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import data.user.User;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TopicDAOTest extends BaseTest{

  private static Topic topic1;
  private static Topic topic2;
  private static Topic topic3;
  private static Topic topic4;

  private static User user;

  private static Rating rating1;
  private static Rating rating2;
  private static Rating rating3;
  private static Rating rating4;

  private static Subject subject;

  @Before
  public void populate(){
    user = new User("John", "john@email.com", "hunter2");
    user.create();

    topic1 = new Topic("Philosophy 101", "Think and argue");
    topic2 = new Topic("Philosophy 102", "Think and argue some more");
    topic3 = new Topic("Programming", "Make the computer do stuff");
    topic4 = new Topic("Math", "Add ones and zeros");
    topic1.create();
    topic2.create();
    topic3.create();
    topic4.create();

    subject = new Subject("Math & Programing & Philosophy", "");
    subject.addParticipant(user);
    subject.addTopic(topic1);
    subject.addTopic(topic2);
    subject.addTopic(topic3);
    subject.addTopic(topic4);
    subject.create();

    rating1 = new Rating(user.getId(), topic1.getId(), RatingEnum.SUPERB);
    rating2 = new Rating(user.getId(), topic2.getId(), RatingEnum.GOOD);
    rating3 = new Rating(user.getId(), topic3.getId(), RatingEnum.OK);
    rating4 = new Rating(user.getId(), topic4.getId(), RatingEnum.POOR);

    rating1.create();
    rating2.create();
    rating3.create();
    rating4.create();
  }

  @Test
  public void testFindAll() throws Exception {
    List result = TopicDAO.getInstance().findAll();
    assertTrue(result.contains(topic1));
    assertTrue(result.contains(topic2));
    assertTrue(result.contains(topic3));
    assertTrue(result.contains(topic4));
    assertEquals(4, result.size());
  }

  @Test
  public void testFindTopicsByTitle() throws Exception {
    List philTopics = TopicDAO.getInstance().findTopicsByTitle("Philosophy");
    assertTrue(philTopics.contains(topic1));
    assertTrue(philTopics.contains(topic2));
    assertEquals(2, philTopics.size());

    List progTopics = TopicDAO.getInstance().findTopicsByTitle("Programming");
    assertTrue(progTopics.contains(topic3));
    assertEquals(1, progTopics.size());
  }

  @Test
  public void testFindTopicBySubjectUserSortedByRating() throws Exception {
    List<Topic> topics = TopicDAO.getInstance().findTopicBySubjectUserSortedByRating(subject, user);
    assertEquals(topic1, topics.get(3));
    assertEquals(topic2, topics.get(2));
    assertEquals(topic3, topics.get(1));
    assertEquals(topic4, topics.get(0));
    assertEquals(4, topics.size());
  }

}
