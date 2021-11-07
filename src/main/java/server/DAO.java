package server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DAO {

    public static ConcurrentMap<String, String> simpleKeyValueMap = new ConcurrentHashMap<>();
}
