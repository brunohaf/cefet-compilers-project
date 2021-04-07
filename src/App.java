import java.io.FileNotFoundException;
import java.util.ArrayList;

import LexicalAnalyzer.Lexer;
import SyntacticAnalyzer.SyntaticAnalyzer;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            Lexer lexer = new Lexer("../cefet-compilers-project/src/testCode");
            ArrayList tokenList = lexer.getTokenList();

            SyntaticAnalyzer syntaticAnalyzer = new SyntaticAnalyzer(tokenList);
            syntaticAnalyzer.scanCode();
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found");
        }
      }
}
