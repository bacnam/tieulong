package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableMap<K, V>
extends ImmutableMap<K, V>
{
private final transient LinkedEntry<K, V>[] entries;
private final transient LinkedEntry<K, V>[] table;
private final transient int mask;
private final transient int keySetHashCode;
private transient ImmutableSet<Map.Entry<K, V>> entrySet;
private transient ImmutableSet<K> keySet;
private transient ImmutableCollection<V> values;
private static final long serialVersionUID = 0L;

RegularImmutableMap(Map.Entry<?, ?>... immutableEntries) {
int size = immutableEntries.length;
this.entries = createEntryArray(size);

int tableSize = chooseTableSize(size);
this.table = createEntryArray(tableSize);
this.mask = tableSize - 1;

int keySetHashCodeMutable = 0;
for (int entryIndex = 0; entryIndex < size; entryIndex++) {

Map.Entry<?, ?> entry = immutableEntries[entryIndex];
K key = (K)entry.getKey();
int keyHashCode = key.hashCode();
keySetHashCodeMutable += keyHashCode;
int tableIndex = Hashing.smear(keyHashCode) & this.mask;
LinkedEntry<K, V> existing = this.table[tableIndex];

LinkedEntry<K, V> linkedEntry = newLinkedEntry(key, (V)entry.getValue(), existing);

this.table[tableIndex] = linkedEntry;
this.entries[entryIndex] = linkedEntry;
while (existing != null) {
Preconditions.checkArgument(!key.equals(existing.getKey()), "duplicate key: %s", new Object[] { key });
existing = existing.next();
} 
} 
this.keySetHashCode = keySetHashCodeMutable;
}

private static int chooseTableSize(int size) {
int tableSize = Integer.highestOneBit(size) << 1;
Preconditions.checkArgument((tableSize > 0), "table too large: %s", new Object[] { Integer.valueOf(size) });
return tableSize;
}

private LinkedEntry<K, V>[] createEntryArray(int size) {
return (LinkedEntry<K, V>[])new LinkedEntry[size];
}

private static <K, V> LinkedEntry<K, V> newLinkedEntry(K key, V value, @Nullable LinkedEntry<K, V> next) {
return (next == null) ? new TerminalEntry<K, V>(key, value) : new NonTerminalEntry<K, V>(key, value, next);
}

private static interface LinkedEntry<K, V>
extends Map.Entry<K, V>
{
@Nullable
LinkedEntry<K, V> next();
}

@Immutable
private static final class NonTerminalEntry<K, V>
extends ImmutableEntry<K, V>
implements LinkedEntry<K, V>
{
final RegularImmutableMap.LinkedEntry<K, V> next;

NonTerminalEntry(K key, V value, RegularImmutableMap.LinkedEntry<K, V> next) {
super(key, value);
this.next = next;
}

public RegularImmutableMap.LinkedEntry<K, V> next() {
return this.next;
}
}

@Immutable
private static final class TerminalEntry<K, V>
extends ImmutableEntry<K, V>
implements LinkedEntry<K, V>
{
TerminalEntry(K key, V value) {
super(key, value);
}
@Nullable
public RegularImmutableMap.LinkedEntry<K, V> next() {
return null;
}
}

public V get(@Nullable Object key) {
if (key == null) {
return null;
}
int index = Hashing.smear(key.hashCode()) & this.mask;
LinkedEntry<K, V> entry = this.table[index];
for (; entry != null; 
entry = entry.next()) {
K candidateKey = entry.getKey();

if (key.equals(candidateKey)) {
return entry.getValue();
}
} 
return null;
}

public int size() {
return this.entries.length;
}

public boolean isEmpty() {
return false;
}

public boolean containsValue(@Nullable Object value) {
if (value == null) {
return false;
}
for (Map.Entry<K, V> entry : this.entries) {
if (entry.getValue().equals(value)) {
return true;
}
} 
return false;
}

boolean isPartialView() {
return false;
}

public ImmutableSet<Map.Entry<K, V>> entrySet() {
ImmutableSet<Map.Entry<K, V>> es = this.entrySet;
return (es == null) ? (this.entrySet = new EntrySet<K, V>(this)) : es;
}

private static class EntrySet<K, V>
extends ImmutableSet.ArrayImmutableSet<Map.Entry<K, V>> {
final transient RegularImmutableMap<K, V> map;

EntrySet(RegularImmutableMap<K, V> map) {
super((Object[])map.entries);
this.map = map;
}

public boolean contains(Object target) {
if (target instanceof Map.Entry) {
Map.Entry<?, ?> entry = (Map.Entry<?, ?>)target;
V mappedValue = this.map.get(entry.getKey());
return (mappedValue != null && mappedValue.equals(entry.getValue()));
} 
return false;
}
}

public ImmutableSet<K> keySet() {
ImmutableSet<K> ks = this.keySet;
return (ks == null) ? (this.keySet = new KeySet<K, V>(this)) : ks;
}

private static class KeySet<K, V>
extends ImmutableSet.TransformedImmutableSet<Map.Entry<K, V>, K>
{
final RegularImmutableMap<K, V> map;

KeySet(RegularImmutableMap<K, V> map) {
super((Map.Entry<K, V>[])map.entries, map.keySetHashCode);
this.map = map;
}

K transform(Map.Entry<K, V> element) {
return element.getKey();
}

public boolean contains(Object target) {
return this.map.containsKey(target);
}

boolean isPartialView() {
return true;
}
}

public ImmutableCollection<V> values() {
ImmutableCollection<V> v = this.values;
return (v == null) ? (this.values = new Values<V>(this)) : v;
}

private static class Values<V>
extends ImmutableCollection<V> {
final RegularImmutableMap<?, V> map;

Values(RegularImmutableMap<?, V> map) {
this.map = map;
}

public int size() {
return this.map.entries.length;
}

public UnmodifiableIterator<V> iterator() {
return new AbstractIndexedListIterator<V>(this.map.entries.length) {
protected V get(int index) {
return RegularImmutableMap.Values.this.map.entries[index].getValue();
}
};
}

public boolean contains(Object target) {
return this.map.containsValue(target);
}

boolean isPartialView() {
return true;
}
}

public String toString() {
StringBuilder result = Collections2.newStringBuilderForCollection(size()).append('{');

Collections2.STANDARD_JOINER.appendTo(result, (Object[])this.entries);
return result.append('}').toString();
}
}

