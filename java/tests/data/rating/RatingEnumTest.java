package data.rating;

import static junit.framework.TestCase.assertEquals;

import base.BaseTest;
import org.junit.Test;

public class RatingEnumTest extends BaseTest {

  @Test
  public void testGet() throws Exception {
    assertEquals(RatingEnum.None, RatingEnum.get(1));
    assertEquals(RatingEnum.Poor, RatingEnum.get(2));
    assertEquals(RatingEnum.Ok, RatingEnum.get(3));
    assertEquals(RatingEnum.Good, RatingEnum.get(4));
    assertEquals(RatingEnum.Superb, RatingEnum.get(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetIllegal1() throws Exception {
    RatingEnum.get(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetIllegal2() throws Exception {
    RatingEnum.get(1000);
  }
}
