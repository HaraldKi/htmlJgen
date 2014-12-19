package de.pifpafpuf.web.urlparam;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Code and decode a String according to a DateFormat object into a time stamp.
 */
public class DateCodec implements ParamCodec<Long> {

  private final DateFormat df;
  private long minStamp;
  private long maxStamp;
  
  /**
   * creates a codec for the given format. Be sure to define the correct
   * {@link java.util.TimeZone} for the given format for parsing dates and
   * times. The result will differ up to 24*3600*1000 milliseconds from what you
   * expect, if you leave the time zone arbitrary.
   * 
   * @param df
   *          to be understood by {@link java.text.SimpleDateFormat}
   */
  public DateCodec(DateFormat df) {    
    this(df, Long.MIN_VALUE, Long.MAX_VALUE);
  }

  /**
   * creates a codec for the given format that, when decoding, only applies, if
   * the resulting time stamp is in the closed interval given
   * 
   * @param df
   *          to be understood by {@link java.text.SimpleDateFormat}
   * @param minStamp is the minimum allowed value for a parsed date
   * @param maxStamp is the maximum  allowed value for a parsed date
   */
  public DateCodec(DateFormat df, long minStamp, long maxStamp) {
    this.df = (DateFormat)df.clone();
    this.minStamp = minStamp;
    this.maxStamp = maxStamp;
  }

  @Override
  public synchronized String asString(Long e) {
    return df.format(new Date(e));
  }

  @Override
  public synchronized Long get(String value) {
    long result = 0;
    try {
      Date d = df.parse(value);
      result = d.getTime();
    } catch (ParseException e) {
      // Since it is expected behavior that a value sometimes cannot be parsed,
      // no logging 
      //e.printStackTrace();
      return null;
    }
    if (result<minStamp || result>maxStamp) {
      return null;
    }
    return result;
  }

}
