package data.DataAccessObjects;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.user.User;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserDAOTest extends BaseTest{

  private static User user1;
  private static User user2;
  private static User user3;

  @BeforeClass
  public static void populate(){
    EntityManager em = entityManagerFactory.createEntityManager();

    user1 = new User("John", "john@email.com", "hunter2");
    user2 = new User("Jane", "jane@email.com", "hunter2");
    user3 = new User("Mary", "mary@email.com", "hunter2");

    em.getTransaction().begin();
    em.persist(user1);
    em.persist(user2);
    em.persist(user3);
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
    List result = UserDAO.getInstance().findAll();
    assertTrue(result.contains(user1));
    assertTrue(result.contains(user2));
    assertTrue(result.contains(user3));
  }

  @Test
  public void testFindRatingByUser1() throws Exception {
    User u1 = UserDAO.getInstance().findUserByUsername(user1.getUsername());
    assertEquals(user1, u1);
  }

  @Test
  public void testFindRatingByUser2() throws Exception {
    User u2 = UserDAO.getInstance().findUserByUsername(user2.getUsername());
    assertEquals(user2, u2);
  }

  @Test
  public void testFindRatingByUser3() throws Exception {
    User u3 = UserDAO.getInstance().findUserByUsername(user3.getUsername());
    assertEquals(user3, u3);

  }

  @Test
  public void testFindRatingById1() throws Exception {
    User u1 = UserDAO.getInstance().findById(user1.getId());
    assertEquals(user1, u1);
  }

  @Test
  public void testFindRatingById2() throws Exception {
    User u2 = UserDAO.getInstance().findById(user2.getId());
    assertEquals(user2, u2);
  }

  @Test
  public void testFindRatingById3() throws Exception {
    User u3 = UserDAO.getInstance().findById(user3.getId());
    assertEquals(user3, u3);
  }

}
