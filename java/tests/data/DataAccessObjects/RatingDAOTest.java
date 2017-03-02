package data.DataAccessObjects;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import java.util.List;
import javax.persistence.EntityManager;
import junit.framework.TestCase;
import org.junit.BeforeClass;
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

  @BeforeClass
  public static void populate(){
    EntityManager em = entityManagerFactory.createEntityManager();

    user1 = new User("John", "john@email.com", "hunter2");
    user2 = new User("Jane", "jane@email.com", "hunter2");

    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic2 = new Topic("Philosophy", "Think and argue");

    em.getTransaction().begin();
    em.persist(user1);
    em.persist(user2);
    em.persist(topic1);
    em.persist(topic2);
    em.getTransaction().commit();

    rk11 = new RatingKey(user1.getId(), topic1.getId());
    rk12 = new RatingKey(user1.getId(), topic2.getId());
    rk21 = new RatingKey(user2.getId(), topic1.getId());

    rating11 = new Rating(rk11.getUserID(), rk11.getTopicID(), RatingEnum.GOOD);
    rating12 = new Rating(rk12.getUserID(), rk12.getTopicID(), RatingEnum.POOR);
    rating21 = new Rating(rk21.getUserID(), rk21.getTopicID(), RatingEnum.SUPERB);

    em.getTransaction().begin();
    em.persist(rating11);
    em.persist(rating12);
    em.persist(rating21);
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
    List result = RatingDAO.getInstance().findAll();
    assertTrue(result.contains(rating11));
    assertTrue(result.contains(rating12));
    assertTrue(result.contains(rating21));
  }

  @Test
  public void testFindRatingByUser1() throws Exception {
    List result1 = RatingDAO.getInstance().findRatingByUser(user1);
    assertTrue(result1.contains(rating11));
    assertTrue(result1.contains(rating12));
    assertFalse(result1.contains(rating21));
  }

  @Test
  public void testFindRatingByUser2() throws Exception {
    List result2 = RatingDAO.getInstance().findRatingByUser(user2);
    assertFalse(result2.contains(rating11));
    assertFalse(result2.contains(rating12));
    assertTrue(result2.contains(rating21));
  }

  @Test
  public void testFindRatingByTopic1() throws Exception {
    List result1 = RatingDAO.getInstance().findRatingByTopic(topic1);
    assertTrue(result1.contains(rating11));
    assertFalse(result1.contains(rating12));
    assertTrue(result1.contains(rating21));
  }

  @Test
  public void testFindRatingByTopic2() throws Exception {
    List result2 = RatingDAO.getInstance().findRatingByTopic(topic2);
    assertFalse(result2.contains(rating11));
    assertTrue(result2.contains(rating12));
    assertFalse(result2.contains(rating21));
  }

  @Test
  public void testFindRatingByRatingKey1() throws Exception {
    Rating r11 = RatingDAO.getInstance().findById(rk11);
    TestCase.assertEquals(rating11, r11);
  }

  @Test
  public void testFindRatingByRatingKey2() throws Exception {
    Rating r12 = RatingDAO.getInstance().findById(rk12);
    TestCase.assertEquals(rating12, r12);
  }

  @Test
  public void testFindRatingByRatingKey3() throws Exception {
    Rating r21 = RatingDAO.getInstance().findById(rk21);
    TestCase.assertEquals(rating21, r21);
  }

}
