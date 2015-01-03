package de.pifpafpuf.web.html;

import java.io.IOException;
/**
 * assorted utilities needed for generating HTML.
 */
class TextUtils {
  private TextUtils() {}

  /**
   * encode characters such that they can be used inside HTML text as well as
   * inside an element's attribute. In particular the characters @{code
   * "'&<>} and everything below ASCII space is converted to an encoded
   * character entity.
   * 
   * @throws IOException only thrown if the appending to @{code out} throws.
   */
  public static void 
  encodeHtml(Appendable out, String value) throws IOException
  {
    for(int i=0; i<value.length(); i++) {
      char ch = value.charAt(i);
      if (ch=='"') {
        out.append("&quot;");
      } else if (ch=='\'') {
        out.append("&#39;");
      } else if (ch=='&') {
        out.append("&amp;");
      } else if (ch=='<') {
        out.append("&lt;");
      } else if (ch=='>') {
        out.append("&gt;");
      } else if (ch<32) {
        out.append("&#").append(Integer.toString(ch)).append(';');
      } else {
        out.append(ch);
      }
    }
  }
  /**
   * appends the given number of space characters to the output.
   * @throws IOException if appending to {@code out} throws this exception
   */
  public static void doIndent(Appendable out, int indent) throws IOException {
    for(int i=0; i<indent; i++) {
      out.append(' ');
    }
  }
  public static String toString(Stringable thing) {
    StringBuilder sb = new StringBuilder(100);
    try {
      thing.print(sb, 0);
    } catch( IOException eNever ) {
      String msg = "how can append to StringBuilder throw IOException?";
      throw new RuntimeException(msg, eNever);
    }
    return sb.toString();
  }
}
