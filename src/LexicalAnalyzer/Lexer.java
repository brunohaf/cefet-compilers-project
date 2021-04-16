package LexicalAnalyzer;

import java.io.*;
import java.util.*;

import Models.*;

public class Lexer {
    public int line = 1; // contador de linhas
    private char ch = ' '; // caractere lido do arquivo
    private FileReader file;

    private Hashtable words = new Hashtable();

    private void reserve(Word w) {
        words.put(w.getLexeme(), w);
    }

    public void getTokens() {
        System.out.println("\n");
        System.out.println(words.toString());
        System.out.println("\n");
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        // Insere palavras reservadas na HashTable
        reserve(new Word("init", Tag.BEG));
        reserve(new Word("stop", Tag.END));
        reserve(new Word("is", Tag.IS));
        reserve(new Word("integer", Tag.INT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("real", Tag.REAL));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("begin", Tag.BEG));
        reserve(new Word("end", Tag.END));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("read", Tag.READ));
        reserve(new Word("write", Tag.WRITE));
        reserve(new Word("not", Tag.NOT));
        reserve(new Word("or", Tag.OR));
        reserve(new Word("and", Tag.AND));
    }

    private void readch() throws IOException {
        int readChar = file.read();
        ch = (char) readChar;
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c */
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c)
            return false;
        ch = ' ';
        return true;
    }

    public void printTokenList() throws IOException {
        for (Token token = scan(); token.tag != 65535; token = scan()) {
            System.out.println(token.toString());
        }

    }

    public ArrayList getTokenList() throws IOException {
        ArrayList tokenList = new ArrayList<Token>();
        for (Token token = scan(); token.tag != 65535; token = scan()) {
            tokenList.add(token);
        }
        return tokenList;
    }

    public void getSymbolTable() {
        System.out.println("\n\nSymbolTable: ");
        System.out.println(this.words.toString());
    }

    public Token scan() throws IOException {
        // Desconsidera delimitadores na entrada
        boolean isCommentSection = false;
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b' || isCommentSection)
                continue;
            else if (ch == '%') {
                isCommentSection = !isCommentSection;
                continue;
            } else if (ch == '\n')
                line++; // conta linhas
            else
                break;
        }
        switch (ch) {
        // Operadores
        case ';':
            readch();
            return new Token(Tag.SEMICOLON, line);
        case ',':
            readch();
            return new Token(Tag.COMMA, line);
        case ':':
            if (readch('='))
                return new Token(Tag.ATTRIB, line);
            else
                return new Token(':', line);
        case '"':
            readch();
            return new Token(Tag.QUOTE, line);
        case '&':
            if (readch('&'))
                return new Word("&&", Tag.AND, line);
            else
                return new Token('&', line);
        case '|':
            if (readch('|'))
                return new Word("||", Tag.OR, line);
            else
                return new Token('|', line);
        case '=':
            if (readch('='))
                return new Word("==", Tag.EQ, line);
            else
                return new Token('=', line);
        case '<':
            if (readch('='))
                return new Word("<=", Tag.LE, line);
            if (readch('>'))
                return new Word("<>", Tag.NE, line);
            else
                return new Token('<', line);
        case '.':
            readch();
            return new Token(Tag.DOT, line);
        case '>':
            if (readch('='))
                return new Word(">=", Tag.GE, line);
            else
                return new Token('>', line);
        case '(':
            readch();
            return new Token(Tag.OPEN_PARENTHESES, line);
        case ')':
            readch();
            return new Token(Tag.CLOSE_PARENTHESES, line);
        }
        // Números
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            return new Num(value, line);
        }
        // Identificadores
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch));
            String s = sb.toString();

            switch (s) {
            case "init":
                return new Token(Tag.INIT, line);
            case "stop":
                return new Token(Tag.STOP, line);

            case "is":
                return new Token(Tag.IS, line);

            case "integer":
                return new Token(Tag.INT, line);
            case "string":
                return new Token(Tag.STRING, line);
            case "real":
                return new Token(Tag.REAL, line);

            case "if":
                return new Token(Tag.IF, line);
            case "begin":
                return new Token(Tag.BEG, line);

            case "end":
                return new Token(Tag.END, line);
            case "else":
                return new Token(Tag.ELSE, line);

            case "do":
                return new Token(Tag.DO, line);
            case "while":
                return new Token(Tag.WHILE, line);

            case "read":
                return new Token(Tag.READ, line);
            case "write":
                return new Token(Tag.WRITE, line);

            default:
                break;
            }

            // Word w = (Word) words.get(s);
            // if (w != null)
            // return w;
            Word w = new Word(s, Tag.ID, line);
            // words.put(s, w);
            return w;
        }
        // Caracteres não especificados
        Token t = new Token(ch, line);
        ch = ' ';
        if (ch == -1) {
            return null;
        }
        return t;
    }
}