package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import org.apache.mina.core.buffer.IoBuffer;

public class CompositeByteArrayRelativeWriter
extends CompositeByteArrayRelativeBase
implements IoRelativeWriter
{
private final Expander expander;
private final Flusher flusher;
private final boolean autoFlush;

public static interface Expander
{
void expand(CompositeByteArray param1CompositeByteArray, int param1Int);
}

public static class NopExpander
implements Expander
{
public void expand(CompositeByteArray cba, int minSize) {}
}

public static class ChunkedExpander
implements Expander
{
private final ByteArrayFactory baf;
private final int newComponentSize;

public ChunkedExpander(ByteArrayFactory baf, int newComponentSize) {
this.baf = baf;
this.newComponentSize = newComponentSize;
}

public void expand(CompositeByteArray cba, int minSize) {
int remaining = minSize;
while (remaining > 0) {
ByteArray component = this.baf.create(this.newComponentSize);
cba.addLast(component);
remaining -= this.newComponentSize;
} 
}
}

public CompositeByteArrayRelativeWriter(CompositeByteArray cba, Expander expander, Flusher flusher, boolean autoFlush) {
super(cba);
this.expander = expander;
this.flusher = flusher;
this.autoFlush = autoFlush;
}

private void prepareForAccess(int size) {
int underflow = this.cursor.getIndex() + size - last();
if (underflow > 0) {
this.expander.expand(this.cba, underflow);
}
}

public void flush() {
flushTo(this.cursor.getIndex());
}

public void flushTo(int index) {
ByteArray removed = this.cba.removeTo(index);
this.flusher.flush(removed);
}

public void skip(int length) {
this.cursor.skip(length);
}

protected void cursorPassedFirstComponent() {
if (this.autoFlush) {
flushTo(this.cba.first() + this.cba.getFirst().length());
}
}

public void put(byte b) {
prepareForAccess(1);
this.cursor.put(b);
}

public void put(IoBuffer bb) {
prepareForAccess(bb.remaining());
this.cursor.put(bb);
}

public void putShort(short s) {
prepareForAccess(2);
this.cursor.putShort(s);
}

public void putInt(int i) {
prepareForAccess(4);
this.cursor.putInt(i);
}

public void putLong(long l) {
prepareForAccess(8);
this.cursor.putLong(l);
}

public void putFloat(float f) {
prepareForAccess(4);
this.cursor.putFloat(f);
}

public void putDouble(double d) {
prepareForAccess(8);
this.cursor.putDouble(d);
}

public void putChar(char c) {
prepareForAccess(2);
this.cursor.putChar(c);
}

public static interface Flusher {
void flush(ByteArray param1ByteArray);
}
}

