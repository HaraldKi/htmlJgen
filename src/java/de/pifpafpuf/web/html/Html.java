package de.pifpafpuf.web.html;

import java.util.LinkedList;
import java.util.List;

/**
 * is a node in an HTML DOM tree intended for HTML code generation. It always
 * generates a close tag.
 */
public final class Html extends FormattingHtml {
  private final List<Stringable> children;
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
  public void add(Stringable child) {
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
   * <p>calls {@link FormattingHtml#setTight} with {@code true}.</p>
   * 
   * @return {@code this}
   */
  public Html setTight() {
    super.setTight(true);
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
   * convenience method combining {@link #add(String)} and {@link #setTight}
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
  /**
   * returns the child elements.
   */
  public final Iterable<Stringable> getChildren() {
    return children;
  }
}
