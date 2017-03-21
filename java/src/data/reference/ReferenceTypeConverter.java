package data.reference;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//ARTICLE, VIDEO, WEBSITE, IMAGE, DOCUMENT, SLIDE, NOTES


/**
 * This class converts between ReferenceType and the corresponding chars in the database
 */
@Converter
public class ReferenceTypeConverter implements AttributeConverter<ReferenceType, String> {

  @Override
  public String convertToDatabaseColumn(ReferenceType attribute) {
    switch (attribute) {
      case ARTICLE:
        return "A";
      case VIDEO:
        return "V";
      case WEBSITE:
        return "W";
      case IMAGE:
        return "I";
      case DOCUMENT:
        return "D";
      case SLIDE:
        return "S";
      case NOTES:
        return "N";
      default:
        throw new IllegalArgumentException("Unknown " + attribute);
    }
  }

  @Override
  public ReferenceType convertToEntityAttribute(String dbData) {
    switch (dbData) {
      case "A":
        return ReferenceType.ARTICLE;
      case "V":
        return ReferenceType.VIDEO;
      case "W":
        return ReferenceType.WEBSITE;
      case "I":
        return ReferenceType.IMAGE;
      case "D":
        return ReferenceType.DOCUMENT;
      case "S":
        return ReferenceType.SLIDE;
      case "N":
        return ReferenceType.NOTES;
      default:
        throw new IllegalArgumentException("Unknown " + dbData);
    }
  }

  public static ReferenceType stringToReferenceType(String fullRatingName) {
    switch (fullRatingName.toLowerCase()) {
      case "article":
        return ReferenceType.ARTICLE;
      case "video":
        return ReferenceType.VIDEO;
      case "website":
        return ReferenceType.WEBSITE;
      case "image":
        return ReferenceType.IMAGE;
      case "document":
        return ReferenceType.DOCUMENT;
      case "slide":
        return ReferenceType.SLIDE;
      case "notes":
        return ReferenceType.NOTES;
      default:
        throw new IllegalArgumentException("Unknown " + fullRatingName);
    }
  }

  public static String referenceTypeToString(ReferenceType referenceEnum) {
    switch (referenceEnum) {
      case ARTICLE:
        return "Article";
      case VIDEO:
        return "Video";
      case WEBSITE:
        return "Website";
      case IMAGE:
        return "Image";
      case DOCUMENT:
        return "Document";
      case SLIDE:
        return "Slide";
      case NOTES:
        return "Notes";
      default:
        throw new IllegalArgumentException("Unknown " + referenceEnum);
    }
  }
}
