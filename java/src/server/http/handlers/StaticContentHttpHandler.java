package server.http.handlers;

import java.io.InputStream;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpCoreContext;
import server.http.HttpFileHandler;

public class StaticContentHttpHandler {

  public static HttpResponse handleRequest(HttpRequest httpRequest){
    HttpResponse response = new DefaultHttpResponseFactory()
        .newHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, new HttpCoreContext());


    String uri = httpRequest.getRequestLine().getUri();
    InputStream fileContent = new HttpFileHandler().getFile(uri);
    if (fileContent != null){
      BasicHttpEntity httpEntity = new BasicHttpEntity();
      httpEntity.setContent(fileContent);
      httpEntity.setContentLength(new HttpFileHandler().length(uri));
      httpEntity.setContentType(HttpFileHandler.translateContentType(uri));
      response.setEntity(httpEntity);
    } else {
      response.setStatusCode(404);
    }

    return response;
  }

}
