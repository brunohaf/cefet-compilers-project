package SyntacticAnalyzer;

import java.util.ArrayList;

import Models.Tag;
import Models.Token;

public class SyntaticAnalyzer {
    private ArrayList<Token> tokenList;
    private int currentTokenIndex = 0;

    public SyntaticAnalyzer(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    private Token nextToken() {
        currentTokenIndex++;
        if(currentTokenIndex <= tokenList.size()) {
            return tokenList.get(currentTokenIndex);
        }
        return new Token(Tag.EOF);
    }

    public void scanCode() throws Exception{

      Validator validator = new Validator(tokenList);

      validator.validateInitStop();
      nextToken();

      do {
        Token currentToken = nextToken();

        if(currentToken.tag == Tag.READ) {
            currentTokenIndex = validator.validateRead(currentTokenIndex);
        }

        else if(currentToken.tag == Tag.WRITE) {
            currentTokenIndex = validator.validateWrite(currentTokenIndex);
        }

        else if(currentToken.tag == Tag.IF) {
            currentTokenIndex = validator.validateIF(currentTokenIndex);
        }

        else if(currentToken.tag == Tag.ELSE) {
            currentTokenIndex = validator.validateElse(currentTokenIndex);
        }

        else if(currentToken.tag == Tag.DO) {
            currentTokenIndex = validator.validateDo(currentTokenIndex);
        }

        else if(currentToken.tag == Tag.WHILE) {
            currentTokenIndex = validator.validateWhile(currentTokenIndex);
        }
        
      }
      while(currentTokenIndex < tokenList.size())

      System.out.println("Deu bom");
      // for(int i = 0 ; i<this.tokenList.size(); i++){

      // }
    }
}
