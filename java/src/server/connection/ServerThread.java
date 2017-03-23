package server.connection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.DefaultHttpRequestParser;
import org.apache.http.impl.io.DefaultHttpResponseWriter;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.impl.io.SessionOutputBufferImpl;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import server.http.HTTPHandler;

/**
 * A server thread handling a single request from a client
 */
public class ServerThread extends Thread {

  private Socket clientSocket;
  private DefaultHttpRequestParser requestParser;
  private DefaultHttpResponseWriter responseWriter;
  private SessionOutputBufferImpl sessionOutputBuffer;
  private SessionInputBufferImpl sessionInputBuffer;
  private OutputStream outputStream;

  public ServerThread(Socket clientSocket) throws IOException {
    this.clientSocket = clientSocket;

    // Initiate input buffers
    sessionInputBuffer = new SessionInputBufferImpl(
        new HttpTransportMetricsImpl(), 33554432);
    sessionInputBuffer.bind(clientSocket.getInputStream());
    requestParser = new DefaultHttpRequestParser(sessionInputBuffer);

    // Initiate output streams and handlers
    outputStream = clientSocket.getOutputStream();
    sessionOutputBuffer = new SessionOutputBufferImpl(
        new HttpTransportMetricsImpl(), 33554432);
    sessionOutputBuffer.bind(outputStream);
    responseWriter = new DefaultHttpResponseWriter(sessionOutputBuffer);
  }

  /**
   * The thread run method parsing a HttpRequest from the client and handling it using the
   * HTTPHandler. Sends a response to the client
   */
  @Override
  public void run() {

    try {
      // Get and handle the request
      HttpRequest request = requestParser.parse();

      HttpResponse response;

      if (request.getRequestLine().getMethod().equals("OPTIONS")) {
        response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
      }
      // Check if request has content, in that case read content
      else if (request.containsHeader("Content-Length")) {
        HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();

        InputStream contentStream;

        ContentLengthStrategy contentLengthStrategy = StrictContentLengthStrategy.INSTANCE;
        long len = contentLengthStrategy.determineLength(request);
        if (len == ContentLengthStrategy.CHUNKED) {
          contentStream = new ChunkedInputStream(sessionInputBuffer);
        } else if (len == ContentLengthStrategy.IDENTITY) {
          contentStream = new IdentityInputStream(sessionInputBuffer);
        } else {
          contentStream = new ContentLengthInputStream(sessionInputBuffer, len);
        }

        basicHttpEntity.setContent(contentStream);

        entityRequest.setEntity(basicHttpEntity);

        if (request.containsHeader("Content-Type")) {
          basicHttpEntity.setContentType(request.getFirstHeader("Content-Type"));
        }

        response = HTTPHandler.handleRequest(entityRequest);
      } else {
        response = HTTPHandler.handleRequest(request);
      }

      // Set base headers
      response.addHeader(new BasicHeader("Allow", "GET,POST,OPTIONS,DELETE"));
      response.addHeader(new BasicHeader("Access-Control-Allow-Origin", "*"));
      response.addHeader(new BasicHeader("Access-Control-Allow-Headers", "x-username, authorization"));

      // Set content headers if there is content
      if (response.getEntity() != null) {
        response.setHeader(response.getEntity().getContentType());
        response.setHeader(response.getEntity().getContentEncoding());
        response.setHeader(new BasicHeader("Content-Length",
            String.valueOf(response.getEntity().getContentLength())));
      }

      // Write the headers to the client
      responseWriter.write(response);
      sessionOutputBuffer.flush();

      // Write content to the client if it exists
      if (response.getEntity() != null) {
        response.getEntity().writeTo(outputStream);
      }
      outputStream.close();

    } catch (IOException | HttpException ignored) {
      // Ignore exceptions. They come from a malformed Http request or the client closing its socket.
    }

    // The socket opened for the client should be closed when the communication is finished
    if (!clientSocket.isClosed()) {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
