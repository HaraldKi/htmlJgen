package de.pifpafpuf.web.urlparam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import de.pifpafpuf.web.urlparam.DateCodec;
import de.pifpafpuf.web.urlparam.StringCodec;

public class DateCodecTest {

  private static final Long HOURMILLIS = 3600L*1000;

  @Test
  public void isoMonthTest() throws Exception {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));

    long max = df.parse("2013-03").getTime();
    long min = df.parse("2013-01").getTime();
    long two = df.parse("2013-02").getTime();
    
    DateCodec dc = new DateCodec(df, min, max);
    assertEquals(Long.valueOf(min), dc.get("2013-01"));
    assertEquals(Long.valueOf(two), dc.get("2013-02"));
    assertEquals(Long.valueOf(max), dc.get("2013-03"));
    
    df.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
    dc = new DateCodec(df, min, max);
    assertEquals(Long.valueOf(two-HOURMILLIS), dc.get("2013-02"));
    assertEquals(Long.valueOf(max-HOURMILLIS), dc.get("2013-03"));
    
    assertNull(dc.get("bla bla"));
    assertNull(dc.get("2013-01"));
    assertNull(dc.get("2013-04"));
  }
  @Test
  public void noRangeTest() throws Exception {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));

    DateCodec dc = new DateCodec(df);
    Long t1 = df.parse("0001-01").getTime();
    Long t2 = df.parse("9595-12").getTime();
    assertEquals(t1, dc.get("0001-01"));
    assertEquals(t2, dc.get("9595-12"));
  }
  @Test
  public void formatTest() throws Exception {    
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    DateCodec dc = new DateCodec(df);

    assertEquals(df.format(new Date(123456)), dc.asString(123456L));
  }
}
