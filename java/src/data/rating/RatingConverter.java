package data.rating;

import static data.rating.RatingEnum.*;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * This class converts between RatingEnum and the corresponding chars in the database
 */
@Converter
public class RatingConverter implements AttributeConverter<RatingEnum, String> {

  @Override
  public String convertToDatabaseColumn(RatingEnum attribute) {
    switch (attribute) {
      case NONE:
        return "N";
      case POOR:
        return "P";
      case OK:
        return "O";
      case GOOD:
        return "G";
      case SUPERB:
        return "S";
      default:
        throw new IllegalArgumentException("Unknown " + attribute);
    }
  }

  @Override
  public RatingEnum convertToEntityAttribute(String dbData) {
    switch (dbData) {
      case "N":
        return NONE;
      case "P":
        return POOR;
      case "O":
        return OK;
      case "G":
        return GOOD;
      case "S":
        return SUPERB;
      default:
        throw new IllegalArgumentException("Unknown " + dbData);
    }
  }

  public static RatingEnum convertFullRatingNameToEnum(String fullRatingName) {
    switch (fullRatingName) {
      case "None":
        return NONE;
      case "Poor":
        return POOR;
      case "Ok":
        return OK;
      case "Good":
        return GOOD;
      case "Superb":
        return SUPERB;
      default:
        throw new IllegalArgumentException("Unknown " + fullRatingName);
    }
  }

  public static String convertEnumToFullRatingName(RatingEnum ratingEnum){
    switch (ratingEnum) {
      case NONE:
        return "None";
      case POOR:
        return "Poor";
      case OK:
        return "Ok";
      case GOOD:
        return "Good";
      case SUPERB:
        return "Superb";
      default:
        throw new IllegalArgumentException("Unknown " + ratingEnum);
    }
  }
}
