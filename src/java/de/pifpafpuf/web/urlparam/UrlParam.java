package de.pifpafpuf.web.urlparam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;

/**
 * <p>describes a single value URL parameter of the given {@code TYPE}. Objects
 * of this class are immutable, but may be used as builders for objects for
 * the same {@code TYPE} but with another value. Objects of this type are
 * used to fetch parameters from the {@link javax.servlet.ServletRequest} as
 * well as to write the parameter with its value into a URL or a HTML form
 * element.</p>
 * 
 * @param <TYPE> is the type of the parameter stored.
 */
public class UrlParam<TYPE> {
  private final String name;
  private final TYPE value;
  private final ParamCodec<TYPE> paramCodec;

  /**
   * creates a url parameter. Objects created with the constructor are
   * supposed to be used as templates. They shall be used to create new
   * <code>UlrParam</code> objects either with {@link #fromValue} or with
   * {@link #fromFirst}.
   * 
   * @param name of the parameter use in the URL.
   * @param value is the default value, may be {@code null}
   * @param pp is the codec to decode strings retrieved from
   *        {@link javax.servlet.ServletRequest} and to encode the value for
   *        a URL or an HTML form input element.
   */
  public UrlParam(String name, TYPE value, ParamCodec<TYPE> pp) {
    if (name==null) {
      throw new NullPointerException("parameter name may not be null");
    }
    this.name = name;
    this.value = value;
    if (pp==null) {
      throw new NullPointerException("parameter pp may not be null");
    }
    this.paramCodec = pp;
  }
  /*+******************************************************************/
  public String getName() {
    return name;
  }
  /*+******************************************************************/
  public TYPE getValue() {
    return value;
  }
  /*+**********************************************************************/
  public String getForInputParam() {
    return paramCodec.asString(value);
  }
  /*+**********************************************************************/
  /**
   * returns the value of <code>this</code> after first encoding it with our
   * coded and then URL-encoding it.
   */
  public String getForUrlParam() {
    return encodeForUrl(paramCodec.asString(value));
  }
  /*+**********************************************************************/
  /**
   * returns a new url parameter with the given new value.
   */
  public UrlParam<TYPE> fromValue(TYPE newValue) {
    return new UrlParam<TYPE>(name, newValue, paramCodec);
  }
  /*+******************************************************************/
  /**
   * appends the encoded value of <code>this</code> to the
   * {@link java.util.StringBuilder} provided. If the URL provided does not
   * end with a '?', the parameter is prefixed with '&'.
   */
  public void appendToUrl(StringBuilder sb) {
    final int l = sb.length();
    if (l>0 && sb.charAt(l-1)!='?') {
      sb.append('&');
    }
    sb.append(name).append('=').append(getForUrlParam());
  }
  /*+******************************************************************/  
  /**
   * creates a new <code>UrlParam</code> from the given text. If the text is
   * <code>null</code> or cannot be parsed by the codec, <code>this</code> is
   * returned.
   * 
   * @param text to be converted to a <code>UrlParam</code>, may be
   *        <code>null</code>
   * @return a new object or <code>this</code>
   */
  public UrlParam<TYPE> fromString(String text) {
    TYPE v = paramCodec.get(text);
    if (v==null) {
      return this;
    } else {
      return fromValue(v);
    }
  }
  /*+******************************************************************/ 
  /**
   * <p>
   * creates a new <code>UrlParam</code> from the value returned by
   * <code>req.getParameter</code>. If the parameter does not exist or can
   * not be parsed by the codec, the default value of <code>this</code> is
   * used to initialize the new object.
   * </p>
   * 
   * @param req where to get the parameter value from, may not be
   *        <code>null</code>
   */
  public UrlParam<TYPE> fromFirst(ServletRequest req) {
    String text = req.getParameter(name);
    return fromString(text);
  }
  /*+******************************************************************/  
  private static String encodeForUrl(String text) {
    try {
      return URLEncoder.encode(text, "UTF-8");
    } catch( UnsupportedEncodingException e ) {
      throw new RuntimeException("this should not happen, "+
          "why don't we have UTF-8?", e);
    }
  }
  /*+******************************************************************/
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime*result+(name.hashCode());
    result = prime*result+((value==null) ? 0 : value.hashCode());
    result = prime*result+(paramCodec.hashCode());
    return result;
  }
  /*+******************************************************************/
  @Override
  public boolean equals(Object obj) {
    if( this==obj ) {
      return true;
    }
    if( obj==null ) {
      return false;
    }
    if( getClass()!=obj.getClass() ) {
      return false;
    }

    @SuppressWarnings("rawtypes")
    UrlParam other = (UrlParam)obj;
    if (!name.equals(other.name) ) {
      return false;
    }

    if (!paramCodec.equals(other.paramCodec)) {
      return false;
    }
    
    if( value==null ) {
      return other.value==null;
    } 
    
    return value.equals(other.value);
  }
  /*+******************************************************************/
  @Override
  public String toString() {
    return name+"=("+paramCodec.getClass().getName()+")"+value;
  }
}
