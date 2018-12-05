package io.nodom;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

  private boolean isShutdown = false;

  public static void main(String[] args) throws IOException {
    HttpServer httpServer = new HttpServer();
    httpServer.await();
  }

  public void await() throws IOException {
    ServerSocket serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
    InputStream inputStream = null;
    OutputStream outputStream = null;
    while (!isShutdown) {
      Socket socket = serverSocket.accept();
      inputStream = socket.getInputStream();
      outputStream = socket.getOutputStream();

      // creating a request
      Request request = new Request(inputStream);
      request.parse();

      // create a response object :
      Response response = new Response(outputStream);
      response.setRequest(request);

      // check if this is a request for servlet or
      // a static resource
      // a request for a servlet begins with /servlet/

      if (request.getUri().startsWith("/servlet/")) {
        ServletProcessor processor = new ServletProcessor();
        processor.process(request, response);
      } else {
        StaticResourceProcessor staticResourceProcessor = new StaticResourceProcessor();
        staticResourceProcessor.process(request, response);
      }
      socket.close();
      isShutdown = request.getUri().equals(Constants.SHUTDOWN_COMMAND);
    }
  }
}
