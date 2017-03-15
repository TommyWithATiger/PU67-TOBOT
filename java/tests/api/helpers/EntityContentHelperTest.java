package api.helpers;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;
import static org.junit.Assert.fail;

import api.exceptions.APIBadRequestException;
import base.BaseTest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Assert;
import org.junit.Test;

public class EntityContentHelperTest extends BaseTest {

  @Test(expected = APIBadRequestException.class)
  public void testNoContent() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "URL");
    checkAndGetEntityContent(httpRequest);
    fail();
  }

  @Test
  public void testWithContent() {
    String content = "Test String, 123æøå#$%";

    HttpEntityEnclosingRequest httpRequest = new BasicHttpEntityEnclosingRequest("GET", "URL");
    BasicHttpEntity httpEntity = new BasicHttpEntity();
    InputStream inputStream = new ByteArrayInputStream(content.getBytes());
    httpEntity.setContent(inputStream);
    httpEntity.setContentType("text; charset=utf-8");
    httpRequest.setEntity(httpEntity);

    String foundContent = checkAndGetEntityContent(httpRequest);
    Assert.assertEquals(content, foundContent);
  }

}
