package base;

import data.user.User;
import java.io.ByteArrayInputStream;
import javax.persistence.EntityManagerFactory;
import main.ServerInitializer;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
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


  protected static HttpRequest buildRequest(String url, String method) {
    return new BasicHttpEntityEnclosingRequest(method, url);
  }

  protected static HttpRequest buildRequest(String url, String method, User user,
      boolean setLoggedIn) {
    BasicHttpEntityEnclosingRequest httpRequest = (BasicHttpEntityEnclosingRequest) buildRequest(
        url, method);
    if (setLoggedIn) {
      httpRequest.addHeader("X-Username", user.getUsername());
      httpRequest.addHeader("Authorization", "Bearer " + user.getSessionToken());
    }
    return httpRequest;
  }

  private static HttpRequest buildRequestContentHelper(BasicHttpEntityEnclosingRequest httpRequest,
      String content) {
    BasicHttpEntity httpEntity = new BasicHttpEntity();
    httpEntity.setContent(new ByteArrayInputStream(content.getBytes()));
    httpRequest.setEntity(httpEntity);
    return httpRequest;
  }

  protected static HttpRequest buildRequestContent(String url, String method, String content) {
    return buildRequestContentHelper((BasicHttpEntityEnclosingRequest) buildRequest(url, method),
        content);
  }

  protected static HttpRequest buildRequestContent(String url, String method, User user,
      boolean setLoggedIn, String content) {
    return buildRequestContentHelper(
        (BasicHttpEntityEnclosingRequest) buildRequest(url, method, user, setLoggedIn),
        content);
  }

}
