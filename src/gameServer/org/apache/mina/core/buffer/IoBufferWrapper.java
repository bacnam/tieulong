/*      */ package org.apache.mina.core.buffer;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.DoubleBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.LongBuffer;
/*      */ import java.nio.ShortBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IoBufferWrapper
/*      */   extends IoBuffer
/*      */ {
/*      */   private final IoBuffer buf;
/*      */   
/*      */   protected IoBufferWrapper(IoBuffer buf) {
/*   60 */     if (buf == null) {
/*   61 */       throw new IllegalArgumentException("buf");
/*      */     }
/*   63 */     this.buf = buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer getParentBuffer() {
/*   70 */     return this.buf;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirect() {
/*   75 */     return this.buf.isDirect();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer buf() {
/*   80 */     return this.buf.buf();
/*      */   }
/*      */ 
/*      */   
/*      */   public int capacity() {
/*   85 */     return this.buf.capacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public int position() {
/*   90 */     return this.buf.position();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer position(int newPosition) {
/*   95 */     this.buf.position(newPosition);
/*   96 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int limit() {
/*  101 */     return this.buf.limit();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer limit(int newLimit) {
/*  106 */     this.buf.limit(newLimit);
/*  107 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer mark() {
/*  112 */     this.buf.mark();
/*  113 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer reset() {
/*  118 */     this.buf.reset();
/*  119 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer clear() {
/*  124 */     this.buf.clear();
/*  125 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer sweep() {
/*  130 */     this.buf.sweep();
/*  131 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer sweep(byte value) {
/*  136 */     this.buf.sweep(value);
/*  137 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer flip() {
/*  142 */     this.buf.flip();
/*  143 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer rewind() {
/*  148 */     this.buf.rewind();
/*  149 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int remaining() {
/*  154 */     return this.buf.remaining();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasRemaining() {
/*  159 */     return this.buf.hasRemaining();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte get() {
/*  164 */     return this.buf.get();
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsigned() {
/*  169 */     return this.buf.getUnsigned();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer put(byte b) {
/*  174 */     this.buf.put(b);
/*  175 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte get(int index) {
/*  180 */     return this.buf.get(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsigned(int index) {
/*  185 */     return this.buf.getUnsigned(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer put(int index, byte b) {
/*  190 */     this.buf.put(index, b);
/*  191 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer get(byte[] dst, int offset, int length) {
/*  196 */     this.buf.get(dst, offset, length);
/*  197 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer getSlice(int index, int length) {
/*  202 */     return this.buf.getSlice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer getSlice(int length) {
/*  207 */     return this.buf.getSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer get(byte[] dst) {
/*  212 */     this.buf.get(dst);
/*  213 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer put(IoBuffer src) {
/*  218 */     this.buf.put(src);
/*  219 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer put(ByteBuffer src) {
/*  224 */     this.buf.put(src);
/*  225 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer put(byte[] src, int offset, int length) {
/*  230 */     this.buf.put(src, offset, length);
/*  231 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer put(byte[] src) {
/*  236 */     this.buf.put(src);
/*  237 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer compact() {
/*  242 */     this.buf.compact();
/*  243 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  248 */     return this.buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  253 */     return this.buf.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object ob) {
/*  258 */     return this.buf.equals(ob);
/*      */   }
/*      */   
/*      */   public int compareTo(IoBuffer that) {
/*  262 */     return this.buf.compareTo(that);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*  267 */     return this.buf.order();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer order(ByteOrder bo) {
/*  272 */     this.buf.order(bo);
/*  273 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar() {
/*  278 */     return this.buf.getChar();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putChar(char value) {
/*  283 */     this.buf.putChar(value);
/*  284 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  289 */     return this.buf.getChar(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putChar(int index, char value) {
/*  294 */     this.buf.putChar(index, value);
/*  295 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CharBuffer asCharBuffer() {
/*  300 */     return this.buf.asCharBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort() {
/*  305 */     return this.buf.getShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort() {
/*  310 */     return this.buf.getUnsignedShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putShort(short value) {
/*  315 */     this.buf.putShort(value);
/*  316 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  321 */     return this.buf.getShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  326 */     return this.buf.getUnsignedShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putShort(int index, short value) {
/*  331 */     this.buf.putShort(index, value);
/*  332 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ShortBuffer asShortBuffer() {
/*  337 */     return this.buf.asShortBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt() {
/*  342 */     return this.buf.getInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt() {
/*  347 */     return this.buf.getUnsignedInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putInt(int value) {
/*  352 */     this.buf.putInt(value);
/*  353 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(byte value) {
/*  358 */     this.buf.putUnsignedInt(value);
/*  359 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(int index, byte value) {
/*  364 */     this.buf.putUnsignedInt(index, value);
/*  365 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(short value) {
/*  370 */     this.buf.putUnsignedInt(value);
/*  371 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(int index, short value) {
/*  376 */     this.buf.putUnsignedInt(index, value);
/*  377 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(int value) {
/*  382 */     this.buf.putUnsignedInt(value);
/*  383 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(int index, int value) {
/*  388 */     this.buf.putUnsignedInt(index, value);
/*  389 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(long value) {
/*  394 */     this.buf.putUnsignedInt(value);
/*  395 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedInt(int index, long value) {
/*  400 */     this.buf.putUnsignedInt(index, value);
/*  401 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(byte value) {
/*  406 */     this.buf.putUnsignedShort(value);
/*  407 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(int index, byte value) {
/*  412 */     this.buf.putUnsignedShort(index, value);
/*  413 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(short value) {
/*  418 */     this.buf.putUnsignedShort(value);
/*  419 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(int index, short value) {
/*  424 */     this.buf.putUnsignedShort(index, value);
/*  425 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(int value) {
/*  430 */     this.buf.putUnsignedShort(value);
/*  431 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(int index, int value) {
/*  436 */     this.buf.putUnsignedShort(index, value);
/*  437 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(long value) {
/*  442 */     this.buf.putUnsignedShort(value);
/*  443 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsignedShort(int index, long value) {
/*  448 */     this.buf.putUnsignedShort(index, value);
/*  449 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  454 */     return this.buf.getInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  459 */     return this.buf.getUnsignedInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putInt(int index, int value) {
/*  464 */     this.buf.putInt(index, value);
/*  465 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IntBuffer asIntBuffer() {
/*  470 */     return this.buf.asIntBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong() {
/*  475 */     return this.buf.getLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putLong(long value) {
/*  480 */     this.buf.putLong(value);
/*  481 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  486 */     return this.buf.getLong(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putLong(int index, long value) {
/*  491 */     this.buf.putLong(index, value);
/*  492 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public LongBuffer asLongBuffer() {
/*  497 */     return this.buf.asLongBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat() {
/*  502 */     return this.buf.getFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putFloat(float value) {
/*  507 */     this.buf.putFloat(value);
/*  508 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  513 */     return this.buf.getFloat(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putFloat(int index, float value) {
/*  518 */     this.buf.putFloat(index, value);
/*  519 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FloatBuffer asFloatBuffer() {
/*  524 */     return this.buf.asFloatBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble() {
/*  529 */     return this.buf.getDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putDouble(double value) {
/*  534 */     this.buf.putDouble(value);
/*  535 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  540 */     return this.buf.getDouble(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putDouble(int index, double value) {
/*  545 */     this.buf.putDouble(index, value);
/*  546 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public DoubleBuffer asDoubleBuffer() {
/*  551 */     return this.buf.asDoubleBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getHexDump() {
/*  556 */     return this.buf.getHexDump();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getString(int fieldSize, CharsetDecoder decoder) throws CharacterCodingException {
/*  561 */     return this.buf.getString(fieldSize, decoder);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getString(CharsetDecoder decoder) throws CharacterCodingException {
/*  566 */     return this.buf.getString(decoder);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getPrefixedString(CharsetDecoder decoder) throws CharacterCodingException {
/*  571 */     return this.buf.getPrefixedString(decoder);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getPrefixedString(int prefixLength, CharsetDecoder decoder) throws CharacterCodingException {
/*  576 */     return this.buf.getPrefixedString(prefixLength, decoder);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putString(CharSequence in, int fieldSize, CharsetEncoder encoder) throws CharacterCodingException {
/*  581 */     this.buf.putString(in, fieldSize, encoder);
/*  582 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putString(CharSequence in, CharsetEncoder encoder) throws CharacterCodingException {
/*  587 */     this.buf.putString(in, encoder);
/*  588 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence in, CharsetEncoder encoder) throws CharacterCodingException {
/*  593 */     this.buf.putPrefixedString(in, encoder);
/*  594 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence in, int prefixLength, CharsetEncoder encoder) throws CharacterCodingException {
/*  600 */     this.buf.putPrefixedString(in, prefixLength, encoder);
/*  601 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence in, int prefixLength, int padding, CharsetEncoder encoder) throws CharacterCodingException {
/*  607 */     this.buf.putPrefixedString(in, prefixLength, padding, encoder);
/*  608 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence in, int prefixLength, int padding, byte padValue, CharsetEncoder encoder) throws CharacterCodingException {
/*  614 */     this.buf.putPrefixedString(in, prefixLength, padding, padValue, encoder);
/*  615 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer skip(int size) {
/*  620 */     this.buf.skip(size);
/*  621 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer fill(byte value, int size) {
/*  626 */     this.buf.fill(value, size);
/*  627 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer fillAndReset(byte value, int size) {
/*  632 */     this.buf.fillAndReset(value, size);
/*  633 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer fill(int size) {
/*  638 */     this.buf.fill(size);
/*  639 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer fillAndReset(int size) {
/*  644 */     this.buf.fillAndReset(size);
/*  645 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAutoExpand() {
/*  650 */     return this.buf.isAutoExpand();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer setAutoExpand(boolean autoExpand) {
/*  655 */     this.buf.setAutoExpand(autoExpand);
/*  656 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer expand(int pos, int expectedRemaining) {
/*  661 */     this.buf.expand(pos, expectedRemaining);
/*  662 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer expand(int expectedRemaining) {
/*  667 */     this.buf.expand(expectedRemaining);
/*  668 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject() throws ClassNotFoundException {
/*  673 */     return this.buf.getObject();
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(ClassLoader classLoader) throws ClassNotFoundException {
/*  678 */     return this.buf.getObject(classLoader);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putObject(Object o) {
/*  683 */     this.buf.putObject(o);
/*  684 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream asInputStream() {
/*  689 */     return this.buf.asInputStream();
/*      */   }
/*      */ 
/*      */   
/*      */   public OutputStream asOutputStream() {
/*  694 */     return this.buf.asOutputStream();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer duplicate() {
/*  699 */     return this.buf.duplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer slice() {
/*  704 */     return this.buf.slice();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer asReadOnlyBuffer() {
/*  709 */     return this.buf.asReadOnlyBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] array() {
/*  714 */     return this.buf.array();
/*      */   }
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/*  719 */     return this.buf.arrayOffset();
/*      */   }
/*      */ 
/*      */   
/*      */   public int minimumCapacity() {
/*  724 */     return this.buf.minimumCapacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer minimumCapacity(int minimumCapacity) {
/*  729 */     this.buf.minimumCapacity(minimumCapacity);
/*  730 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer capacity(int newCapacity) {
/*  735 */     this.buf.capacity(newCapacity);
/*  736 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  741 */     return this.buf.isReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public int markValue() {
/*  746 */     return this.buf.markValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/*  751 */     return this.buf.hasArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public void free() {
/*  756 */     this.buf.free();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDerived() {
/*  761 */     return this.buf.isDerived();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAutoShrink() {
/*  766 */     return this.buf.isAutoShrink();
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer setAutoShrink(boolean autoShrink) {
/*  771 */     this.buf.setAutoShrink(autoShrink);
/*  772 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer shrink() {
/*  777 */     this.buf.shrink();
/*  778 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumInt() {
/*  783 */     return this.buf.getMediumInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumInt() {
/*  788 */     return this.buf.getUnsignedMediumInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumInt(int index) {
/*  793 */     return this.buf.getMediumInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumInt(int index) {
/*  798 */     return this.buf.getUnsignedMediumInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putMediumInt(int value) {
/*  803 */     this.buf.putMediumInt(value);
/*  804 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putMediumInt(int index, int value) {
/*  809 */     this.buf.putMediumInt(index, value);
/*  810 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getHexDump(int lengthLimit) {
/*  815 */     return this.buf.getHexDump(lengthLimit);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean prefixedDataAvailable(int prefixLength) {
/*  820 */     return this.buf.prefixedDataAvailable(prefixLength);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean prefixedDataAvailable(int prefixLength, int maxDataLength) {
/*  825 */     return this.buf.prefixedDataAvailable(prefixLength, maxDataLength);
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(byte b) {
/*  830 */     return this.buf.indexOf(b);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnum(Class<E> enumClass) {
/*  835 */     return this.buf.getEnum(enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnum(int index, Class<E> enumClass) {
/*  840 */     return this.buf.getEnum(index, enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumShort(Class<E> enumClass) {
/*  845 */     return this.buf.getEnumShort(enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumShort(int index, Class<E> enumClass) {
/*  850 */     return this.buf.getEnumShort(index, enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumInt(Class<E> enumClass) {
/*  855 */     return this.buf.getEnumInt(enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumInt(int index, Class<E> enumClass) {
/*  860 */     return this.buf.getEnumInt(index, enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putEnum(Enum<?> e) {
/*  865 */     this.buf.putEnum(e);
/*  866 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putEnum(int index, Enum<?> e) {
/*  871 */     this.buf.putEnum(index, e);
/*  872 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumShort(Enum<?> e) {
/*  877 */     this.buf.putEnumShort(e);
/*  878 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumShort(int index, Enum<?> e) {
/*  883 */     this.buf.putEnumShort(index, e);
/*  884 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumInt(Enum<?> e) {
/*  889 */     this.buf.putEnumInt(e);
/*  890 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumInt(int index, Enum<?> e) {
/*  895 */     this.buf.putEnumInt(index, e);
/*  896 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSet(Class<E> enumClass) {
/*  901 */     return this.buf.getEnumSet(enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSet(int index, Class<E> enumClass) {
/*  906 */     return this.buf.getEnumSet(index, enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetShort(Class<E> enumClass) {
/*  911 */     return this.buf.getEnumSetShort(enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetShort(int index, Class<E> enumClass) {
/*  916 */     return this.buf.getEnumSetShort(index, enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetInt(Class<E> enumClass) {
/*  921 */     return this.buf.getEnumSetInt(enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetInt(int index, Class<E> enumClass) {
/*  926 */     return this.buf.getEnumSetInt(index, enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetLong(Class<E> enumClass) {
/*  931 */     return this.buf.getEnumSetLong(enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetLong(int index, Class<E> enumClass) {
/*  936 */     return this.buf.getEnumSetLong(index, enumClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSet(Set<E> set) {
/*  941 */     this.buf.putEnumSet(set);
/*  942 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSet(int index, Set<E> set) {
/*  947 */     this.buf.putEnumSet(index, set);
/*  948 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetShort(Set<E> set) {
/*  953 */     this.buf.putEnumSetShort(set);
/*  954 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetShort(int index, Set<E> set) {
/*  959 */     this.buf.putEnumSetShort(index, set);
/*  960 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetInt(Set<E> set) {
/*  965 */     this.buf.putEnumSetInt(set);
/*  966 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetInt(int index, Set<E> set) {
/*  971 */     this.buf.putEnumSetInt(index, set);
/*  972 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetLong(Set<E> set) {
/*  977 */     this.buf.putEnumSetLong(set);
/*  978 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetLong(int index, Set<E> set) {
/*  983 */     this.buf.putEnumSetLong(index, set);
/*  984 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(byte value) {
/*  989 */     this.buf.putUnsigned(value);
/*  990 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, byte value) {
/*  995 */     this.buf.putUnsigned(index, value);
/*  996 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(short value) {
/* 1001 */     this.buf.putUnsigned(value);
/* 1002 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, short value) {
/* 1007 */     this.buf.putUnsigned(index, value);
/* 1008 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int value) {
/* 1013 */     this.buf.putUnsigned(value);
/* 1014 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, int value) {
/* 1019 */     this.buf.putUnsigned(index, value);
/* 1020 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(long value) {
/* 1025 */     this.buf.putUnsigned(value);
/* 1026 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, long value) {
/* 1031 */     this.buf.putUnsigned(index, value);
/* 1032 */     return this;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/buffer/IoBufferWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */