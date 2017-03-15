package data.exerciserating;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * This class converts between ExerciseRatingEnum and the corresponding ints in the database
 */
@Converter
public class ExerciseRatingConverter implements AttributeConverter<ExerciseRatingEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ExerciseRatingEnum attribute) {
    return attribute.value();
  }

  @Override
  public ExerciseRatingEnum convertToEntityAttribute(Integer dbData) {
    return ExerciseRatingEnum.get(dbData);
  }

}
