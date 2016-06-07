package de.pifpafpuf.web.urlparam;

public class LongCodec implements ParamCodec<Long> {
  public static final LongCodec INSTANCE = 
      new LongCodec(AcceptAll.INSTANCE);
  private Acceptor<Long> acceptor;
  /*+******************************************************************/
  public LongCodec(Acceptor<Long> accept) {
    this.acceptor = accept;
  }
  /*+******************************************************************/
  public LongCodec(long min, long max) {
    this(new RangeAcceptor(min, max));
  }
  /*+******************************************************************/
  @Override
  public Long get(String value) {
    long result = 0;
    try {
      result = Long.parseLong(value);
    } catch (NumberFormatException e) {
      return null;
    }
    return acceptor.accept(result) ? result : null; 
  }

  @Override
  public String asString(Long e) {
    return e.toString();
  }
  /*+******************************************************************/
  private static enum AcceptAll implements Acceptor<Long> {
    INSTANCE;

    @Override
    public boolean accept(Long arg0) {
      return Boolean.TRUE;
    }
  }
  /*+******************************************************************/
  private static final class RangeAcceptor implements Acceptor<Long> {
    private final long min;
    private final long max;
    public RangeAcceptor(long min, long max) {
      this.min = min;
      this.max = max;
    }
    @Override
    public boolean accept(Long value) {
      return value>=min && value<=max;
    }
    
  }
  /*+******************************************************************/
}
