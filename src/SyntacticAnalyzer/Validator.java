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
  private static final String NO_SEMICOLON_ERROR_MESSAGE = "Statement has no SEMICOLON.";
  private static final String INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE = "Attritution syntax error.";
  private static final String UNCLOSED_PARENTHESES_ERROR_MESSAGE = "A parentheses was open but not closed in the same line or was never closed.";
  private static final String FACTORA_END_ERROR_MESSAGE = "FACTOR-A does not contain a FACTOR, NOT FACTOR or -FACTOR.";
  private static final String TERM_WITH_NO_IDENTIFIER_ERROR_MESSAGE = "TERM with no-IDENTIFIER.";
  private static final String SIMPLEEXPR_WITH_NO_FACTORA_ERROR_MESSAGE = "SIMPLE-EXPRESSION with no-FACTOR-A.";
  private static final String EXPRESSION_WITH_NO_SIMPLEEXPR_ERROR_MESSAGE = "EXPRESSION with no-SIMPLE-EXPRESSION.";
  private static final String WRITE_NOT_FOLLOWED_BY_PARENTHESES_ERROR_MESSAGE = "WRITE command not followed by parentheses.";
  private static final String WRITE_WITH_NO_IDENTIFIER_ERROR_MESSAGE = "WRITE command with invalid identifier.";
  private static final String WRITE_WITHOUT_CLOSE_PARENTHESES_ERROR_MESSAGE = "WRITE command does not end with parentheses.";
  private static final String WRITE_WITH_NO_WRITABLE_ERROR_MESSAGE = "WRITE command with no WRITABLE value.";
  private static final String IF_WITHOUT_END_ERROR_MESSAGE = "IF STATEMENT does not end with END.";
  private static final String IDENTLIST_NOT_STARTING_WITH_IDENTIFIER_ERROR_MESSAGE = "IDENTLIST does not start with IDENTIFIER.";
  private static final String IDENTLIST_WITH_NO_IS_STATEMENT_ERROR_MESSAGE = "IDENTLIST has no IS statement.";
  private static final String IDENTLIST_WITH_NO_TYPE_ERROR_MESSAGE = "IDENTLIST has no declared TYPE";
  private static final String PROGRAM_DO_NOT_START_INIT_ERROR_MESSAGE = "Program does not start with INIT.";
  private static final String PROGRAM_DO_NOT_END_WITH_STOP = "Program does not end with STOP.";
  private static final String LITERAL_NOT_BETWEEN_QUOTES_ERROR_MESSAGE = "LITERAL is not between QUOTES.";
  private static final String REAL_CONST_NOT_STARTING_WITH_INTEGER_CONST = null;
  private static final String REAL_CONST_NOT_STARTING_WITH_INTEGER_CONST_ERROR_MESSAGE = null;
  private static final String REAL_CONST_HAS_NO_DOT_ERROR_MESSAGE = null;
  private static final String SIMPLEEXPR_WITH_NO_ADDOP_ERROR_MESSAGE = null;
  private static final String INVALID_TERM_ERROR_MESSAGE = null;
  private static final String INVALID_SIMPLEEXPR_ERROR_MESSAGE = null;
  private ArrayList<Token> tokenList;
  private Stack<Token> lambdaStk;

  public Validator(ArrayList<Token> tokenList) {
    this.tokenList = tokenList;
    this.lambdaStk = new Stack<Token>();
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

  // program ::= init [decl-list] begin stmt-list stop
  public int validateProgram() throws InvalidSyntaxException {
    if (tokenList.get(0).tag != Tag.INIT) {
      throw new InvalidSyntaxException(PROGRAM_DO_NOT_START_INIT_ERROR_MESSAGE, tokenList.get(0).line);
    }

    int indexOfNextTokenAfterInit = 1;
    int indexOfNextTokenAfterDeclList = indexOfNextTokenAfterInit;
    if (tokenList.get(indexOfNextTokenAfterInit).tag != Tag.BEG) {
      indexOfNextTokenAfterDeclList = validateDeclList(indexOfNextTokenAfterInit);
    }
    int indexOfNextTokenAfterStatementList = validateStatementList(indexOfNextTokenAfterDeclList + 1) + 1;

    if (tokenList.get(indexOfNextTokenAfterStatementList).tag != Tag.STOP) {
      throw new InvalidSyntaxException(PROGRAM_DO_NOT_END_WITH_STOP,
          tokenList.get(indexOfNextTokenAfterStatementList).line);
    }
    return indexOfNextTokenAfterStatementList;
  }

  // decl-list ::= decl ";"
  public int validateDeclList(int index) throws InvalidSyntaxException {
    int indexOfNextTokenAfterDecl = validateDecl(index) + 1;

    Token nextToken = tokenList.get(indexOfNextTokenAfterDecl);
    if (nextToken.tag != Tag.SEMICOLON) {
      throw new InvalidSyntaxException(NO_SEMICOLON_ERROR_MESSAGE, tokenList.get(index).line);
    }

    return indexOfNextTokenAfterDecl;
  }

  // decl ::= ident-list is type
  public int validateDecl(int index) throws InvalidSyntaxException {

    Tuple<Integer, Boolean> isIdentList = validateIdentList(index);
    int indexOfNextTokenAfterIdentList = isIdentList.key;
    if (tokenList.get(indexOfNextTokenAfterIdentList).tag != Tag.IS) {
      throw new InvalidSyntaxException(IDENTLIST_WITH_NO_IS_STATEMENT_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterIdentList).line);
    }
    int indexOfNextTokenAfterIsStatement = indexOfNextTokenAfterIdentList + 1;
    if (!isType(indexOfNextTokenAfterIsStatement)) {
      throw new InvalidSyntaxException(IDENTLIST_WITH_NO_TYPE_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterIdentList).line);
    }
    return indexOfNextTokenAfterIsStatement;
  }

  // ident-list ::= identifier {"," identifier}
  public Tuple<Integer, Boolean> validateIdentList(int index) throws InvalidSyntaxException {
    Token token = tokenList.get(index);
    boolean isIdentifier = isIdentifier(index);
    if (!isIdentifier) {
      throw new InvalidSyntaxException(IDENTLIST_NOT_STARTING_WITH_IDENTIFIER_ERROR_MESSAGE, token.line);
    }

    int indexOfNextTokenAfterIdentifier = index + 1;

    int indexOfIdentListEnd = indexOfNextTokenAfterIdentifier;

    if (tokenList.get(indexOfNextTokenAfterIdentifier).tag == Tag.COMMA) {
      indexOfIdentListEnd = validateIdentList(indexOfNextTokenAfterIdentifier + 1).key;
    }

    return new Tuple<Integer, Boolean>(indexOfIdentListEnd, true);
  }

  // type ::= integer | string | real
  public boolean isType(int index) {
    Token currentToken = tokenList.get(index);
    return currentToken.tag == Tag.STRING || currentToken.tag == Tag.INT || currentToken.tag == Tag.REAL;
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
    int indexOfNextTokenAfterRead = index + 1;
    if (tokenList.get(indexOfNextTokenAfterRead).tag != Tag.OPEN_PARENTHESES) {
      throw new InvalidSyntaxException(WRITE_NOT_FOLLOWED_BY_PARENTHESES_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterRead).line);
    }
    int indexOfNextTokenAfterParentheses = indexOfNextTokenAfterRead + 1;
    if (!isIdentifier(indexOfNextTokenAfterParentheses)) {
      throw new InvalidSyntaxException(WRITE_WITH_NO_IDENTIFIER_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterParentheses).line);
    }
    int indexOfNextTokenAfterIdentifier = indexOfNextTokenAfterParentheses + 1;
    if (tokenList.get(indexOfNextTokenAfterIdentifier).tag != Tag.CLOSE_PARENTHESES) {
      throw new InvalidSyntaxException(WRITE_WITHOUT_CLOSE_PARENTHESES_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterIdentifier).line);
    }

    return new Tuple<Integer, Boolean>(indexOfNextTokenAfterIdentifier, true);
  }

  public Tuple<Integer, Boolean> validateWriteStatement(int index) throws InvalidSyntaxException {
    int indexOfNextTokenAfterWrite = index + 1;
    if (tokenList.get(indexOfNextTokenAfterWrite).tag != Tag.OPEN_PARENTHESES) {
      throw new InvalidSyntaxException(WRITE_NOT_FOLLOWED_BY_PARENTHESES_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterWrite).line);
    }
    int indexOfNextTokenAfterParentheses = validateSimpleExpr(indexOfNextTokenAfterWrite).key + 1;
    if (!isIdentifier(indexOfNextTokenAfterParentheses)) {
      throw new InvalidSyntaxException(WRITE_WITH_NO_WRITABLE_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterWrite).line);
    }
    int indexOfNextTokenAfterIdentifier = indexOfNextTokenAfterParentheses + 1;
    if (tokenList.get(indexOfNextTokenAfterIdentifier).tag != Tag.CLOSE_PARENTHESES) {
      throw new InvalidSyntaxException(WRITE_WITHOUT_CLOSE_PARENTHESES_ERROR_MESSAGE,
          tokenList.get(indexOfNextTokenAfterIdentifier).line);
    }

    return new Tuple<Integer, Boolean>(indexOfNextTokenAfterIdentifier, true);
  }

  // stmt-list ::= stmt ";" { stmt ";" }
  public int validateStatementList(int index) throws InvalidSyntaxException {
    int indexOfNextTokenAfterStatement = validateStatement(index) + 1;
    Token nextToken = tokenList.get(indexOfNextTokenAfterStatement);
    if (nextToken.tag != Tag.SEMICOLON) {
      throw new InvalidSyntaxException(NO_SEMICOLON_ERROR_MESSAGE, tokenList.get(index).line);
    }

    int indexOfNextTokenSemicolon = indexOfNextTokenAfterStatement + 1;

    if (tokenList.get(indexOfNextTokenSemicolon).tag != Tag.STOP && indexOfNextTokenSemicolon < tokenList.size()) {
      validateStatementList(indexOfNextTokenSemicolon);
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
      Tuple<Integer, Boolean> isValidAssignStatement = validateAssignStatement(index);
      index = isValidAssignStatement.key;
      if (!isValidAssignStatement.value) {
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
        throw new InvalidSyntaxException(IF_WITHOUT_END_ERROR_MESSAGE, token.line);
      }

      int indexOfNextTokenAfterEnd = indexOfNextTokenAfterStatementList + 1;
      return new Tuple<Integer, Boolean>(indexOfNextTokenAfterEnd, true);
    }
  }

  // assign-statement:= identifier ":=" simple_expr
  // maior := 22
  public Tuple<Integer, Boolean> validateAssignStatement(int index) throws InvalidSyntaxException {
    Token token = tokenList.get(index);

    if (!isIdentifier(index)) {
      throw new InvalidSyntaxException(INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE, token.line);
    }

    int indexOfNextTokenAfterIdentifier = index + 1;

    if (tokenList.get(indexOfNextTokenAfterIdentifier).tag != Tag.ATTRIB) {
      throw new InvalidSyntaxException(INVALID_ATTRIB_SYNTAX_ERROR_MESSAGE, token.line);
    }

    int indexOfNextTokenAfterAttribution = indexOfNextTokenAfterIdentifier + 1;

    index = validateSimpleExpr(indexOfNextTokenAfterAttribution).key;
    return new Tuple<Integer, Boolean>(index, true);
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
    Tuple<Integer, Boolean> isSimpleExpr = validateSimpleExpr(termIndex);
    if (isSimpleExpr.value) {
      int indexOfNextTokenAfterSimplExpression = isSimpleExpr.key + 1;
      if (validateRelOp(tokenList.get(indexOfNextTokenAfterSimplExpression))) {
        int indexOfNextTokenAfterRelOp = indexOfNextTokenAfterSimplExpression + 1;
        Tuple<Integer, Boolean> isSimpleExpr2 = validateSimpleExpr(indexOfNextTokenAfterRelOp);
        if (!isSimpleExpr2.value) {
          throw new InvalidSyntaxException(EXPRESSION_WITH_NO_SIMPLEEXPR_ERROR_MESSAGE, tokenList.get(index).line);
        } else {
          return isSimpleExpr2;
        }
      } else {
        return isSimpleExpr;
      }
    } else {
      throw new InvalidSyntaxException(EXPRESSION_WITH_NO_SIMPLEEXPR_ERROR_MESSAGE, tokenList.get(index).line);
    }
  }

  // simple-expr := term simpl-expr2
  private Tuple<Integer, Boolean> validateSimpleExpr(int index) throws InvalidSyntaxException {
    Tuple<Integer, Boolean> isTerm = validateTerm(index);

    if (isTerm.value) {
      int indexOfNextTokenAfterTerm = isTerm.key + 1;
      return validateSimpleExpr2(indexOfNextTokenAfterTerm);
    } else {
      throw new InvalidSyntaxException(INVALID_SIMPLEEXPR_ERROR_MESSAGE, tokenList.get(isTerm.key).line);
    }
  }

  // simpl-expr2 := addop term simpl-expr2 | lambda
  private Tuple<Integer, Boolean> validateSimpleExpr2(int index) throws InvalidSyntaxException {
    if (validateAddOp(tokenList.get(index))) {
      int indexOfNextTokenAfterAddop = index + 1;
      Tuple<Integer, Boolean> isTerm = validateTerm(indexOfNextTokenAfterAddop);
      if (isTerm.value) {
        int indexOfNextTokenAfterTerm = indexOfNextTokenAfterAddop + 1;
        return validateSimpleExpr2(indexOfNextTokenAfterTerm);
      } else {
        throw new InvalidSyntaxException(INVALID_SIMPLEEXPR_ERROR_MESSAGE,
            tokenList.get(indexOfNextTokenAfterAddop).line);
      }
    } else {
      return new Tuple<Integer, Boolean>(index - 1, true);
    }
  }

  // term := factorA term2
  private Tuple<Integer, Boolean> validateTerm(int index) throws InvalidSyntaxException {
    Tuple<Integer, Boolean> isFactorA = validateFactorA(index);
    if (isFactorA.value) {
      int indexOfNextTokenAfterFactorA = isFactorA.key + 1;
      return validateTerm2(indexOfNextTokenAfterFactorA);
    } else {
      throw new InvalidSyntaxException(INVALID_TERM_ERROR_MESSAGE, tokenList.get(index).line);
    }
  }

  // term2 := mulop factorA term2 | lambda
  private Tuple<Integer, Boolean> validateTerm2(int index) throws InvalidSyntaxException {
    if (validateMulOp(tokenList.get(index))) {
      int indexOfNextTokenAfterMulop = index + 1;
      Tuple<Integer, Boolean> isFactorA = validateFactorA(indexOfNextTokenAfterMulop);
      if (isFactorA.value) {
        int indexOfNextTokenAfterFactorA = indexOfNextTokenAfterMulop + 1;
        return validateTerm2(indexOfNextTokenAfterFactorA);
      } else {
        throw new InvalidSyntaxException(INVALID_TERM_ERROR_MESSAGE, tokenList.get(index).line);
      }
    } else {
      return new Tuple<Integer, Boolean>(index - 1, true);
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
    if (token.tag != Tag.OPEN_PARENTHESES) {
      Tuple<Integer, Boolean> isConst = isConst(index);
      boolean assertion = !isIdentifier(index) && !isConst.value;
      if (assertion) {
        return new Tuple<Integer, Boolean>(index, false);
      }
      return new Tuple<Integer, Boolean>(isConst.value ? isConst.key : index, true);
    } else {
      int indexOfNextTokenAfterExpression = validateExpression(index).key + 1;
      if (tokenList.get(indexOfNextTokenAfterExpression).tag != Tag.CLOSE_PARENTHESES) {
        return new Tuple<Integer, Boolean>(indexOfNextTokenAfterExpression, false);
      }
      return new Tuple<Integer, Boolean>(indexOfNextTokenAfterExpression, true);
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

  // constant := integer_const | real_const | literal
  private Tuple<Integer, Boolean> isConst(int index) throws InvalidSyntaxException {
    Tuple<Integer, Boolean> isLiteral = validateLiteral(index);
    Tuple<Integer, Boolean> isRealConst = validateRealConst(index);
    boolean isConst = isIntegerConst(index);
    int returnIndex = isLiteral.value ? isLiteral.key : isRealConst.value ? isRealConst.key : index;
    return new Tuple<Integer, Boolean>(returnIndex, isConst || isLiteral.value || isRealConst.value);
  }

  // integer_const := nonzero {digit} | “0”
  private boolean isIntegerConst(int index) {
    Token token = tokenList.get(index);
    return isNonZeroDigit(index) || token.toString() == "0";
  }

  // real_const := interger_const "." digit+
  private Tuple<Integer, Boolean> validateRealConst(int index) throws InvalidSyntaxException {
    if (!isIntegerConst(index)) {
      return new Tuple<Integer, Boolean>(index, false);
    }

    int indexOfNextTokenAfterInteger = index + 1;
    if (tokenList.get(indexOfNextTokenAfterInteger).tag != Tag.DOT) {
      return new Tuple<Integer, Boolean>(index, false);
    }
    int indexOfNextTokenAfterDot = indexOfNextTokenAfterInteger + 1;

    if (!isDigit(indexOfNextTokenAfterDot)) {
      return new Tuple<Integer, Boolean>(index, false);
    }
    return new Tuple<Integer, Boolean>(indexOfNextTokenAfterDot, true);
  }

  // digit := [0-9]+
  private boolean isDigit(int index) {
    Token token = tokenList.get(index);
    String digitPattern = "[0-9]+";
    return validatePattern(token.toString(), digitPattern);
  }

  // nonZero := [1-9]+
  private boolean isNonZeroDigit(int index) {
    Token token = tokenList.get(index);
    String nonZeroDigitPattern = "[1-9]+";
    return validatePattern(token.toString(), nonZeroDigitPattern);
  }

  // letter := [A-Za-z]
  private boolean isLetter(int index) {
    Token token = tokenList.get(index);
    String letterPattern = "[A-Za-z]+";
    return validatePattern(token.toString(), letterPattern);
  }

  // identifier := letter {letter | digit | " _ " }
  private boolean isIdentifier(int index) {
    Token token = tokenList.get(index);
    String letterPattern = "(^(^[A-Za-z]([A-Za-z0-9]|_)*)+$)";
    return validatePattern(token.toString(), letterPattern);
  }

  // relop := "=" | ">" | ">=" | "<" | "<=" | "<>"
  private boolean validateRelOp(Token token) {
    String lexem = token.toString();
    return lexem == "=" || lexem == ">" || lexem == ">=" || lexem == "<" || lexem == "<=" || lexem == "<>";
  }

  // mulop := "*" | "/" | and
  private boolean validateMulOp(Token token) {
    String lexem = token.toString();
    return lexem.equals("*") || lexem.equals("/") || lexem.equals("and");
  }

  // addop := "+" | "-" | or
  private boolean validateAddOp(Token token) {
    String lexem = token.toString();
    return lexem.equals("+") || lexem.equals("-") || lexem.equals("or");
  }

  // literal := " “ " caractere* " ” "
  private Tuple<Integer, Boolean> validateLiteral(int index) throws InvalidSyntaxException {
    if (tokenList.get(index).tag != Tag.QUOTE) {
      return new Tuple<Integer, Boolean>(index, false);
    }
    int indexOfNextTokenAfterQuote = index + 1;

    int indexOfNextTokenAfterConsumeString = consumeCharacter(indexOfNextTokenAfterQuote) + 1;

    if (tokenList.get(indexOfNextTokenAfterConsumeString).tag != Tag.QUOTE) {
      return new Tuple<Integer, Boolean>(indexOfNextTokenAfterConsumeString, false);
    }
    return new Tuple<Integer, Boolean>(indexOfNextTokenAfterConsumeString, true);
  }

  private int consumeCharacter(int index) {
    int currentIndex;
    for (currentIndex = index; tokenList.get(currentIndex).tag < 256; currentIndex++) {
    }
    ;
    return currentIndex;
  }
}