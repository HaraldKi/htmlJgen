package de.pifpafpuf.web.urlparam;

import java.util.HashMap;
import java.util.Map;

public class EnumCodec<E extends Enum<E>>
implements ParamCodec<E>
{
  private final Map<String,E> toEnum;
  /*+******************************************************************/
  public EnumCodec(E[] values) {
    this.toEnum = new HashMap<String,E>(values.length); 
    for(E e : values) {
      toEnum.put(e.name(), e);
    }
  }
  /*+******************************************************************/  
  public E get(String value) {
    return toEnum.get(value);
  }
  /*+******************************************************************/
  @Override
  public String asString(E e) {
    return e.name();
  }
}
