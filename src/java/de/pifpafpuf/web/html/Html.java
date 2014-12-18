package de.pifpafpuf.web.html;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * is a node in an HTML DOM tree intended for HTML code generation.
 */
public class Html extends EmptyElem {
  private final List<Stringable> children;
  private boolean tight = false;
  /*+******************************************************************/
  /**
   * creates an HTML element for the given {@code tagname}.
   * @param tagname
   */
  public Html(String tagname) {
    super(tagname);
    this.children = new LinkedList<Stringable>();
  }
  /*+******************************************************************/
  /**
   * adds a child element with the given text. When printed, the text will be
   * encode as to not contain illegal characters like '<' and so on.
   * 
   * @param text is the text for the child element.
   * @return {@code this}
   */
  public Html addText(String text) {
    children.add(HtmlText.fromRaw(text));
    return this;
  }
  /*+******************************************************************/
  /**
   * calls {@link EmptyElem#setAttr}, but returns {@code this} to allow
   * fluent use.
   */
  @Override
  public Html setAttr(String key, String value) {
    super.setAttr(key, value);
    return this;
  }
  /*+******************************************************************/
  /**
   * like {@link #addText} but the given text must already be properly HTML
   * encoded, because on output with {@link #print} it is not encoded.
   * 
   * @param text
   * @return {@code this}
   */
  public Html addEncoded(String text) {
    children.add(HtmlText.fromEncoded(text));
    return this;
  }
  /*+******************************************************************/
  /**
   * adds the given text as child element. 
   */
  public void add(HtmlText child) {
    children.add(child);
  }
  /*+******************************************************************/
  /**
   * adds a child element for the given {@code tagname} and returns it.
   * @param tagname name of the element to generate
   * @return the generated child element
   */
  public Html add(String tagname) {
    Html child = new Html(tagname);
    add(child);
    return child;
  }
  /*+******************************************************************/
  /**
   * adds the given child element
   */
  public void add(EmptyElem child) {
    children.add(child);
  }
  /*+**********************************************************************/
  public EmptyElem addEmpty(String tagname) {
    EmptyElem child = new EmptyElem(tagname);
    add(child);
    return child;
  }
  /*+**********************************************************************/
  /**
   * requests tight output of this HTML element. The children of this element
   * are printed out inside the element's tags without any additional
   * whitespace like line breaks and indentation. Tightness is inherited by
   * all children.
   * 
   * @return {@code this}
   */
  public Html setTight() {
    tight = true;
    return this;
  }
  /*+******************************************************************/
  /**
   * returns the number of child elements
   */
  public int getNumChildren() {
    return children.size();
  }
  /*+******************************************************************/
  /**
   * convenience method combining {@link #add(Html)} and {@link #setTight}
   * for the generated child element.
   * 
   * @param tagname is the name of the generated child element
   * @return the generated child element
   */
  public Html addTight(String tagname) {
    Html child = add(tagname);
    child.setTight();
    return child;
  }
  /*+******************************************************************/
  @Override
  public void print(Appendable out, int indent) throws IOException {
    super.print(out, indent);
    if (tight) {
      indent = Integer.MIN_VALUE;
    }
    printChildren(out, indent);
    if (indent>=0) {
      out.append('\n');
      TextUtils.doIndent(out, indent);
    }
    out.append("</").append(super.getName()).append('>');    
  }
  /*+******************************************************************/
  private void printChildren(Appendable out, int indent) throws IOException {
    for(Stringable child : children) {
      if (indent>=0) {
        out.append('\n');
      }
      child.print(out, indent+2);
    }
  }
  /*+******************************************************************/
}
