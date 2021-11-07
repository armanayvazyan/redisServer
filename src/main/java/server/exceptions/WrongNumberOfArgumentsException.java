package server.exceptions;

public class WrongNumberOfArgumentsException extends RuntimeException {

    public WrongNumberOfArgumentsException(String command) {
        super("ERR ERR wrong number of arguments for '" + command + "' command");
    }
}
