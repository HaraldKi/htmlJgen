package de.pifpafpuf.web.urlparam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;

import de.pifpafpuf.web.html.EmptyElem;
import de.pifpafpuf.web.urlparam.ParamCodec;

/**
 * <p>
 * decodes and encodes parameter values of type {@code TYPE} from URLs
 * and for HTML forms and links.</p>
 *
 * <p>Objects of this class are immutable. They are used to fetch parameters
 * from the {@link javax.servlet.ServletRequest} as well as to write the
 * parameter with its value into a URL or an HTML form element.
 * </p>
 *
 * @param <TYPE> is the type of the parameter stored.
 */
public class UrlParamCodec<TYPE> {
  private final String name;
  private final ParamCodec<TYPE> paramCodec;

  /**
   * creates a url parameter codec for a parameter with the given name and
   * type.
   *
   * @param name of the parameter as used in the URL. No check is made
   *        whether the name conforms to the syntax rules of URL parameter
   *        names and no encoding/decoding is performed for the name.
   * @param codec is the codec to decode strings retrieved from
   *        {@link javax.servlet.ServletRequest} and to encode the value for
   *        a URL or an HTML form input element. The codec should only
   *        encode/decode to/from an arbitrary <code>String</code> URL
   *        en/decoding is taken care of by {@code this}.
   */
  public UrlParamCodec(String name,  ParamCodec<TYPE> codec) {
    if (name==null) {
      throw new NullPointerException("parameter name may not be null");
    }
    this.name = name;
    if (codec==null) {
      throw new NullPointerException("parameter pp may not be null");
    }
    this.paramCodec = codec;
  }
  /*+******************************************************************/
  public String getName() {
    return name;
  }
  /*+******************************************************************/
  /**
   * returns the value encoded in string form by the codec defined in the
   * constructor.
   */
  public String asString(TYPE value) {
    return paramCodec.asString(value);
  }
  /*+******************************************************************/
  /**
   * sets the name and the value attribute in the given {@code html} element
   * to reflect this
   */
  public void setParam(EmptyElem html, TYPE value) {
    html.setAttr("name", name);
    html.setAttr("value", paramCodec.asString(value));
  }
  /*+**********************************************************************/
  /**
   * returns the value of <code>this</code> after first encoding it with our
   * codec and then URL-encoding it.
   */
  public String getForUrlParam(TYPE value) {
    return encodeForUrl(paramCodec.asString(value));
  }
  /*+**********************************************************************/
  /**
   * appends the encoded {@code value} to the
   * {@link java.util.StringBuilder} provided. If the URL provided does not
   * end with a '?', the parameter is prefixed with '&'.
   */
  public void appendToUrl(StringBuilder sb, TYPE value) {
    final int l = sb.length();
    if (l==0 || sb.charAt(l-1)!='?') {
      sb.append('&');
    }
    sb.append(name).append('=').append(getForUrlParam(value));
  }
  /*+******************************************************************/
  /**
   * creates a {@code TYPE} from the given text. If the text is
   * cannot be parsed by the codec, the {@code defaultValue} is
   * returned.
   *
   * @param text to be converted, may be {@code null}
   * @return a new object or the default value
   */
  public TYPE fromString(String text, TYPE defaultValue) {
    TYPE v = paramCodec.get(text);
    if (v==null) {
      return defaultValue;
    }
    return v;
  }
  /*+******************************************************************/
  /**
   * <p>
   * creates a {@code TYPE} from the value returned by
   * <code>req.getParameter</code> by calling {@link #fromString}.
   * </p>
   *
   * @param req where to get the parameter value from, may not be
   *        <code>null</code>
   */
  public TYPE fromFirst(ServletRequest req, TYPE defaultValue) {
    String text = req.getParameter(name);
    return fromString(text, defaultValue);
  }
  /*+******************************************************************/
  /**
   * <p>
   * creates a list of {@code TYPE} objects from all values returned by
   * <code>req.getParameterValues()</code> by calling {@link #fromString} for
   * each of them. If the request parameter does not exist or
   * <code>ServletRequest.getParameterValues</code> returns an empty array
   * (no idea whether this may happen at all), a list of length zero is
   * returned. If an individual parameter value cannot be parsed, it is
   * skipped.
   * </p>
   *
   * @param req where to get the parameter values from, may not be
   *        <code>null</code>
   */
  public List<TYPE> fromAll(ServletRequest req) {
    String[] values = req.getParameterValues(name);
    if (values==null) {
      return new LinkedList<TYPE>();
    }
    List<TYPE> result = new ArrayList<TYPE>(values.length);
    for(String value : values) {
      TYPE v = fromString(value, null);
      if (v!=null) {
        result.add(v);
      }
    }
    return result;
  }
  /*+******************************************************************/
  // TODO: according to several stackoverflow comments, URLEncoder is not
  // exactly the right way to do this. (A) it is slow, (B) it is for form
  // encoding (what the browser does, when POSTing a form). But looking at
  // the suggested java.net.URI, this is even more hideous, as it first
  // creates a string and then parses it (ouch). So, how's the encoding done
  // right? Caveat: I would like to not introduce a dependency to Spring or
  // some Apache library.
  private static String encodeForUrl(String text) {
    try {
      return URLEncoder.encode(text, "UTF-8");
    } catch (UnsupportedEncodingException e) {
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
    UrlParamCodec other = (UrlParamCodec)obj;
    if (!name.equals(other.name) ) {
      return false;
    }

    if (!paramCodec.equals(other.paramCodec)) {
      return false;
    }

    return true;
  }
  /*+******************************************************************/
  @Override
  public String toString() {
    return name+"=("+paramCodec.getClass().getName()+")";
  }
}

