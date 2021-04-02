package SyntacticAnalyzer;

import java.util.ArrayList;

import Exceptions.InvalidSyntaxException;
import Models.Tag;
import Models.Token;

public class Validator {
  private ArrayList<Token> tokenList;

  public Validator(ArrayList<Token> tokenList){
    this.tokenList = tokenList;
  }

  public void validateInitStop() throws InvalidSyntaxException {
      int firstToken = tokenList.get(0).tag;
      int lastToken = tokenList.get(tokenList.size() -1 ).tag;
      if(firstToken != Tag.INIT || lastToken != Tag.STOP){
        throw new InvalidSyntaxException("Code doesnt start with 'INIT' or doesnt finish with 'STOP'");
      }
  }
}