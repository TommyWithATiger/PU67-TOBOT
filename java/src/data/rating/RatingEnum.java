package data.rating;

/**
 * Enum type to specify the value of the rating
 */
public enum RatingEnum {
  None(1), Poor(2), Ok(3), Good(4), Superb(5);

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
        return None;
      case 2:
        return Poor;
      case 3:
        return Ok;
      case 4:
        return Good;
      case 5:
        return Superb;
      default:
        throw new IllegalArgumentException("Unknown " + ratingValue);
    }
  }
}
