package server.http.handlers;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpCoreContext;
import server.http.HttpFileHandler;

public class IndexPageHTTPHandler {

  public static HttpResponse handleRequest(HttpRequest httpRequest){
    HttpResponse response = new DefaultHttpResponseFactory()
        .newHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, new HttpCoreContext());

    BasicHttpEntity httpEntity = new BasicHttpEntity();
    httpEntity.setContent(new HttpFileHandler().getFile("index.html"));
    response.setEntity(httpEntity);

    return response;
  }

}
