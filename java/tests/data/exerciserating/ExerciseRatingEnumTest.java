package data.exerciserating;

import static org.junit.Assert.assertEquals;

import base.BaseTest;
import data.exerciserating.ExerciseRatingEnum;
import org.junit.Test;

public class ExerciseRatingEnumTest extends BaseTest {

  @Test
  public void testGet() throws Exception {
    assertEquals(ExerciseRatingEnum.Easy, ExerciseRatingEnum.get(1));
    assertEquals(ExerciseRatingEnum.Medium, ExerciseRatingEnum.get(2));
    assertEquals(ExerciseRatingEnum.Hard, ExerciseRatingEnum.get(3));
    assertEquals(ExerciseRatingEnum.Unknown, ExerciseRatingEnum.get(4));
    assertEquals(ExerciseRatingEnum.Unknown, ExerciseRatingEnum.get(1000));
  }

}