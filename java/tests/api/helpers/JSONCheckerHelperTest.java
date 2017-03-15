package api.helpers;

import api.exceptions.APIBadRequestException;
import base.BaseTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static api.helpers.JSONCheckerHelper.checkAndGetJSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class JSONCheckerHelperTest extends BaseTest {

  @Test
  public void testValidJSON() {
    String JSONString = "{'books':['test', 'test2'], 'user':'admin'}";
    JSONObject jsonObject = checkAndGetJSON(JSONString);
    assertEquals(jsonObject.get("user"), "admin");
    assertTrue(jsonObject.get("books") instanceof JSONArray);
    assertEquals(((JSONArray) jsonObject.get("books")).get(0), "test");
    assertEquals(((JSONArray) jsonObject.get("books")).get(1), "test2");
  }

  @Test
  public void testInvalidJSON() {
    testInvalidJSON("hei");
    testInvalidJSON("{'1\":1}");
    testInvalidJSON("{'books':['test', 'test2'], 'user':}");
  }

  private void testInvalidJSON(String JSONString) {
    try {
      checkAndGetJSON(JSONString);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof APIBadRequestException);
    }
  }
}