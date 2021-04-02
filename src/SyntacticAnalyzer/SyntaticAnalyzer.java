package SyntacticAnalyzer;

import java.util.ArrayList;

import Models.Token;

public class SyntaticAnalyzer {
    private ArrayList<Token> tokenList;
    private int currentTokenIndex = 0;

    public SyntaticAnalyzer(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    private Token nextToken() {
        currentTokenIndex++;
        return tokenList.get(currentTokenIndex);
    }

    public void scanCode() throws Exception{
      int currentTokent = 0;

      Validator validator = new Validator(tokenList);
      validator.validateInitStop();

      System.out.println("Deu bom");
      // for(int i = 0 ; i<this.tokenList.size(); i++){

      // }
    }
}
