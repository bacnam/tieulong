package javolution.util;

import javolution.lang.Realtime;
import javolution.util.internal.bitset.BitSetServiceImpl;
import javolution.util.service.BitSetService;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;

public class FastBitSet
extends FastSet<Index>
{
private static final long serialVersionUID = 1536L;
private final BitSetService service;

public FastBitSet() {
this.service = (BitSetService)new BitSetServiceImpl();
}

protected FastBitSet(BitSetService impl) {
this.service = impl;
}

public FastBitSet unmodifiable() {
throw new UnsupportedOperationException("NOT DONE YET");
}

public FastBitSet shared() {
throw new UnsupportedOperationException("NOT DONE YET");
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void and(FastBitSet that) {
this.service.and(that.service);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void andNot(FastBitSet that) {
this.service.andNot(that.service);
}

public int cardinality() {
return this.service.cardinality();
}

public void clear() {
this.service.clear();
}

public void clear(int bitIndex) {
this.service.clear(bitIndex);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void clear(int fromIndex, int toIndex) {
this.service.clear(fromIndex, toIndex);
}

public void flip(int bitIndex) {
this.service.flip(bitIndex);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void flip(int fromIndex, int toIndex) {
this.service.flip(fromIndex, toIndex);
}

public boolean get(int bitIndex) {
return this.service.get(bitIndex);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public FastBitSet get(int fromIndex, int toIndex) {
return new FastBitSet(this.service.get(fromIndex, toIndex));
}

@Realtime(limit = Realtime.Limit.LINEAR)
public boolean intersects(FastBitSet that) {
return this.service.intersects(that.service);
}

public int length() {
return this.service.length();
}

public int nextClearBit(int fromIndex) {
return this.service.nextClearBit(fromIndex);
}

public int nextSetBit(int fromIndex) {
return this.service.nextSetBit(fromIndex);
}

public int previousClearBit(int fromIndex) {
return this.service.previousClearBit(fromIndex);
}

public int previousSetBit(int fromIndex) {
return this.service.previousSetBit(fromIndex);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void or(FastBitSet that) {
this.service.or(that.service);
}

public void set(int bitIndex) {
this.service.set(bitIndex);
}

public void set(int bitIndex, boolean value) {
this.service.set(bitIndex, value);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void set(int fromIndex, int toIndex) {
if (fromIndex < 0 || toIndex < fromIndex) throw new IndexOutOfBoundsException(); 
this.service.set(fromIndex, toIndex);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void set(int fromIndex, int toIndex, boolean value) {
this.service.set(fromIndex, toIndex, value);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void xor(FastBitSet that) {
this.service.xor(that.service);
}

public FastBitSet addAll(Index... elements) {
return (FastBitSet)super.addAll(elements);
}

public FastBitSet addAll(FastCollection<? extends Index> elements) {
return (FastBitSet)super.addAll(elements);
}

protected BitSetService service() {
return this.service;
}
}

