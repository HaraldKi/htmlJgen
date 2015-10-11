package de.pifpafpuf.web.urlparam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import de.pifpafpuf.web.html.EmptyElem;
import de.pifpafpuf.web.html.Html;

public class UrlParamCodecTest {
  private enum E1 {eins, zwei;};
  private enum E2 {drei, vier;};
  
  @Test
  public void equalsTest() {
    UrlParamCodec<Integer> p1 = 
        new UrlParamCodec<Integer>("p1", IntegerCodec.INSTANCE);
    UrlParamCodec<Integer> p2 = 
        new UrlParamCodec<Integer>("p1", IntegerCodec.INSTANCE);
    UrlParamCodec<String> p3 = 
        new UrlParamCodec<String>("p1", StringCodec.INSTANCE);

    UrlParamCodec<Integer> p4 = 
        new UrlParamCodec<Integer>("p4",  IntegerCodec.INSTANCE);


    assertTrue(p1.equals(p2));
    assertFalse(p1.equals(p3));
    assertTrue(p1.equals(p1));
    assertFalse(p1.equals(null));
    assertFalse(p1.equals(new Object()));
    assertFalse(p1.equals(p4));
    assertFalse(p1.equals(BooleanCodec.INSTANCE));
   
    EnumCodec<E1> ec1 = new EnumCodec<E1>(E1.values());
    EnumCodec<E2> ec2 = new EnumCodec<E2>(E2.values());
    
    UrlParamCodec<E1> upe1 = new UrlParamCodec<E1>("upe", ec1);
    UrlParamCodec<E2> upe2 = new UrlParamCodec<E2>("upe", ec2);
    assertFalse(upe1.equals(upe2));
   
    UrlParamCodec<E1> upe3 = new UrlParamCodec<E1>("upex", ec1);
    assertFalse(upe3.equals(upe1));
    assertFalse(upe1.equals(upe3));
    
  }
  @Test
  public void hashTest() {
    EnumCodec<E1> ec1 = new EnumCodec<E1>(E1.values());
    UrlParamCodec<E1> upe1 = new UrlParamCodec<E1>("upe", ec1);

    UrlParamCodec<E1> upe2 = new UrlParamCodec<E1>("upe", ec1);
    assertTrue(upe1.hashCode()==upe2.hashCode());    
  }
  @Test
  public void exceptionsTest() {
    try {
      new UrlParamCodec<Integer>("bla", null);
      fail("should have caught an NPE");
    } catch (Exception e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
    
    try {
      new UrlParamCodec<String>(null,  StringCodec.INSTANCE);
      fail("should have caught an NPE");
    } catch (Exception e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
  }
  @Test
  public void toParamTest() {
    UrlParamCodec<Integer> p = 
        new UrlParamCodec<Integer> ("bla", IntegerCodec.INSTANCE);
    EmptyElem el = new EmptyElem("div");
    p.setParam(el, 42);
    assertEquals("42", el.getAttr("value", null));
    assertEquals("bla", el.getAttr("name", null));
  }
  
  @Test
  public void UrlParamFirstTest() {
    ServletRequestMock req = new ServletRequestMock();
    UrlParamCodec<Integer> p1 = 
        new UrlParamCodec<Integer>("dodo", IntegerCodec.INSTANCE);
    int result = p1.fromFirst(req, 42);
    assertEquals(42, result);
    
    req.setParameter("dodo", "one", "two", "three");
    result = p1.fromFirst(req, 42);
    assertEquals(42, result);

    req.setParameter("dodo", "112");
    result = p1.fromFirst(req, 42);
    assertEquals(112, result);
  }
  @Test
  public void UrlParamFirstAll() {
    ServletRequestMock req = new ServletRequestMock();
    UrlParamCodec<Integer> p1 = 
        new UrlParamCodec<Integer>("dodo",IntegerCodec.INSTANCE);
    List<Integer> result = p1.fromAll(req);
    assertEquals(0, result.size());
    
    req.setParameter("dodo", "1", "two", "3");
    result = p1.fromAll(req);
    assertEquals(2, result.size());
    assertEquals(Integer.valueOf(1), result.get(0));

    req.setParameter("dodo", "112", "113");
    result = p1.fromAll(req);
    assertEquals(Integer.valueOf(112), result.get(0));
    assertEquals(Integer.valueOf(113), result.get(1));
  }
  
  @Test
  public void forUrl() {
    UrlParamCodec<String> upc = 
        new UrlParamCodec<>("dodo", StringCodec.INSTANCE);
    String query = upc.getForUrlParam("a b/cä");
    assertEquals("a+b%2Fc%C3%A4", query);
  }
  
  
  @Test
  public void addToUrl() {
    UrlParamCodec<String> upc = 
        new UrlParamCodec<>("dodo", StringCodec.INSTANCE);
    StringBuilder sb = new StringBuilder(200);
    
    upc.appendToUrl(sb, "eins");
    assertEquals("&dodo=eins", sb.toString());
    
    sb.setLength(0);
    sb.append('?');
    upc.appendToUrl(sb, "+ +");
    assertEquals("?dodo=%2B+%2B", sb.toString());
    upc.appendToUrl(sb, "zwei");
    assertEquals("?dodo=%2B+%2B&dodo=zwei", sb.toString());
    
  }
}
