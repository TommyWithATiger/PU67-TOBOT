package base;

import org.junit.Before;

public class BaseTest {

  /*
   All tests in the system should extend this tests class, which sets properties that are required
   for the tests to work in the same way the actual system will run.
  */


  @Before
  public void setUpBaseTest() {
    System.setProperty("javax.xml.accessExternalDTD", "all");
  }

}
