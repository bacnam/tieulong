package redis.clients.jedis;

import static redis.clients.jedis.Protocol.Keyword.ALPHA;
import static redis.clients.jedis.Protocol.Keyword.ASC;
import static redis.clients.jedis.Protocol.Keyword.BY;
import static redis.clients.jedis.Protocol.Keyword.DESC;
import static redis.clients.jedis.Protocol.Keyword.GET;
import static redis.clients.jedis.Protocol.Keyword.LIMIT;
import static redis.clients.jedis.Protocol.Keyword.NOSORT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import redis.clients.util.SafeEncoder;

public class SortingParams {
    private List<byte[]> params = new ArrayList<byte[]>();

    public SortingParams by(final String pattern) {
        return by(SafeEncoder.encode(pattern));
    }

    public SortingParams by(final byte[] pattern) {
        params.add(BY.raw);
        params.add(pattern);
        return this;
    }

    public SortingParams nosort() {
        params.add(BY.raw);
        params.add(NOSORT.raw);
        return this;
    }

    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection(params);
    }

    public SortingParams desc() {
        params.add(DESC.raw);
        return this;
    }

    public SortingParams asc() {
        params.add(ASC.raw);
        return this;
    }

    public SortingParams limit(final int start, final int count) {
        params.add(LIMIT.raw);
        params.add(Protocol.toByteArray(start));
        params.add(Protocol.toByteArray(count));
        return this;
    }

    public SortingParams alpha() {
        params.add(ALPHA.raw);
        return this;
    }

    public SortingParams get(String... patterns) {
        for (final String pattern : patterns) {
            params.add(GET.raw);
            params.add(SafeEncoder.encode(pattern));
        }
        return this;
    }

    public SortingParams get(byte[]... patterns) {
        for (final byte[] pattern : patterns) {
            params.add(GET.raw);
            params.add(pattern);
        }
        return this;
    }
}