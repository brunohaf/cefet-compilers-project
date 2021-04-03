package Models;

public class Word extends Token {
  private String lexeme = "";

  public Word(String s, int tag, int line) {
    super(tag, line);
    lexeme = s;
  }

  public Word(String s, int tag) {
    super(tag);
    lexeme = s;
  }

  public String getLexeme() {
    return this.lexeme;
  }

  public String toString() {
    return "" + lexeme;
  }
}