package de.pifpafpuf.web.urlparam;


/**
 * encodes and decodes values from and to a string form. The string form must
 * be uniquely decodable back in to the value.
 * 
 * @param <E>
 */
public interface ParamCodec<E> {
  /**
   * converts the value into a String such that the {@code #get} method is able
   * to convert it back.
   * 
   * @return may never be {@code null}. If {@code e==null} and the coder cannot
   *         convert it to a string that the decoder makes into {@code null}
   *         again, throw NullPointerException.
   */
  String asString(E e);

  /**
   * decodes the String into a value.
   * @return {@code null} if the String cannot be properly decoded. 
   */
  E get(String value);
}
