package server;

import kotlin.Pair;
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
        switch (split[0]) {
            case "SET": {
                if(split.length != 3) throw new WrongNumberOfArgumentsException(split[0]);
                return StorageService.saveSimpleKayeValue(new Pair<>(split[1], split[2]));
            }
            case "SETNX": {
                if(split.length != 3) throw new WrongNumberOfArgumentsException(split[0]);
                return StorageService.saveSimpleKayeValueIfNotExist(split[1], split[2]);
            }
            case "SETEX": {
                if(split.length != 4) throw new WrongNumberOfArgumentsException(split[0]);
                try{
                    Integer seconds = Integer.valueOf(split[2]);
                    return StorageService.saveSimpleKayeValueWithExpiration(split[1], split[3], seconds);
                } catch (NumberFormatException e) {
                   throw new NumberFormatException("ERR ERR value is not an integer or out of range");
                }
            }
            case "MSET": {
                if(split.length < 3  || split.length % 2 == 0) throw new WrongNumberOfArgumentsException(split[0]);
                Pair<String, String>[] pairs = new Pair[(split.length - 1)/2];
                for (int i = 1; i < split.length; i+=2) {
                    pairs[(i-1)/2] = new Pair(split[i], split[i+1]);
                }
                return StorageService.saveSimpleKayeValue(pairs);
            }
            case "GET": {
                if(split.length != 2) throw new WrongNumberOfArgumentsException(split[0]);
                return StorageService.getSimpleValueForKey(split[1]);
            }
            case "MGET ": {
                if(split.length < 2) throw new WrongNumberOfArgumentsException(split[0]);
                String[] keysArray = new String[split.length - 1];
                System.arraycopy(split, 1, keysArray, 0, split.length - 1);
                return StorageService.getSimpleValueForKey(keysArray);
            }
            default:
                throw new WrongCommandException(command);
        }
    }
}
