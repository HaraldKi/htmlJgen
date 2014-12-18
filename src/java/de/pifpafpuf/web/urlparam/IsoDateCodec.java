package de.pifpafpuf.web.urlparam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.pifpafpuf.web.util.DayRange;

/**
 * is a codec for dates of the form yyyyMMdd restricted by a @{link
 * DayRange}.
 * 
 * @deprecated this is idiosyncratic remains for a project that uses it. Use
 *             {@link DateCodec} instead.
 */
@Deprecated
public class IsoDateCodec implements ParamCodec<Calendar> {
  private static final ThreadLocal<DateFormat> isoDayFormat =
      new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyyMMdd");
    }
  };
  private final TimeZone tz;
  private final Locale loc;
  private final DayRange times;
  
  /*+******************************************************************/
  public IsoDateCodec(TimeZone tz, Locale loc, DayRange times) {
    this.tz = tz;
    this.loc = loc;
    this.times = times;
  }
  /*+******************************************************************/
  @Override
  public Calendar get(String value) {
    Date d = null;
    try {
      d = isoDayFormat.get().parse(value);
    } catch( ParseException e ) {
      //e.printStackTrace();
      return null;
    }
    Calendar cal = Calendar.getInstance(tz, loc);
    cal.setTime(d);
    int dayRep = dayRep(cal);
    if (dayRep<times.dayMin || dayRep>times.dayMax) {
      return null;
    }
    
    return cal;
  }
  @Override
  public String asString(Calendar cal) {
    int d = dayRep(cal);
    return Integer.toString(d);
  }
  /*+******************************************************************/
  public static int dayRep(Calendar cal) {
    int d = 10000*cal.get(Calendar.YEAR)
        + 100*(1+cal.get(Calendar.MONTH))
        + cal.get(Calendar.DAY_OF_MONTH);
    return d;
  }
  public Calendar fromDayRep(int dayRep) {
    Calendar cal = Calendar.getInstance(tz, loc);
    int day = dayRep % 100;
    int month = (dayRep/100)%100-1;
    int year = dayRep/10000;
    cal.set(year, month, day, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal;    
  }
}
