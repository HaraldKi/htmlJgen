package de.pifpafpuf.web.html;

import java.io.IOException;

/**
 * describes objects that can append themselves in a readable manner to an
 * {@code Appendable}.
 */
interface Stringable {
  /**
   * appends the object's string representation to {@code out}, possibly using
   * an indent of the given number of whitespace characters. Whether and
   * where line breaks and indentation is used, is free to the implementing
   * object.
   * 
   * @param out is the stream to append the text to
   * @param indent must be used right after line breaks inserted
   * @throws IOException
   */
  void print(Appendable out, int indent) throws IOException;
}
