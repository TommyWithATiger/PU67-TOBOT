package data.exerciserating;

/**
 * Enum type to specify the value of the exercise rating
 */
public enum ExerciseRatingEnum {
  Easy(1), Medium(2), Hard(3), Unknown(4);

  private int value;

  ExerciseRatingEnum(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static ExerciseRatingEnum get(float ratingValue){
    switch (Math.round(ratingValue)){
      case 1:
        return Easy;
      case 2:
        return Medium;
      case 3:
        return Hard;
      default:
        return Unknown;
    }
  }
}
