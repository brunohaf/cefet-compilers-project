package Models;

public class Token {
    public final int tag; // constante que representa o token
    public int line; // linha em que o token se encontra;

    public Token(int t, int line) {
        this.tag = t;
        this.line = line;
    }

    public Token(int t) {
        this.tag = t;
        this.line = -1;
    }

    public String toString() {
        return "" + (char)this.tag;
    }
}