package de.pifpafpuf.web.html;

import java.io.IOException;

/**
 * <p>
 * base class for HTML elements with children. This class does not store or
 * handle children, it only knows how to output them in a pretty fashion. The
 * general class with child elements is {@link Html}.
 * </p>
 * 
 * <p>
 * Applications using this library will define application specific
 * subclasses with, for example, a fixed number of child elements of a
 * possibly fixed type. Examples could be page header, or a repeatedly used
 * UI element like a button or a type of list.
 * </p>
 */
public abstract class FormattingHtml extends EmptyElem {
  private boolean tight = false; 
  /*+******************************************************************/
  public FormattingHtml(String tagname) {
    super(tagname);
  }
  /*+******************************************************************/
  /**
   * requests tight or indented output of this HTML element. The children of
   * this element are printed out inside the element's tags without any
   * additional whitespace like line breaks and indentation in tight mode.
   * Tightness is inherited by all children.
   */
  public void setTight(boolean tight) {
    this.tight = tight;
  }
  /*+******************************************************************/
  /**
   * <p>
   * subclasses must provide the children for the benefit of {@link #print}.
   * </p>
   * 
   * @return the children managed by the subclass
   */
  protected abstract Iterable<Stringable> getChildren();
  /*+******************************************************************/
  /**
   * locked against overriding, because without this method, this class has
   * no service to offer. If you feel you need to override this method,
   * derive directly from {@link EmptyElem} instead.
   */
  @Override
  public final void print(Appendable out, int indent) throws IOException {
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
    for(Stringable child : getChildren()) {
      if (indent>=0) {
        out.append('\n');
      }
      child.print(out, indent+2);
    }
  }
  /*+******************************************************************/

}
