package de.pifpafpuf.web.urlparam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.pifpafpuf.web.urlparam.BooleanCodec;
import de.pifpafpuf.web.urlparam.EnumCodec;
import de.pifpafpuf.web.urlparam.IntegerCodec;

public class CodecTest {
  private static enum Num {
    ONE, TWO, THREE;
  }
  
  @Test
  public void integerTest() {
    int value = IntegerCodec.INSTANCE.get("1111");
    assertEquals(1111, value);
    String encoded = IntegerCodec.INSTANCE.asString(1234);
    assertEquals("1234", encoded);
    
    Integer nullValue = IntegerCodec.INSTANCE.get("xyz");
    assertNull(nullValue);
  }
  
  @Test
  public void booleanTest() {
    BooleanCodec codec = BooleanCodec.INSTANCE;
    boolean value = codec.get("true");
    assertTrue(value);
    value = BooleanCodec.INSTANCE.get("1");
    assertTrue(value);

    value = BooleanCodec.INSTANCE.get("false");
    assertFalse(value);
    value = BooleanCodec.INSTANCE.get("0");
    assertFalse(value);
    
    assertEquals("true", codec.asString(true));
    assertEquals("false", codec.asString(false));
   
    assertNull(codec.get("blabla"));
  }
  @Test 
  public void EnumTest() {
    EnumCodec<Num> codec = new EnumCodec<Num>(Num.values());
    for(String name : new String[] {"ONE", "TWO", "THREE"}) {
      Num value = codec.get(name);
      assertEquals(name, value.toString());
    }
    assertNull(codec.get("faselbla"));
    for(Num n : Num.values()) {
      assertEquals(n.name(), codec.asString(n));
    }
  }
  

}
