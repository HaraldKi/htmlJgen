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
  private final List<BodyJsUrl> bodyJsUrls = new ArrayList<>(5);

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
   * Adss a type="text/javascript" link to the page head.
   */
  public void addJs(String url) {
    addJs(head, url, false);
  }

  /**
   * add a Javascript or Javascript module link to the page head.
   */
  public void addJs(String url, boolean isModule) {
    addJs(head, url, isModule);
  }

  /**
   * Adds a javascript script element to a given element.
   */
  public static void addJs(Html elem, String url, boolean isModule) {
    elem.add("script")
    .setAttr("type", isModule ? "module" : "text/javascript")
    .setAttr("src", url);
  }

  /**
   * register a javascript url to be added to the body element just before
   * calling {@link #print(Appendable)
   */
  public void registerBodyJs(String url) {
    bodyJsUrls.add(new BodyJsUrl(url, false));
  }

  public void registerBodyJs(String url, boolean isModule) {
    bodyJsUrls.add(new BodyJsUrl(url, isModule));
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
    for(BodyJsUrl jsUrl: bodyJsUrls) {
      addJs(body, jsUrl.url, jsUrl.isModule);
    }
    out.append("<!DOCTYPE html>\n");
    html.print(out, indent);
  }
  /*+******************************************************************/
  @Override
  public String toString() {
    return TextUtils.toString(this);
  }

  private static class BodyJsUrl {
    public final String url;
    public final boolean isModule;
    public BodyJsUrl(String url, boolean isModule) {
      this.url = url;
      this.isModule = isModule;

    }
  }
}
