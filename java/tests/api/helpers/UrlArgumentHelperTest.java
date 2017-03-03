package api.helpers;

import static api.helpers.UrlArgumentHelper.getArgumentsInURL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import api.exceptions.APIBadRequestException;
import base.BaseTest;
import java.util.HashMap;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Test;

public class UrlArgumentHelperTest extends BaseTest {

  @Test(expected = APIBadRequestException.class)
  public void testNoArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "URL");
    getArgumentsInURL(httpRequest);
  }

  @Test
  public void testSingleArgument() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "test.html?index=1");
    HashMap<String, String> arguments = getArgumentsInURL(httpRequest);

    assertEquals(1, arguments.size());
    assertTrue(arguments.containsKey("index"));
    assertEquals("1", arguments.get("index"));
  }

  @Test
  public void testSeveralArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "test.html?index=1&test=2");
    HashMap<String, String> arguments = getArgumentsInURL(httpRequest);

    assertEquals(2, arguments.size());
    assertTrue(arguments.containsKey("index"));
    assertEquals("1", arguments.get("index"));
    assertTrue(arguments.containsKey("test"));
    assertEquals("2", arguments.get("test"));
  }

  @Test
  public void testInvalidArgumentFormat() {
    HttpRequest httpRequest = new BasicHttpRequest("GET",
        "test.html?index=21=21&test?1&picture=test.png");
    HashMap<String, String> arguments = getArgumentsInURL(httpRequest);

    assertEquals(1, arguments.size());
    assertFalse(arguments.containsKey("index"));
    assertFalse(arguments.containsKey("test"));
    assertTrue(arguments.containsKey("picture"));
    assertEquals("test.png", arguments.get("picture"));
  }

}