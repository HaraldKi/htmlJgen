package de.pifpafpuf.web.html;

import java.io.IOException;

/**
 * to be used as a root node for an HTML page tree.
 */
public class HtmlPage implements Stringable {
  private final Html html = new Html("html");
  private final Html head = html.add("head");
  private final Html body = html.add("body");
  /*+******************************************************************/
  public HtmlPage(String title) {
    head.addTight("title").addText(title);
  }
  /*+******************************************************************/
  public void addLink(String type, String href, String rel) {
    EmptyElem link = new EmptyElem("link")
    .setAttr("type", type)
    .setAttr("rel", rel)
    .setAttr("href", href)
    ;
    
    head.add(link);
  }
  public void addCss(String href) {
    addLink("text/css", href, "stylesheet");
  }
  public void addJs(String url) {
    head.add("script")
    .setAttr("type", "text/javascript")
    .setAttr("src", url);
  }
  public void addMeta(String name, String content) {
    EmptyElem meta = new EmptyElem("meta")
    .setAttr("name", name)
    .setAttr("content", content);
    
    head.add(meta);
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
    out.append("<!doctype html>\n");
    html.print(out, indent);
  }
  /*+******************************************************************/
  @Override
  public String toString() {
    return TextUtils.toString(this);
  }
}
