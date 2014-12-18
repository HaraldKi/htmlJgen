package de.pifpafpuf.web.urlparam;

public class IntegerCodec implements ParamCodec<Integer> {
  public static final IntegerCodec INSTANCE = 
      new IntegerCodec(AcceptAll.INSTANCE1);
  private Acceptor<Integer> acceptor;
  /*+******************************************************************/
  public IntegerCodec(Acceptor<Integer> accept) {
    this.acceptor = accept;
  }
  /*+******************************************************************/
  public IntegerCodec(int min, int max) {
    this(new RangeAcceptor(min, max));
  }
  /*+******************************************************************/
  @Override
  public Integer get(String value) {
    int result = 0;
    try {
      result = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return null;
    }
    return acceptor.accept(result) ? result : null; 
  }

  @Override
  public String asString(Integer e) {
    return e.toString();
  }
  /*+******************************************************************/
  private static enum AcceptAll implements Acceptor<Integer> {
    INSTANCE1;

    @Override
    public boolean accept(Integer arg0) {
      return Boolean.TRUE;
    }
  }
  /*+******************************************************************/
  private static final class RangeAcceptor implements Acceptor<Integer> {
    private final int min;
    private final int max;
    public RangeAcceptor(int min, int max) {
      this.min = min;
      this.max = max;
    }
    @Override
    public boolean accept(Integer value) {
      return value>=min && value<=max;
    }
    
  }
  /*+******************************************************************/
}
