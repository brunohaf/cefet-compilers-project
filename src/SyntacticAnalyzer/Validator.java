package SyntacticAnalyzer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.InvalidSyntaxException;
import Models.Tag;
import Models.Token;
import Models.Word;
import Models.Utils.Tuple;

public class Validator {

  private ArrayList<Token> tokenList;
  private String INIT_ERROR_MESSAGE = "Code doesnt start with 'INIT'.";
  private String STOP_ERROR_MESSAGE = "Code doesnt finish with 'STOP'.";
  private String PARENTHESES_ERROR_MESSAGE = "Error on parentheses condition.";
  private String BEGIN_ERROR_MESSAGE = "If statement not followed by BEGIN.";
  private String NO_SEMICOLON_ERROR_MESSAGE = "Statement has no SEMICOLON.";
  private String SEMICOLON_NOT_IN_THE_SAME_LINE_ERROR_MESSAGE = "SEMICOLON is missing.";
  private String ATTRIB_WITHOUT_LITERAL_DIGIT_ID_ERROR_MESSAGE = "Attribution not followed by a LITERAL, IDENTIFIER or DIGITs.";
  private String INVALID_IS_ATTRIB_ERROR_MESSAGE = " 'IS' Attribution must be of type INTEGER, REAL or STRING.";
  private String INVALID_ID_ATTRIB_ERROR_MESSAGE = " 'IDENTIFIER' Attribution not ending with SEMICOLON";
  private String INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE = "Attritution syntax error.";
  private String COMMA_WITHOUT_IDENTIFIER_ERROR_MESSAGE = "Comma not followed by IDENTIFIER on attribution.";
  private String LITERAL_WITHOUT_QUOTE_END_ERROR_MESSAGE = "LITERAL does not end with '\"'.";
  private String UNCLOSED_PARENTHESES_ERROR_MESSAGE = "A parentheses was open but not closed in the same line or was never closed.";
  private String FACTORA_END_ERROR_MESSAGE = "FACTOR-A does not contain a FACTOR, NOT FACTOR or -FACTOR.";
  private String INVALID_TERM_ERROR_MESSAGE = "Invalid TERM expression.";
  private String TERM_WITH_NO_IDENTIFIER_ERROR_MESSAGE = "TERM with no-IDENTIFIER.";
  private String SIMPLEEXPR_WITH_NO_FACTORA_ERROR_MESSAGE = "SIMPLE-EXPRESSION with no-FACTOR-A.";
  private String INVALID_SIMPLEEXPR_ERROR_MESSAGE = "Invalid SIMPLE-EXPRESSION.";
  private String EXPRESSION_WITH_NO_SIMPLEEXPR_ERROR_MESSAGE = "EXPRESSION with no-SIMPLE-EXPRESSION.";
  private String INVALID_EXPRESSION_ERROR_MESSAGE = "Invalid EXPRESSION expression.";

  public Validator(ArrayList<Token> tokenList) {
    this.tokenList = tokenList;
  }

  public void validateInitStop() throws InvalidSyntaxException {
    Token firstToken = tokenList.get(0);
    Token lastToken = tokenList.get(tokenList.size() - 1);
    if (firstToken.tag != Tag.INIT) {
      throw new InvalidSyntaxException(INIT_ERROR_MESSAGE, firstToken.line);
    } else if (lastToken.tag != Tag.STOP) {
      throw new InvalidSyntaxException(STOP_ERROR_MESSAGE, lastToken.line);
    }
  }

  // ----------------------------------------WORK IN PROGRESS
  //
  // public int validateIf(int index) throws InvalidSyntaxException {
  // int nextIndex = validateParentheses(index + 1);
  // Token token = tokenList.get(nextIndex);
  // if (token.tag != Tag.BEG) {
  // throw new InvalidSyntaxException(BEGIN_ERROR_MESSAGE, token.line);
  // }
  // nextIndex++;
  // for (; (nextIndex < tokenList.size() - 1 || token.tag == Tag.END);
  // nextIndex++) {
  // token = tokenList.get(nextIndex);
  // if (token.tag == Tag.IF) {
  // nextIndex = validateIf(nextIndex);
  // } else if (token.tag == Tag.ELSE) {
  // nextIndex = validateElse(nextIndex);
  // } else if (token.tag == Tag.DO) {
  // nextIndex = validateElse(nextIndex);
  // } else if (token.tag == Tag.WHILE) {
  // nextIndex = validateElse(nextIndex);
  // }
  // }

  // return nextIndex;
  // }

  public int validateIf(int index) throws InvalidSyntaxException {
    int nextIndex = index + 1;
    return validateExpression(nextIndex).key;
  }

  public int validateElse(int index) throws InvalidSyntaxException {
    int nextIndex = index;
    return nextIndex;
  }

  public int validateDo(int index) throws InvalidSyntaxException {
    int nextIndex = index;
    return nextIndex;
  }

  public int validateWhile(int index) throws InvalidSyntaxException {
    int nextIndex = index;
    return nextIndex;
  }

  public int validateRead(int index) throws InvalidSyntaxException {
    int nextIndex = index;
    return nextIndex;
  }

  public int validateWrite(int index) throws InvalidSyntaxException {
    int nextIndex = index;
    return nextIndex;
  }

  // public int validateStatement(int index) throws InvalidSyntaxException {
  // int semicolonIndex = indexOfToken(index, Tag.SEMICOLON);
  // if (semicolonIndex == -1) {
  // throw new InvalidSyntaxException(NO_SEMICOLON_ERROR_MESSAGE,
  // tokenList.get(index).line);
  // } else {
  // ArrayList<Token> statementTokenList = new
  // ArrayList<Token>(tokenList.subList(index, semicolonIndex));

  // if (!isSameLineToken(statementTokenList)) {
  // throw new
  // InvalidSyntaxException(SEMICOLON_NOT_IN_THE_SAME_LINE_ERROR_MESSAGE,
  // tokenList.get(index).line);
  // }

  // for (int i = 0; i < statementTokenList.size() - 1; i++) {
  // Token token = statementTokenList.get(i);
  // try {
  // Token nextToken = statementTokenList.get(i + 1);
  // switch (token.tag) {
  // case Tag.ATTRIB:
  // Tuple<String, Integer> literalStr = null;

  // if (nextToken.tag == Tag.QUOTE) {
  // literalStr = buildLiteralString(statementTokenList, i + 1);
  // }

  // boolean literalCondition = literalStr == null ? false :
  // strIsLiteral(literalStr.key);
  // if (!literalCondition && !isLiteral(nextToken) && !isDigit(nextToken) &&
  // !isIdentifier(nextToken)) {
  // throw new
  // InvalidSyntaxException(ATTRIB_WITHOUT_LITERAL_DIGIT_ID_ERROR_MESSAGE,
  // token.line);
  // } else if (literalCondition) {
  // i = literalStr.value;
  // } else if (isIdentifier(nextToken)) {
  // try {
  // if (nextToken != statementTokenList.get(statementTokenList.size() - 1)) {
  // throw new InvalidSyntaxException(INVALID_ID_ATTRIB_ERROR_MESSAGE,
  // token.line);
  // }
  // } catch (IndexOutOfBoundsException exception) {
  // throw new InvalidSyntaxException(INVALID_ID_ATTRIB_ERROR_MESSAGE,
  // token.line);
  // }
  // }
  // break;
  // case Tag.COMMA:
  // if (!isIdentifier(nextToken)) {
  // throw new InvalidSyntaxException(COMMA_WITHOUT_IDENTIFIER_ERROR_MESSAGE,
  // token.line);
  // }
  // break;
  // case Tag.IS:
  // if (!isType(nextToken)) {
  // throw new InvalidSyntaxException(INVALID_IS_ATTRIB_ERROR_MESSAGE,
  // token.line);
  // }
  // break;
  // default:
  // if (!isIdentifier(token)) {
  // throw new InvalidSyntaxException(INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE,
  // token.line);
  // }
  // break;
  // }
  // } catch (ArrayIndexOutOfBoundsException e) {
  // throw new InvalidSyntaxException(INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE,
  // token.line);
  // }
  // }
  // }
  // return semicolonIndex;
  // }

  public void validateOperators() throws InvalidSyntaxException {

  }

  public Tuple<String, Integer> buildLiteralString(ArrayList<Token> statementList, int index)
      throws InvalidSyntaxException {
    String literalString = "\"";
    int lastIndex = index;
    for (int i = index + 1; i < statementList.size() && statementList.get(i).tag != Tag.QUOTE; i++, lastIndex++) {
      literalString += ((Word) statementList.get(i)).toString() + ' ';
    }
    literalString += "\"";
    Token lastToken = statementList.get(lastIndex + 1);
    if (lastToken.tag != Tag.QUOTE) {
      throw new InvalidSyntaxException(LITERAL_WITHOUT_QUOTE_END_ERROR_MESSAGE, lastToken.line);
    }
    return new Tuple<String, Integer>(literalString, lastIndex + 1);
  }

  public int indexOfToken(int startIndex, int tag) {
    for (int i = startIndex; i < tokenList.size() - 1; i++)
      if (tokenList.get(i).tag == tag) {
        return i;
      }
    return -1;
  }

  public boolean isSameLineToken(ArrayList<Token> list) {
    return list.get(0).line == list.get(list.size() - 1).line;
  }

  private boolean validatePattern(String str, String patternString) {
    Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(str);
    return matcher.find();
  }

  private boolean isType(Token token) {
    return token.tag == Tag.STRING || token.tag == Tag.INT || token.tag == Tag.REAL;
  }

  // caractere := um dos 256 caracteres do conjunto ASCII, exceto as aspas e
  // quebra de linha.
  private boolean isCharacter(Token token) {
    char[] charList = token.toString().toCharArray();
    for (int i = 0; i < charList.length; i++) {
      char character = charList[i];
      if (!(0 < character && character < 255)) {
        return false;
      }
    }
    return true;
  }

  // expression := simple-expr | simple-expr relop simple-expr
  //  simple-expr :=      term | simple-expr addop term
  private Tuple<Integer, Boolean> validateExpression(int index) throws InvalidSyntaxException {
    int termIndex = index + 1;
    if (!validateSimpleExpr(index).value || termIndex > tokenList.size()) {
      throw new InvalidSyntaxException(EXPRESSION_WITH_NO_SIMPLEEXPR_ERROR_MESSAGE, tokenList.get(index).line);
    }
    if (tokenList.get(termIndex).tag != Tag.SEMICOLON) {
      if (validateRelOp(tokenList.get(termIndex))) {
        Tuple<Integer, Boolean> isSimpleExpr = validateSimpleExpr(termIndex + 1);
        return new Tuple<Integer, Boolean>(termIndex, isSimpleExpr.value);
      } else {
        throw new InvalidSyntaxException(INVALID_EXPRESSION_ERROR_MESSAGE, tokenList.get(termIndex).line);
      }
    } else {
      Tuple<Integer, Boolean> isSimpleExpr = validateSimpleExpr(index);
      return isSimpleExpr;
    }
  }

  // simple-expr := term | simple-expr addop term
  private Tuple<Integer, Boolean> validateSimpleExpr(int index) throws InvalidSyntaxException {
    int termIndex = index + 1;
    if (!validateFactorA(index).value || termIndex > tokenList.size()) {
      throw new InvalidSyntaxException(SIMPLEEXPR_WITH_NO_FACTORA_ERROR_MESSAGE, tokenList.get(index).line);
    }
    if (tokenList.get(termIndex).tag != Tag.SEMICOLON) {
      if (validateAddOp(tokenList.get(termIndex))) {
        Tuple<Integer, Boolean> isSimpleExpr = validateSimpleExpr(termIndex + 1);
        return new Tuple<Integer, Boolean>(termIndex, isSimpleExpr.value);
      } else {
        throw new InvalidSyntaxException(INVALID_SIMPLEEXPR_ERROR_MESSAGE, tokenList.get(termIndex).line);
      }
    } else {
      Tuple<Integer, Boolean> isTerm = validateTerm(index);
      return isTerm;
    }
  }

  // term := factorA | term mulop factorA
  private Tuple<Integer, Boolean> validateTerm(int index) throws InvalidSyntaxException {
    int termIndex = index + 1;
    if (!validateFactorA(index).value || termIndex > tokenList.size()) {
      throw new InvalidSyntaxException(TERM_WITH_NO_IDENTIFIER_ERROR_MESSAGE, tokenList.get(index).line);
    }
    if (tokenList.get(termIndex).tag != Tag.SEMICOLON) {
      if (validateMulOp(tokenList.get(termIndex))) {
        Tuple<Integer, Boolean> isTerm = validateTerm(termIndex + 1);
        return new Tuple<Integer, Boolean>(termIndex, isTerm.value);
      } else {
        throw new InvalidSyntaxException(INVALID_TERM_ERROR_MESSAGE, tokenList.get(termIndex).line);
      }
    } else {
      Tuple<Integer, Boolean> isFactorA = validateFactorA(index);
      return isFactorA;
    }
  }

  // factor-a := factor | not factor | "-" factor
  private Tuple<Integer, Boolean> validateFactorA(int index) throws InvalidSyntaxException {
    Token token = tokenList.get(index);
    String lexem = token.toString();
    if (lexem == "-" || lexem == "not") {
      index++;
    }
    Tuple<Integer, Boolean> isFactor = validateFactor(index);
    if (lexem != "-" && lexem != "not" && !isFactor.value) {
      throw new InvalidSyntaxException(FACTORA_END_ERROR_MESSAGE, token.line);
    } else {
      return isFactor;
    }
  }

  // factor := identifier | constant | "(" expression ")"
  private Tuple<Integer, Boolean> validateFactor(int index) throws InvalidSyntaxException {
    Token token = tokenList.get(index);
    boolean assertion = false;
    if (token.tag != Tag.OPEN_PARENTHESES) {
      assertion = isIdentifier(token) || isConst(token);
      return new Tuple<Integer, Boolean>(index + 1, assertion);
    } else {
      Tuple<Integer, ArrayList<Token>> expressionCadidate = buildParenthesesExpression(index);
      assertion = validateExpression(index + 1).value && validateParentheses(expressionCadidate.value);
      return new Tuple<Integer, Boolean>(expressionCadidate.key, assertion);
    }
  }

  private boolean validateParentheses(ArrayList<Token> expressionCadidate) throws InvalidSyntaxException {
    boolean parenthesesAreOnTheSameLine = isSameLineToken(expressionCadidate);
    if (parenthesesAreOnTheSameLine) {
      return parenthesesAreOnTheSameLine;
    } else {
      throw new InvalidSyntaxException(UNCLOSED_PARENTHESES_ERROR_MESSAGE, expressionCadidate.get(0).line);
    }
  }

  public Tuple<Integer, ArrayList<Token>> buildParenthesesExpression(int index) throws InvalidSyntaxException {
    Token firstParentheses = tokenList.get(index);
    for (int i = index; i < tokenList.size() - 1; i++) {
      Token token = tokenList.get(i);
      if (token.line == firstParentheses.line && token.tag == Tag.CLOSE_PARENTHESES) {
        return new Tuple<Integer, ArrayList<Token>>(i, new ArrayList<Token>(tokenList.subList(index, i)));
      }
    }
    throw new InvalidSyntaxException(UNCLOSED_PARENTHESES_ERROR_MESSAGE, firstParentheses.line);
  }

  // relop := "=" | ">" | ">=" | "<" | "<=" | "<>"
  private boolean validateRelOp(Token token) {
    String lexem = token.toString();
    return lexem == "=" || lexem == ">" || lexem == ">=" || lexem == "<" || lexem == "<=" || lexem == "<>";
  }

  // mulop := "*" | "/" | and
  private boolean validateMulOp(Token token) {
    String lexem = token.toString();
    return lexem == "*" || lexem == "/" || lexem == "and";
  }

  // addop := "+" | "-" | or
  private boolean validateAddOp(Token token) {
    String lexem = token.toString();
    return lexem == "+" || lexem == "-" || lexem == "or";
  }

  // constant := integer_const | litera
  private boolean isConst(Token token) {
    return isIntegerConst(token) || isLiteral(token);
  }

  // nteger_const := nonzero {digit} | “0”
  private boolean isIntegerConst(Token token) {
    return isNonZeroDigit(token) || token.toString() == "0";
  }

  // real_const := interger_const "." digit+
  private boolean isRealConst(Token token) {
    String lexem = ((Word) token).toString();
    String[] splittedLexem = lexem.split(".");
    return isIntegerConst(new Word(splittedLexem[0], Tag.REAL)) && isDigit(new Word(splittedLexem[1], Tag.NUM));
  }

  // digit := [0-9]+
  private boolean isDigit(Token token) {
    String digitPattern = "[0-9]+";
    return validatePattern(token.toString(), digitPattern);
  }

  // nonZero := [1-9]+
  private boolean isNonZeroDigit(Token token) {
    String nonZeroDigitPattern = "[1-9]+";
    return validatePattern(token.toString(), nonZeroDigitPattern);
  }

  // letter := [A-Za-z]
  private boolean isLetter(Token token) {
    String letterPattern = "[A-Za-z]+";
    return validatePattern(token.toString(), letterPattern);
  }

  // identifier := letter {letter | digit | " _ " }
  private boolean isIdentifier(Token token) {
    String letterPattern = "(^(^[A-Za-z]([A-Za-z0-9]|_)*)+$)";
    return validatePattern(token.toString(), letterPattern);
  }

  // literal := " “ " caractere* " ” "
  private boolean isLiteral(Token token) {
    return strIsLiteral(token.toString());
  }

  private boolean strIsLiteral(String str) {
    String letterPattern = "(^\"([A-Za-z]|\s|[0-9])*\"$)";
    return validatePattern(str, letterPattern);
  }
}