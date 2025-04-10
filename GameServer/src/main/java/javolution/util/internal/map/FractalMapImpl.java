package javolution.util.internal.map;

final class FractalMapImpl
{
static final int EMPTINESS_LEVEL = 2;
static final int INITIAL_BLOCK_CAPACITY = 8;
static final int SHIFT = 10;
private int count;
private MapEntryImpl[] entries = new MapEntryImpl[8];
private final int shift;

public FractalMapImpl() {
this.shift = 0;
}

public FractalMapImpl(int shift) {
this.shift = shift;
}

public MapEntryImpl addEntry(MapEntryImpl newEntry, Object key, int hash) {
int i = indexOfKey(key, hash);
MapEntryImpl entry = this.entries[i];
if (entry != null) return entry; 
this.entries[i] = newEntry;
newEntry.key = (K)key;
newEntry.hash = hash;

if (++this.count << 2 > this.entries.length) {
resize(this.entries.length << 1);
}
return newEntry;
}

public void clear() {
this.entries = new MapEntryImpl[8];
this.count = 0;
}

public MapEntryImpl getEntry(Object key, int hash) {
return this.entries[indexOfKey(key, hash)];
}

public MapEntryImpl removeEntry(Object key, int hash) {
int i = indexOfKey(key, hash);
MapEntryImpl oldEntry = this.entries[i];
if (oldEntry == null) return null; 
this.entries[i] = null;

while (true) {
i = i + 1 & this.entries.length - 1;
MapEntryImpl entry = this.entries[i];
if (entry == null)
break;  int correctIndex = indexOfKey(entry.key, entry.hash);
if (correctIndex != i) {
this.entries[correctIndex] = this.entries[i];
this.entries[i] = null;
} 
} 

if (--this.count << 3 <= this.entries.length && this.entries.length > 8)
{
resize(this.entries.length >> 1);
}
return oldEntry;
}

private int indexOfKey(Object key, int hash) {
int mask = this.entries.length - 1;
int i = hash >> this.shift & mask;
while (true) {
MapEntryImpl entry = this.entries[i];
if (entry == null) return i; 
if (entry.hash == hash && key.equals(entry.key)) return i; 
i = i + 1 & mask;
} 
}

private void resize(int newCapacity) {
MapEntryImpl[] newEntries = new MapEntryImpl[newCapacity];
int newMask = newEntries.length - 1;
for (int i = 0, n = this.entries.length; i < n; i++) {
MapEntryImpl entry = this.entries[i];
if (entry != null) {
int newIndex = entry.hash & newMask;
while (newEntries[newIndex] != null) {
newIndex = newIndex + 1 & newMask;
}
newEntries[newIndex] = entry;
} 
}  this.entries = newEntries;
}
}

