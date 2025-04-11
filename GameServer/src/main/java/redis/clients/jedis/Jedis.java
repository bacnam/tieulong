package redis.clients.jedis;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.util.SafeEncoder;
import redis.clients.util.Slowlog;

import java.util.*;

public class Jedis extends BinaryJedis implements JedisCommands {
    public Jedis(final String host) {
        super(host);
    }

    public Jedis(final String host, final int port) {
        super(host, port);
    }

    public Jedis(final String host, final int port, final int timeout) {
        super(host, port, timeout);
    }

    public Jedis(JedisShardInfo shardInfo) {
        super(shardInfo);
    }

    public String ping() {
        checkIsInMulti();
        client.ping();
        return client.getStatusCodeReply();
    }

    public String set(final String key, String value) {
        checkIsInMulti();
        client.set(key, value);
        return client.getStatusCodeReply();
    }

    public String get(final String key) {
        checkIsInMulti();
        client.sendCommand(Protocol.Command.GET, key);
        return client.getBulkReply();
    }

    public String quit() {
        checkIsInMulti();
        client.quit();
        return client.getStatusCodeReply();
    }

    public Boolean exists(final String key) {
        checkIsInMulti();
        client.exists(key);
        return client.getIntegerReply() == 1;
    }

    public Long del(final String... keys) {
        checkIsInMulti();
        client.del(keys);
        return client.getIntegerReply();
    }

    public String type(final String key) {
        checkIsInMulti();
        client.type(key);
        return client.getStatusCodeReply();
    }

    public String flushDB() {
        checkIsInMulti();
        client.flushDB();
        return client.getStatusCodeReply();
    }

    public Set<String> keys(final String pattern) {
        checkIsInMulti();
        client.keys(pattern);
        return BuilderFactory.STRING_SET
                .build(client.getBinaryMultiBulkReply());
    }

    public String randomKey() {
        checkIsInMulti();
        client.randomKey();
        return client.getBulkReply();
    }

    public String rename(final String oldkey, final String newkey) {
        checkIsInMulti();
        client.rename(oldkey, newkey);
        return client.getStatusCodeReply();
    }

    public Long renamenx(final String oldkey, final String newkey) {
        checkIsInMulti();
        client.renamenx(oldkey, newkey);
        return client.getIntegerReply();
    }

    public Long expire(final String key, final int seconds) {
        checkIsInMulti();
        client.expire(key, seconds);
        return client.getIntegerReply();
    }

    public Long expireAt(final String key, final long unixTime) {
        checkIsInMulti();
        client.expireAt(key, unixTime);
        return client.getIntegerReply();
    }

    public Long ttl(final String key) {
        checkIsInMulti();
        client.ttl(key);
        return client.getIntegerReply();
    }

    public String select(final int index) {
        checkIsInMulti();
        client.select(index);
        return client.getStatusCodeReply();
    }

    public Long move(final String key, final int dbIndex) {
        checkIsInMulti();
        client.move(key, dbIndex);
        return client.getIntegerReply();
    }

    public String flushAll() {
        checkIsInMulti();
        client.flushAll();
        return client.getStatusCodeReply();
    }

    public String getSet(final String key, final String value) {
        checkIsInMulti();
        client.getSet(key, value);
        return client.getBulkReply();
    }

    public List<String> mget(final String... keys) {
        checkIsInMulti();
        client.mget(keys);
        return client.getMultiBulkReply();
    }

    public Long setnx(final String key, final String value) {
        checkIsInMulti();
        client.setnx(key, value);
        return client.getIntegerReply();
    }

    public String setex(final String key, final int seconds, final String value) {
        checkIsInMulti();
        client.setex(key, seconds, value);
        return client.getStatusCodeReply();
    }

    public String mset(final String... keysvalues) {
        checkIsInMulti();
        client.mset(keysvalues);
        return client.getStatusCodeReply();
    }

    public Long msetnx(final String... keysvalues) {
        checkIsInMulti();
        client.msetnx(keysvalues);
        return client.getIntegerReply();
    }

    public Long decrBy(final String key, final long integer) {
        checkIsInMulti();
        client.decrBy(key, integer);
        return client.getIntegerReply();
    }

    public Long decr(final String key) {
        checkIsInMulti();
        client.decr(key);
        return client.getIntegerReply();
    }

    public Long incrBy(final String key, final long integer) {
        checkIsInMulti();
        client.incrBy(key, integer);
        return client.getIntegerReply();
    }

    public Long incr(final String key) {
        checkIsInMulti();
        client.incr(key);
        return client.getIntegerReply();
    }

    public Long append(final String key, final String value) {
        checkIsInMulti();
        client.append(key, value);
        return client.getIntegerReply();
    }

    public String substr(final String key, final int start, final int end) {
        checkIsInMulti();
        client.substr(key, start, end);
        return client.getBulkReply();
    }

    public Long hset(final String key, final String field, final String value) {
        checkIsInMulti();
        client.hset(key, field, value);
        return client.getIntegerReply();
    }

    public String hget(final String key, final String field) {
        checkIsInMulti();
        client.hget(key, field);
        return client.getBulkReply();
    }

    public Long hsetnx(final String key, final String field, final String value) {
        checkIsInMulti();
        client.hsetnx(key, field, value);
        return client.getIntegerReply();
    }

    public String hmset(final String key, final Map<String, String> hash) {
        checkIsInMulti();
        client.hmset(key, hash);
        return client.getStatusCodeReply();
    }

    public List<String> hmget(final String key, final String... fields) {
        checkIsInMulti();
        client.hmget(key, fields);
        return client.getMultiBulkReply();
    }

    public Long hincrBy(final String key, final String field, final long value) {
        checkIsInMulti();
        client.hincrBy(key, field, value);
        return client.getIntegerReply();
    }

    public Boolean hexists(final String key, final String field) {
        checkIsInMulti();
        client.hexists(key, field);
        return client.getIntegerReply() == 1;
    }

    public Long hdel(final String key, final String... fields) {
        checkIsInMulti();
        client.hdel(key, fields);
        return client.getIntegerReply();
    }

    public Long hlen(final String key) {
        checkIsInMulti();
        client.hlen(key);
        return client.getIntegerReply();
    }

    public Set<String> hkeys(final String key) {
        checkIsInMulti();
        client.hkeys(key);
        return BuilderFactory.STRING_SET
                .build(client.getBinaryMultiBulkReply());
    }

    public List<String> hvals(final String key) {
        checkIsInMulti();
        client.hvals(key);
        final List<String> lresult = client.getMultiBulkReply();
        return lresult;
    }

    public Map<String, String> hgetAll(final String key) {
        checkIsInMulti();
        client.hgetAll(key);
        return BuilderFactory.STRING_MAP
                .build(client.getBinaryMultiBulkReply());
    }

    public Long rpush(final String key, final String... strings) {
        checkIsInMulti();
        client.rpush(key, strings);
        return client.getIntegerReply();
    }

    public Long lpush(final String key, final String... strings) {
        checkIsInMulti();
        client.lpush(key, strings);
        return client.getIntegerReply();
    }

    public Long llen(final String key) {
        checkIsInMulti();
        client.llen(key);
        return client.getIntegerReply();
    }

    public List<String> lrange(final String key, final long start,
                               final long end) {
        checkIsInMulti();
        client.lrange(key, start, end);
        return client.getMultiBulkReply();
    }

    public String ltrim(final String key, final long start, final long end) {
        checkIsInMulti();
        client.ltrim(key, start, end);
        return client.getStatusCodeReply();
    }

    public String lindex(final String key, final long index) {
        checkIsInMulti();
        client.lindex(key, index);
        return client.getBulkReply();
    }

    public String lset(final String key, final long index, final String value) {
        checkIsInMulti();
        client.lset(key, index, value);
        return client.getStatusCodeReply();
    }

    public Long lrem(final String key, final long count, final String value) {
        checkIsInMulti();
        client.lrem(key, count, value);
        return client.getIntegerReply();
    }

    public String lpop(final String key) {
        checkIsInMulti();
        client.lpop(key);
        return client.getBulkReply();
    }

    public String rpop(final String key) {
        checkIsInMulti();
        client.rpop(key);
        return client.getBulkReply();
    }

    public String rpoplpush(final String srckey, final String dstkey) {
        checkIsInMulti();
        client.rpoplpush(srckey, dstkey);
        return client.getBulkReply();
    }

    public Long sadd(final String key, final String... members) {
        checkIsInMulti();
        client.sadd(key, members);
        return client.getIntegerReply();
    }

    public Set<String> smembers(final String key) {
        checkIsInMulti();
        client.smembers(key);
        final List<String> members = client.getMultiBulkReply();
        return new HashSet<String>(members);
    }

    public Long srem(final String key, final String... members) {
        checkIsInMulti();
        client.srem(key, members);
        return client.getIntegerReply();
    }

    public String spop(final String key) {
        checkIsInMulti();
        client.spop(key);
        return client.getBulkReply();
    }

    public Long smove(final String srckey, final String dstkey,
                      final String member) {
        checkIsInMulti();
        client.smove(srckey, dstkey, member);
        return client.getIntegerReply();
    }

    public Long scard(final String key) {
        checkIsInMulti();
        client.scard(key);
        return client.getIntegerReply();
    }

    public Boolean sismember(final String key, final String member) {
        checkIsInMulti();
        client.sismember(key, member);
        return client.getIntegerReply() == 1;
    }

    public Set<String> sinter(final String... keys) {
        checkIsInMulti();
        client.sinter(keys);
        final List<String> members = client.getMultiBulkReply();
        return new HashSet<String>(members);
    }

    public Long sinterstore(final String dstkey, final String... keys) {
        checkIsInMulti();
        client.sinterstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<String> sunion(final String... keys) {
        checkIsInMulti();
        client.sunion(keys);
        final List<String> members = client.getMultiBulkReply();
        return new HashSet<String>(members);
    }

    public Long sunionstore(final String dstkey, final String... keys) {
        checkIsInMulti();
        client.sunionstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<String> sdiff(final String... keys) {
        checkIsInMulti();
        client.sdiff(keys);
        return BuilderFactory.STRING_SET
                .build(client.getBinaryMultiBulkReply());
    }

    public Long sdiffstore(final String dstkey, final String... keys) {
        checkIsInMulti();
        client.sdiffstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public String srandmember(final String key) {
        checkIsInMulti();
        client.srandmember(key);
        return client.getBulkReply();
    }

    public Long zadd(final String key, final double score, final String member) {
        checkIsInMulti();
        client.zadd(key, score, member);
        return client.getIntegerReply();
    }

    public Long zadd(final String key, final Map<Double, String> scoreMembers) {
        checkIsInMulti();
        client.zadd(key, scoreMembers);
        return client.getIntegerReply();
    }

    public Set<String> zrange(final String key, final long start, final long end) {
        checkIsInMulti();
        client.zrange(key, start, end);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
    }

    public Long zrem(final String key, final String... members) {
        checkIsInMulti();
        client.zrem(key, members);
        return client.getIntegerReply();
    }

    public Double zincrby(final String key, final double score,
                          final String member) {
        checkIsInMulti();
        client.zincrby(key, score, member);
        String newscore = client.getBulkReply();
        return Double.valueOf(newscore);
    }

    public Long zrank(final String key, final String member) {
        checkIsInMulti();
        client.zrank(key, member);
        return client.getIntegerReply();
    }

    public Long zrevrank(final String key, final String member) {
        checkIsInMulti();
        client.zrevrank(key, member);
        return client.getIntegerReply();
    }

    public Set<String> zrevrange(final String key, final long start,
                                 final long end) {
        checkIsInMulti();
        client.zrevrange(key, start, end);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
    }

    public Set<Tuple> zrangeWithScores(final String key, final long start,
                                       final long end) {
        checkIsInMulti();
        client.zrangeWithScores(key, start, end);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start,
                                          final long end) {
        checkIsInMulti();
        client.zrevrangeWithScores(key, start, end);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Long zcard(final String key) {
        checkIsInMulti();
        client.zcard(key);
        return client.getIntegerReply();
    }

    public Double zscore(final String key, final String member) {
        checkIsInMulti();
        client.zscore(key, member);
        final String score = client.getBulkReply();
        return (score != null ? new Double(score) : null);
    }

    public String watch(final String... keys) {
        client.watch(keys);
        return client.getStatusCodeReply();
    }

    public List<String> sort(final String key) {
        checkIsInMulti();
        client.sort(key);
        return client.getMultiBulkReply();
    }

    public List<String> sort(final String key,
                             final SortingParams sortingParameters) {
        checkIsInMulti();
        client.sort(key, sortingParameters);
        return client.getMultiBulkReply();
    }

    public List<String> blpop(final int timeout, final String... keys) {
        checkIsInMulti();
        List<String> args = new ArrayList<String>();
        for (String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));

        client.blpop(args.toArray(new String[args.size()]));
        client.setTimeoutInfinite();
        final List<String> multiBulkReply = client.getMultiBulkReply();
        client.rollbackTimeout();
        return multiBulkReply;
    }

    public Long sort(final String key, final SortingParams sortingParameters,
                     final String dstkey) {
        checkIsInMulti();
        client.sort(key, sortingParameters, dstkey);
        return client.getIntegerReply();
    }

    public Long sort(final String key, final String dstkey) {
        checkIsInMulti();
        client.sort(key, dstkey);
        return client.getIntegerReply();
    }

    public List<String> brpop(final int timeout, final String... keys) {
        checkIsInMulti();
        List<String> args = new ArrayList<String>();
        for (String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));

        client.brpop(args.toArray(new String[args.size()]));
        client.setTimeoutInfinite();
        List<String> multiBulkReply = client.getMultiBulkReply();
        client.rollbackTimeout();

        return multiBulkReply;
    }

    public String auth(final String password) {
        checkIsInMulti();
        client.auth(password);
        return client.getStatusCodeReply();
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        checkIsInMulti();
        connect();
        client.setTimeoutInfinite();
        jedisPubSub.proceed(client, channels);
        client.rollbackTimeout();
    }

    public Long publish(String channel, String message) {
        checkIsInMulti();
        client.publish(channel, message);
        return client.getIntegerReply();
    }

    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        checkIsInMulti();
        connect();
        client.setTimeoutInfinite();
        jedisPubSub.proceedWithPatterns(client, patterns);
        client.rollbackTimeout();
    }

    public Long zcount(final String key, final double min, final double max) {
        checkIsInMulti();
        client.zcount(key, min, max);
        return client.getIntegerReply();
    }

    public Long zcount(final String key, final String min, final String max) {
        checkIsInMulti();
        client.zcount(key, min, max);
        return client.getIntegerReply();
    }

    public Set<String> zrangeByScore(final String key, final double min,
                                     final double max) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<String> zrangeByScore(final String key, final String min,
                                     final String max) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<String> zrangeByScore(final String key, final double min,
                                     final double max, final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max, offset, count);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<String> zrangeByScore(final String key, final String min,
                                     final String max, final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max, offset, count);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final double min, final double max) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final String min, final String max) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final double min, final double max, final int offset,
                                              final int count) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final String min, final String max, final int offset,
                                              final int count) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    private Set<Tuple> getTupledSet() {
        checkIsInMulti();
        List<String> membersWithScores = client.getMultiBulkReply();
        Set<Tuple> set = new LinkedHashSet<Tuple>();
        Iterator<String> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            set.add(new Tuple(iterator.next(), Double.valueOf(iterator.next())));
        }
        return set;
    }

    public Set<String> zrevrangeByScore(final String key, final double max,
                                        final double min) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<String> zrevrangeByScore(final String key, final String max,
                                        final String min) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<String> zrevrangeByScore(final String key, final double max,
                                        final double min, final int offset, final int count) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min, offset, count);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key,
                                                 final double max, final double min) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key,
                                                 final double max, final double min, final int offset,
                                                 final int count) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key,
                                                 final String max, final String min, final int offset,
                                                 final int count) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<String> zrevrangeByScore(final String key, final String max,
                                        final String min, final int offset, final int count) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min, offset, count);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key,
                                                 final String max, final String min) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Long zremrangeByRank(final String key, final long start,
                                final long end) {
        checkIsInMulti();
        client.zremrangeByRank(key, start, end);
        return client.getIntegerReply();
    }

    public Long zremrangeByScore(final String key, final double start,
                                 final double end) {
        checkIsInMulti();
        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
    }

    public Long zremrangeByScore(final String key, final String start,
                                 final String end) {
        checkIsInMulti();
        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
    }

    public Long zunionstore(final String dstkey, final String... sets) {
        checkIsInMulti();
        client.zunionstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zunionstore(final String dstkey, final ZParams params,
                            final String... sets) {
        checkIsInMulti();
        client.zunionstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final String dstkey, final String... sets) {
        checkIsInMulti();
        client.zinterstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final String dstkey, final ZParams params,
                            final String... sets) {
        checkIsInMulti();
        client.zinterstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public Long strlen(final String key) {
        client.strlen(key);
        return client.getIntegerReply();
    }

    public Long lpushx(final String key, final String string) {
        client.lpushx(key, string);
        return client.getIntegerReply();
    }

    public Long persist(final String key) {
        client.persist(key);
        return client.getIntegerReply();
    }

    public Long rpushx(final String key, final String string) {
        client.rpushx(key, string);
        return client.getIntegerReply();
    }

    public String echo(final String string) {
        client.echo(string);
        return client.getBulkReply();
    }

    public Long linsert(final String key, final LIST_POSITION where,
                        final String pivot, final String value) {
        client.linsert(key, where, pivot, value);
        return client.getIntegerReply();
    }

    public String brpoplpush(String source, String destination, int timeout) {
        client.brpoplpush(source, destination, timeout);
        client.setTimeoutInfinite();
        String reply = client.getBulkReply();
        client.rollbackTimeout();
        return reply;
    }

    public Boolean setbit(String key, long offset, boolean value) {
        client.setbit(key, offset, value);
        return client.getIntegerReply() == 1;
    }

    public Boolean getbit(String key, long offset) {
        client.getbit(key, offset);
        return client.getIntegerReply() == 1;
    }

    public Long setrange(String key, long offset, String value) {
        client.setrange(key, offset, value);
        return client.getIntegerReply();
    }

    public String getrange(String key, long startOffset, long endOffset) {
        client.getrange(key, startOffset, endOffset);
        return client.getBulkReply();
    }

    public List<String> configGet(final String pattern) {
        client.configGet(pattern);
        return client.getMultiBulkReply();
    }

    public String configSet(final String parameter, final String value) {
        client.configSet(parameter, value);
        return client.getStatusCodeReply();
    }

    public Object eval(String script, int keyCount, String... params) {
        client.setTimeoutInfinite();
        client.eval(script, keyCount, params);

        return getEvalResult();
    }

    private String[] getParams(List<String> keys, List<String> args) {
        int keyCount = keys.size();
        int argCount = args.size();

        String[] params = new String[keyCount + args.size()];

        for (int i = 0; i < keyCount; i++)
            params[i] = keys.get(i);

        for (int i = 0; i < argCount; i++)
            params[keyCount + i] = args.get(i);

        return params;
    }

    public Object eval(String script, List<String> keys, List<String> args) {
        return eval(script, keys.size(), getParams(keys, args));
    }

    public Object eval(String script) {
        return eval(script, 0);
    }

    public Object evalsha(String script) {
        return evalsha(script, 0);
    }

    private Object getEvalResult() {
        Object result = client.getOne();

        if (result instanceof byte[])
            return SafeEncoder.encode((byte[]) result);

        if (result instanceof List<?>) {
            List<?> list = (List<?>) result;
            List<String> listResult = new ArrayList<String>(list.size());
            for (Object bin : list)
                listResult.add(SafeEncoder.encode((byte[]) bin));

            return listResult;
        }

        return result;
    }

    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        return evalsha(sha1, keys.size(), getParams(keys, args));
    }

    public Object evalsha(String sha1, int keyCount, String... params) {
        checkIsInMulti();
        client.evalsha(sha1, keyCount, params);

        return getEvalResult();
    }

    public Boolean scriptExists(String sha1) {
        String[] a = new String[1];
        a[0] = sha1;
        return scriptExists(a).get(0);
    }

    public List<Boolean> scriptExists(String... sha1) {
        client.scriptExists(sha1);
        List<Long> result = client.getIntegerMultiBulkReply();
        List<Boolean> exists = new ArrayList<Boolean>();

        for (Long value : result)
            exists.add(value == 1);

        return exists;
    }

    public String scriptLoad(String script) {
        client.scriptLoad(script);
        return client.getBulkReply();
    }

    public List<Slowlog> slowlogGet() {
        client.slowlogGet();
        return Slowlog.from(client.getObjectMultiBulkReply());
    }

    public List<Slowlog> slowlogGet(long entries) {
        client.slowlogGet(entries);
        return Slowlog.from(client.getObjectMultiBulkReply());
    }

    public Long objectRefcount(String string) {
        client.objectRefcount(string);
        return client.getIntegerReply();
    }

    public String objectEncoding(String string) {
        client.objectEncoding(string);
        return client.getBulkReply();
    }

    public Long objectIdletime(String string) {
        client.objectIdletime(string);
        return client.getIntegerReply();
    }
}
