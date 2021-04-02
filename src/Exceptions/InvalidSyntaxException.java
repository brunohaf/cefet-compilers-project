package Exceptions;

public class InvalidSyntaxException extends Exception{
    private String message;
    
    public InvalidSyntaxException(String message){
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
