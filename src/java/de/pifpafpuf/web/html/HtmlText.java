package de.pifpafpuf.web.html;

import java.io.IOException;

/**
 * is a text node within an HTML DOM tree. 
 */
public class HtmlText implements Stringable {

  private final String text;
  private final boolean isEncoded;
  /*+******************************************************************/
  /**
   * creates a text node from raw text. The text will be properly HTML
   * encoded on output.
   */
  public static HtmlText fromRaw(String text) {
    return new HtmlText(text, false);
  }
  /*+******************************************************************/
  /**
   * creates a text node from already properly HTML encoded text. The text
   * will not be HTML encoded again on output.
   */
  public static HtmlText fromEncoded(String encodedText) {
    return new HtmlText(encodedText, true);
  }
  /*+******************************************************************/
  private HtmlText(String text, boolean isEncoded) {
    if (text==null) {
      throw new NullPointerException("parameter text may not be null");
    }
    this.text = text;
    this.isEncoded = isEncoded;
  }
  /*+******************************************************************/
  @Override
  public void print(Appendable out, int indent) throws IOException {
    TextUtils.doIndent(out, indent);
    if (isEncoded) {
      out.append(text);
    } else {
      TextUtils.encodeHtml(out, text);
    }
  }
  /*+******************************************************************/
}
