package data;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import base.BaseTest;
import org.junit.Test;

public class UserTests extends BaseTest {

  @Test(expected = IllegalArgumentException.class)
  public void testWrongEmail() throws Exception {
    User user = new User();
    user.setEmail("this_is_not_valid");
  }

  @Test
  public void testCorrectEmail() throws Exception {
    User user = new User();
    user.setEmail("this_is_valid@this_is_valid");
    assertEquals(user.getEmail(), "this_is_valid@this_is_valid");
  }

  @Test
  public void testPassword() throws Exception {
    User user = new User();
    user.setPassword("hunter2");
    assertTrue(user.checkPassword("hunter2"));
    assertFalse(user.checkPassword("password123"));
  }
}
