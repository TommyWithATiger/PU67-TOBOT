package data;

import static junit.framework.TestCase.assertEquals;

import base.BaseTest;
import data.rating.Rating;
import data.rating.RatingEnum;
import data.rating.RatingKey;
import javax.persistence.EntityManager;
import org.junit.Test;

public class RatingTest extends BaseTest {

  @Test
  public void testCreate() throws Exception {
    Rating rating = new Rating();
    rating.setRating(RatingEnum.GOOD);
    rating.setTopicID(1);
    rating.setUserID(1);

    rating.create();

    EntityManager em = entityManagerFactory.createEntityManager();
    Rating rating1 = em.find(Rating.class, new RatingKey(1, 1));
    em.close();

    assertEquals(rating, rating1);
  }

  @Test
  public void testDelete() throws Exception {
    Rating rating = new Rating();
    rating.setRating(RatingEnum.GOOD);
    rating.setTopicID(1);
    rating.setUserID(1);

    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    em.persist(rating);
    em.getTransaction().commit();
    em.close();

    rating.delete();

    em = entityManagerFactory.createEntityManager();
    Rating rating1 = em.find(Rating.class, new RatingKey(1, 1));
    em.close();

    assertEquals(null, rating1);
  }

  @Test
  public void testUpdate() throws Exception {
    Rating rating = new Rating();
    rating.setRating(RatingEnum.GOOD);
    rating.setTopicID(1);
    rating.setUserID(1);

    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    em.persist(rating);
    em.getTransaction().commit();
    em.close();

    rating.setRating(RatingEnum.POOR);
    rating.update();

    em = entityManagerFactory.createEntityManager();
    Rating rating1 = em.find(Rating.class, new RatingKey(1, 1));
    em.close();

    assertEquals(RatingEnum.POOR, rating1.getRating());
  }

}
