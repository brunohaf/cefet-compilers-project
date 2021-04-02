package SyntacticAnalyzer;

import java.util.ArrayList;

import Exceptions.InvalidSyntaxException;
import Models.Tag;
import Models.Token;

public class Validator {

  private ArrayList<Token> tokenList;
  private String INIT_STOP_ERROR_MESSAGE = "Code doesnt start with 'INIT' or doesnt finish with 'STOP'";
  private String PARENTHESES_ERROR_MESSAGE = "Error on parentheses condition";
  private String BEGIN_ERROR_MESSAGE = "If statement not followed by BEGIN";

  public Validator(ArrayList<Token> tokenList) {
    this.tokenList = tokenList;
  }

  public void validateInitStop() throws InvalidSyntaxException {
    int firstToken = tokenList.get(0).tag;
    int lastToken = tokenList.get(tokenList.size() - 1).tag;
    if (firstToken != Tag.INIT || lastToken != Tag.STOP) {
      throw new InvalidSyntaxException(INIT_STOP_ERROR_MESSAGE);
    }
  }

  //W.I.P.
  public int validateIf(int index) throws InvalidSyntaxException {
    int nextIndex = validateParentheses(index + 1);
    if(tokenList.get(nextIndex) != TAG.BEG){
      throw new InvalidSyntaxException(BEGIN_ERROR_MESSAGE);
    }
    nextIndex++;
    for (; (nextIndex < tokenList.size() - 1 || tokenList.get(nextIndex).tag == TAG.END); nextIndex++) {
      Token token = tokenList.get(nextIndex);
      if(token.tag == Tag.IF){
        nextIndex = validateIf(nextIndex);
      }
      else if(token.tag == Tag.ELSE){
        nextIndex = validateElse(nextIndex);
      }
      else if(token.tag == Tag.DO){
        nextIndex = validateElse(nextIndex);
      }
      else if(token.tag == Tag.WHILE){
        nextIndex = validateElse(nextIndex);
      }
    }

    return nextIndex;
  }


  public int validateElse() throws InvalidSyntaxException {
    return nextIndex;
  }

  public int validateParentheses(int index) throws InvalidSyntaxException {
    return nextIndex;
  }

  public int validateDo() throws InvalidSyntaxException {
    return nextIndex;
  }

  public int validateWhile() throws InvalidSyntaxException {
    return nextIndex;
  }

  public int validateExpression() throws InvalidSyntaxException {
    return nextIndex;
  }

  public int validateRead() throws InvalidSyntaxException {
    return nextIndex;
  }

  public int validateWrite(int index) throws InvalidSyntaxException {
    int nextIndex = validateParentheses(index);
    return nextIndex;
  }

  public void validateStatement() throws InvalidSyntaxException {

  }

  public void validateOperators() throws InvalidSyntaxException {

  }

}