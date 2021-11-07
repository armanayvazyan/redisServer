package server;

import kotlin.Pair;

import java.util.Timer;
import java.util.TimerTask;

public class StorageService {

    @SafeVarargs
    public static String saveSimpleKayeValue(Pair<String, String> ...pairs) {
        for (Pair<String, String> pair: pairs) {
            DAO.simpleKeyValueMap.put(pair.getFirst(), pair.getSecond());
        }
        return "OK";
    }

    public static String saveSimpleKayeValueIfNotExist(String key, String value) {
        DAO.simpleKeyValueMap.putIfAbsent(key, value);
        return "OK";
    }

    public static String getSimpleValueForKey(String key) {
        return DAO.simpleKeyValueMap.getOrDefault(key, "(nil)");
    }

    public static String getSimpleValueForKey(String ...keys) {
        String returnString = "";
        for (int cursor = 1; cursor <= keys.length; cursor++) {
            String value = DAO.simpleKeyValueMap.getOrDefault(keys[cursor - 1], "(nil)");
            if(cursor == keys.length) {
                returnString = returnString.concat(cursor + ") " + value);
            } else {
                returnString= returnString.concat(cursor + ") " + value + "\n");
            }
        }
        return returnString.isEmpty() ? "1) (nil)" : returnString;
    }

    public static String saveSimpleKayeValueWithExpiration(String key, String value, Integer seconds) {
        DAO.simpleKeyValueMap.put(key, value);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                DAO.simpleKeyValueMap.remove(key);
            }
        }, seconds * 1000);
        return "OK";
    }
}
