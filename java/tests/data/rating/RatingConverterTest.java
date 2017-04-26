package data.rating;


import static org.junit.Assert.assertEquals;

import base.BaseTest;
import org.junit.Test;

public class RatingConverterTest extends BaseTest {

  @Test
  public void testConvertFullRatingNameToEnum() throws Exception {
    assertEquals(RatingEnum.Good, RatingConverter.convertFullRatingNameToEnum("good"));
    assertEquals(RatingEnum.Good, RatingConverter.convertFullRatingNameToEnum("Good"));
    assertEquals(RatingEnum.Good, RatingConverter.convertFullRatingNameToEnum("gOOd"));
  }

  @Test
  public void testConvertEnumToFullRatingName() throws Exception {
    assertEquals("Good", RatingConverter.convertEnumToFullRatingName(RatingEnum.Good));
  }
}
