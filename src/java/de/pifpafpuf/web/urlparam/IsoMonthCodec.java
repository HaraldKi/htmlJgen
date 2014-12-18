package de.pifpafpuf.web.urlparam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public enum IsoMonthCodec implements ParamCodec<String> {
  INSTANCE;
  
  private static final ThreadLocal<DateFormat> isoMonthFormat =
      new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM");
    }
  };
  /*+******************************************************************/
  @Override
  public String get(String value) {
    try {
      isoMonthFormat.get().parse(value);
      return value;
    } catch( ParseException e ) {
      return null;
    }    
  }
  @Override
  public String asString(String e) {
    return e;
  }
  
}
