package de.pifpafpuf.web.urlparam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class UrlParamTest {
  private enum E1 {eins, zwei;};
  private enum E2 {drei, vier;};
  
  @SuppressWarnings("deprecation")
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
  @SuppressWarnings("deprecation")
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

  @SuppressWarnings("deprecation")
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
  @SuppressWarnings("deprecation")
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
  
  @SuppressWarnings("deprecation")
  @Test
  public void UrlParamFirstTest() {
    ServletRequestMock req = new ServletRequestMock();
    UrlParam<Integer> p1 = 
        new UrlParam<Integer>("dodo", 42, IntegerCodec.INSTANCE);
    UrlParam<Integer> result = p1.fromFirst(req);
    assertEquals(p1, result);
    
    req.setParameter("dodo", "one", "two", "three");
    result = p1.fromFirst(req);
    assertEquals(p1, result);

    req.setParameter("dodo", "112");
    result = p1.fromFirst(req);
    assertEquals(Integer.valueOf(112), result.getValue());
  }

  @SuppressWarnings("deprecation")
  @Test
  public void UrlParamFirstAll() {
    ServletRequestMock req = new ServletRequestMock();
    UrlParam<Integer> p1 = 
        new UrlParam<Integer>("dodo", 42, IntegerCodec.INSTANCE);
    List<UrlParam<Integer>> result = p1.fromAll(req);
    assertEquals(0, result.size());
    
    req.setParameter("dodo", "one", "two", "three");
    result = p1.fromAll(req);
    assertEquals(3, result.size());
    assertEquals(p1, result.get(0));

    req.setParameter("dodo", "112", "113");
    result = p1.fromAll(req);
    assertEquals(Integer.valueOf(112), result.get(0).getValue());
    assertEquals(Integer.valueOf(113), result.get(1).getValue());
  }
}
