package server.connection;

import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.junit.Test;
import base.ServerBaseTest;

public class SocketHandlerTest extends ServerBaseTest {

  @Test
  public void testConnection() throws IOException {
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress("localhost", SocketHandler.PORT));
    assertTrue(socket.isConnected());
  }


}
