package server.connection;

import static junit.framework.TestCase.assertTrue;

import base.ServerBaseTest;
import java.io.IOException;
import org.junit.Test;

public class SocketHandlerTest extends ServerBaseTest {

  @Test
  public void testConnection() throws IOException {
    assertTrue(socket.isConnected());
  }


}
