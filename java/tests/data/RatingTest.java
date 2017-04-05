package data;

import static junit.framework.TestCase.assertEquals;

import base.BaseTest;
import data.dao.RatingDAO;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import org.junit.Test;

public class RatingTest extends BaseTest {

  @Test
  public void testCreate() throws Exception {
    RatingKey rk = new RatingKey(1, 1);
    Rating rating = new Rating(rk.getUserID(), rk.getTopicID(), RatingEnum.Good);

    rating.create();

    Rating rating1 = RatingDAO.getInstance().findById(new RatingKey(1, 1));
    assertEquals(rating, rating1);
    assertEquals((Integer) 1,  rating1.getUserID());
    assertEquals((Integer) 1, rating1.getTopicID());
    assertEquals(RatingEnum.Good, rating1.getRating());
    assertEquals(RatingEnum.Good.value(), rating1.getIntRating());
  }

  @Test
  public void testDelete() throws Exception {
    RatingKey rk = new RatingKey(1, 1);
    Rating rating = new Rating(rk.getUserID(), rk.getTopicID(), RatingEnum.Good);
    rating.create();

    rating.delete();

    Rating rating1 = RatingDAO.getInstance().findById(new RatingKey(1, 1));
    assertEquals(null, rating1);
  }

  @Test
  public void testUpdate() throws Exception {
    RatingKey rk = new RatingKey(1, 1);
    Rating rating = new Rating(rk.getUserID(), rk.getTopicID(), RatingEnum.Good);
    rating.create();

    rating.setRating(RatingEnum.Poor);
    rating.update();

    Rating rating1 = RatingDAO.getInstance().findById(new RatingKey(1, 1));
    assertEquals(RatingEnum.Poor, rating1.getRating());
  }

}
