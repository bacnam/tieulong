package redis.clients.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sharded<R, S extends ShardInfo<R>> {

    public static final int DEFAULT_WEIGHT = 1;
    private TreeMap<Long, S> nodes;
    private final Hashing algo;
    private final Map<ShardInfo<R>, R> resources = new LinkedHashMap<ShardInfo<R>, R>();

    private Pattern tagPattern = null;

    public static final Pattern DEFAULT_KEY_TAG_PATTERN = Pattern
            .compile("\\{(.+?)\\}");

    public Sharded(List<S> shards) {
        this(shards, Hashing.MURMUR_HASH); 

    }

    public Sharded(List<S> shards, Hashing algo) {
        this.algo = algo;
        initialize(shards);
    }

    public Sharded(List<S> shards, Pattern tagPattern) {
        this(shards, Hashing.MURMUR_HASH, tagPattern); 

    }

    public Sharded(List<S> shards, Hashing algo, Pattern tagPattern) {
        this.algo = algo;
        this.tagPattern = tagPattern;
        initialize(shards);
    }

    private void initialize(List<S> shards) {
        nodes = new TreeMap<Long, S>();

        for (int i = 0; i != shards.size(); ++i) {
            final S shardInfo = shards.get(i);
            if (shardInfo.getName() == null)
            	for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
            		nodes.put(this.algo.hash("SHARD-" + i + "-NODE-" + n), shardInfo);
            	}
            else
            	for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
            		nodes.put(this.algo.hash(shardInfo.getName() + "*" + shardInfo.getWeight() + n), shardInfo);
            	}
            resources.put(shardInfo, shardInfo.createResource());
        }
    }

    public R getShard(byte[] key) {
        return resources.get(getShardInfo(key));
    }

    public R getShard(String key) {
        return resources.get(getShardInfo(key));
    }

    public S getShardInfo(byte[] key) {
        SortedMap<Long, S> tail = nodes.tailMap(algo.hash(key));
        if (tail.size() == 0) {
            return nodes.get(nodes.firstKey());
        }
        return tail.get(tail.firstKey());
    }

    public S getShardInfo(String key) {
        return getShardInfo(SafeEncoder.encode(getKeyTag(key)));
    }

    public String getKeyTag(String key) {
        if (tagPattern != null) {
            Matcher m = tagPattern.matcher(key);
            if (m.find())
                return m.group(1);
        }
        return key;
    }

    public Collection<S> getAllShardInfo() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    public Collection<R> getAllShards() {
        return Collections.unmodifiableCollection(resources.values());
    }
}

