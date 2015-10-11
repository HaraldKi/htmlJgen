package de.pifpafpuf;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.io.IOException;

import org.junit.Test;

import de.pifpafpuf.web.html.Html;
import de.pifpafpuf.web.html.HtmlPage;

public class HtmlPageTest {
  @Test
  public void pageBasicTest() throws IOException {
    HtmlPage page = new HtmlPage("a title for the page");
    String expected = "<!DOCTYPE html>\n" +
        "<html>\n"+
        "  <head>\n"+
        "    <title>a title for the page</title>\n"+
        "  </head>\n"+
        "  <body>\n"+
        "  </body>\n"+
        "</html>";
    assertEquals(expected, page.toString());
    
    StringBuilder sb = new StringBuilder();
    page.print(sb);
    assertEquals(expected, sb.toString());
  }
  
  @Test
  public void pageCssTest() {
    HtmlPage page = new HtmlPage("bla");
    page.addCss("halligalli.css");
    String text = page.toString();
    assertThat(text, containsString("<link "));
    assertThat(text, containsString(" rel=\"stylesheet\""));
    assertThat(text, containsString(" type=\"text/css\""));
    assertThat(text, containsString(" href=\"halligalli.css\""));
  }

    @Test
  public void pageJsTest() {
    HtmlPage page = new HtmlPage("bla");
    page.addJs("application.js");
    String text = page.toString();
    assertThat(text, containsString("<script "));
    assertThat(text, containsString("</script>"));
    assertThat(text, containsString(" type=\"text/javascript\""));
    assertThat(text, containsString(" src=\"application.js\""));
  }

  @Test 
  public void pageBodyTest() {
    HtmlPage page = new HtmlPage("bla");
    Html content = new Html("div").addText("hallo").setTight();
    page.addContent(content);
    String text = page.toString();
    String expected = "<body>\n    "+content+"\n  </body>";
    assertThat(text, containsString(expected));
  }
  @Test
  public void pageAddMetaTest() {
    HtmlPage page = new HtmlPage("bla");
    page.addMeta("content-type", "text-html");
    String text = page.toString();
    assertThat(text, containsString("<meta "));
    assertThat(text, containsString("name=\"content-type\""));
    assertThat(text, containsString("content=\"text-html\""));
  }
                                 
}
