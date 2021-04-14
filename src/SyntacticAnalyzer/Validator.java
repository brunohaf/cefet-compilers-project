package SyntacticAnalyzer;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.InvalidSyntaxException;
import Models.Tag;
import Models.Token;
import Models.Word;
import Models.Utils.Tuple;

public class Validator {

  private static final String INVALID_IF_STATEMENT_ERROR_MESSAGE = "Invalid IF STATEMENT.";
  private static final String DO_STATEMENT_WITHOUT_WHILE_ERROR_MESSAGE = "DO STATEMENT without WHILE.";
  private static final String WHILE_NOT_FOLLOWED_BY_PARENTHESES_ERROR_MESSAGE = "WHILE not followed by '('.";
  private static final String EXPRESSION_NOT_FOLLOWED_BY_PARENTHESES_ERROR_MESSAGE = "EXPRESSION not followed by ')'.";
  private static final String INVALID_DO_STATEMENT_ERROR_MESSAGE = "Invalid DO STATEMENT.";
  private static final String INVALID_READ_STATEMENT_ERROR_MESSAGE = "Invalid READ STATEMENT.";
  private static final String INVALID_WRITE_STATEMENT_ERROR_MESSAGE = "Invalid WRITE STATEMENT.";
  private static final String IF_STATEMENT_NOT_START_WITH_PARENTHESES_ERROR_MESSAGE = "IF STATEMENT do not end with '('.";
  private static final String IF_STATEMENT_NOT_END_WITH_PARENTHESES_ERROR_MESSAGE = "IF STATEMENT do not end with ')'.";
  private static final String IF_STATEMENT_NOT_START_WITH_BEGIN_ERROR_MESSAGE = "IF STATEMENT do not start with BEGIN.";
  private static final String INIT_ERROR_MESSAGE = "Code doesnt start with 'INIT'.";
  private static final String STOP_ERROR_MESSAGE = "Code doesnt finish with 'STOP'.";
  private static final String BEGIN_ERROR_MESSAGE = "If statement not followed by BEGIN.";
  private static final String PARENTHESES_ERROR_MESSAGE = "Error on parentheses condition.";
  private static final String NO_SEMICOLON_ERROR_MESSAGE = "Statement has no SEMICOLON.";
  private static final String SEMICOLON_NOT_IN_THE_SAME_LINE_ERROR_MESSAGE = "SEMICOLON is missing.";
  private static final String ATTRIB_WITHOUT_LITERAL_DIGIT_ID_ERROR_MESSAGE = "Attribution not followed by a LITERAL, IDENTIFIER or DIGITs.";
  private static final String INVALID_IS_ATTRIB_ERROR_MESSAGE = " 'IS' Attribution must be of type INTEGER, REAL or STRING.";
  private static final String INVALID_ID_ATTRIB_ERROR_MESSAGE = " 'IDENTIFIER' Attribution not ending with SEMICOLON";
  private static final String INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE = "Attritution syntax error.";
  private static final String COMMA_WITHOUT_IDENTIFIER_ERROR_MESSAGE = "Comma not followed by IDENTIFIER on attribution.";
  private static final String LITERAL_WITHOUT_QUOTE_END_ERROR_MESSAGE = "LITERAL does not end with '\"'.";
  private static final String UNCLOSED_PARENTHESES_ERROR_MESSAGE = "A parentheses was open but not closed in the same line or was never closed.";
  private static final String FACTORA_END_ERROR_MESSAGE = "FACTOR-A does not contain a FACTOR, NOT FACTOR or -FACTOR.";
  private static final String INVALID_TERM_ERROR_MESSAGE = "Invalid TERM expression.";
  private static final String TERM_WITH_NO_IDENTIFIER_ERROR_MESSAGE = "TERM with no-IDENTIFIER.";
  private static final String SIMPLEEXPR_WITH_NO_FACTORA_ERROR_MESSAGE = "SIMPLE-EXPRESSION with no-FACTOR-A.";
  private static final String INVALID_SIMPLEEXPR_ERROR_MESSAGE = "Invalid SIMPLE-EXPRESSION.";
  private static final String EXPRESSION_WITH_NO_SIMPLEEXPR_ERROR_MESSAGE = "EXPRESSION with no-SIMPLE-EXPRESSION.";
  private static final String INVALID_EXPRESSION_ERROR_MESSAGE = "Invalid EXPRESSION expression.";
  private ArrayList<Token> tokenList;

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

  // do-stmt ::= do stmt-list do-suffix
  // do-suffix ::= while "(" condition ")"
  public Tuple<Integer, Boolean> validateDoStatement(int index) throws InvalidSyntaxException {
    int indexOfNextTokenAfterDo = index + 1;
    int indexOfNextTokenAfterStatementList = validateStatementList(indexOfNextTokenAfterDo) + 1;

    if (tokenList.get(indexOfNextTokenAfterStatementList).tag != Tag.WHILE) {
      throw new InvalidSyntaxException(DO_STATEMENT_WITHOUT_WHILE_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterStatementList).line);
    }

    int indexOfNextTokenAfterWhile = indexOfNextTokenAfterStatementList + 1;

    if (tokenList.get(indexOfNextTokenAfterWhile).tag != Tag.OPEN_PARENTHESES) {
      throw new InvalidSyntaxException(WHILE_NOT_FOLLOWED_BY_PARENTHESES_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterWhile).line);
    }

    int indexOfNextTokenAfterParentheses = indexOfNextTokenAfterWhile + 1;
    Tuple<Integer, Boolean> validExpression = validateExpression(indexOfNextTokenAfterParentheses);
    int indexOfNextTokenAfterExpression = validExpression.key + 1;
    if (tokenList.get(indexOfNextTokenAfterExpression).tag != Tag.CLOSE_PARENTHESES) {
      throw new InvalidSyntaxException(EXPRESSION_NOT_FOLLOWED_BY_PARENTHESES_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterExpression).line);
    }

    return new Tuple<Integer, Boolean>(indexOfNextTokenAfterExpression, true);
  }

  public Tuple<Integer, Boolean> validateReadStatement(int index) throws InvalidSyntaxException {
    int nextIndex = index;
    return null;
  }

  public Tuple<Integer, Boolean> validateWriteStatement(int index) throws InvalidSyntaxException {
    int nextIndex = index;
    return null;
  }

  // stmt-list ::= stmt ";" { stmt ";" }
  public int validateStatementList(int index) throws InvalidSyntaxException {
    int indexOfNextTokenAfterStatement = validateStatement(index) + 1;
    if (tokenList.get(indexOfNextTokenAfterStatement).tag != Tag.SEMICOLON) {
      throw new InvalidSyntaxException(NO_SEMICOLON_ERROR_MESSAGE, tokenList.get(index).line);
    }

    return indexOfNextTokenAfterStatement;
  }

  // statment ::= assign-stmt | if-stmt | do-stmt | read-stmt | write-stmt
  public int validateStatement(int index) throws InvalidSyntaxException {
    Token token = tokenList.get(index);
    Tuple<Integer, Boolean> assertion;
    switch (token.tag) {
    case Tag.IF:
      assertion = validateIFStatement(index);
      if (!assertion.value) {
        throw new InvalidSyntaxException(INVALID_IF_STATEMENT_ERROR_MESSAGE, token.line);
      }
      index = assertion.key;
      break;
    case Tag.DO:
      assertion = validateDoStatement(index);
      if (!assertion.value) {
        throw new InvalidSyntaxException(INVALID_DO_STATEMENT_ERROR_MESSAGE, token.line);
      }
      index = assertion.key;
      break;
    case Tag.READ:
      assertion = validateReadStatement(index);
      if (!assertion.value) {
        throw new InvalidSyntaxException(INVALID_READ_STATEMENT_ERROR_MESSAGE, token.line);
      }
      index = assertion.key;
      break;
    case Tag.WRITE:
      assertion = validateWriteStatement(index);
      if (!assertion.value) {
        throw new InvalidSyntaxException(INVALID_WRITE_STATEMENT_ERROR_MESSAGE, token.line);
      }
      index = assertion.key;
      break;
    default:
      if (!validateAssignStatement(index).value) {
        throw new InvalidSyntaxException(INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE, token.line);
      }
      break;
    }
    return index;
  }

  private Tuple<Integer, Boolean> validateIFStatement(int index) throws InvalidSyntaxException {
    Token token = tokenList.get(index + 1);
    if (token.tag != Tag.OPEN_PARENTHESES) {
      throw new InvalidSyntaxException(IF_STATEMENT_NOT_START_WITH_PARENTHESES_ERROR_MESSAGE, token.line);
    }
    Tuple<Integer, Boolean> expressionAssertion = validateExpression(index + 1);

    int indexOfNextTokenAfterCondition = expressionAssertion.key + 1;

    if (tokenList.get(indexOfNextTokenAfterCondition).tag != Tag.CLOSE_PARENTHESES) {
      throw new InvalidSyntaxException(IF_STATEMENT_NOT_END_WITH_PARENTHESES_ERROR_MESSAGE, token.line);
    }

    int indexOfNextTokenAfterCloseParentheses = indexOfNextTokenAfterCondition + 1;

    if (tokenList.get(indexOfNextTokenAfterCloseParentheses).tag != Tag.BEG) {
      throw new InvalidSyntaxException(IF_STATEMENT_NOT_START_WITH_BEGIN_ERROR_MESSAGE, token.line);
    } else {

      // beginStack.push(tokenList.get(indexOfNextTokenAfterCloseParentheses));
      int indexOfNextTokenAfterBegin = indexOfNextTokenAfterCloseParentheses + 1;

      int indexOfNextTokenAfterStatementList = validateStatementList(indexOfNextTokenAfterBegin) + 1;

      if (tokenList.get(indexOfNextTokenAfterStatementList).tag != Tag.END) {
        throw new InvalidSyntaxException("IF STATEMENT do not end with END.", token.line);
      }

      int indexOfNextTokenAfterEnd = indexOfNextTokenAfterStatementList + 1;
      return new Tuple<Integer, Boolean>(indexOfNextTokenAfterEnd, true);
    }
  }

  // assign-statement:= identifier ":=" simple_expr
  // maior := 22
  public Tuple<Integer, Boolean> validateAssignStatement(int index) throws InvalidSyntaxException {
    Token token = tokenList.get(index);

    if (!isIdentifier(token)) {
      throw new InvalidSyntaxException(INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE, token.line);
    }

    int indexOfNextTokenAfterIdentifier = index + 1;

    if (tokenList.get(indexOfNextTokenAfterIdentifier).tag != Tag.ATTRIB) {
      throw new InvalidSyntaxException(INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE, token.line);
    }

    int indexOfNextTokenAfterAttribution = indexOfNextTokenAfterIdentifier + 1;

    index = validateSimpleExpr(indexOfNextTokenAfterAttribution).key;
    return new Tuple<Integer, Boolean>(indexOfToken(index, Tag.SEMICOLON), true);
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

  public boolean isSameLineToken(int startIndex, int stopIndex) {
    return tokenList.get(startIndex).line == tokenList.get(stopIndex).line;
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
  private Tuple<Integer, Boolean> validateExpression(int index) throws InvalidSyntaxException {
    int termIndex = index + 1;
    Tuple<Integer, Boolean> isSimpleExpr = validateSimpleExpr(index);

    if (!isSimpleExpr.value || termIndex > tokenList.size()) {
      throw new InvalidSyntaxException(EXPRESSION_WITH_NO_SIMPLEEXPR_ERROR_MESSAGE, tokenList.get(index).line);
    }
    if (validateRelOp(tokenList.get(termIndex))) {
      Tuple<Integer, Boolean> isSimpleExpr2 = validateSimpleExpr(termIndex + 1);
      return new Tuple<Integer, Boolean>(termIndex, isSimpleExpr2.value);
    }
    return isSimpleExpr;
  }

  // simple-expr := term | simple-expr addop term
  private Tuple<Integer, Boolean> validateSimpleExpr(int index) throws InvalidSyntaxException {
    int termIndex = index + 1;
    Tuple<Integer, Boolean> isTerm = validateTerm(index);

    if (!isTerm.value || termIndex > tokenList.size()) {
      throw new InvalidSyntaxException(SIMPLEEXPR_WITH_NO_FACTORA_ERROR_MESSAGE, tokenList.get(index).line);
    }
    if (validateAddOp(tokenList.get(termIndex))) {
      Tuple<Integer, Boolean> isSimpleExpr = validateSimpleExpr(termIndex + 1);
      return new Tuple<Integer, Boolean>(termIndex, isSimpleExpr.value);
    }
    return isTerm;
  }

  // term := factorA | term mulop factorA
  private Tuple<Integer, Boolean> validateTerm(int index) throws InvalidSyntaxException {
    int nextIndex = index + 1;
    Tuple<Integer, Boolean> isFactorA = validateFactorA(index);
    if (!isFactorA.value || nextIndex > tokenList.size()) {
      throw new InvalidSyntaxException(TERM_WITH_NO_IDENTIFIER_ERROR_MESSAGE, tokenList.get(index).line);
    }
    if (validateMulOp(tokenList.get(nextIndex))) {
      Tuple<Integer, Boolean> isTerm = validateTerm(nextIndex + 1);
      return new Tuple<Integer, Boolean>(nextIndex, isTerm.value);
    }
    return isFactorA;
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
      int indexOfFirstCloseParentheses = indexOfToken(index, Tag.CLOSE_PARENTHESES);
      assertion = validateExpression(index + 1).value && validateParentheses(index, indexOfFirstCloseParentheses);
      return new Tuple<Integer, Boolean>(indexOfFirstCloseParentheses, assertion);
    }
  }

  private boolean validateParentheses(int startIndex, int endIndex) throws InvalidSyntaxException {
    boolean parenthesesAreOnTheSameLine = isSameLineToken(startIndex, endIndex);
    if (parenthesesAreOnTheSameLine) {
      return parenthesesAreOnTheSameLine;
    } else {
      throw new InvalidSyntaxException(UNCLOSED_PARENTHESES_ERROR_MESSAGE, tokenList.get(startIndex).line);
    }
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