package server;

import server.exceptions.WrongCommandException;
import server.exceptions.WrongNumberOfArgumentsException;

import java.util.concurrent.Callable;

public class CommandProcessor implements Callable {

    private String command;

    public CommandProcessor(String command) {
        this.command = command;
    }

    @Override
    public String call() {
        if(command == null)
            throw new WrongCommandException("");

        String[] split = command.split(" ");
        Commands command = null;
        try {
             command = Commands.valueOf(split[0]);
             return command.process(split);
        } catch (NullPointerException e) {
            throw new WrongCommandException(command.toString());
        } catch (WrongCommandException | WrongNumberOfArgumentsException e) {
            return e.getMessage();
        }
    }
}
