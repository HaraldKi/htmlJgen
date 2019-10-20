package de.pifpafpuf.web.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * to be used as a root node for an HTML page tree.
 */
public class HtmlPage implements Stringable {
  private final Html html = new Html("html");
  private final Html head = html.add("head");
  private final Html body = html.add("body");
  private final List<String> bodyJsUrls = new ArrayList<>(5);

  /*+******************************************************************/
  public HtmlPage(String title) {
    head.addTight("title").addText(title);
  }
  /*+******************************************************************/
  /**
   * <p>adds a {@code link}-element to the page header with standard attributes
   * type, href and rel. The element is returned to allow for additional
   * attributes to be added.</p>

   * @return the generated link element
   */
  public EmptyElem addLink(String type, String href, String rel) {
    EmptyElem link = new EmptyElem("link")
    .setAttr("type", type)
    .setAttr("rel", rel)
    .setAttr("href", href)
    ;
    
    head.add(link);
    return link;
  }
  public void addCss(String href) {
    addLink("text/css", href, "stylesheet");
  }

  /**
   * add a javascript link to the page head.
   */
  public void addJs(String url) {
    addJs(head, url);
  }
  
  private void addJs(Html elem, String url) {
    elem.add("script")
    .setAttr("type", "text/javascript")
    .setAttr("src", url);
  }

  /**
   * register a javascript url to be added to the body element just before
   * calling {@link #print(Appendable)
   */
  public void registerBodyJs(String url)
  {
    bodyJsUrls.add(url);
  }

  public void addMeta(String name, String content) {
    EmptyElem meta = new EmptyElem("meta")
    .setAttr("name", name)
    .setAttr("content", content);
    
    head.add(meta);
  }
  public void addHeadElem(EmptyElem elem) {
    head.add(elem);
  }
  /*+******************************************************************/
  public void addContent(EmptyElem content) {
    body.add(content);
  }
  /*+******************************************************************/
  public void print(Appendable out) throws IOException {
    print(out, 0);
  }
  /*+******************************************************************/
  @Override
  public void print(Appendable out, int indent) throws IOException {
    for(String url : bodyJsUrls) {
      addJs(body, url);
    }
    out.append("<!DOCTYPE html>\n");
    html.print(out, indent);
  }
  /*+******************************************************************/
  @Override
  public String toString() {
    return TextUtils.toString(this);
  }
}
