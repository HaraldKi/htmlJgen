package de.pifpafpuf.web.urlparam;

/**
 * is a do-nothing (identity) codec for strings to be used as-is in a URL.
 */
public enum StringCodec implements ParamCodec<String> {
  INSTANCE;
  
  @Override
  public String get(String value) {
    return value;
  }

  @Override
  public String asString(String e) {
    if (e==null) {
      throw new NullPointerException("parameter is null");
    }
    return e;
  }

}
