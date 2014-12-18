package de.pifpafpuf.web.urlparam;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.pifpafpuf.web.urlparam.BooleanCodec;
import de.pifpafpuf.web.urlparam.DateCodec;
import de.pifpafpuf.web.urlparam.EnumCodec;
import de.pifpafpuf.web.urlparam.IntegerCodec;
import de.pifpafpuf.web.urlparam.ParamCodec;
import de.pifpafpuf.web.urlparam.StringCodec;

/**
 * Tests whether {@link paramCodec#asString} works as required with 
 * regard to {@code null} parameters. 
 */
@RunWith(Parameterized.class)
public class AllCodecsTest {
  private enum Nums {ONE, TWO, THREE};
  private final ParamCodec<?> codec;
  private final Object value;
  
  @Parameters
  public static Collection<Object[]> data() throws ParseException {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
    
    ParamCodec<?>[] codecs = new ParamCodec[] {
        BooleanCodec.INSTANCE,
        new DateCodec(df),
        new EnumCodec<Nums>(Nums.values()),
        IntegerCodec.INSTANCE,
        StringCodec.INSTANCE,
    };
    Object[] valuesToConvert = new Object[] {
        true,
        df.parse("2013-12").getTime(),
        Nums.TWO,
        111,
        "alle"
    };
    List<Object[]> testParams = new LinkedList<Object[]>();
    for(int i=0; i<codecs.length; i++) {
      Object[] constructorParams =
          new Object[] {codecs[i], valuesToConvert[i]};
      testParams.add(constructorParams);
    }
    return testParams;
  }
  
  public AllCodecsTest(ParamCodec<?> codec, Object valueToConvert) {
    this.codec = codec;
    this.value = valueToConvert;
  }
  
  @Test
  public void nullToStringTest() {    
    String whatisit = null;
    try {
      whatisit = codec.asString(null);
    } catch (NullPointerException e) {
      // this is ok for null as input
      assertEquals(NullPointerException.class, e.getClass());
      return;
    }
    System.out.println("got some whatisit="+whatisit+" for "+codec.getClass());
    assertNotNull(whatisit);
    assertNull(codec.get(whatisit));
  }
  
  @Test
  public void roundTripTest() {
    @SuppressWarnings("unchecked")
    ParamCodec<Object> pc = (ParamCodec<Object>)codec;
    String o = pc.asString(value);
    assertEquals(value, pc.get(o));
    
  }
}
