# Implementation of redis server with Java

---
Description

Project was done for learning purposes, feel free for contribution

---
Command List

SET ``key`` ``value``

<i>Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type. Any previous time to live associated with the key is discarded on successful SET operation.</i>

SETNX ``key`` ``value``

<i>Set key to hold string value if key does not exist. In that case, it is equal to SET. When key already holds a value, no operation is performed. SETNX is short for "SET if Not eXists".</i>

SETEX ``key`` ``value``

<i>Set key to hold the string value and set key to timeout after a given number of seconds. This command is equivalent to executing the following commands:</i>

MSET `key` `value` `[key value...]`

<i>Sets the given keys to their respective values. MSET replaces existing values with new values, just as regular SET. See MSETNX if you don't want to overwrite existing values.</i>

GET `key`

<i>Get the value of key. If the key does not exist the special value nil is returned. An error is returned if the value stored at key is not a string, because GET only handles string values.</i>

MGET `key` `[key...]`

<i>Returns the values of all specified keys. For every key that does not hold a string value or does not exist, the special value nil is returned. Because of this, the operation never fails.</i>


---
LPUSH `key` `value` `[value...]`

<i>Insert all the specified values at the head of the list stored at key. If key does not exist, it is created as empty list before performing the push operations. When key holds a value that is not a list, an error is returned</i>

RPUSH `key` `value` `[value...]`

<i>Insert all the specified values at the tail of the list stored at key. If key does not exist, it is created as empty list before performing the push operation. When key holds a value that is not a list, an error is returned.</i>

LRANGE `key` `start` `end`

<i>Returns the specified elements of the list stored at key. The offsets start and stop are zero-based indexes, with 0 being the first element of the list (the head of the list), 1 being the next element and so on.

These offsets can also be negative numbers indicating offsets starting at the end of the list.</i>

---
ZADD `key` `score` `value` `[score value...]`

<i>Adds all the specified members with the specified scores to the sorted set stored at key. It is possible to specify multiple score / member pairs. If a specified member is already a member of the sorted set, the score is updated and the element reinserted at the right position to ensure the correct ordering.</i>

ZPOPMAX `key`

<i>Removes and returns up to count members with the highest scores in the sorted set stored at key.</i>

ZPOPMIN `key`

<i>Removes and returns up to count members with the lowest scores in the sorted set stored at key.</i>

ZRANGE `key` `start` `end`

<i>Returns the specified range of elements in the sorted set stored at <key>.

ZRANGE can perform different types of range queries: by index (rank), by the score, or by lexicographical order.</i>

ZRANGE `key` `start` `end` WITHSCORES

<i>The optional WITHSCORES argument supplements the command's reply with the scores of elements returned. The returned list contains value1,score1,...,valueN,scoreN instead of value1,...,valueN. Client libraries are free to return a more appropriate data type (suggestion: an array with (value, score) arrays/tuples).</i>

---
HSET `key` `field` `value` `[field value...]`

<i>Sets field in the hash stored at key to value. If key does not exist, a new key holding a hash is created. If field already exists in the hash, it is overwritten.</i>

HGET `key` `field`

<i>Returns the value associated with `field` in the hash stored at key.</i>

HSETNX `key` `field` `value`

<i>Sets field in the hash stored at key to value, only if field does not yet exist. If key does not exist, a new key holding a hash is created. If field already exists, this operation has no effect.</i>

HVALS `key`

<i>Returns all values in the hash stored at key.</i>

HDEL `key` `field`

<i>Removes the specified fields from the hash stored at key. Specified fields that do not exist within this hash are ignored. If key does not exist, it is treated as an empty hash and this command returns 0.</i>

