package de.pifpafpuf.web.urlparam;

public enum BooleanCodec implements ParamCodec<Boolean> {
  INSTANCE;
  @Override
  public Boolean get(String value) {
    if ("true".equals(value) || "1".equals(value)) {
      return Boolean.TRUE;
    }
    if ("false".equals(value) || "0".equals(value)) {
      return Boolean.FALSE;
    }
    return null;
  }

  @Override
  public String asString(Boolean e) {
    return e.toString();
  }

}
