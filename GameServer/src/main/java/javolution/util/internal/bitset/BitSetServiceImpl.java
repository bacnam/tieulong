package javolution.util.internal.bitset;

import java.io.Serializable;
import java.util.Iterator;
import javolution.lang.MathLib;
import javolution.util.Index;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.internal.set.SetView;
import javolution.util.service.BitSetService;

public class BitSetServiceImpl
extends SetView<Index>
implements BitSetService, Serializable
{
private static final long serialVersionUID = 1536L;
private long[] bits;

public BitSetServiceImpl() {
super(null);
this.bits = new long[0];
}

public boolean add(Index index) {
return !getAndSet(index.intValue(), true);
}

public void and(BitSetService that) {
long[] thatBits = that.toLongArray();
int n = MathLib.min(this.bits.length, thatBits.length); int i;
for (i = 0; i < n; i++) {
this.bits[i] = this.bits[i] & thatBits[i];
}
for (i = n; i < this.bits.length; i++) {
this.bits[i] = 0L;
}
trim();
}

public void andNot(BitSetService that) {
long[] thatBits = that.toLongArray();
int n = MathLib.min(this.bits.length, thatBits.length);
for (int i = 0; i < n; i++) {
this.bits[i] = this.bits[i] & (thatBits[i] ^ 0xFFFFFFFFFFFFFFFFL);
}
trim();
}

public int cardinality() {
int sum = 0;
for (int i = 0; i < this.bits.length; i++) {
sum += MathLib.bitCount(this.bits[i]);
}
return sum;
}

public void clear() {
this.bits = new long[0];
}

public void clear(int bitIndex) {
int longIndex = bitIndex >> 6;
if (longIndex >= this.bits.length)
return; 
this.bits[longIndex] = this.bits[longIndex] & (1L << bitIndex ^ 0xFFFFFFFFFFFFFFFFL);
trim();
}

public void clear(int fromIndex, int toIndex) {
if (fromIndex < 0 || toIndex < fromIndex)
throw new IndexOutOfBoundsException(); 
int i = fromIndex >>> 6;
if (i >= this.bits.length)
return; 
int j = toIndex >>> 6;
if (i == j) {
this.bits[i] = this.bits[i] & ((1L << fromIndex) - 1L | -1L << toIndex);
return;
} 
this.bits[i] = this.bits[i] & (1L << fromIndex) - 1L;
if (j < this.bits.length) {
this.bits[j] = this.bits[j] & -1L << toIndex;
}
for (int k = i + 1; k < j && k < this.bits.length; k++) {
this.bits[k] = 0L;
}
trim();
}

public Equality<? super Index> comparator() {
return Equalities.IDENTITY;
}

public boolean contains(Object index) {
return get(((Index)index).intValue());
}

public void flip(int bitIndex) {
int i = bitIndex >> 6;
setLength(i + 1);
this.bits[i] = this.bits[i] ^ 1L << bitIndex;
trim();
}

public void flip(int fromIndex, int toIndex) {
if (fromIndex < 0 || toIndex < fromIndex)
throw new IndexOutOfBoundsException(); 
int i = fromIndex >>> 6;
int j = toIndex >>> 6;
setLength(j + 1);
if (i == j) {
this.bits[i] = this.bits[i] ^ -1L << fromIndex & (1L << toIndex) - 1L;
return;
} 
this.bits[i] = this.bits[i] ^ -1L << fromIndex;
this.bits[j] = this.bits[j] ^ (1L << toIndex) - 1L;
for (int k = i + 1; k < j; k++) {
this.bits[k] = this.bits[k] ^ 0xFFFFFFFFFFFFFFFFL;
}
trim();
}

public boolean get(int bitIndex) {
int i = bitIndex >> 6;
return (i >= this.bits.length) ? false : (((this.bits[i] & 1L << bitIndex) != 0L));
}

public BitSetServiceImpl get(int fromIndex, int toIndex) {
if (fromIndex < 0 || fromIndex > toIndex)
throw new IndexOutOfBoundsException(); 
BitSetServiceImpl bitSet = new BitSetServiceImpl();
int length = MathLib.min(this.bits.length, (toIndex >>> 6) + 1);
bitSet.bits = new long[length];
System.arraycopy(this.bits, 0, bitSet.bits, 0, length);
bitSet.clear(0, fromIndex);
bitSet.clear(toIndex, length << 6);
return bitSet;
}

public boolean getAndSet(int bitIndex, boolean value) {
int i = bitIndex >> 6;
if (i >= this.bits.length) {
setLength(i + 1);
}
boolean previous = ((this.bits[i] & 1L << bitIndex) != 0L);
if (value) {
this.bits[i] = this.bits[i] | 1L << bitIndex;
} else {
this.bits[i] = this.bits[i] & (1L << bitIndex ^ 0xFFFFFFFFFFFFFFFFL);
} 
trim();
return previous;
}

public boolean intersects(BitSetService that) {
long[] thatBits = that.toLongArray();
int i = MathLib.min(this.bits.length, thatBits.length);
while (--i >= 0) {
if ((this.bits[i] & thatBits[i]) != 0L) return true; 
} 
return false;
}

public Iterator<Index> iterator() {
return new BitSetIteratorImpl(this, 0);
}

public int length() {
if (this.bits.length == 0) return 0; 
return (this.bits.length << 6) - MathLib.numberOfLeadingZeros(this.bits[this.bits.length - 1]);
}

public int nextClearBit(int fromIndex) {
int offset = fromIndex >> 6;
long mask = 1L << fromIndex;
while (offset < this.bits.length) {
long h = this.bits[offset];
while (true) {
if ((h & mask) == 0L) return fromIndex; 
mask <<= 1L;
fromIndex++;
if (mask == 0L)
{ mask = 1L;
offset++; } 
} 
}  return fromIndex;
}

public int nextSetBit(int fromIndex) {
int offset = fromIndex >> 6;
long mask = 1L << fromIndex;
while (offset < this.bits.length) {
long h = this.bits[offset];
while (true) {
if ((h & mask) != 0L)
return fromIndex; 
mask <<= 1L;
fromIndex++;
if (mask == 0L)
{ mask = 1L;
offset++; } 
} 
}  return -1;
}

public void or(BitSetService that) {
long[] thatBits = (that instanceof BitSetServiceImpl) ? ((BitSetServiceImpl)that).bits : that.toLongArray();

if (thatBits.length > this.bits.length) {
setLength(thatBits.length);
}
for (int i = thatBits.length; --i >= 0;) {
this.bits[i] = this.bits[i] | thatBits[i];
}
trim();
}

public int previousClearBit(int fromIndex) {
int offset = fromIndex >> 6;
long mask = 1L << fromIndex;
while (offset >= 0) {
long h = this.bits[offset];
while (true) {
if ((h & mask) == 0L)
return fromIndex; 
mask >>= 1L;
fromIndex--;
if (mask == 0L)
{ mask = Long.MIN_VALUE;
offset--; } 
} 
}  return -1;
}

public int previousSetBit(int fromIndex) {
int offset = fromIndex >> 6;
long mask = 1L << fromIndex;
while (offset >= 0) {
long h = this.bits[offset];
while (true) {
if ((h & mask) != 0L)
return fromIndex; 
mask >>= 1L;
fromIndex--;
if (mask == 0L)
{ mask = Long.MIN_VALUE;
offset--; } 
} 
}  return -1;
}

public boolean remove(Object index) {
return getAndSet(((Index)index).intValue(), false);
}

public void set(int bitIndex) {
int i = bitIndex >> 6;
if (i >= this.bits.length) {
setLength(i + 1);
}
this.bits[i] = this.bits[i] | 1L << bitIndex;
}

public void set(int bitIndex, boolean value) {
if (value) {
set(bitIndex);
} else {
clear(bitIndex);
} 
}

public void set(int fromIndex, int toIndex) {
int i = fromIndex >>> 6;
int j = toIndex >>> 6;
setLength(j + 1);
if (i == j) {
this.bits[i] = this.bits[i] | -1L << fromIndex & (1L << toIndex) - 1L;
return;
} 
this.bits[i] = this.bits[i] | -1L << fromIndex;
this.bits[j] = this.bits[j] | (1L << toIndex) - 1L;
for (int k = i + 1; k < j; k++) {
this.bits[k] = -1L;
}
}

public void set(int fromIndex, int toIndex, boolean value) {
if (value) {
set(fromIndex, toIndex);
} else {
clear(fromIndex, toIndex);
} 
}

public int size() {
return cardinality();
}

public long[] toLongArray() {
return this.bits;
}

public void xor(BitSetService that) {
long[] thatBits = (that instanceof BitSetServiceImpl) ? ((BitSetServiceImpl)that).bits : that.toLongArray();

if (thatBits.length > this.bits.length) {
setLength(thatBits.length);
}
for (int i = thatBits.length; --i >= 0;) {
this.bits[i] = this.bits[i] ^ thatBits[i];
}
trim();
}

private void setLength(int newLength) {
long[] tmp = new long[newLength];
if (newLength >= this.bits.length) {
System.arraycopy(this.bits, 0, tmp, 0, this.bits.length);
} else {
System.arraycopy(this.bits, 0, tmp, 0, newLength);
} 
this.bits = tmp;
}

private void trim() {
int n = this.bits.length;
while (--n >= 0 && this.bits[n] == 0L);
if (++n < this.bits.length) setLength(n); 
}
}

