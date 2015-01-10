package de.pifpafpuf.web.urlparam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

/**
 * <p>describes a single value URL parameter of the given {@code TYPE}. Objects
 * of this class are immutable, but may be used as builders for objects for
 * the same {@code TYPE} but with another value. Objects of this type are
 * used to fetch parameters from the {@link javax.servlet.ServletRequest} as
 * well as to write the parameter with its value into a URL or an HTML form
 * element.</p>
 * <p>Example use of an <code>int</code> parameter.</p>
 * <pre> // define the template
 * private static final UrlParam<Integer> P_COUNT =
 *    new UrlParam<Integer>("count", 0, new IntegerCodec(0, 10));
 * ...
 * // read the value from the request, using the default if necessary
 * int count = P_COUNT.getFirst(request).getValue();
 * ...
 * // create a new parameter value to put it into the response
 * UrlParam<Integer> newCount = P_COUNT.fromValue(9);
 * newCount.appendToUrl(buf);
 * ...
 * new Html("input").setAttr("type", "text")
 * .setAttr("name", newCount.getName())
 * .setAttr("value", newCount.getForInputParam()));
 * </pre.
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
   * @param name of the parameter as used in the URL. No check is made
   *        whether the name conforms to the syntax rules of URL parameter
   *        names and no encoding/decoding is performed for the name.
   * @param value is the default value for this parameter and may be
   *        {@code null} to signal the complete absence of a the parameter.
   *        (Advice: Don't use <code>null</code>, rather use a good default
   *        value if at all possible.)
   * @param pp is the codec to decode strings retrieved from
   *        {@link javax.servlet.ServletRequest} and to encode the value for
   *        a URL or an HTML form input element. The codec should only
   *        encode/decode to/from an arbitrary <code>String</code> URL
   *        en/decoding is taken care of separately.
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
  /**
   * returns the value, which may be <code>null</code> if it was so set in
   * the constructor.
   * 
   */
  public TYPE getValue() {
    return value;
  }
  /*+**********************************************************************/
  /**
   * returns the value encoded in string form by the codec defined in the
   * constructor. No HTML or URL encoding is performed.
   * 
   */
  public String getForInputParam() {
    return paramCodec.asString(value);
  }
  /*+**********************************************************************/
  /**
   * returns the value of <code>this</code> after first encoding it with our
   * codec and then URL-encoding it.
   */
  public String getForUrlParam() {
    return encodeForUrl(paramCodec.asString(value));
  }
  /*+**********************************************************************/
  /**
   * returns a new url parameter with the given new value, but the the same
   * name and codec.
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
   * <code>req.getParameter</code> by calling {@link #fromString}.
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
  /**
   * <p>
   * creates a list of <code>UrlParam</code> from all values returned by
   * <code>req.getParameterValues()</code> by calling {@link #fromString} for
   * each of them. If parameter does not exist or
   * <code>getParameterValues</code> returns an empty array (no idea whether
   * this may happen at all), a list of length zero is returned.</p>
   * 
   */
  public List<UrlParam<TYPE>> fromAll(ServletRequest req) {
    String[] values = req.getParameterValues(name);
    if (values==null) {
      return new ArrayList<UrlParam<TYPE>>(0);
    }
    List<UrlParam<TYPE>> result = new ArrayList<UrlParam<TYPE>>(values.length);
    for(String v : values) {
      result.add(fromString(v));
    }
  
    return result;
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
