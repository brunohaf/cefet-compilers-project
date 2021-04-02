package LexicalAnalyzer;

import java.io.*;
import java.util.*;

import Models.*;


public class Lexer {
    public static int line = 1; // contador de linhas
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
        //Insere palavras reservadas na HashTable
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
        for(Token token = scan(); token.tag != 65535; token = scan()) {
            System.out.println(token.toString());
        }
        
    }

    public ArrayList getTokenList() throws IOException {
        ArrayList tokenList = new ArrayList<Token>();
        for(Token token = scan(); token.tag != 65535; token = scan()) {
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
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
                continue;
            else if (ch == '\n')
                line++; // conta linhas
            else
                break;
        }
        switch (ch) {
            // Operadores
            case '&':
                if (readch('&'))
                    return Word.and;
                else
                    return new Token('&');
            case '|':
                if (readch('|'))
                    return Word.or;
                else
                    return new Token('|');
            case '=':
                if (readch('='))
                    return Word.eq;
                else
                    return new Token('=');
            case '<':
                if (readch('='))
                    return Word.le;
                if (readch('>'))
                    return Word.ne;
                else
                    return new Token('<');
            case '>':
                if (readch('='))
                    return Word.ge;
                else
                    return new Token('>');
        }
        // Números
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            return new Num(value);
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
                    return new Token(Tag.INIT);
                case "stop":
                    return new Token(Tag.STOP);
                default:
                    break;
            }

            Word w = (Word) words.get(s);
            if (w != null)
                return w;
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        // Caracteres não especificados
        Token t = new Token(ch);
        ch = ' ';
        if(ch == -1){
            return null;
        }
        return t;
    }
}