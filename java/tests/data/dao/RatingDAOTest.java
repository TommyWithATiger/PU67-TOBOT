package data.dao;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Subject;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class RatingDAOTest extends BaseTest{

  private static User user1;
  private static User user2;

  private static Topic topic1;
  private static Topic topic2;
  private static Topic topic3;

  private static RatingKey rk11;
  private static RatingKey rk12;
  private static RatingKey rk21;
  private static RatingKey rk13;
  private static RatingKey rk23;

  private static Rating rating11;
  private static Rating rating12;
  private static Rating rating21;
  private static Rating rating13;
  private static Rating rating23;

  private static Subject subject;

  @Before
  public void populate(){
    user1 = new User("John", "john@email.com", "hunter2");
    user2 = new User("Jane", "jane@email.com", "hunter2");
    user1.create();
    user2.create();

    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic2 = new Topic("Philosophy", "Think and argue");
    topic3 = new Topic("Math", "Add ones and zeros");
    topic1.create();
    topic2.create();
    topic3.create();

    subject = new Subject();
    subject.addParticipant(user1);
    subject.addParticipant(user2);
    subject.addTopic(topic1);
    subject.addTopic(topic2);
    subject.create();

    rk11 = new RatingKey(user1.getId(), topic1.getId());
    rk12 = new RatingKey(user1.getId(), topic2.getId());
    rk21 = new RatingKey(user2.getId(), topic1.getId());
    rk13 = new RatingKey(user1.getId(), topic3.getId());
    rk23 = new RatingKey(user2.getId(), topic3.getId());

    rating11 = new Rating(rk11.getUserID(), rk11.getTopicID(), RatingEnum.GOOD);
    rating12 = new Rating(rk12.getUserID(), rk12.getTopicID(), RatingEnum.POOR);
    rating21 = new Rating(rk21.getUserID(), rk21.getTopicID(), RatingEnum.SUPERB);
    rating13 = new Rating(rk13.getUserID(), rk13.getTopicID(), RatingEnum.OK);
    rating23 = new Rating(rk23.getUserID(), rk23.getTopicID(), RatingEnum.NONE);
    rating11.create();
    rating12.create();
    rating21.create();
    rating13.create();
    rating23.create();
  }

  @Test
  public void testFindAll() throws Exception {
    List result = RatingDAO.getInstance().findAll();
    assertTrue(result.contains(rating11));
    assertTrue(result.contains(rating12));
    assertTrue(result.contains(rating21));
    assertTrue(result.contains(rating13));
    assertTrue(result.contains(rating23));
    assertEquals(5, result.size());
  }

  @Test
  public void testFindRatingByUser() throws Exception {
    List result1 = RatingDAO.getInstance().findRatingByUser(user1);
    assertTrue(result1.contains(rating11));
    assertTrue(result1.contains(rating12));
    assertFalse(result1.contains(rating21));

    List result2 = RatingDAO.getInstance().findRatingByUser(user2);
    assertFalse(result2.contains(rating11));
    assertFalse(result2.contains(rating12));
    assertTrue(result2.contains(rating21));
  }

  @Test
  public void testFindRatingByTopic() throws Exception {
    List result1 = RatingDAO.getInstance().findRatingByTopic(topic1);
    assertTrue(result1.contains(rating11));
    assertFalse(result1.contains(rating12));
    assertTrue(result1.contains(rating21));

    List result2 = RatingDAO.getInstance().findRatingByTopic(topic2);
    assertFalse(result2.contains(rating11));
    assertTrue(result2.contains(rating12));
    assertFalse(result2.contains(rating21));
  }

  @Test
  public void testFindRatingByRatingKey() throws Exception {
    Rating r11 = RatingDAO.getInstance().findById(rk11);
    TestCase.assertEquals(rating11, r11);

    Rating r12 = RatingDAO.getInstance().findById(rk12);
    TestCase.assertEquals(rating12, r12);

    Rating r21 = RatingDAO.getInstance().findById(rk21);
    TestCase.assertEquals(rating21, r21);
  }

  @Test
  public void testFindParticipatingRatingBySubjectTopic() throws Exception {
    Collection<Rating> ratings1 = RatingDAO.getInstance().findParticipatingRatingBySubjectTopic(subject, topic1);
    TestCase.assertEquals(2, ratings1.size());
    assertTrue(ratings1.contains(rating11));
    assertTrue(ratings1.contains(rating21));

    Collection<Rating> ratings2 = RatingDAO.getInstance().findParticipatingRatingBySubjectTopic(subject, topic2);
    TestCase.assertEquals(1, ratings2.size());
    assertTrue(ratings2.contains(rating12));

    Collection<Rating> ratings3 = RatingDAO.getInstance().findParticipatingRatingBySubjectTopic(subject, topic3);
    TestCase.assertEquals(0, ratings3.size());
  }

}
