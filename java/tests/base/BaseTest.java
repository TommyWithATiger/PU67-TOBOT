package base;

import data.Subject;
import data.Topic;
import data.User;
import data.rating.Rating;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaDelete;
import main.ServerInitializer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class BaseTest {
  protected static EntityManagerFactory entityManagerFactory;

  /*
   All tests in the system should extend this tests class, which sets properties that are required
   for the tests to work in the same way the actual system will run.
  */


  @Before
  public void setUpBaseTest() {
    entityManagerFactory = ServerInitializer.setup("h2-eclipselink");
  }

  @After
  public void tearDownBaseTest(){
    entityManagerFactory.close();
  }

}
