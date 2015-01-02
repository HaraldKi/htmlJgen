package de.pifpafpuf.web.html;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * translates text strings into replacemend strings and wraps them into a
 * {@link HtmlText} objects. Instead of using code like:
 * </p>
 *
 * <pre>new Html(&quot;p&quot;).addText(&quot;hello&quot;);</pre>
 *
 * <p>
 * you now use
 * </p>
 *
 * <pre>Translator lang = new Translator(); // and fill it with data
 *...
 *new Html(&quot;p&quot;).add(lang.trans(&quot;hello&quot;));
 * </pre>
 *
 * <p>
 * The assumption is that the translator <code>lang</code> was previously
 * selected depending on the user's language settings.
 * </p>
 *
 * <p>
 * Why do we not use {@link java.util.ResourceBundle}? Well, it throws mean
 * things at us if a key has no value, and there is no way to specify default
 * return values.
 * </p>
 */
public class Translator {
  private final Map<String,String> translations = new HashMap<>();
  /**
   * creates an empty instance with no translations.
   */
  public Translator() {};
  
  /**
   * adds a translation.
   */
  public void add(String text, String translation) {
    translations.put(text, translation);
  }

  /**
   * <p>prepare an {@link HtmlText} with the translation of <code>text</code>.
   * If no translation is available, <code>text</code> is used untranslated.</p>
   */
  public HtmlText trans(String text) {
    return trans(text, text);
  }

  /**
   * prepare an {@link HtmlText} with the translation of <code>text</code>.
   * If no translation is available, the <code>defaultText</code> is used.
   */
  public HtmlText trans(String text, String defaultText) {
    String translated = translations.get(text);

    if (translated==null) {
      return HtmlText.fromRaw(defaultText);
    } else {
      return HtmlText.fromRaw(translated);
    }

  }
}
