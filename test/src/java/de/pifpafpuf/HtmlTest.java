package de.pifpafpuf;
import static org.junit.Assert.*;

import org.junit.Test;

import de.pifpafpuf.web.html.EmptyElem;
import de.pifpafpuf.web.html.Html;
import de.pifpafpuf.web.html.HtmlText;

public class HtmlTest {

  @Test
  public void basicTest() {
    Html div = new Html("div").addText("hallo").setTight();
    div.setAttr("class", "bla");
    String html = div.toString();
    String expected = "<div class=\"bla\">hallo</div>";
    assertEquals(expected, html);
  }
  @Test
  public void encodingTest() {
    Html div = new Html("div").addText("<hallo>").setTight();
    div.setAttr("class", "x\"x");
    String html = div.toString();
    String expected = "<div class=\"x&quot;x\">&lt;hallo&gt;</div>";
    assertEquals(expected, html);
  }
  @Test 
  public void untightTest() {
    Html div = new Html("div").addText("bla");
    String expected = "<div>\n  bla\n</div>";
    assertEquals(expected, div.toString());
  }
  @Test
  public void elemIsEmptyTest() {
    Html div = new Html("div").setTight();
    div.setAttr("some", "attr");
    String expected = "<div some=\"attr\"></div>";
    assertEquals(expected, div.toString());
  }
  @Test
  public void addTextTest() {
    Html div = new Html("div").setTight();
    div.add(HtmlText.fromRaw("bla"));
    String expected = "<div>bla</div>";
    assertEquals(expected, div.toString());
  }
  @Test
  public void numChildrenTest() {
    Html div = new Html("div");
    assertEquals(0, div.getNumChildren());
    div.add("p");
    assertEquals(1, div.getNumChildren());
    div.addText("hallo");
    assertEquals(2, div.getNumChildren());
    div.add("div");
    assertEquals(3, div.getNumChildren());
  }
  @Test
  public void addTightTest() {
    Html div = new Html("div");
    div.addTight("p").addText("hallo");
    String expected = "<div>\n  <p>hallo</p>\n</div>";
    assertEquals(expected, div.toString());
  }
  @Test
  public void addEncodedTest() {
    Html div = new Html("div").addEncoded("&lt;dong&gt;").setTight();
    String expected = "<div>&lt;dong&gt;</div>";
    assertEquals(expected, div.toString());
  }
  @Test
  public void nullTextTest() {
    try {
      HtmlText.fromRaw(null);
      fail("we should have seen an NPE, but got here instead");
    } catch (Throwable e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
  }
  @Test
  public void charEncodingsTest() {
    Html div = new Html("div").addText("a'a b&b c\nc").setTight();
    String expected = "<div>a&#39;a b&amp;b c&#10;c</div>";
    assertEquals(expected, div.toString());
  }
  
  @Test 
  public void getAttrTest() {
    EmptyElem l = new Html("link").setAttr("bla", "falsel");
    assertEquals("default", l.getAttr("zxx", "default"));
    assertEquals("falsel", l.getAttr("bla", null));
  }
}
