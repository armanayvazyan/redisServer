package server;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DAO {
    public static ConcurrentMap<String, String> simpleKeyValueMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, LinkedList<String>> listMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, Map<Integer, LinkedList<String>>> sortedSetWithScore = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, HashMap<String, String>> hashsetMap = new ConcurrentHashMap<>();
}
