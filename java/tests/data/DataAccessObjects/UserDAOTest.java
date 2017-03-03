package data.DataAccessObjects;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.user.User;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserDAOTest extends BaseTest{

  private static User user1;
  private static User user2;
  private static User user3;

  @BeforeClass
  public static void populate(){
    user1 = new User("John", "john@email.com", "hunter2");
    user1.create();
    user2 = new User("Jane", "jane@email.com", "hunter2");
    user2.create();
    user3 = new User("Mary", "mary@email.com", "hunter2");
    user3.create();
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
    assertEquals(3, result.size());
  }

  @Test
  public void testFindRatingByUser() throws Exception {
    User u1 = UserDAO.getInstance().findUserByUsername(user1.getUsername());
    assertEquals(user1, u1);

    User u2 = UserDAO.getInstance().findUserByUsername(user2.getUsername());
    assertEquals(user2, u2);

    User u3 = UserDAO.getInstance().findUserByUsername(user3.getUsername());
    assertEquals(user3, u3);
  }

  @Test
  public void testFindRatingById() throws Exception {
    User u1 = UserDAO.getInstance().findById(user1.getId());
    assertEquals(user1, u1);

    User u2 = UserDAO.getInstance().findById(user2.getId());
    assertEquals(user2, u2);

    User u3 = UserDAO.getInstance().findById(user3.getId());
    assertEquals(user3, u3);
  }

}
