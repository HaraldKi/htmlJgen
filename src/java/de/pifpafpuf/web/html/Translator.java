package de.pifpafpuf.web.html;

import java.util.HashMap;
import java.util.Map;

public class Translator {
  private final Map<String,String> translations = new HashMap<>();
  
  public void add(String text, String translation) {
    translations.put(text, translation);
  }
  
  public HtmlText get(String text) {
    String translated = translations.get(text);
    
    if (translated==null) {
      return HtmlText.fromRaw(text);
    } else {
      return HtmlText.fromRaw(translated);
    }
  }
}
