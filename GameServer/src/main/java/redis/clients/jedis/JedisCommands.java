package redis.clients.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface JedisCommands {
    String set(String key, String value);

    String get(String key);

    Boolean exists(String key);

    String type(String key);

    Long expire(String key, int seconds);

    Long expireAt(String key, long unixTime);

    Long ttl(String key);

    Boolean setbit(String key, long offset, boolean value);

    Boolean getbit(String key, long offset);

    Long setrange(String key, long offset, String value);

    String getrange(String key, long startOffset, long endOffset);

    String getSet(String key, String value);

    Long setnx(String key, String value);

    String setex(String key, int seconds, String value);

    Long decrBy(String key, long integer);

    Long decr(String key);

    Long incrBy(String key, long integer);

    Long incr(String key);

    Long append(String key, String value);

    String substr(String key, int start, int end);

    Long hset(String key, String field, String value);

    String hget(String key, String field);

    Long hsetnx(String key, String field, String value);

    String hmset(String key, Map<String, String> hash);

    List<String> hmget(String key, String... fields);

    Long hincrBy(String key, String field, long value);

    Boolean hexists(String key, String field);

    Long hdel(String key, String... field);

    Long hlen(String key);

    Set<String> hkeys(String key);

    List<String> hvals(String key);

    Map<String, String> hgetAll(String key);

    Long rpush(String key, String... string);

    Long lpush(String key, String... string);

    Long llen(String key);

    List<String> lrange(String key, long start, long end);

    String ltrim(String key, long start, long end);

    String lindex(String key, long index);

    String lset(String key, long index, String value);

    Long lrem(String key, long count, String value);

    String lpop(String key);

    String rpop(String key);

    Long sadd(String key, String... member);

    Set<String> smembers(String key);

    Long srem(String key, String... member);

    String spop(String key);

    Long scard(String key);

    Boolean sismember(String key, String member);

    String srandmember(String key);

    Long zadd(String key, double score, String member);

    Long zadd(String key, Map<Double, String> scoreMembers);

    Set<String> zrange(String key, long start, long end);

    Long zrem(String key, String... member);

    Double zincrby(String key, double score, String member);

    Long zrank(String key, String member);

    Long zrevrank(String key, String member);

    Set<String> zrevrange(String key, long start, long end);

    Set<Tuple> zrangeWithScores(String key, long start, long end);

    Set<Tuple> zrevrangeWithScores(String key, long start, long end);

    Long zcard(String key);

    Double zscore(String key, String member);

    List<String> sort(String key);

    List<String> sort(String key, SortingParams sortingParameters);

    Long zcount(String key, double min, double max);

    Long zcount(String key, String min, String max);

    Set<String> zrangeByScore(String key, double min, double max);

    Set<String> zrangeByScore(String key, String min, String max);

    Set<String> zrevrangeByScore(String key, double max, double min);

    Set<String> zrangeByScore(String key, double min, double max, int offset,
            int count);

    Set<String> zrevrangeByScore(String key, String max, String min);

    Set<String> zrangeByScore(String key, String min, String max, int offset,
            int count);

    Set<String> zrevrangeByScore(String key, double max, double min,
            int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max,
            int offset, int count);

    Set<String> zrevrangeByScore(String key, String max, String min,
            int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max,
            int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min,
            int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min,
            int offset, int count);

    Long zremrangeByRank(String key, long start, long end);

    Long zremrangeByScore(String key, double start, double end);

    Long zremrangeByScore(String key, String start, String end);

    Long linsert(String key, Client.LIST_POSITION where, String pivot,
            String value);

    Long lpushx(String key, String string);

    Long rpushx(String key, String string); 
}
