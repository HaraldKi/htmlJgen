package de.pifpafpuf.web.html;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * is a node for an empty HTML element in an HTML DOM tree intended for HTML
 * code generation.
 */
public class EmptyElem implements Stringable {
  private final String elemName;
  private final Map<String,String> attributes;
  /*+******************************************************************/
  /**
   * creates an HTML element for the given {@code tagname}.
   * @param tagname
   */
  public EmptyElem(String tagname) {
    this.elemName = tagname;
    this.attributes = new HashMap<String,String>(2);
  }
  /*+******************************************************************/
  public final String getName() {
    return elemName;
  }
  /*+******************************************************************/
  /**
   * sets the attribute of this HTML element.
   * @return {@code this}
   */
  public EmptyElem setAttr(String key, String value) {
    attributes.put(key, value);
    return this;
  }
  /*+******************************************************************/
  public String getAttr(String key, String deflt) {
    String result = attributes.get(key);
    if (result==null) {
      return deflt;
    }
    return result;
  }
  /*+******************************************************************/
  @Override
  public void print(Appendable out, int indent) throws IOException {
    //System.out.println(tagname+" indent="+indent+" pretty="+tight);
    TextUtils.doIndent(out, indent);
    out.append('<').append(elemName);
    if (attributes.size()>0) {
      printAttributes(out);
    }
    out.append('>');
  }
  /*+******************************************************************/
  private void printAttributes(Appendable out) throws IOException {
    for(Map.Entry<String,String> attr : attributes.entrySet()) {
      out.append(' ').append(attr.getKey()).append("=\"");
      TextUtils.encodeHtml(out, attr.getValue());
      out.append('"');
    }
  }
  /*+******************************************************************/
  @Override
  public final String toString() {
    return TextUtils.toString(this);
  }
}
