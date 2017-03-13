package data.rating;

/**
 * Enum type to specify the value of the rating
 */
public enum RatingEnum {
  NONE(1), POOR(2), OK(3), GOOD(4), SUPERB(5);

  private int value;

  RatingEnum(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static RatingEnum get(int ratingValue){
    switch (ratingValue){
      case 0:
        // should maybe return null here ?
        throw new IllegalArgumentException("0 is currently not supported as a rating");
      case 1:
        return NONE;
      case 2:
        return POOR;
      case 3:
        return OK;
      case 4:
        return GOOD;
      case 5:
        return SUPERB;
      default:
        throw new IllegalArgumentException("Unknown " + ratingValue);
    }
  }
}
