package data.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * This class converts between UserTypeEnum and the corresponding chars in the database
 */
@Converter
public class UserTypeConverter implements AttributeConverter<UserType, String> {

  @Override
  public String convertToDatabaseColumn(UserType attribute) {
    switch (attribute) {
      case STUDENT:
        return "S";
      case TEACHER:
        return "T";
      case ADMIN:
        return "A";
      default:
        throw new IllegalArgumentException("Unknown " + attribute);
    }
  }

  @Override
  public UserType convertToEntityAttribute(String dbData) {
    switch (dbData) {
      case "S":
        return UserType.STUDENT;
      case "T":
        return UserType.TEACHER;
      case "A":
        return UserType.ADMIN;
      default:
        throw new IllegalArgumentException("Unknown " + dbData);
    }
  }

  public static UserType stringToUserType(String fullRatingName) {
    switch (fullRatingName.toLowerCase()) {
      case "student":
        return UserType.STUDENT;
      case "teacher":
        return UserType.TEACHER;
      case "admin":
        return UserType.ADMIN;
      default:
        throw new IllegalArgumentException("Unknown " + fullRatingName);
    }
  }

  public static String userTypeToString(UserType userEnum){
    switch (userEnum) {
      case STUDENT:
        return "Student";
      case TEACHER:
        return "Teacher";
      case ADMIN:
        return "Admin";
      default:
        throw new IllegalArgumentException("Unknown " + userEnum);
    }
  }
}
