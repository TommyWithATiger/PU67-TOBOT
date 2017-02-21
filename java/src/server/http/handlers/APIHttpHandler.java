package server.http.handlers;

import api.APIDelegator;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpCoreContext;

public class APIHttpHandler {

  public static HttpResponse handleRequest(HttpRequest httpRequest) {

    HttpResponse httpResponse = new DefaultHttpResponseFactory()
        .newHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, new HttpCoreContext());

    BasicHttpEntity httpEntity = new BasicHttpEntity();
    httpEntity.setContent(new ByteArrayInputStream(APIDelegator.delegate(httpRequest).getBytes(
        StandardCharsets.UTF_8)));
    httpResponse.setEntity(httpEntity);

    return httpResponse;
  }

}
