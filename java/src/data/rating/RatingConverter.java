package data.rating;

import static data.rating.RatingEnum.*;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
}
