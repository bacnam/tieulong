package redis.clients.jedis;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.JedisByteHashMap;
import redis.clients.util.SafeEncoder;

import java.util.*;

import static redis.clients.jedis.Protocol.toByteArray;

public class BinaryJedis implements BinaryJedisCommands {
    protected Client client = null;

    public BinaryJedis(final String host) {
        client = new Client(host);
    }

    public BinaryJedis(final String host, final int port) {
        client = new Client(host, port);
    }

    public BinaryJedis(final String host, final int port, final int timeout) {
        client = new Client(host, port);
        client.setTimeout(timeout);
    }

    public BinaryJedis(final JedisShardInfo shardInfo) {
        client = new Client(shardInfo.getHost(), shardInfo.getPort());
        client.setTimeout(shardInfo.getTimeout());
        client.setPassword(shardInfo.getPassword());
    }

    public String ping() {
        checkIsInMulti();
        client.ping();
        return client.getStatusCodeReply();
    }

    public String set(final byte[] key, final byte[] value) {
        checkIsInMulti();
        client.set(key, value);
        return client.getStatusCodeReply();
    }

    public byte[] get(final byte[] key) {
        checkIsInMulti();
        client.get(key);
        return client.getBinaryBulkReply();
    }

    public String quit() {
        checkIsInMulti();
        client.quit();
        return client.getStatusCodeReply();
    }

    public Boolean exists(final byte[] key) {
        checkIsInMulti();
        client.exists(key);
        return client.getIntegerReply() == 1;
    }

    public Long del(final byte[]... keys) {
        checkIsInMulti();
        client.del(keys);
        return client.getIntegerReply();
    }

    public String type(final byte[] key) {
        checkIsInMulti();
        client.type(key);
        return client.getStatusCodeReply();
    }

    public String flushDB() {
        checkIsInMulti();
        client.flushDB();
        return client.getStatusCodeReply();
    }

    public Set<byte[]> keys(final byte[] pattern) {
        checkIsInMulti();
        client.keys(pattern);
        final HashSet<byte[]> keySet = new HashSet<byte[]>(
                client.getBinaryMultiBulkReply());
        return keySet;
    }

    public byte[] randomBinaryKey() {
        checkIsInMulti();
        client.randomKey();
        return client.getBinaryBulkReply();
    }

    public String rename(final byte[] oldkey, final byte[] newkey) {
        checkIsInMulti();
        client.rename(oldkey, newkey);
        return client.getStatusCodeReply();
    }

    public Long renamenx(final byte[] oldkey, final byte[] newkey) {
        checkIsInMulti();
        client.renamenx(oldkey, newkey);
        return client.getIntegerReply();
    }

    public Long dbSize() {
        checkIsInMulti();
        client.dbSize();
        return client.getIntegerReply();
    }

    public Long expire(final byte[] key, final int seconds) {
        checkIsInMulti();
        client.expire(key, seconds);
        return client.getIntegerReply();
    }

    public Long expireAt(final byte[] key, final long unixTime) {
        checkIsInMulti();
        client.expireAt(key, unixTime);
        return client.getIntegerReply();
    }

    public Long ttl(final byte[] key) {
        checkIsInMulti();
        client.ttl(key);
        return client.getIntegerReply();
    }

    public String select(final int index) {
        checkIsInMulti();
        client.select(index);
        return client.getStatusCodeReply();
    }

    public Long move(final byte[] key, final int dbIndex) {
        checkIsInMulti();
        client.move(key, dbIndex);
        return client.getIntegerReply();
    }

    public String flushAll() {
        checkIsInMulti();
        client.flushAll();
        return client.getStatusCodeReply();
    }

    public byte[] getSet(final byte[] key, final byte[] value) {
        checkIsInMulti();
        client.getSet(key, value);
        return client.getBinaryBulkReply();
    }

    public List<byte[]> mget(final byte[]... keys) {
        checkIsInMulti();
        client.mget(keys);
        return client.getBinaryMultiBulkReply();
    }

    public Long setnx(final byte[] key, final byte[] value) {
        checkIsInMulti();
        client.setnx(key, value);
        return client.getIntegerReply();
    }

    public String setex(final byte[] key, final int seconds, final byte[] value) {
        checkIsInMulti();
        client.setex(key, seconds, value);
        return client.getStatusCodeReply();
    }

    public String mset(final byte[]... keysvalues) {
        checkIsInMulti();
        client.mset(keysvalues);
        return client.getStatusCodeReply();
    }

    public Long msetnx(final byte[]... keysvalues) {
        checkIsInMulti();
        client.msetnx(keysvalues);
        return client.getIntegerReply();
    }

    public Long decrBy(final byte[] key, final long integer) {
        checkIsInMulti();
        client.decrBy(key, integer);
        return client.getIntegerReply();
    }

    public Long decr(final byte[] key) {
        checkIsInMulti();
        client.decr(key);
        return client.getIntegerReply();
    }

    public Long incrBy(final byte[] key, final long integer) {
        checkIsInMulti();
        client.incrBy(key, integer);
        return client.getIntegerReply();
    }

    public Long incr(final byte[] key) {
        checkIsInMulti();
        client.incr(key);
        return client.getIntegerReply();
    }

    public Long append(final byte[] key, final byte[] value) {
        checkIsInMulti();
        client.append(key, value);
        return client.getIntegerReply();
    }

    public byte[] substr(final byte[] key, final int start, final int end) {
        checkIsInMulti();
        client.substr(key, start, end);
        return client.getBinaryBulkReply();
    }

    public Long hset(final byte[] key, final byte[] field, final byte[] value) {
        checkIsInMulti();
        client.hset(key, field, value);
        return client.getIntegerReply();
    }

    public byte[] hget(final byte[] key, final byte[] field) {
        checkIsInMulti();
        client.hget(key, field);
        return client.getBinaryBulkReply();
    }

    public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        checkIsInMulti();
        client.hsetnx(key, field, value);
        return client.getIntegerReply();
    }

    public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        checkIsInMulti();
        client.hmset(key, hash);
        return client.getStatusCodeReply();
    }

    public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
        checkIsInMulti();
        client.hmget(key, fields);
        return client.getBinaryMultiBulkReply();
    }

    public Long hincrBy(final byte[] key, final byte[] field, final long value) {
        checkIsInMulti();
        client.hincrBy(key, field, value);
        return client.getIntegerReply();
    }

    public Boolean hexists(final byte[] key, final byte[] field) {
        checkIsInMulti();
        client.hexists(key, field);
        return client.getIntegerReply() == 1;
    }

    public Long hdel(final byte[] key, final byte[]... fields) {
        checkIsInMulti();
        client.hdel(key, fields);
        return client.getIntegerReply();
    }

    public Long hlen(final byte[] key) {
        checkIsInMulti();
        client.hlen(key);
        return client.getIntegerReply();
    }

    public Set<byte[]> hkeys(final byte[] key) {
        checkIsInMulti();
        client.hkeys(key);
        final List<byte[]> lresult = client.getBinaryMultiBulkReply();
        return new HashSet<byte[]>(lresult);
    }

    public List<byte[]> hvals(final byte[] key) {
        checkIsInMulti();
        client.hvals(key);
        final List<byte[]> lresult = client.getBinaryMultiBulkReply();
        return lresult;
    }

    public Map<byte[], byte[]> hgetAll(final byte[] key) {
        checkIsInMulti();
        client.hgetAll(key);
        final List<byte[]> flatHash = client.getBinaryMultiBulkReply();
        final Map<byte[], byte[]> hash = new JedisByteHashMap();
        final Iterator<byte[]> iterator = flatHash.iterator();
        while (iterator.hasNext()) {
            hash.put(iterator.next(), iterator.next());
        }

        return hash;
    }

    public Long rpush(final byte[] key, final byte[]... strings) {
        checkIsInMulti();
        client.rpush(key, strings);
        return client.getIntegerReply();
    }

    public Long lpush(final byte[] key, final byte[]... strings) {
        checkIsInMulti();
        client.lpush(key, strings);
        return client.getIntegerReply();
    }

    public Long llen(final byte[] key) {
        checkIsInMulti();
        client.llen(key);
        return client.getIntegerReply();
    }

    public List<byte[]> lrange(final byte[] key, final int start, final int end) {
        checkIsInMulti();
        client.lrange(key, start, end);
        return client.getBinaryMultiBulkReply();
    }

    public String ltrim(final byte[] key, final int start, final int end) {
        checkIsInMulti();
        client.ltrim(key, start, end);
        return client.getStatusCodeReply();
    }

    public byte[] lindex(final byte[] key, final int index) {
        checkIsInMulti();
        client.lindex(key, index);
        return client.getBinaryBulkReply();
    }

    public String lset(final byte[] key, final int index, final byte[] value) {
        checkIsInMulti();
        client.lset(key, index, value);
        return client.getStatusCodeReply();
    }

    public Long lrem(final byte[] key, final int count, final byte[] value) {
        checkIsInMulti();
        client.lrem(key, count, value);
        return client.getIntegerReply();
    }

    public byte[] lpop(final byte[] key) {
        checkIsInMulti();
        client.lpop(key);
        return client.getBinaryBulkReply();
    }

    public byte[] rpop(final byte[] key) {
        checkIsInMulti();
        client.rpop(key);
        return client.getBinaryBulkReply();
    }

    public byte[] rpoplpush(final byte[] srckey, final byte[] dstkey) {
        checkIsInMulti();
        client.rpoplpush(srckey, dstkey);
        return client.getBinaryBulkReply();
    }

    public Long sadd(final byte[] key, final byte[]... members) {
        checkIsInMulti();
        client.sadd(key, members);
        return client.getIntegerReply();
    }

    public Set<byte[]> smembers(final byte[] key) {
        checkIsInMulti();
        client.smembers(key);
        final List<byte[]> members = client.getBinaryMultiBulkReply();
        return new HashSet<byte[]>(members);
    }

    public Long srem(final byte[] key, final byte[]... member) {
        checkIsInMulti();
        client.srem(key, member);
        return client.getIntegerReply();
    }

    public byte[] spop(final byte[] key) {
        checkIsInMulti();
        client.spop(key);
        return client.getBinaryBulkReply();
    }

    public Long smove(final byte[] srckey, final byte[] dstkey,
                      final byte[] member) {
        checkIsInMulti();
        client.smove(srckey, dstkey, member);
        return client.getIntegerReply();
    }

    public Long scard(final byte[] key) {
        checkIsInMulti();
        client.scard(key);
        return client.getIntegerReply();
    }

    public Boolean sismember(final byte[] key, final byte[] member) {
        checkIsInMulti();
        client.sismember(key, member);
        return client.getIntegerReply() == 1;
    }

    public Set<byte[]> sinter(final byte[]... keys) {
        checkIsInMulti();
        client.sinter(keys);
        final List<byte[]> members = client.getBinaryMultiBulkReply();
        return new HashSet<byte[]>(members);
    }

    public Long sinterstore(final byte[] dstkey, final byte[]... keys) {
        checkIsInMulti();
        client.sinterstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<byte[]> sunion(final byte[]... keys) {
        checkIsInMulti();
        client.sunion(keys);
        final List<byte[]> members = client.getBinaryMultiBulkReply();
        return new HashSet<byte[]>(members);
    }

    public Long sunionstore(final byte[] dstkey, final byte[]... keys) {
        checkIsInMulti();
        client.sunionstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<byte[]> sdiff(final byte[]... keys) {
        checkIsInMulti();
        client.sdiff(keys);
        final List<byte[]> members = client.getBinaryMultiBulkReply();
        return new HashSet<byte[]>(members);
    }

    public Long sdiffstore(final byte[] dstkey, final byte[]... keys) {
        checkIsInMulti();
        client.sdiffstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public byte[] srandmember(final byte[] key) {
        checkIsInMulti();
        client.srandmember(key);
        return client.getBinaryBulkReply();
    }

    public Long zadd(final byte[] key, final double score, final byte[] member) {
        checkIsInMulti();
        client.zadd(key, score, member);
        return client.getIntegerReply();
    }

    public Long zadd(final byte[] key, final Map<Double, byte[]> scoreMembers) {
        checkIsInMulti();
        client.zaddBinary(key, scoreMembers);
        return client.getIntegerReply();
    }

    public Set<byte[]> zrange(final byte[] key, final int start, final int end) {
        checkIsInMulti();
        client.zrange(key, start, end);
        final List<byte[]> members = client.getBinaryMultiBulkReply();
        return new LinkedHashSet<byte[]>(members);
    }

    public Long zrem(final byte[] key, final byte[]... members) {
        checkIsInMulti();
        client.zrem(key, members);
        return client.getIntegerReply();
    }

    public Double zincrby(final byte[] key, final double score,
                          final byte[] member) {
        checkIsInMulti();
        client.zincrby(key, score, member);
        String newscore = client.getBulkReply();
        return Double.valueOf(newscore);
    }

    public Long zrank(final byte[] key, final byte[] member) {
        checkIsInMulti();
        client.zrank(key, member);
        return client.getIntegerReply();
    }

    public Long zrevrank(final byte[] key, final byte[] member) {
        checkIsInMulti();
        client.zrevrank(key, member);
        return client.getIntegerReply();
    }

    public Set<byte[]> zrevrange(final byte[] key, final int start,
                                 final int end) {
        checkIsInMulti();
        client.zrevrange(key, start, end);
        final List<byte[]> members = client.getBinaryMultiBulkReply();
        return new LinkedHashSet<byte[]>(members);
    }

    public Set<Tuple> zrangeWithScores(final byte[] key, final int start,
                                       final int end) {
        checkIsInMulti();
        client.zrangeWithScores(key, start, end);
        Set<Tuple> set = getBinaryTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeWithScores(final byte[] key, final int start,
                                          final int end) {
        checkIsInMulti();
        client.zrevrangeWithScores(key, start, end);
        Set<Tuple> set = getBinaryTupledSet();
        return set;
    }

    public Long zcard(final byte[] key) {
        checkIsInMulti();
        client.zcard(key);
        return client.getIntegerReply();
    }

    public Double zscore(final byte[] key, final byte[] member) {
        checkIsInMulti();
        client.zscore(key, member);
        final String score = client.getBulkReply();
        return (score != null ? new Double(score) : null);
    }

    public Transaction multi() {
        client.multi();
        return new Transaction(client);
    }

    public List<Object> multi(final TransactionBlock jedisTransaction) {
        List<Object> results = null;
        jedisTransaction.setClient(client);
        try {
            client.multi();
            jedisTransaction.execute();
            results = jedisTransaction.exec();
        } catch (Exception ex) {
            jedisTransaction.discard();
        }
        return results;
    }

    protected void checkIsInMulti() {
        if (client.isInMulti()) {
            throw new JedisDataException(
                    "Cannot use Jedis when in Multi. Please use JedisTransaction instead.");
        }
    }

    public void connect() {
        client.connect();
    }

    public void disconnect() {
        client.disconnect();
    }

    public String watch(final byte[]... keys) {
        client.watch(keys);
        return client.getStatusCodeReply();
    }

    public String unwatch() {
        client.unwatch();
        return client.getStatusCodeReply();
    }

    public List<byte[]> sort(final byte[] key) {
        checkIsInMulti();
        client.sort(key);
        return client.getBinaryMultiBulkReply();
    }

    public List<byte[]> sort(final byte[] key,
                             final SortingParams sortingParameters) {
        checkIsInMulti();
        client.sort(key, sortingParameters);
        return client.getBinaryMultiBulkReply();
    }

    public List<byte[]> blpop(final int timeout, final byte[]... keys) {
        checkIsInMulti();
        final List<byte[]> args = new ArrayList<byte[]>();
        for (final byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));

        client.blpop(args.toArray(new byte[args.size()][]));
        client.setTimeoutInfinite();
        final List<byte[]> multiBulkReply = client.getBinaryMultiBulkReply();
        client.rollbackTimeout();
        return multiBulkReply;
    }

    public Long sort(final byte[] key, final SortingParams sortingParameters,
                     final byte[] dstkey) {
        checkIsInMulti();
        client.sort(key, sortingParameters, dstkey);
        return client.getIntegerReply();
    }

    public Long sort(final byte[] key, final byte[] dstkey) {
        checkIsInMulti();
        client.sort(key, dstkey);
        return client.getIntegerReply();
    }

    public List<byte[]> brpop(final int timeout, final byte[]... keys) {
        checkIsInMulti();
        final List<byte[]> args = new ArrayList<byte[]>();
        for (final byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));

        client.brpop(args.toArray(new byte[args.size()][]));
        client.setTimeoutInfinite();
        final List<byte[]> multiBulkReply = client.getBinaryMultiBulkReply();
        client.rollbackTimeout();

        return multiBulkReply;
    }

    public String auth(final String password) {
        checkIsInMulti();
        client.auth(password);
        return client.getStatusCodeReply();
    }

    public List<Object> pipelined(final PipelineBlock jedisPipeline) {
        jedisPipeline.setClient(client);
        jedisPipeline.execute();
        return jedisPipeline.syncAndReturnAll();
    }

    public Pipeline pipelined() {
        Pipeline pipeline = new Pipeline();
        pipeline.setClient(client);
        return pipeline;
    }

    public void subscribe(final JedisPubSub jedisPubSub,
                          final String... channels) {
        client.setTimeoutInfinite();
        jedisPubSub.proceed(client, channels);
        client.rollbackTimeout();
    }

    public Long publish(final String channel, final String message) {
        client.publish(channel, message);
        return client.getIntegerReply();
    }

    public void psubscribe(final JedisPubSub jedisPubSub,
                           final String... patterns) {
        client.setTimeoutInfinite();
        jedisPubSub.proceedWithPatterns(client, patterns);
        client.rollbackTimeout();
    }

    public Long zcount(final byte[] key, final double min, final double max) {
        return zcount(key, toByteArray(min), toByteArray(max));
    }

    public Long zcount(final byte[] key, final byte[] min, final byte[] max) {
        checkIsInMulti();
        client.zcount(key, min, max);
        return client.getIntegerReply();
    }

    public Set<byte[]> zrangeByScore(final byte[] key, final double min,
                                     final double max) {
        return zrangeByScore(key, toByteArray(min), toByteArray(max));
    }

    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min,
                                     final byte[] max) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max);
        return new LinkedHashSet<byte[]>(client.getBinaryMultiBulkReply());
    }

    public Set<byte[]> zrangeByScore(final byte[] key, final double min,
                                     final double max, final int offset, final int count) {
        return zrangeByScore(key, toByteArray(min), toByteArray(max), offset, count);
    }

    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min,
                                     final byte[] max, final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max, offset, count);
        return new LinkedHashSet<byte[]>(client.getBinaryMultiBulkReply());
    }

    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final double min, final double max) {
        return zrangeByScoreWithScores(key, toByteArray(min), toByteArray(max));
    }

    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final byte[] min, final byte[] max) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max);
        Set<Tuple> set = getBinaryTupledSet();
        return set;
    }

    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final double min, final double max, final int offset,
                                              final int count) {
        return zrangeByScoreWithScores(key, toByteArray(min), toByteArray(max), offset, count);
    }

    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final byte[] min, final byte[] max, final int offset,
                                              final int count) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max, offset, count);
        Set<Tuple> set = getBinaryTupledSet();
        return set;
    }

    private Set<Tuple> getBinaryTupledSet() {
        checkIsInMulti();
        List<byte[]> membersWithScores = client.getBinaryMultiBulkReply();
        Set<Tuple> set = new LinkedHashSet<Tuple>();
        Iterator<byte[]> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            set.add(new Tuple(iterator.next(), Double.valueOf(SafeEncoder
                    .encode(iterator.next()))));
        }
        return set;
    }

    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max,
                                        final double min) {
        return zrevrangeByScore(key, toByteArray(max), toByteArray(min));
    }

    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max,
                                        final byte[] min) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min);
        return new LinkedHashSet<byte[]>(client.getBinaryMultiBulkReply());
    }

    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max,
                                        final double min, final int offset, final int count) {
        return zrevrangeByScore(key, toByteArray(max), toByteArray(min), offset, count);
    }

    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max,
                                        final byte[] min, final int offset, final int count) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min, offset, count);
        return new LinkedHashSet<byte[]>(client.getBinaryMultiBulkReply());
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final double max, final double min) {
        return zrevrangeByScoreWithScores(key, toByteArray(max), toByteArray(min));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final double max, final double min, final int offset,
                                                 final int count) {
        return zrevrangeByScoreWithScores(key, toByteArray(max), toByteArray(min), offset, count);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final byte[] max, final byte[] min) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min);
        Set<Tuple> set = getBinaryTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final byte[] max, final byte[] min, final int offset,
                                                 final int count) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        Set<Tuple> set = getBinaryTupledSet();
        return set;
    }

    public Long zremrangeByRank(final byte[] key, final int start, final int end) {
        checkIsInMulti();
        client.zremrangeByRank(key, start, end);
        return client.getIntegerReply();
    }

    public Long zremrangeByScore(final byte[] key, final double start,
                                 final double end) {
        return zremrangeByScore(key, toByteArray(start), toByteArray(end));
    }

    public Long zremrangeByScore(final byte[] key, final byte[] start,
                                 final byte[] end) {
        checkIsInMulti();
        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
    }

    public Long zunionstore(final byte[] dstkey, final byte[]... sets) {
        checkIsInMulti();
        client.zunionstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zunionstore(final byte[] dstkey, final ZParams params,
                            final byte[]... sets) {
        checkIsInMulti();
        client.zunionstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final byte[] dstkey, final byte[]... sets) {
        checkIsInMulti();
        client.zinterstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final byte[] dstkey, final ZParams params,
                            final byte[]... sets) {
        checkIsInMulti();
        client.zinterstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public String save() {
        client.save();
        return client.getStatusCodeReply();
    }

    public String bgsave() {
        client.bgsave();
        return client.getStatusCodeReply();
    }

    public String bgrewriteaof() {
        client.bgrewriteaof();
        return client.getStatusCodeReply();
    }

    public Long lastsave() {
        client.lastsave();
        return client.getIntegerReply();
    }

    public String shutdown() {
        client.shutdown();
        String status = null;
        try {
            status = client.getStatusCodeReply();
        } catch (JedisException ex) {
            status = null;
        }
        return status;
    }

    public String info() {
        client.info();
        return client.getBulkReply();
    }

    public void monitor(final JedisMonitor jedisMonitor) {
        client.monitor();
        jedisMonitor.proceed(client);
    }

    public String slaveof(final String host, final int port) {
        client.slaveof(host, port);
        return client.getStatusCodeReply();
    }

    public String slaveofNoOne() {
        client.slaveofNoOne();
        return client.getStatusCodeReply();
    }

    public List<byte[]> configGet(final byte[] pattern) {
        client.configGet(pattern);
        return client.getBinaryMultiBulkReply();
    }

    public String configResetStat() {
        client.configResetStat();
        return client.getStatusCodeReply();
    }

    public byte[] configSet(final byte[] parameter, final byte[] value) {
        client.configSet(parameter, value);
        return client.getBinaryBulkReply();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public Long strlen(final byte[] key) {
        client.strlen(key);
        return client.getIntegerReply();
    }

    public void sync() {
        client.sync();
    }

    public Long lpushx(final byte[] key, final byte[] string) {
        client.lpushx(key, string);
        return client.getIntegerReply();
    }

    public Long persist(final byte[] key) {
        client.persist(key);
        return client.getIntegerReply();
    }

    public Long rpushx(final byte[] key, final byte[] string) {
        client.rpushx(key, string);
        return client.getIntegerReply();
    }

    public byte[] echo(final byte[] string) {
        client.echo(string);
        return client.getBinaryBulkReply();
    }

    public Long linsert(final byte[] key, final LIST_POSITION where,
                        final byte[] pivot, final byte[] value) {
        client.linsert(key, where, pivot, value);
        return client.getIntegerReply();
    }

    public String debug(final DebugParams params) {
        client.debug(params);
        return client.getStatusCodeReply();
    }

    public Client getClient() {
        return client;
    }

    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
        client.brpoplpush(source, destination, timeout);
        client.setTimeoutInfinite();
        byte[] reply = client.getBinaryBulkReply();
        client.rollbackTimeout();
        return reply;
    }

    public Boolean setbit(byte[] key, long offset, byte[] value) {
        client.setbit(key, offset, value);
        return client.getIntegerReply() == 1;
    }

    public Boolean getbit(byte[] key, long offset) {
        client.getbit(key, offset);
        return client.getIntegerReply() == 1;
    }

    public Long setrange(byte[] key, long offset, byte[] value) {
        client.setrange(key, offset, value);
        return client.getIntegerReply();
    }

    public String getrange(byte[] key, long startOffset, long endOffset) {
        client.getrange(key, startOffset, endOffset);
        return client.getBulkReply();
    }

    public Long publish(byte[] channel, byte[] message) {
        client.publish(channel, message);
        return client.getIntegerReply();
    }

    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        client.setTimeoutInfinite();
        jedisPubSub.proceed(client, channels);
        client.rollbackTimeout();
    }

    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
        client.setTimeoutInfinite();
        jedisPubSub.proceedWithPatterns(client, patterns);
        client.rollbackTimeout();
    }

    public Long getDB() {
        return client.getDB();
    }

    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        client.setTimeoutInfinite();
        client.eval(script, toByteArray(keys.size()), getParams(keys, args));
        return client.getOne();
    }

    private byte[][] getParams(List<byte[]> keys, List<byte[]> args) {
        int keyCount = keys.size();
        byte[][] params = new byte[keyCount + args.size()][];

        for (int i = 0; i < keyCount; i++)
            params[i] = keys.get(i);

        for (int i = 0; i < keys.size(); i++)
            params[keyCount + i] = args.get(i);

        return params;
    }

    public Object eval(byte[] script, byte[] keyCount, byte[][] params) {
        client.setTimeoutInfinite();
        client.eval(script, keyCount, params);
        return client.getOne();
    }

    public byte[] scriptFlush() {
        client.scriptFlush();
        return client.getBinaryBulkReply();
    }

    public List<Long> scriptExists(byte[]... sha1) {
        client.scriptExists(sha1);
        return client.getIntegerMultiBulkReply();
    }

    public byte[] scriptLoad(byte[] script) {
        client.scriptLoad(script);
        return client.getBinaryBulkReply();
    }

    public byte[] scriptKill() {
        client.scriptKill();
        return client.getBinaryBulkReply();
    }

    public byte[] slowlogReset() {
        client.slowlogReset();
        return client.getBinaryBulkReply();
    }

    public long slowlogLen() {
        client.slowlogLen();
        return client.getIntegerReply();
    }

    public List<byte[]> slowlogGetBinary() {
        client.slowlogGet();
        return client.getBinaryMultiBulkReply();
    }

    public List<byte[]> slowlogGetBinary(long entries) {
        client.slowlogGet(entries);
        return client.getBinaryMultiBulkReply();
    }

    public Long objectRefcount(byte[] key) {
        client.objectRefcount(key);
        return client.getIntegerReply();
    }

    public byte[] objectEncoding(byte[] key) {
        client.objectEncoding(key);
        return client.getBinaryBulkReply();
    }

    public Long objectIdletime(byte[] key) {
        client.objectIdletime(key);
        return client.getIntegerReply();
    }
}
