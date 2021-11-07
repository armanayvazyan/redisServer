package server.exceptions;

public class WrongCommandException extends RuntimeException {

    public WrongCommandException(String command) {
        super("ERR Unknown or disabled command "  + command);
    }
}
