package base;

import data.Subject;
import data.Topic;
import data.user.User;
import data.rating.Rating;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaDelete;
import main.ServerInitializer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseTest {
  protected static EntityManagerFactory entityManagerFactory;

  /*
   All tests in the system should extend this tests class, which sets properties that are required
   for the tests to work in the same way the actual system will run.
  */


  @BeforeClass
  public static void setUpBaseTest() {
    entityManagerFactory = ServerInitializer.setup("h2-eclipselink");
  }

  @AfterClass
  public static void tearDownBaseTest(){
    entityManagerFactory.close();
  }

  @After
  public void cleanDB(){

    deleteAllEntities(Subject.class);
    // This has to be before the calls to delete all topics and users
    // Or else h2 will complain about referential constraints

    deleteAllEntities(User.class);
    deleteAllEntities(Topic.class);
    deleteAllEntities(Rating.class);
  }

  private <T> void deleteAllEntities(Class<T> entityType) {
    EntityManager em = entityManagerFactory.createEntityManager();
    CriteriaDelete<T> query = em.getCriteriaBuilder().createCriteriaDelete(entityType);
    query.from(entityType);
    em.getTransaction().begin();
    em.createQuery(query).executeUpdate();
    em.getTransaction().commit();
    em.close();
  }

}
