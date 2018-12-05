package io.nodom;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ServletProcessor {

  public void process(Request request, Response response) {
    String uri = request.getUri();
    String servletName = uri.substring(uri.lastIndexOf("/") + 1);
    URLClassLoader classLoader = null;

    try {
      URL[] urls = new URL[1];
      URLStreamHandler urlStreamHandler = null;
      File classPath = new File(Constants.WEB_ROOT);

      // the location where the servlet container can find classes to load
      String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator))
          .toString();
      urls[0] = new URL(null, repository, urlStreamHandler);
      classLoader = new URLClassLoader(urls);

    } catch (IOException e) {
      e.printStackTrace();
    }

    Class myClass = null;
    try {
      myClass = classLoader.loadClass(servletName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    Servlet servlet = null;
    RequestFacade requestFacade = new RequestFacade(request);
    ResponseFacade responseFacade = new ResponseFacade(response);
    try {
      servlet = (Servlet) myClass.newInstance();
      servlet.service((ServletRequest) requestFacade, (ServletResponse) responseFacade);
    } catch (Exception e) {

    }
  }

}
