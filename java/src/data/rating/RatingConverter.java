package data.rating;

import static data.rating.RatingEnum.*;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * This class converts between RatingEnum and the corresponding ints in the database
 */
@Converter
public class RatingConverter implements AttributeConverter<RatingEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(RatingEnum attribute) {
    return attribute.value();
  }

  @Override
  public RatingEnum convertToEntityAttribute(Integer dbData) {
    return RatingEnum.get(dbData);
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
