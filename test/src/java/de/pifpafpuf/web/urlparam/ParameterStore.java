package de.pifpafpuf.web.urlparam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterStore {
  private final Map<String, List<String>> params = new HashMap<>();

  public void setParameter(String name, String... values) {
    params.put(name, Arrays.asList(values));
  }

  public List<String> getParameterValues(String name) {
    return params.get(name);
  }
}