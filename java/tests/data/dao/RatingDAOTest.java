package data.dao;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class RatingDAOTest extends BaseTest{

  private static User user1;
  private static User user2;

  private static Topic topic1;
  private static Topic topic2;

  private static RatingKey rk11;
  private static RatingKey rk12;
  private static RatingKey rk21;

  private static Rating rating11;
  private static Rating rating12;
  private static Rating rating21;

  @Before
  public void populate(){
    user1 = new User("John", "john@email.com", "hunter2");
    user1.create();
    user2 = new User("Jane", "jane@email.com", "hunter2");
    user2.create();

    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic1.create();
    topic2 = new Topic("Philosophy", "Think and argue");
    topic2.create();

    rk11 = new RatingKey(user1.getId(), topic1.getId());
    rk12 = new RatingKey(user1.getId(), topic2.getId());
    rk21 = new RatingKey(user2.getId(), topic1.getId());

    rating11 = new Rating(rk11.getUserID(), rk11.getTopicID(), RatingEnum.GOOD);
    rating11.create();
    rating12 = new Rating(rk12.getUserID(), rk12.getTopicID(), RatingEnum.POOR);
    rating12.create();
    rating21 = new Rating(rk21.getUserID(), rk21.getTopicID(), RatingEnum.SUPERB);
    rating21.create();
  }

  @Test
  public void testFindAll() throws Exception {
    List result = RatingDAO.getInstance().findAll();
    assertTrue(result.contains(rating11));
    assertTrue(result.contains(rating12));
    assertTrue(result.contains(rating21));
    assertEquals(3, result.size());
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

}
