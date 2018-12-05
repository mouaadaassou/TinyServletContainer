package io.nodom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class Response implements ServletResponse {


  private static final int BUFFER_SIZE = 1024;
  Request request;
  OutputStream outputStream;
  PrintWriter printWriter;

  public Response(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  /**
   * this is method is used to serve static content : HTML files, JS files, CSS files, ...
   *
   * @throws IOException if no files was found
   */
  public void sendStaticResource() throws IOException {
    byte[] bytes = new byte[BUFFER_SIZE];
    InputStream fileInputStream = null;
    try {
      File file = new File(Constants.WEB_ROOT, request.getUri());
      fileInputStream = new FileInputStream(file);
      int chr = fileInputStream.read(bytes, 0, BUFFER_SIZE);
      while (chr != -1) {
        outputStream.write(bytes, 0, chr);
        chr = fileInputStream.read(bytes, 0, BUFFER_SIZE);
      }
    } catch (FileNotFoundException e) {
      String errorMessage = "HTTP/1.1 404 File Not Found \r\n".concat("Content-Type: text/html\r\n")
          .concat("Content-Length: 23\r\n").concat("\r\n").concat("<h1>File Not Found</h1>");
      outputStream.write(errorMessage.getBytes());
    } finally {
      if (fileInputStream != null) {
        fileInputStream.close();
      }
    }
  }


  @Override
  public String getCharacterEncoding() {
    return null;
  }

  @Override
  public String getContentType() {
    return null;
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    /**
     * autoflush is true, println() will flush,
     * but print() will not
     */
    printWriter = new PrintWriter(outputStream, true);
    return printWriter;
  }

  @Override
  public void setCharacterEncoding(String s) {

  }

  @Override
  public void setContentLength(int i) {

  }

  @Override
  public void setContentLengthLong(long l) {

  }

  @Override
  public void setContentType(String s) {

  }

  @Override
  public void setBufferSize(int i) {

  }

  @Override
  public int getBufferSize() {
    return 0;
  }

  @Override
  public void flushBuffer() throws IOException {

  }

  @Override
  public void resetBuffer() {

  }

  @Override
  public boolean isCommitted() {
    return false;
  }

  @Override
  public void reset() {

  }

  @Override
  public void setLocale(Locale locale) {

  }

  @Override
  public Locale getLocale() {
    return null;
  }
}
