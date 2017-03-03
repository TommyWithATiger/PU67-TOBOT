package data.user;

import static data.user.UserType.*;
import static junit.framework.TestCase.assertEquals;

import base.BaseTest;
import org.junit.Test;


public class UserTypeConverterTest extends BaseTest {

  private UserTypeConverter userTypeConverter = new UserTypeConverter();

  @Test
  public void convertToDatabaseColumnTest() throws Exception {
    assertEquals(userTypeConverter.convertToDatabaseColumn(STUDENT), "S");
    assertEquals(userTypeConverter.convertToDatabaseColumn(TEACHER), "T");
    assertEquals(userTypeConverter.convertToDatabaseColumn(ADMIN), "A");
  }

  @Test(expected = IllegalArgumentException.class)
  public void convertToEntityAttributeException() {
    userTypeConverter.convertToEntityAttribute("invalid");
  }

  @Test
  public void convertToEntityAttributeTest() throws Exception {
    assertEquals(userTypeConverter.convertToEntityAttribute("S"), STUDENT);
    assertEquals(userTypeConverter.convertToEntityAttribute("T"), TEACHER);
    assertEquals(userTypeConverter.convertToEntityAttribute("A"), ADMIN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void stringToUserTypeException() {
    UserTypeConverter.stringToUserType("invalid");
  }

  @Test
  public void stringToUserTypeTest() throws Exception {
    assertEquals(UserTypeConverter.stringToUserType("Student"), STUDENT);
    assertEquals(UserTypeConverter.stringToUserType("Teacher"), TEACHER);
    assertEquals(UserTypeConverter.stringToUserType("Admin"), ADMIN);
  }

  @Test
  public void userTypeToStringTest() throws Exception {
    assertEquals(UserTypeConverter.userTypeToString(STUDENT), "Student");
    assertEquals(UserTypeConverter.userTypeToString(TEACHER), "Teacher");
    assertEquals(UserTypeConverter.userTypeToString(ADMIN), "Admin");
  }

}