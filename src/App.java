import LexicalAnalyzer.Lexer;

public class App {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("testCode");
            lexer.scanEntireFile();
            lexer.getSymbolTable();
        }
        catch(Exception e) {
            System.out.println("File not found");
        }
      }
}
