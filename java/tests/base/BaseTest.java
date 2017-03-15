package base;

import javax.persistence.EntityManagerFactory;
import main.ServerInitializer;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

  private EntityManagerFactory entityManagerFactory;

  /*
   All tests in the system should extend this tests class, which sets properties that are required
   for the tests to work in the same way the actual system will run.
  */


  @Before
  public void setUpBaseTest() {
    entityManagerFactory = ServerInitializer.setup("h2-eclipselink");
  }

  @After
  public void tearDownBaseTest() {
    entityManagerFactory.close();
  }

}
