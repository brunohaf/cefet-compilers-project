package SyntacticAnalyzer;

import java.util.ArrayList;

import Exceptions.InvalidSyntaxException;
import Models.Tag;
import Models.Token;
import Models.Utils.Tuple;

public class SyntaticAnalyzer {
    private ArrayList<Token> tokenList;

    public SyntaticAnalyzer(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public void scanCode() throws Exception {

        Validator validator = new Validator(tokenList);
        
        validator.validateProgram();

        System.out.println("Success!! The code is valid");
    }
}
