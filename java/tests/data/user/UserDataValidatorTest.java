package data.user;

import static data.user.UserDataValidator.checkEmail;
import static data.user.UserDataValidator.checkPassword;
import static data.user.UserDataValidator.checkUsername;
import static org.junit.Assert.*;

import base.BaseTest;
import data.user.UserDataValidator.IsValidToken;
import org.junit.Test;

public class UserDataValidatorTest extends BaseTest {

  @Test
  public void testCheckUsernameWrongLength() {
    IsValidToken validationToken = checkUsername("a");
    assertFalse(validationToken.isValid());
    assertEquals("Username must have a length of 3 - 32 symbols", validationToken.getMessage());

    validationToken = checkUsername("thisisatoolongusernameforthesystemonewouldthink");
    assertFalse(validationToken.isValid());
    assertEquals("Username must have a length of 3 - 32 symbols", validationToken.getMessage());
  }

  @Test
  public void testCheckUsernameTaken() {
    User user = new User("username", "user@email.com", "password");
    user.create();

    IsValidToken validationToken = checkUsername("username");
    assertFalse(validationToken.isValid());
    assertEquals("Username already taken", validationToken.getMessage());
  }

  @Test
  public void testCheckUsernameValidUsername() {
    IsValidToken validationToken = checkUsername("username");
    assertTrue(validationToken.isValid());
  }

  @Test
  public void testCheckEmailInvalidEmail() {
    IsValidToken validationToken = checkEmail("thisisnotavalidemail");
    assertFalse(validationToken.isValid());
    assertEquals("Email address is not in a valid format", validationToken.getMessage());
  }

  @Test
  public void testCheckEmailTaken() {
    User user = new User("username", "user@email.com", "password");
    user.create();

    IsValidToken validationToken = checkEmail("user@email.com");
    assertFalse(validationToken.isValid());
    assertEquals("An account with that email address already exists", validationToken.getMessage());
  }

  @Test
  public void testCheckEmailValidEmail() {
    IsValidToken validationToken = checkEmail("user@email.com");
    assertTrue(validationToken.isValid());
  }

  @Test
  public void testCheckPasswordToShort() {
    IsValidToken validationToken = checkPassword("hunter2");
    assertFalse(validationToken.isValid());
    assertEquals("Passwords must be of length 8 or longer", validationToken.getMessage());
  }

  @Test
  public void testCheckPasswordValidPassword() {
    IsValidToken validationToken = checkPassword("8symbols");
    assertTrue(validationToken.isValid());
  }

}