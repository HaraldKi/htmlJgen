package de.pifpafpuf.web.urlparam;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.pifpafpuf.web.urlparam.IntegerCodec;
import de.pifpafpuf.web.urlparam.ParamManager;
import de.pifpafpuf.web.urlparam.UrlParam;

@SuppressWarnings("deprecation")
public class ParamManagerTest {
  private static String BASEURL = "http://bla.dum/start";
  private UrlParam<Integer> count;

  @Before
  public void setup() {
    count = new UrlParam<Integer>("count", 17, IntegerCodec.INSTANCE);
  }
  @Test
  public void basicTest() {
    ParamManager pm = new ParamManager(BASEURL);
    pm.add(count, "111");
    String url = pm.getUrl(Collections.<UrlParam<?>>emptySet(),
                           Collections.<UrlParam<?>>emptyList());
    assertEquals(BASEURL+"?count=111", url);
  }
  @Test
  public void addWrongTest() {
    ParamManager pm = new ParamManager(BASEURL);
    UrlParam<Integer> countOrig = pm.add(count, "no integer");
    String url = pm.getUrl(Collections.<UrlParam<?>>emptySet(),
                           Collections.<UrlParam<?>>emptyList());
    assertTrue(countOrig==count);
    assertEquals(BASEURL+"?count=17", url);
  }
  @Test
  public void replaceTest() {
    ParamManager pm = new ParamManager(BASEURL);
    UrlParam<Integer> p512 = pm.add(count, "512");

    String url = pm.getUrl(p512, count.fromValue(20));
    assertEquals(BASEURL+"?count=20", url);
  }

  @Test
  public void replaceWrongTest() {
    ParamManager pm = new ParamManager(BASEURL);
    UrlParam<Integer> freshCount = pm.add(count, "no integer");

    String url = pm.getUrl(freshCount, count.fromValue(25));
    assertEquals(BASEURL+"?count=25", url);
  }

  @Test
  public void addNullTest() {
    ParamManager pm = new ParamManager(BASEURL);
    UrlParam<Integer> countOrig = pm.add(count, null);
    String url = pm.getUrl(Collections.<UrlParam<?>>emptySet(),
                           Collections.<UrlParam<?>>emptyList());
    assertTrue(countOrig==count);
    assertEquals(BASEURL+"?count=17", url);
  }
  @Test
  public void addAllTest() {
    String[] counts = {"1", "111", "1112"};
    ParamManager pm = new ParamManager(BASEURL);
    List<UrlParam<Integer>> params = pm.addAll(count, counts);
    Set<UrlParam<?>> drop = new HashSet<UrlParam<?>>();
    drop.addAll(params);
    String newUrl = pm.getUrl(drop, Collections.<UrlParam<?>>emptyList());

    assertEquals(BASEURL, newUrl);

    newUrl = pm.getUrlWithOut(params.get(1));
    assertEquals(BASEURL+"?count=1&count=1112", newUrl);
  }
  @Test
  public void addAllWithWrongValuesTest() {
    String[] counts = {"A", "B"};
    ParamManager pm = new ParamManager(BASEURL);
    UrlParam<Integer> nullTemplate = count.fromValue(null);
    List<UrlParam<Integer>> params = pm.addAll(nullTemplate, counts);
    assertEquals(0, params.size());
    assertEquals(BASEURL, pm.getUrl());

    params = pm.addAll(count, counts);
    assertEquals(1, params.size());
    assertTrue(params.get(0)==count);
  }
  @Test
  public void withTest() {
    ParamManager pm = new ParamManager(BASEURL);
    assertEquals(BASEURL+"?count=-13",
                 pm.getUrlWith(count.fromValue(-13)));
  }
  @Test
  public void addAllWithNullTest() {
    ParamManager pm = new ParamManager(BASEURL);
    pm.addAll(count, null);
    assertEquals(BASEURL, pm.getUrl(Collections.<UrlParam<?>>emptySet(),
                                    Collections.<UrlParam<?>>emptyList()));
  }
  @Test
  public void addAllNonsenseTest() {
    ParamManager pm = new ParamManager(BASEURL);
    String[] nonsense = {"noint1", "noint2"};
    pm.addAll(count, nonsense);
    assertEquals(BASEURL+"?count=17", pm.getUrl());
  }
  @Test
  public void clientTest() {
    ParamManager client = new ParamManager("unused");
    client.add(count, "4711");
    ParamManager pm = new ParamManager(BASEURL, client);
    assertEquals(BASEURL+"?count=4711", pm.getUrl());
  }
}
