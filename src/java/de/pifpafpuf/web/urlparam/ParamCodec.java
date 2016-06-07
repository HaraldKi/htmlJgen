package de.pifpafpuf.web.urlparam;


/**
 * encodes and decodes values from and to a string form. The string form must
 * be uniquely decodable back in to the value.
 * 
 * @param <E>
 */
public interface ParamCodec<E> {
  /**
   * converts the value into a String such that the {@link #get} method is able
   * to convert it back.
   * 
   * @return may never be {@code null}. If {@code e==null} and the we cannot
   *         convert it to a string that the decoder makes into {@code null}
   *         again, throw <code>NullPointerException</code>.
   */
  String asString(E e);

  /**
   * decodes the String into a value or returns {@code null} if it cannot be
   * decoded.
   * 
   * @param value may be {@code null} in which case the return value should
   *        most likely be {@code null} too, except you know exactly that you
   *        want.
   * 
   * @return {@code null} if the string cannot be properly decoded.
   */
  E get(String value);
}
