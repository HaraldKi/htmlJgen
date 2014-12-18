package de.pifpafpuf.web.urlparam;

public interface Acceptor<T> {
  boolean accept(T t);
}
