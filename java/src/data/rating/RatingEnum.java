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
}
