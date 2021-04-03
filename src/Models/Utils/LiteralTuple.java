package Models.Utils;

public class LiteralTuple{

  public String string;
  public int lastLiteralQuoteIndex;

  public LiteralTuple(String string, int lastLiteralQuoteIndex) {
    this.string = string;
    this.lastLiteralQuoteIndex = lastLiteralQuoteIndex;
  }
}