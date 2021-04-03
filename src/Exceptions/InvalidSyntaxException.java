package Exceptions;

public class InvalidSyntaxException extends Exception{
    private String message;
    private int currentLine;
    
    public InvalidSyntaxException(String message, int currentLine){
        super();
        this.message = message;
        this.currentLine = currentLine;
    }

    @Override
    public String getMessage() {
        return this.message + " At line: " + this.currentLine;
    }
}
