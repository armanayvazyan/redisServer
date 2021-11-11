package server;

import org.javatuples.Pair;
import server.exceptions.WrongNumberOfArgumentsException;

import java.util.ArrayList;
import java.util.List;

public enum Commands implements Processable {
    /***Simple KEY VALUE Commands****/
    SET {
        @Override
        public String process(String[] command) {
            if (command.length != 3) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.saveSimpleKayeValue(new Pair<>(command[1], command[2]));
        }
    },
    SETNX {
        @Override
        public String process(String[] command) {
            if (command.length != 3) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.saveSimpleKayeValueIfNotExist(command[1], command[2]);
        }
    },
    SETEX {
        @Override
        public String process(String[] command) {
            if (command.length != 4) throw new WrongNumberOfArgumentsException(command[0]);
            try {
                Integer seconds = Integer.valueOf(command[2]);
                return StorageService.saveSimpleKayeValueWithExpiration(command[1], command[3], seconds);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("ERR ERR value is not an integer or out of range");
            }
        }
    },
    MSET {
        @Override
        public String process(String[] command) {
            if (command.length < 3 || command.length % 2 == 0) throw new WrongNumberOfArgumentsException(command[0]);
            Pair<String, String>[] pairs = new Pair[(command.length - 1) / 2];
            for (int i = 1; i < command.length; i += 2) {
                pairs[(i - 1) / 2] = new Pair(command[i], command[i + 1]);
            }
            return StorageService.saveSimpleKayeValue(pairs);
        }
    },
    GET {
        @Override
        public String process(String[] command) {
            if (command.length != 2) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.getSimpleValueForKey(command[1]);
        }
    },
    MGET {
        @Override
        public String process(String[] command) {
            if (command.length < 2) throw new WrongNumberOfArgumentsException(command[0]);
            String[] keysArray = new String[command.length - 1];
            System.arraycopy(command, 1, keysArray, 0, command.length - 1);
            return StorageService.getSimpleValueForKey(keysArray);
        }
    },
    /***List Commands****/
    LPUSH {
        @Override
        public String process(String[] command) {
            if (command.length < 3) throw new WrongNumberOfArgumentsException(command[0]);
            String[] valuesArray = new String[command.length - 2];
            if (command.length > 3) {
                for (int i = 2; i < command.length; i++) {
                    valuesArray[i - 2] = command[i];
                }
                return StorageService.leftPush(command[1], valuesArray);
            }
            return StorageService.leftPush(command[1], command[2]);
        }
    },
    RPUSH {
        @Override
        public String process(String[] command) {
            if (command.length < 3) throw new WrongNumberOfArgumentsException(command[0]);
            String[] valuesArray = new String[command.length - 2];
            if (command.length > 3) {
                for (int i = 2; i < command.length; i++) {
                    valuesArray[i - 2] = command[i];
                }
                return StorageService.rightPush(command[1], valuesArray);
            }
            return StorageService.rightPush(command[1], command[2]);
        }
    },
    LRANGE {
        @Override
        public String process(String[] command) {
            if (command.length < 4) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.getAllInRange(command[1], command[2], command[3]);
        }
    },
    /***Set with Score Commands****/
    ZADD {
        @Override
        public String process(String[] command) {
            if (command.length < 4) throw new WrongNumberOfArgumentsException(command[0]);
            int pairsCount = (command.length - 2) / 2;
            List<Pair<Integer, String>> pairs = new ArrayList<>(pairsCount);
            for (int i = 2; i < command.length; i += 2) {
                try {
                    pairs.add(new Pair<>(Integer.parseInt(command[i]), command[i + 1]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    return "ERR ERR value is not a valid float"; //TODO make exception
                }
            }
            return StorageService.addIntoSortedSetWithScore(command[1], pairs);
        }
    },
    ZRANGE {
        @Override
        public String process(String[] command) {
            if (command.length < 4) throw new WrongNumberOfArgumentsException(command[0]);
            if (command[command.length - 1].equals("WITHSCORES")) {
                return StorageService.getAllSortedSetInRange(command[1], command[2], command[3], true);
            }
            return StorageService.getAllSortedSetInRange(command[1], command[2], command[3]);
        }
    },
    ZPOPMAX {
        @Override
        public String process(String[] command) {
            if (command.length < 2) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.popMaximumKey(command[1]);
        }
    },
    ZPOPMIN {
        @Override
        public String process(String[] command) {
            if (command.length < 2) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.popMinimumKey(command[1]);
        }
    },
    /***Set with Hashes Commands****/
    HSET {
        @Override
        public String process(String[] command) {
            if (command.length < 4) throw new WrongNumberOfArgumentsException(command[0]);
            int pairsCount = (command.length - 2) / 2;
            List<Pair<String, String>> pairs = new ArrayList<>(pairsCount);
            for (int i = 2; i < command.length; i += 2) {
                try {
                    pairs.add(new Pair<>(command[i], command[i + 1]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    return "ERR ERR value is not a valid float"; //TODO make exception
                }
            }
            return StorageService.addIntoHashMap(command[1], pairs);
        }
    },
    HGET {
        @Override
        public String process(String[] command) {
            if (command.length != 3) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.getValuesFromHashMap(command[1], command[2]);
        }
    },
    HSETNX {
        @Override
        public String process(String[] command) {
            if (command.length < 4) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.addIntoHashMapIfNotExists(command[1], command[2], command[3]);
        }
    },
    HVALS {
        @Override
        public String process(String[] command) {
            if (command.length != 2) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.getAllValuesFromHashmap(command[1]);
        }
    },
    HDEL {
        @Override
        public String process(String[] command) {
            if (command.length < 3) throw new WrongNumberOfArgumentsException(command[0]);
            return StorageService.removeKeyFromHashmap(command[1], command[2]);
        }
    }
}
