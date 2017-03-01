package base;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import main.ServerInitializer;
import org.junit.Before;

public class BaseTest {

  /*
   All tests in the system should extend this tests class, which sets properties that are required
   for the tests to work in the same way the actual system will run.
  */


  @Before
  public void setUpBaseTest() {
    EntityManagerFactory emf = ServerInitializer.setup("h2-eclipselink");
    System.setProperty("javax.xml.accessExternalDTD", "all");
  }

}
