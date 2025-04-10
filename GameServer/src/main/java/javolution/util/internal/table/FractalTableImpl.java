package javolution.util.internal.table;

import javolution.lang.MathLib;

final class FractalTableImpl
{
static final int BASE_CAPACITY_MIN = 16;
static final int SHIFT = 8;
private static final int BASE_CAPACITY_MAX = 256;
int offset;
private Object[] data;
private final int shift;

public FractalTableImpl() {
this.shift = 0;
this.data = new Object[16];
}

public FractalTableImpl(int shift) {
this.shift = shift;
this.data = new Object[2];
}

public FractalTableImpl(int shift, Object[] data, int offset) {
this.shift = shift;
this.data = data;
this.offset = offset;
}

public int capacity() {
return this.data.length - 1 << this.shift;
}

public Object get(int index) {
Object fractal = this.data[index + this.offset >> this.shift & this.data.length - 1];
return (this.shift == 0) ? fractal : ((FractalTableImpl)fractal).get(index + this.offset);
}

public Object set(int index, Object element) {
int i = index + this.offset >> this.shift & this.data.length - 1;
if (this.shift != 0) return F(i).set(index + this.offset, element); 
Object previous = this.data[i];
this.data[i] = element;
return previous;
}

public void shiftLeft(Object inserted, int last, int length) {
int mask = (this.data.length << this.shift) - 1;
int tail = last + this.offset & mask;
int head = last + this.offset - length & mask;
if (this.shift == 0) {
int n = tail - head;
if (head > tail) {
System.arraycopy(this.data, head + 1, this.data, head, mask - head);
this.data[mask] = this.data[0];
n = tail;
} 
System.arraycopy(this.data, tail - n + 1, this.data, tail - n, n);
this.data[tail] = inserted;
} else if (head <= tail && head >> this.shift == tail >> this.shift) {
F(head >> this.shift).shiftLeft(inserted, tail, length);
} else {
int low = head >> this.shift;
int high = (low != this.data.length - 1) ? (low + 1) : 0;
F(low).shiftLeft(F(high).get(0), -1, mask - head);
while (high != tail >> this.shift) {
low = high;
high = (low != this.data.length - 1) ? (low + 1) : 0;
(F(low)).offset++;
F(low).set(-1, F(high).get(0));
} 
F(high).shiftLeft(inserted, tail, tail);
} 
}

public void shiftRight(Object inserted, int first, int length) {
int mask = (this.data.length << this.shift) - 1;
int head = first + this.offset & mask;
int tail = first + this.offset + length & mask;
if (this.shift == 0) {
int n = tail - head;
if (head > tail) {
System.arraycopy(this.data, 0, this.data, 1, tail);
this.data[0] = this.data[mask];
n = mask - head;
} 
System.arraycopy(this.data, head, this.data, head + 1, n);
this.data[head] = inserted;
} else if (head <= tail && head >> this.shift == tail >> this.shift) {
F(head >> this.shift).shiftRight(inserted, head, length);
} else {
int high = tail >> this.shift;
int low = (high != 0) ? (high - 1) : (this.data.length - 1);
F(high).shiftRight(F(low).get(-1), 0, tail);
while (low != head >> this.shift) {
high = low;
low = (high != 0) ? (high - 1) : (this.data.length - 1);
(F(high)).offset--;
F(high).set(0, F(low).get(-1));
} 
F(low).shiftRight(inserted, head, mask - head);
} 
}

public FractalTableImpl upsize() {
if (this.data.length >= 256) {
FractalTableImpl fractalTableImpl = new FractalTableImpl(this.shift + 8);
copyTo(fractalTableImpl.F(0));
return fractalTableImpl;
} 
FractalTableImpl table = new FractalTableImpl(this.shift, new Object[this.data.length << 1], 0);

copyTo(table);
return table;
}

private void copyTo(FractalTableImpl that) {
int n = MathLib.min(this.data.length, that.data.length);
this.offset &= (this.data.length << this.shift) - 1;
int o = this.offset >> this.shift;
if (o + n > this.data.length) {
int w = o + n - this.data.length;
n -= w;
System.arraycopy(this.data, 0, that.data, n, w);
} 
System.arraycopy(this.data, o, that.data, 0, n);
this.offset -= o << this.shift;
}

private FractalTableImpl allocate(int i) {
FractalTableImpl fractal = new FractalTableImpl(this.shift - 8, new Object[256], 0);

this.data[i] = fractal;
return fractal;
}

private FractalTableImpl F(int i) {
FractalTableImpl table = (FractalTableImpl)this.data[i];
return (table != null) ? table : allocate(i);
}
}

