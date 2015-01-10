package de.pifpafpuf.web.urlparam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

public class ServletRequestMock implements ServletRequest {
  private final Map<String,String[]> params = new HashMap<>();
  
  public void setParameter(String name, String ... values) {
    params.put(name, values);
  }
  
  @Override
  public String getParameter(String name) {
    String[] p = params.get(name);
    if (p!=null && p.length>0) {
      return p[0];
    }
    return null;
  }

  @Override
  public String[] getParameterValues(String name) {
    return params.get(name);
  }

  // all others not needed for testing
  @Override
  public Object getAttribute(String name) {
    // not needed for the mock
    return null;
  }

  @Override
  public Enumeration<Object> getAttributeNames() {
    // not needed for the mock
    return null;
  }

  @Override
  public String getCharacterEncoding() {
    // not needed for the mock
    return null;
  }

  @Override
  public int getContentLength() { 
    // not needed for the mock
    return 0;
  }

  @Override
  public String getContentType() {
    // not needed for the mock
    return null;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    // not needed for the mock
    return null;
  }

  @Override
  public String getLocalAddr() {
    // not needed for the mock
    return null;
  }

  @Override
  public String getLocalName() {
    // not needed for the mock
    return null;
  }

  @Override
  public int getLocalPort() {
    // not needed for the mock
    return 0;
  }

  @Override
  public Locale getLocale() {
    // not needed for the mock
    return null;
  }

  @Override
  public Enumeration<Object> getLocales() {
    // not needed for the mock
    return null;
  }

  @Override
  public Map<String,String> getParameterMap() {
    // not needed for the mock
    return null;
  }

  @Override
  public Enumeration<String> getParameterNames() {
    // not needed for the mock
    return null;
  }

  @Override
  public String getProtocol() {
    // not needed for the mock
    return null;
  }

  @Override
  public BufferedReader getReader() throws IOException {
    // not needed for the mock
    return null;
  }

  @Override
  public String getRealPath(String path) {
    // not needed for the mock
    return null;
  }

  @Override
  public String getRemoteAddr() {
    // not needed for the mock
    return null;
  }

  @Override
  public String getRemoteHost() {
    return null;
  }

  @Override
  public int getRemotePort() {
    // not needed for the mock
    return 0;
  }

  @Override
  public RequestDispatcher getRequestDispatcher(String path) {
    // not needed for the mock
    return null;
  }

  @Override
  public String getScheme() {
    // not needed for the mock
    return null;
  }

  @Override
  public String getServerName() {
    // not needed for the mock
    return null;
  }

  @Override
  public int getServerPort() {
    // not needed for the mock
    return 0;
  }

  @Override
  public boolean isSecure() {
    // not needed for the mock
    return false;
  }

  @Override
  public void removeAttribute(String name) {
    // not needed for the mock
  }

  @Override
  public void setAttribute(String name, Object o) {
    // not needed for the mock
  }

  @Override
  public void setCharacterEncoding(String env)
    throws UnsupportedEncodingException
  {
    // not needed for the mock
  }

}
