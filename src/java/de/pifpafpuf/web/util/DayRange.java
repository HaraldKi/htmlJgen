package de.pifpafpuf.web.util;

/**
 * defines an inclusive interval of days.
 * 
 * FIXME: The day encoding is idiosyncratic and should be changed to
 * something that can be better verified to be correct.
 * 
 * @deprecated only used by deprecated IsoDateCodec
 */
@Deprecated
public class DayRange {
  public final int dayMax;
  public final int dayMin;

  /**
   * both parameters must have the form of an iso date as an integer, e.g.
   * 20130427, however, this is not verified.
   */
  public DayRange(int dayMin, int dayMax) {
    if (dayMin>dayMax) {
      String msg = String.format("dayMin=%d may not be larger than dayMax=%d",
                                 dayMin, dayMax);
      throw new IllegalArgumentException(msg);
    }
    this.dayMin = dayMin;
    this.dayMax = dayMax;
  }

  public boolean contains(int dayRep) {
    return dayRep>=dayMin && dayRep<=dayMax;
  }
}
