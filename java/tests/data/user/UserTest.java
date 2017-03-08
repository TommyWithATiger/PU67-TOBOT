package data.user;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertTrue;

import base.BaseTest;
import data.dao.UserDAO;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

public class UserTest extends BaseTest {

  @Test(expected = IllegalArgumentException.class)
  public void testWrongEmail1() throws Exception {
    User user = new User();
    user.setEmail("this_is_not_valid");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrongEmail2() throws Exception {
    User user = new User();
    user.setEmail("this_is_not_valid@email");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrongEmail3() throws Exception {
    User user = new User();
    user.setEmail("#@%^%#$@#$@#.com");
  }

  @Test
  public void testCorrectEmail() throws Exception {
    User user = new User();
    user.setEmail("user@mail.com");
    assertEquals(user.getEmail(), "user@mail.com");
  }

  @Test
  public void testPassword() throws Exception {
    User user = new User();
    user.setPassword("hunter2");
    assertTrue(user.checkPassword("hunter2"));
    assertFalse(user.checkPassword("password123"));
  }

  @Test
  public void testGenerateSessionToken() throws Exception {
    User user = new User();
    user.createSessionToken();
    assertEquals(64, user.getSessionToken().length());
  }

  @Test
  public void testGenerateSessionTokenExpireDate() throws Exception {
    User user = new User();
    user.generateSessionTokenExpireDate();
    assertTrue(user.getSessionTokenExpireDate().after(new Date()));
  }

  @Test
  public void testCheckUserSessionToken() throws Exception {
    User user = new User();
    assertFalse(user.checkUserSessionToken("garbage"));

    user.createSessionToken();
    assertTrue(user.checkUserSessionToken(user.getSessionToken()));
  }

  @Test
  public void testCheckUserSessionTokenExpired() throws Exception {
    User user = new User();
    user.createSessionToken();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -1);
    user.setSessionTokenExpireDate(calendar.getTime());

    assertFalse(user.checkUserSessionToken(user.getSessionToken()));
    assertEquals(null, user.getSessionTokenExpireDate());
    assertEquals(null, user.getSessionToken());
  }

  @Test
  public void testCreate() throws Exception {
    User user = new User("user", "dad@dad.com", "adad");

    user.create();

    User user1 = UserDAO.getInstance().findById(user.getId());
    assertEquals(user, user1);
  }

  @Test
  public void testDelete() throws Exception {
    User user = new User("user", "dad@dad.com", "adad");
    user.create();

    user.delete();

    User user1 = UserDAO.getInstance().findById(user.getId());
    assertEquals(null, user1);
  }

  @Test
  public void testUpdate() throws Exception {
    User user = new User("user", "dad@dad.com", "adad");
    user.create();

    user.setUsername("other user");
    user.update();

    User user1 = UserDAO.getInstance().findById(user.getId());
    assertEquals("other user", user1.getUsername());
  }

  @Test
  public void testEquals() {
    User user = new User("user", "dad@dad.com", "adad");
    User user2 = new User("user2", "dad2@dad.com", "adad");
    user.create();
    user2.create();

    assertNotSame(user, user2);
    assertEquals(user, UserDAO.getInstance().findById(user.getId()));
    assertFalse(user.equals(5));
    assertFalse(user.equals(null));
  }

  @Test
  public void testHasCode() {
    User user = new User("user", "dad@dad.com", "adad");
    user.create();

    assertEquals(user.hashCode(), user.getId());
  }

  @Test
  public void getSetUserType() {
    User user = new User("user", "dad@dad.com", "adad");

    assertEquals(user.getUserType(), UserType.STUDENT);

    user.setUserType(UserType.TEACHER);

    assertNotSame(user.getUserType(), UserType.STUDENT);
    assertEquals(user.getUserType(), UserType.TEACHER);

    user.setUserType(UserType.ADMIN);
    assertTrue(user.isAdmin());

  }

}
