package data.rating;

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

  /**
   * @deprecated Use RatingEnum.valueOf instead
   */
  @Deprecated
  public static RatingEnum convertFullRatingNameToEnum(String fullRatingName) {
    return RatingEnum.valueOf(fullRatingName);
  }

  /**
   * @deprecated Use ratingEnum.toString instead
   */
  @Deprecated
  public static String convertEnumToFullRatingName(RatingEnum ratingEnum){
    return ratingEnum.toString();
  }
}
