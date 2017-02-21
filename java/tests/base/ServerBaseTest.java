package base;

import org.junit.After;
import org.junit.Before;
import server.connection.SocketHandler;

public class ServerBaseTest extends BaseTest{

  private SocketHandler socketHandler;

  @Before
  public void setupSocketHandler(){
    socketHandler = new SocketHandler();
    socketHandler.start();
  }

  @After
  public void tearDownSocketHandler(){
    socketHandler.stopServer();
  }

}
