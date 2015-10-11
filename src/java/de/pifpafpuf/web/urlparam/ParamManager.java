package de.pifpafpuf.web.urlparam;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * helps to manage url query parameters by retrieving them from a request and
 * by providing them again for a url.
 * 
 * @deprecated The idea of a central manager for all URL parameters proved
 *             not to be too helpful. Rather define {@code static final}
 *             {@link UrlParamCodec} objects in the servlets that use the
 *             parameters and access the object in other servlets that
 *             generate the a parameter of this name and type into a URL or a
 *             form element.
 */
@Deprecated
public class ParamManager {
  
  private final List<UrlParam<?>> params = new LinkedList<UrlParam<?>>();
  private final String requestUri;
  private final ParamManager client;
  /*+******************************************************************/ 
  /**
   * creates a manager for the given requestUri with an empty list of url
   * parameters. Use the @{code addXX} methods to add parameters..
   * 
   * @param requestUri is the URI used when creating URLs. In a servlet, 
   * {@code request.getRequestURI()} should typically be used 
   */
  public ParamManager(String requestUri) {
    this.requestUri = requestUri;
    this.client = null;
  }
  /*+******************************************************************/
  /**
   * creates a manager for the given requestUri and a chained client. The
   * chained client is used in url generation to add its own URL parameters.
   * The client's requestUri is not used.
   * 
   */
  public ParamManager(String requestUri, ParamManager client) {
    this.requestUri = requestUri;
    if (client==null) {
      throw new NullPointerException("client may not be null");
    }
    this.client = client;
  }
  /*+******************************************************************/
  public String getRequestUri() {
    return requestUri;
  }
  /*+******************************************************************/
  /**
   * add the given value with the name specified by the template. The parameter
   * is stored internally as well as returned. The template is used as-is, if
   * the parameter is not specified or has the wrong format.
   * 
   * In a Servlet, this method will usually be called like
   * 
   * <pre>
   * add(template, request.getParameter(template.getName()))
   * </pre>
   * 
   * @param value for the templated parameter, 
   * encoded as a String or {@code null}.
   */
  public <T> UrlParam<T> add(UrlParam<T> template, String value) {
    UrlParam<T> result;
    if (value==null) {
      result = template;
    } else {
      result = template.fromString(value);
    }
    params.add(result);
    return result;
  }
  /*+******************************************************************/
  /**
   * parses all parameters from the array given. All which can be parsed are
   * added to the result list. If the result list stays empty the template alone
   * is put into the result list, given it has a non-@{code null} value.
   * 
   * The result list is stored internally and is also returned.
   * 
   * In a Servlet, this method will usually be called like
   * <pre>add(request.getParameterValues(template.getName()), template)</pre>

   * @param values may be {@code null}
   */
  public <T> List<UrlParam<T>> 
  addAll(UrlParam<T> template, String[] values) 
  {
    if (values==null) {
      return Collections.emptyList();
    }

    LinkedList<UrlParam<T>> result = new LinkedList<UrlParam<T>>();
    for(String value : values) {
      UrlParam<T> p = template.fromString(value);
      if (p.getValue()==null || p==template) {
        continue;
      }
      result.add(p);
    }
    if (result.isEmpty() && template.getValue()!=null) {
      result.add(template);
    }
    params.addAll(result);
    return result;
  }
  /*+******************************************************************/
  public String getUrl() {
    List<UrlParam<?>> addList = Collections.<UrlParam<?>>emptyList();
    Set<UrlParam<?>> dropSet = Collections.<UrlParam<?>>emptySet();
    return getUrl(dropSet, addList);
  }
  /*+******************************************************************/
  /**
   * calls @{link #getUrl(Set,List)} with empty drop set.
   */
  public String getUrlWith(UrlParam<?> add) {
    Set<UrlParam<?>> dropSet = Collections.<UrlParam<?>>emptySet();
    List<UrlParam<?>> addList =
        Collections.<UrlParam<?>>singletonList(add);
    return getUrl(dropSet, addList);    
  }
  /*+******************************************************************/
  /**
   * calls @{link #getUrl} with empty add list.
   */
  public String getUrlWithOut(UrlParam<?> drop) {
    Set<UrlParam<?>> dropSet = Collections.<UrlParam<?>>singleton(drop);
    List<UrlParam<?>> addList = Collections.<UrlParam<?>>emptyList();
    return getUrl(dropSet, addList);    
  }
  /*+******************************************************************/
  /**
   * calls @{link #getUrl} with single element drop set and add list.
   */
  public String getUrl(UrlParam<?> drop, UrlParam<?> add) {
    Set<UrlParam<?>> dropSet = Collections.<UrlParam<?>>singleton(drop);
                                 
    List<UrlParam<?>> addList =
        Collections.<UrlParam<?>>singletonList(add);
    return getUrl(dropSet, addList);
  }
  /*+******************************************************************/
  /**
   * creates a URL from the requestUri specified in the constructor and the
   * stored parameters minus the parameters of the drop list plus the
   * parameters of the add list.
   * 
   * @param drop parameters to drop during URL generation
   * @param add parameters to add during URL generation
   * @return the generated URL
   */
  public String getUrl(Set<UrlParam<?>> drop,
                       List<UrlParam<?>> add)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(requestUri);
    char sep = '?';
    sep = paramsToUrl(sb, sep, drop, add);
    if (client!=null) {
      sep = client.paramsToUrl(sb, sep, drop, add);
    }
    return sb.toString();
  }
  /*+******************************************************************/
  private char paramsToUrl(StringBuilder sb,
                           char sep,
                           Set<UrlParam<?>> drop,
                           Collection<UrlParam<?>> add) {
    for (UrlParam<?> p : params) {
      if (drop.contains(p)) {
        continue;
      }
      sb.append(sep);
      sep = '&';
      appendUrlParam(sb, p);
    }
    for(UrlParam<?> p : add) {
      sb.append(sep);
      sep = '&';
      appendUrlParam(sb, p);
    }
    return sep;
  }
  /*+******************************************************************/
  /**
   * creates a URL containing only the explicitly provided parameters
   */
  public String getNewUrl(UrlParam<?> ... onlyParams) {
    StringBuilder sb = new StringBuilder();
    sb.append(requestUri);
    char sep = '?';
    for (UrlParam<?> p : onlyParams) {
      sb.append(sep);
      sep = '&';
      appendUrlParam(sb, p);
    }
    return sb.toString();
  }
  /*+******************************************************************/
  private void appendUrlParam(StringBuilder sb, UrlParam<?> p) {
    sb.append(p.getName())
    .append('=')
    .append(p.getForUrlParam());
  }
}
