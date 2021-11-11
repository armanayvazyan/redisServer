package server;

import org.javatuples.Pair;
import org.javatuples.Tuple;

import javax.swing.text.html.parser.Entity;
import java.util.*;

import static server.DAO.hashsetMap;

public class StorageService {

    // --------------------------------------------
    // --------------- KEY - VALUE ----------------
    // --------------------------------------------

    @SafeVarargs
    public static String saveSimpleKayeValue(Pair<String, String> ...pairs) {
        for (Pair<String, String> pair: pairs) {
            DAO.simpleKeyValueMap.put(pair.getValue0(), pair.getValue1());
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

    // --------------------------------------------
    // --------------- L I S T S ------------------
    // --------------------------------------------

    public static String leftPush(String key, String ...values) {
        DAO.listMap.computeIfAbsent(key, k -> new LinkedList<>());
        for (String value: values) {
            DAO.listMap.get(key).addFirst(value);
        }
        return "(integer) " + DAO.listMap.get(key).size();
    }

    public static String rightPush(String key, String ...values) {
        DAO.listMap.computeIfAbsent(key, k -> new LinkedList<>());
        for (String value: values) {
            DAO.listMap.get(key).add(value);
        }
        return "(integer) " + DAO.listMap.get(key).size();
    }

    public static String getAllInRange(String key, String s, String e) {
        StringBuilder returnString = new StringBuilder("\n");
        int start, end;
        if(DAO.listMap.get(key) == null)
            return "ERR ERR value is not an integer or out of range";
        try {
            start = Integer.parseInt(s);
            end = Integer.parseInt(e);
        } catch (NumberFormatException error) {
            return "ERR ERR value is not an integer or out of range";
        }

        if(start > DAO.listMap.get(key).size()- 1)
            return "(empty list or set)";

        if(end == -1 || end > DAO.listMap.get(key).size() -1) {
            end = DAO.listMap.get(key).size() -1;
        }
        for (int i = start; i <= end ; i++) {

            returnString.append(i).append(")").append(DAO.listMap.get(key).get(i)).append("\n");
        }
        return returnString.toString();
    }

    // --------------------------------------------
    // ----------- SORTED SET SCORED---------------
    // --------------------------------------------

    public static String addIntoSortedSetWithScore(String key, List<Pair<Integer, String>> pairs) {
        if(DAO.sortedSetWithScore.get(key) == null) {
            Map<Integer, LinkedList<String>> map = new HashMap<>();
            for (Pair<Integer, String> pair : pairs) {
                LinkedList<String> list = new LinkedList<>();
                list.add(pair.getValue1());
                map.put(pair.getValue0(), list);
            }
            DAO.sortedSetWithScore.put(key, map);
            System.out.println(DAO.sortedSetWithScore.toString());
            return "(integer) " + pairs.size();
        }
        for (Pair<Integer, String> pair : pairs) {
            if(DAO.sortedSetWithScore.get(key).get(pair.getValue0()) == null) {
                LinkedList<String> list = new LinkedList<>();
                list.add(pair.getValue1());
                DAO.sortedSetWithScore.get(key).put(pair.getValue0(), list);
            } else
                DAO.sortedSetWithScore.get(key).get(pair.getValue0()).add(pair.getValue1());
        }
        System.out.println(DAO.sortedSetWithScore.toString());
        return "(integer) " + pairs.size();
    }

    public static String getAllSortedSetInRange(String key, String s, String e) {
       return getAllSortedSetInRange(key, s, e, false);
    }

    public static String getAllSortedSetInRange(String key, String s, String e, boolean withScores) {
        StringBuilder returnString = new StringBuilder("\n");
        int start, end;
        if(DAO.sortedSetWithScore.get(key) == null)
            return "ERR ERR value is not an integer or out of range";
        try {
            start = Integer.parseInt(s);
            end = Integer.parseInt(e);
        } catch (NumberFormatException error) {
            return "ERR ERR value is not an integer or out of range";
        }

        if(start > DAO.sortedSetWithScore.get(key).size()- 1)
            return "(empty list or set)";

        int totalCountOfElements = 0;
        for (Integer k : DAO.sortedSetWithScore.get(key).keySet()) {
            totalCountOfElements += DAO.sortedSetWithScore.get(key).get(k).size();
        }

        if(end == -1 || end > DAO.sortedSetWithScore.get(key).size() -1) {
            end = withScores ? totalCountOfElements * 2 : totalCountOfElements;
        }

        int i = start;
        for (Integer k1 :DAO.sortedSetWithScore.get(key).keySet()) {
            for (String k2 : DAO.sortedSetWithScore.get(key).get(k1)) {
                returnString.append(++i).append(")").append(k2).append("\n");
                if(withScores) {
                    returnString.append(++i).append(")").append(k1).append("\n");
                }
            }
            if(i == end) break;
        }
        return returnString.toString();
    }

    public static String popMaximumKey(String key) {
        if(DAO.sortedSetWithScore.get(key) == null) {
            return "(empty list or set)";
        }
        int max = 0;
        int cursor = 0;
        for (String k1 : DAO.sortedSetWithScore.keySet()) {
            for (Integer k2 : DAO.sortedSetWithScore.get(k1).keySet()) {
                if (cursor == 0) {
                    max = k2;
                    cursor++;
                }
                if (k2 > max) {
                    max = k2;
                }
            }
        }
        int i = 0;
        StringBuilder returnString = new StringBuilder("\n");
        returnString.append(++i).append(")").append(DAO.sortedSetWithScore.get(key).get(max).get(0)).append("\n");
        returnString.append(++i).append(")").append(max).append("\n");

        if(DAO.sortedSetWithScore.get(key).get(max).size() == 1)
            DAO.sortedSetWithScore.get(key).get(max).remove();
        else
            DAO.sortedSetWithScore.get(key).get(max).remove(0);

        return returnString.toString();
    }

    public static String popMinimumKey(String key) {
        if(DAO.sortedSetWithScore.get(key) == null) {
            return "(empty list or set)";
        }
        int min = 0;
        int cursor = 0;
        for (String k1 : DAO.sortedSetWithScore.keySet()) {
            for (Integer k2 : DAO.sortedSetWithScore.get(k1).keySet()) {
                if (cursor == 0) {
                    min = k2;
                    cursor++;
                }
                if (k2 < min) {
                    min = k2;
                }
            }
        }
        int i = 0;
        StringBuilder returnString = new StringBuilder("\n");
        returnString.append(++i).append(")").append(DAO.sortedSetWithScore.get(key).get(min).get(0)).append("\n");
        returnString.append(++i).append(")").append(min).append("\n");

        if(DAO.sortedSetWithScore.get(key).get(min).size() == 1)
            DAO.sortedSetWithScore.get(key).get(min).remove();
        else
            DAO.sortedSetWithScore.get(key).get(min).remove(0);

        return returnString.toString();
    }

    // --------------------------------------------
    // ----------- HASH SET SCORED---------------
    // --------------------------------------------

    public static String addIntoHashMap(String key, List<Pair<String,String>> pairs) {
        hashsetMap.computeIfAbsent(key, k -> new HashMap<>());
        for (Pair<String, String> pair : pairs) {
            hashsetMap.get(key).put(pair.getValue0(), pair.getValue1());
        }
        return "(integer) " + pairs.size();
    }

    public static String addIntoHashMapIfNotExists(String key, String field, String value) {
        if(hashsetMap.get(key) != null)
            return "(integer) 0";

        hashsetMap.get(key).put(field, value);
        return "(integer) 1";
    }

    public static String getValuesFromHashMap(String key, String field) {
        if(hashsetMap.get(key) == null) {
            return "(nil)";
        }
        return hashsetMap.get(key).get(field);
    }

    public static String getAllValuesFromHashmap(String key) {
        if(hashsetMap.get(key) == null) {
            return "(nil)";
        }
        int i = 0;
        StringBuilder returnString = new StringBuilder("\n");
        for (String k1 : hashsetMap.get(key).keySet()) {
            String value = hashsetMap.get(key).get(k1);
            returnString.append(++i).append(")").append(value).append("\n");
        }
        return returnString.toString();
    }

    public static String removeKeyFromHashmap(String key, String field) {
        if(hashsetMap.get(key) == null) {
            return "(integer) 0";
        }
        hashsetMap.get(key).remove(field);
        return "(integer) 1";
    }
}

