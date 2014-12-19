package de.pifpafpuf.web.urlparam;

import static org.junit.Assert.*;

import org.junit.Test;

import de.pifpafpuf.web.urlparam.BooleanCodec;
import de.pifpafpuf.web.urlparam.EnumCodec;
import de.pifpafpuf.web.urlparam.IntegerCodec;
import de.pifpafpuf.web.urlparam.StringCodec;
import de.pifpafpuf.web.urlparam.UrlParam;

public class UrlParamTest {
  private enum E1 {eins, zwei;};
  private enum E2 {drei, vier;};
  
  @Test
  public void urlParamEqualsTest() {
    UrlParam<Integer> p1 = 
        new UrlParam<Integer>("p1", 42, IntegerCodec.INSTANCE);
    UrlParam<Integer> p2 = 
        new UrlParam<Integer>("p1", 42, IntegerCodec.INSTANCE);
    UrlParam<Integer> p3 = 
        new UrlParam<Integer>("p1", 100, IntegerCodec.INSTANCE);

    UrlParam<Integer> p4 = 
        new UrlParam<Integer>("p4", 100, IntegerCodec.INSTANCE);


    UrlParam<Integer> p1Copy = p1.fromValue(42);
    assertFalse(p1==p1Copy);
    assertTrue(p1.equals(p2));
    assertTrue(p1.equals(p1Copy));
    assertFalse(p1.equals(p3));
    assertTrue(p1.equals(p1));
    assertFalse(p1.equals(null));
    assertFalse(p1.equals(new Object()));
    assertFalse(p1.equals(p4));
    assertFalse(p1.equals(BooleanCodec.INSTANCE));
   
    EnumCodec<E1> ec1 = new EnumCodec<E1>(E1.values());
    EnumCodec<E2> ec2 = new EnumCodec<E2>(E2.values());
    
    UrlParam<E1> upe1 = new UrlParam<E1>("upe", null, ec1);
    UrlParam<E2> upe2 = new UrlParam<E2>("upe", null, ec2);
    assertFalse(upe1.equals(upe2));
   
    UrlParam<E1> upe3 = upe1.fromValue(E1.eins);
    assertFalse(upe3.equals(upe1));
    assertFalse(upe1.equals(upe3));
    
  }
  @Test
  public void urlParamHashTest() {
    EnumCodec<E1> ec1 = new EnumCodec<E1>(E1.values());
    UrlParam<E1> upe1 = new UrlParam<E1>("upe", E1.eins, ec1);

    UrlParam<E1> upe2 = upe1.fromString("blafasel");
    assertTrue(upe1.hashCode()==upe2.hashCode());
    
    upe1 = new UrlParam<E1>("upe", null, ec1);
    upe2 = upe1.fromString("asdfa");
    assertTrue(upe1.hashCode()==upe2.hashCode());    
  }
  @Test
  public void exceptionsTest() {
    try {
      new UrlParam<Integer>("bla", 0, null);
      fail("should have caught an NPE");
    } catch (Exception e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
    
    try {
      new UrlParam<String>(null, "hallo", StringCodec.INSTANCE);
      fail("should have caught an NPE");
    } catch (Exception e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
  }
  @Test
  public void toParamTest() {
    UrlParam<Integer> p = 
        new UrlParam<Integer> ("bla", null, IntegerCodec.INSTANCE);
    try {
      p.getForInputParam();
      fail("should have caught NPE");
    } catch (NullPointerException e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
    p = p.fromString("12");
    assertEquals("12", p.getForInputParam());    
  }
}
