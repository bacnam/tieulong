/*      */ package org.apache.mina.core.buffer;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectStreamClass;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.nio.BufferOverflowException;
/*      */ import java.nio.BufferUnderflowException;
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
/*      */ import java.nio.charset.CoderResult;
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
/*      */ public abstract class AbstractIoBuffer
/*      */   extends IoBuffer
/*      */ {
/*      */   private final boolean derived;
/*      */   private boolean autoExpand;
/*      */   private boolean autoShrink;
/*      */   private boolean recapacityAllowed = true;
/*      */   private int minimumCapacity;
/*      */   private static final long BYTE_MASK = 255L;
/*      */   private static final long SHORT_MASK = 65535L;
/*      */   private static final long INT_MASK = 4294967295L;
/*   86 */   private int mark = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractIoBuffer(IoBufferAllocator allocator, int initialCapacity) {
/*   95 */     setAllocator(allocator);
/*   96 */     this.recapacityAllowed = true;
/*   97 */     this.derived = false;
/*   98 */     this.minimumCapacity = initialCapacity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractIoBuffer(AbstractIoBuffer parent) {
/*  108 */     setAllocator(getAllocator());
/*  109 */     this.recapacityAllowed = false;
/*  110 */     this.derived = true;
/*  111 */     this.minimumCapacity = parent.minimumCapacity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDirect() {
/*  119 */     return buf().isDirect();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isReadOnly() {
/*  127 */     return buf().isReadOnly();
/*      */   }
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
/*      */   public final int minimumCapacity() {
/*  142 */     return this.minimumCapacity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer minimumCapacity(int minimumCapacity) {
/*  150 */     if (minimumCapacity < 0) {
/*  151 */       throw new IllegalArgumentException("minimumCapacity: " + minimumCapacity);
/*      */     }
/*  153 */     this.minimumCapacity = minimumCapacity;
/*  154 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int capacity() {
/*  162 */     return buf().capacity();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer capacity(int newCapacity) {
/*  170 */     if (!this.recapacityAllowed) {
/*  171 */       throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
/*      */     }
/*      */ 
/*      */     
/*  175 */     if (newCapacity > capacity()) {
/*      */ 
/*      */       
/*  178 */       int pos = position();
/*  179 */       int limit = limit();
/*  180 */       ByteOrder bo = order();
/*      */ 
/*      */       
/*  183 */       ByteBuffer oldBuf = buf();
/*  184 */       ByteBuffer newBuf = getAllocator().allocateNioBuffer(newCapacity, isDirect());
/*  185 */       oldBuf.clear();
/*  186 */       newBuf.put(oldBuf);
/*  187 */       buf(newBuf);
/*      */ 
/*      */       
/*  190 */       buf().limit(limit);
/*  191 */       if (this.mark >= 0) {
/*  192 */         buf().position(this.mark);
/*  193 */         buf().mark();
/*      */       } 
/*  195 */       buf().position(pos);
/*  196 */       buf().order(bo);
/*      */     } 
/*      */     
/*  199 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAutoExpand() {
/*  207 */     return (this.autoExpand && this.recapacityAllowed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAutoShrink() {
/*  215 */     return (this.autoShrink && this.recapacityAllowed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDerived() {
/*  223 */     return this.derived;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer setAutoExpand(boolean autoExpand) {
/*  231 */     if (!this.recapacityAllowed) {
/*  232 */       throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
/*      */     }
/*  234 */     this.autoExpand = autoExpand;
/*  235 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer setAutoShrink(boolean autoShrink) {
/*  243 */     if (!this.recapacityAllowed) {
/*  244 */       throw new IllegalStateException("Derived buffers and their parent can't be shrinked.");
/*      */     }
/*  246 */     this.autoShrink = autoShrink;
/*  247 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer expand(int expectedRemaining) {
/*  255 */     return expand(position(), expectedRemaining, false);
/*      */   }
/*      */   
/*      */   private IoBuffer expand(int expectedRemaining, boolean autoExpand) {
/*  259 */     return expand(position(), expectedRemaining, autoExpand);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer expand(int pos, int expectedRemaining) {
/*  267 */     return expand(pos, expectedRemaining, false);
/*      */   }
/*      */   private IoBuffer expand(int pos, int expectedRemaining, boolean autoExpand) {
/*      */     int newCapacity;
/*  271 */     if (!this.recapacityAllowed) {
/*  272 */       throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
/*      */     }
/*      */     
/*  275 */     int end = pos + expectedRemaining;
/*      */     
/*  277 */     if (autoExpand) {
/*  278 */       newCapacity = IoBuffer.normalizeCapacity(end);
/*      */     } else {
/*  280 */       newCapacity = end;
/*      */     } 
/*  282 */     if (newCapacity > capacity())
/*      */     {
/*  284 */       capacity(newCapacity);
/*      */     }
/*      */     
/*  287 */     if (end > limit())
/*      */     {
/*  289 */       buf().limit(end);
/*      */     }
/*  291 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer shrink() {
/*  300 */     if (!this.recapacityAllowed) {
/*  301 */       throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
/*      */     }
/*      */     
/*  304 */     int position = position();
/*  305 */     int capacity = capacity();
/*  306 */     int limit = limit();
/*      */     
/*  308 */     if (capacity == limit) {
/*  309 */       return this;
/*      */     }
/*      */     
/*  312 */     int newCapacity = capacity;
/*  313 */     int minCapacity = Math.max(this.minimumCapacity, limit);
/*      */ 
/*      */     
/*  316 */     while (newCapacity >>> 1 >= minCapacity) {
/*      */ 
/*      */ 
/*      */       
/*  320 */       newCapacity >>>= 1;
/*      */       
/*  322 */       if (minCapacity == 0) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/*  327 */     newCapacity = Math.max(minCapacity, newCapacity);
/*      */     
/*  329 */     if (newCapacity == capacity) {
/*  330 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  335 */     ByteOrder bo = order();
/*      */ 
/*      */     
/*  338 */     ByteBuffer oldBuf = buf();
/*  339 */     ByteBuffer newBuf = getAllocator().allocateNioBuffer(newCapacity, isDirect());
/*  340 */     oldBuf.position(0);
/*  341 */     oldBuf.limit(limit);
/*  342 */     newBuf.put(oldBuf);
/*  343 */     buf(newBuf);
/*      */ 
/*      */     
/*  346 */     buf().position(position);
/*  347 */     buf().limit(limit);
/*  348 */     buf().order(bo);
/*  349 */     this.mark = -1;
/*      */     
/*  351 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int position() {
/*  359 */     return buf().position();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer position(int newPosition) {
/*  367 */     autoExpand(newPosition, 0);
/*  368 */     buf().position(newPosition);
/*  369 */     if (this.mark > newPosition) {
/*  370 */       this.mark = -1;
/*      */     }
/*  372 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int limit() {
/*  380 */     return buf().limit();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer limit(int newLimit) {
/*  388 */     autoExpand(newLimit, 0);
/*  389 */     buf().limit(newLimit);
/*  390 */     if (this.mark > newLimit) {
/*  391 */       this.mark = -1;
/*      */     }
/*  393 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer mark() {
/*  401 */     ByteBuffer byteBuffer = buf();
/*  402 */     byteBuffer.mark();
/*  403 */     this.mark = byteBuffer.position();
/*      */     
/*  405 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int markValue() {
/*  413 */     return this.mark;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer reset() {
/*  421 */     buf().reset();
/*  422 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer clear() {
/*  430 */     buf().clear();
/*  431 */     this.mark = -1;
/*  432 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer sweep() {
/*  440 */     clear();
/*  441 */     return fillAndReset(remaining());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer sweep(byte value) {
/*  449 */     clear();
/*  450 */     return fillAndReset(value, remaining());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer flip() {
/*  458 */     buf().flip();
/*  459 */     this.mark = -1;
/*  460 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer rewind() {
/*  468 */     buf().rewind();
/*  469 */     this.mark = -1;
/*  470 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int remaining() {
/*  478 */     ByteBuffer byteBuffer = buf();
/*      */     
/*  480 */     return byteBuffer.limit() - byteBuffer.position();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean hasRemaining() {
/*  488 */     ByteBuffer byteBuffer = buf();
/*      */     
/*  490 */     return (byteBuffer.limit() > byteBuffer.position());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte get() {
/*  498 */     return buf().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getUnsigned() {
/*  506 */     return (short)(get() & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer put(byte b) {
/*  514 */     autoExpand(1);
/*  515 */     buf().put(b);
/*  516 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(byte value) {
/*  523 */     autoExpand(1);
/*  524 */     buf().put((byte)(value & 0xFF));
/*  525 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, byte value) {
/*  532 */     autoExpand(index, 1);
/*  533 */     buf().put(index, (byte)(value & 0xFF));
/*  534 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(short value) {
/*  541 */     autoExpand(1);
/*  542 */     buf().put((byte)(value & 0xFF));
/*  543 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, short value) {
/*  550 */     autoExpand(index, 1);
/*  551 */     buf().put(index, (byte)(value & 0xFF));
/*  552 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int value) {
/*  559 */     autoExpand(1);
/*  560 */     buf().put((byte)(value & 0xFF));
/*  561 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, int value) {
/*  568 */     autoExpand(index, 1);
/*  569 */     buf().put(index, (byte)(value & 0xFF));
/*  570 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(long value) {
/*  577 */     autoExpand(1);
/*  578 */     buf().put((byte)(int)(value & 0xFFL));
/*  579 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putUnsigned(int index, long value) {
/*  586 */     autoExpand(index, 1);
/*  587 */     buf().put(index, (byte)(int)(value & 0xFFL));
/*  588 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte get(int index) {
/*  596 */     return buf().get(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getUnsigned(int index) {
/*  604 */     return (short)(get(index) & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer put(int index, byte b) {
/*  612 */     autoExpand(index, 1);
/*  613 */     buf().put(index, b);
/*  614 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer get(byte[] dst, int offset, int length) {
/*  622 */     buf().get(dst, offset, length);
/*  623 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer put(ByteBuffer src) {
/*  631 */     autoExpand(src.remaining());
/*  632 */     buf().put(src);
/*  633 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer put(byte[] src, int offset, int length) {
/*  641 */     autoExpand(length);
/*  642 */     buf().put(src, offset, length);
/*  643 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer compact() {
/*  651 */     int remaining = remaining();
/*  652 */     int capacity = capacity();
/*      */     
/*  654 */     if (capacity == 0) {
/*  655 */       return this;
/*      */     }
/*      */     
/*  658 */     if (isAutoShrink() && remaining <= capacity >>> 2 && capacity > this.minimumCapacity) {
/*  659 */       int newCapacity = capacity;
/*  660 */       int minCapacity = Math.max(this.minimumCapacity, remaining << 1);
/*      */       
/*  662 */       while (newCapacity >>> 1 >= minCapacity)
/*      */       {
/*      */         
/*  665 */         newCapacity >>>= 1;
/*      */       }
/*      */       
/*  668 */       newCapacity = Math.max(minCapacity, newCapacity);
/*      */       
/*  670 */       if (newCapacity == capacity) {
/*  671 */         return this;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  676 */       ByteOrder bo = order();
/*      */ 
/*      */       
/*  679 */       if (remaining > newCapacity) {
/*  680 */         throw new IllegalStateException("The amount of the remaining bytes is greater than the new capacity.");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  685 */       ByteBuffer oldBuf = buf();
/*  686 */       ByteBuffer newBuf = getAllocator().allocateNioBuffer(newCapacity, isDirect());
/*  687 */       newBuf.put(oldBuf);
/*  688 */       buf(newBuf);
/*      */ 
/*      */       
/*  691 */       buf().order(bo);
/*      */     } else {
/*  693 */       buf().compact();
/*      */     } 
/*  695 */     this.mark = -1;
/*  696 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ByteOrder order() {
/*  704 */     return buf().order();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer order(ByteOrder bo) {
/*  712 */     buf().order(bo);
/*  713 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final char getChar() {
/*  721 */     return buf().getChar();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putChar(char value) {
/*  729 */     autoExpand(2);
/*  730 */     buf().putChar(value);
/*  731 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final char getChar(int index) {
/*  739 */     return buf().getChar(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putChar(int index, char value) {
/*  747 */     autoExpand(index, 2);
/*  748 */     buf().putChar(index, value);
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CharBuffer asCharBuffer() {
/*  757 */     return buf().asCharBuffer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getShort() {
/*  765 */     return buf().getShort();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putShort(short value) {
/*  773 */     autoExpand(2);
/*  774 */     buf().putShort(value);
/*  775 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getShort(int index) {
/*  783 */     return buf().getShort(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putShort(int index, short value) {
/*  791 */     autoExpand(index, 2);
/*  792 */     buf().putShort(index, value);
/*  793 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ShortBuffer asShortBuffer() {
/*  801 */     return buf().asShortBuffer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getInt() {
/*  809 */     return buf().getInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putInt(int value) {
/*  817 */     autoExpand(4);
/*  818 */     buf().putInt(value);
/*  819 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(byte value) {
/*  827 */     autoExpand(4);
/*  828 */     buf().putInt(value & 0xFF);
/*  829 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(int index, byte value) {
/*  837 */     autoExpand(index, 4);
/*  838 */     buf().putInt(index, value & 0xFF);
/*  839 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(short value) {
/*  847 */     autoExpand(4);
/*  848 */     buf().putInt(value & 0xFFFF);
/*  849 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(int index, short value) {
/*  857 */     autoExpand(index, 4);
/*  858 */     buf().putInt(index, value & 0xFFFF);
/*  859 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(int value) {
/*  867 */     autoExpand(4);
/*  868 */     buf().putInt(value);
/*  869 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(int index, int value) {
/*  877 */     autoExpand(index, 4);
/*  878 */     buf().putInt(index, value);
/*  879 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(long value) {
/*  887 */     autoExpand(4);
/*  888 */     buf().putInt((int)(value & 0xFFFFFFFFFFFFFFFFL));
/*  889 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedInt(int index, long value) {
/*  897 */     autoExpand(index, 4);
/*  898 */     buf().putInt(index, (int)(value & 0xFFFFFFFFL));
/*  899 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(byte value) {
/*  907 */     autoExpand(2);
/*  908 */     buf().putShort((short)(value & 0xFF));
/*  909 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(int index, byte value) {
/*  917 */     autoExpand(index, 2);
/*  918 */     buf().putShort(index, (short)(value & 0xFF));
/*  919 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(short value) {
/*  927 */     autoExpand(2);
/*  928 */     buf().putShort(value);
/*  929 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(int index, short value) {
/*  937 */     autoExpand(index, 2);
/*  938 */     buf().putShort(index, value);
/*  939 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(int value) {
/*  947 */     autoExpand(2);
/*  948 */     buf().putShort((short)value);
/*  949 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(int index, int value) {
/*  957 */     autoExpand(index, 2);
/*  958 */     buf().putShort(index, (short)value);
/*  959 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(long value) {
/*  967 */     autoExpand(2);
/*  968 */     buf().putShort((short)(int)value);
/*  969 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putUnsignedShort(int index, long value) {
/*  977 */     autoExpand(index, 2);
/*  978 */     buf().putShort(index, (short)(int)value);
/*  979 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getInt(int index) {
/*  987 */     return buf().getInt(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putInt(int index, int value) {
/*  995 */     autoExpand(index, 4);
/*  996 */     buf().putInt(index, value);
/*  997 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IntBuffer asIntBuffer() {
/* 1005 */     return buf().asIntBuffer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLong() {
/* 1013 */     return buf().getLong();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putLong(long value) {
/* 1021 */     autoExpand(8);
/* 1022 */     buf().putLong(value);
/* 1023 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLong(int index) {
/* 1031 */     return buf().getLong(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putLong(int index, long value) {
/* 1039 */     autoExpand(index, 8);
/* 1040 */     buf().putLong(index, value);
/* 1041 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LongBuffer asLongBuffer() {
/* 1049 */     return buf().asLongBuffer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFloat() {
/* 1057 */     return buf().getFloat();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putFloat(float value) {
/* 1065 */     autoExpand(4);
/* 1066 */     buf().putFloat(value);
/* 1067 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFloat(int index) {
/* 1075 */     return buf().getFloat(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putFloat(int index, float value) {
/* 1083 */     autoExpand(index, 4);
/* 1084 */     buf().putFloat(index, value);
/* 1085 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final FloatBuffer asFloatBuffer() {
/* 1093 */     return buf().asFloatBuffer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getDouble() {
/* 1101 */     return buf().getDouble();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putDouble(double value) {
/* 1109 */     autoExpand(8);
/* 1110 */     buf().putDouble(value);
/* 1111 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getDouble(int index) {
/* 1119 */     return buf().getDouble(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer putDouble(int index, double value) {
/* 1127 */     autoExpand(index, 8);
/* 1128 */     buf().putDouble(index, value);
/* 1129 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleBuffer asDoubleBuffer() {
/* 1137 */     return buf().asDoubleBuffer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer asReadOnlyBuffer() {
/* 1145 */     this.recapacityAllowed = false;
/* 1146 */     return asReadOnlyBuffer0();
/*      */   }
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
/*      */   public final IoBuffer duplicate() {
/* 1160 */     this.recapacityAllowed = false;
/* 1161 */     return duplicate0();
/*      */   }
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
/*      */   public final IoBuffer slice() {
/* 1175 */     this.recapacityAllowed = false;
/* 1176 */     return slice0();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer getSlice(int index, int length) {
/* 1184 */     if (length < 0) {
/* 1185 */       throw new IllegalArgumentException("length: " + length);
/*      */     }
/*      */     
/* 1188 */     int pos = position();
/* 1189 */     int limit = limit();
/*      */     
/* 1191 */     if (index > limit) {
/* 1192 */       throw new IllegalArgumentException("index: " + index);
/*      */     }
/*      */     
/* 1195 */     int endIndex = index + length;
/*      */     
/* 1197 */     if (endIndex > limit) {
/* 1198 */       throw new IndexOutOfBoundsException("index + length (" + endIndex + ") is greater " + "than limit (" + limit + ").");
/*      */     }
/*      */ 
/*      */     
/* 1202 */     clear();
/* 1203 */     limit(endIndex);
/* 1204 */     position(index);
/*      */     
/* 1206 */     IoBuffer slice = slice();
/* 1207 */     limit(limit);
/* 1208 */     position(pos);
/*      */     
/* 1210 */     return slice;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoBuffer getSlice(int length) {
/* 1218 */     if (length < 0) {
/* 1219 */       throw new IllegalArgumentException("length: " + length);
/*      */     }
/* 1221 */     int pos = position();
/* 1222 */     int limit = limit();
/* 1223 */     int nextPos = pos + length;
/* 1224 */     if (limit < nextPos) {
/* 1225 */       throw new IndexOutOfBoundsException("position + length (" + nextPos + ") is greater " + "than limit (" + limit + ").");
/*      */     }
/*      */ 
/*      */     
/* 1229 */     limit(pos + length);
/* 1230 */     IoBuffer slice = slice();
/* 1231 */     position(nextPos);
/* 1232 */     limit(limit);
/* 1233 */     return slice;
/*      */   }
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
/*      */   public int hashCode() {
/* 1247 */     int h = 1;
/* 1248 */     int p = position();
/* 1249 */     for (int i = limit() - 1; i >= p; i--) {
/* 1250 */       h = 31 * h + get(i);
/*      */     }
/* 1252 */     return h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/* 1260 */     if (!(o instanceof IoBuffer)) {
/* 1261 */       return false;
/*      */     }
/*      */     
/* 1264 */     IoBuffer that = (IoBuffer)o;
/* 1265 */     if (remaining() != that.remaining()) {
/* 1266 */       return false;
/*      */     }
/*      */     
/* 1269 */     int p = position();
/* 1270 */     for (int i = limit() - 1, j = that.limit() - 1; i >= p; i--, j--) {
/* 1271 */       byte v1 = get(i);
/* 1272 */       byte v2 = that.get(j);
/* 1273 */       if (v1 != v2) {
/* 1274 */         return false;
/*      */       }
/*      */     } 
/* 1277 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(IoBuffer that) {
/* 1284 */     int n = position() + Math.min(remaining(), that.remaining());
/* 1285 */     for (int i = position(), j = that.position(); i < n; ) {
/* 1286 */       byte v1 = get(i);
/* 1287 */       byte v2 = that.get(j);
/* 1288 */       if (v1 == v2) {
/*      */         i++; j++; continue;
/*      */       } 
/* 1291 */       if (v1 < v2) {
/* 1292 */         return -1;
/*      */       }
/*      */       
/* 1295 */       return 1;
/*      */     } 
/* 1297 */     return remaining() - that.remaining();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1305 */     StringBuilder buf = new StringBuilder();
/* 1306 */     if (isDirect()) {
/* 1307 */       buf.append("DirectBuffer");
/*      */     } else {
/* 1309 */       buf.append("HeapBuffer");
/*      */     } 
/* 1311 */     buf.append("[pos=");
/* 1312 */     buf.append(position());
/* 1313 */     buf.append(" lim=");
/* 1314 */     buf.append(limit());
/* 1315 */     buf.append(" cap=");
/* 1316 */     buf.append(capacity());
/* 1317 */     buf.append(": ");
/* 1318 */     buf.append(getHexDump(16));
/* 1319 */     buf.append(']');
/* 1320 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer get(byte[] dst) {
/* 1328 */     return get(dst, 0, dst.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer put(IoBuffer src) {
/* 1336 */     return put(src.buf());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer put(byte[] src) {
/* 1344 */     return put(src, 0, src.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getUnsignedShort() {
/* 1352 */     return getShort() & 0xFFFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/* 1360 */     return getShort(index) & 0xFFFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getUnsignedInt() {
/* 1368 */     return getInt() & 0xFFFFFFFFL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMediumInt() {
/* 1376 */     byte b1 = get();
/* 1377 */     byte b2 = get();
/* 1378 */     byte b3 = get();
/* 1379 */     if (ByteOrder.BIG_ENDIAN.equals(order())) {
/* 1380 */       return getMediumInt(b1, b2, b3);
/*      */     }
/*      */     
/* 1383 */     return getMediumInt(b3, b2, b1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumInt() {
/* 1391 */     int b1 = getUnsigned();
/* 1392 */     int b2 = getUnsigned();
/* 1393 */     int b3 = getUnsigned();
/* 1394 */     if (ByteOrder.BIG_ENDIAN.equals(order())) {
/* 1395 */       return b1 << 16 | b2 << 8 | b3;
/*      */     }
/*      */     
/* 1398 */     return b3 << 16 | b2 << 8 | b1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMediumInt(int index) {
/* 1406 */     byte b1 = get(index);
/* 1407 */     byte b2 = get(index + 1);
/* 1408 */     byte b3 = get(index + 2);
/* 1409 */     if (ByteOrder.BIG_ENDIAN.equals(order())) {
/* 1410 */       return getMediumInt(b1, b2, b3);
/*      */     }
/*      */     
/* 1413 */     return getMediumInt(b3, b2, b1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumInt(int index) {
/* 1421 */     int b1 = getUnsigned(index);
/* 1422 */     int b2 = getUnsigned(index + 1);
/* 1423 */     int b3 = getUnsigned(index + 2);
/* 1424 */     if (ByteOrder.BIG_ENDIAN.equals(order())) {
/* 1425 */       return b1 << 16 | b2 << 8 | b3;
/*      */     }
/*      */     
/* 1428 */     return b3 << 16 | b2 << 8 | b1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getMediumInt(byte b1, byte b2, byte b3) {
/* 1435 */     int ret = b1 << 16 & 0xFF0000 | b2 << 8 & 0xFF00 | b3 & 0xFF;
/*      */     
/* 1437 */     if ((b1 & 0x80) == 128)
/*      */     {
/* 1439 */       ret |= 0xFF000000;
/*      */     }
/* 1441 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putMediumInt(int value) {
/* 1449 */     byte b1 = (byte)(value >> 16);
/* 1450 */     byte b2 = (byte)(value >> 8);
/* 1451 */     byte b3 = (byte)value;
/*      */     
/* 1453 */     if (ByteOrder.BIG_ENDIAN.equals(order())) {
/* 1454 */       put(b1).put(b2).put(b3);
/*      */     } else {
/* 1456 */       put(b3).put(b2).put(b1);
/*      */     } 
/*      */     
/* 1459 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putMediumInt(int index, int value) {
/* 1467 */     byte b1 = (byte)(value >> 16);
/* 1468 */     byte b2 = (byte)(value >> 8);
/* 1469 */     byte b3 = (byte)value;
/*      */     
/* 1471 */     if (ByteOrder.BIG_ENDIAN.equals(order())) {
/* 1472 */       put(index, b1).put(index + 1, b2).put(index + 2, b3);
/*      */     } else {
/* 1474 */       put(index, b3).put(index + 1, b2).put(index + 2, b1);
/*      */     } 
/*      */     
/* 1477 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/* 1485 */     return getInt(index) & 0xFFFFFFFFL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream asInputStream() {
/* 1493 */     return new InputStream()
/*      */       {
/*      */         public int available() {
/* 1496 */           return AbstractIoBuffer.this.remaining();
/*      */         }
/*      */ 
/*      */         
/*      */         public synchronized void mark(int readlimit) {
/* 1501 */           AbstractIoBuffer.this.mark();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean markSupported() {
/* 1506 */           return true;
/*      */         }
/*      */ 
/*      */         
/*      */         public int read() {
/* 1511 */           if (AbstractIoBuffer.this.hasRemaining()) {
/* 1512 */             return AbstractIoBuffer.this.get() & 0xFF;
/*      */           }
/*      */           
/* 1515 */           return -1;
/*      */         }
/*      */ 
/*      */         
/*      */         public int read(byte[] b, int off, int len) {
/* 1520 */           int remaining = AbstractIoBuffer.this.remaining();
/* 1521 */           if (remaining > 0) {
/* 1522 */             int readBytes = Math.min(remaining, len);
/* 1523 */             AbstractIoBuffer.this.get(b, off, readBytes);
/* 1524 */             return readBytes;
/*      */           } 
/*      */           
/* 1527 */           return -1;
/*      */         }
/*      */ 
/*      */         
/*      */         public synchronized void reset() {
/* 1532 */           AbstractIoBuffer.this.reset();
/*      */         }
/*      */ 
/*      */         
/*      */         public long skip(long n) {
/*      */           int bytes;
/* 1538 */           if (n > 2147483647L) {
/* 1539 */             bytes = AbstractIoBuffer.this.remaining();
/*      */           } else {
/* 1541 */             bytes = Math.min(AbstractIoBuffer.this.remaining(), (int)n);
/*      */           } 
/* 1543 */           AbstractIoBuffer.this.skip(bytes);
/* 1544 */           return bytes;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputStream asOutputStream() {
/* 1554 */     return new OutputStream()
/*      */       {
/*      */         public void write(byte[] b, int off, int len) {
/* 1557 */           AbstractIoBuffer.this.put(b, off, len);
/*      */         }
/*      */ 
/*      */         
/*      */         public void write(int b) {
/* 1562 */           AbstractIoBuffer.this.put((byte)b);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getHexDump() {
/* 1572 */     return getHexDump(2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getHexDump(int lengthLimit) {
/* 1580 */     return IoBufferHexDumper.getHexdump(this, lengthLimit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(CharsetDecoder decoder) throws CharacterCodingException {
/*      */     int newPos;
/* 1588 */     if (!hasRemaining()) {
/* 1589 */       return "";
/*      */     }
/*      */     
/* 1592 */     boolean utf16 = decoder.charset().name().startsWith("UTF-16");
/*      */     
/* 1594 */     int oldPos = position();
/* 1595 */     int oldLimit = limit();
/* 1596 */     int end = -1;
/*      */ 
/*      */     
/* 1599 */     if (!utf16) {
/* 1600 */       end = indexOf((byte)0);
/* 1601 */       if (end < 0) {
/* 1602 */         newPos = end = oldLimit;
/*      */       } else {
/* 1604 */         newPos = end + 1;
/*      */       } 
/*      */     } else {
/* 1607 */       int i = oldPos;
/*      */       while (true) {
/* 1609 */         boolean wasZero = (get(i) == 0);
/* 1610 */         i++;
/*      */         
/* 1612 */         if (i >= oldLimit) {
/*      */           break;
/*      */         }
/*      */         
/* 1616 */         if (get(i) != 0) {
/* 1617 */           i++;
/* 1618 */           if (i >= oldLimit) {
/*      */             break;
/*      */           }
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 1625 */         if (wasZero) {
/* 1626 */           end = i - 1;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1631 */       if (end < 0) {
/* 1632 */         newPos = end = oldPos + (oldLimit - oldPos & 0xFFFFFFFE);
/*      */       }
/* 1634 */       else if (end + 2 <= oldLimit) {
/* 1635 */         newPos = end + 2;
/*      */       } else {
/* 1637 */         newPos = end;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1642 */     if (oldPos == end) {
/* 1643 */       position(newPos);
/* 1644 */       return "";
/*      */     } 
/*      */     
/* 1647 */     limit(end);
/* 1648 */     decoder.reset();
/*      */     
/* 1650 */     int expectedLength = (int)(remaining() * decoder.averageCharsPerByte()) + 1;
/* 1651 */     CharBuffer out = CharBuffer.allocate(expectedLength);
/*      */     while (true) {
/*      */       CoderResult cr;
/* 1654 */       if (hasRemaining()) {
/* 1655 */         cr = decoder.decode(buf(), out, true);
/*      */       } else {
/* 1657 */         cr = decoder.flush(out);
/*      */       } 
/*      */       
/* 1660 */       if (cr.isUnderflow()) {
/*      */         break;
/*      */       }
/*      */       
/* 1664 */       if (cr.isOverflow()) {
/* 1665 */         CharBuffer o = CharBuffer.allocate(out.capacity() + expectedLength);
/* 1666 */         out.flip();
/* 1667 */         o.put(out);
/* 1668 */         out = o;
/*      */         
/*      */         continue;
/*      */       } 
/* 1672 */       if (cr.isError()) {
/*      */         
/* 1674 */         limit(oldLimit);
/* 1675 */         position(oldPos);
/* 1676 */         cr.throwException();
/*      */       } 
/*      */     } 
/*      */     
/* 1680 */     limit(oldLimit);
/* 1681 */     position(newPos);
/* 1682 */     return out.flip().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(int fieldSize, CharsetDecoder decoder) throws CharacterCodingException {
/* 1690 */     checkFieldSize(fieldSize);
/*      */     
/* 1692 */     if (fieldSize == 0) {
/* 1693 */       return "";
/*      */     }
/*      */     
/* 1696 */     if (!hasRemaining()) {
/* 1697 */       return "";
/*      */     }
/*      */     
/* 1700 */     boolean utf16 = decoder.charset().name().startsWith("UTF-16");
/*      */     
/* 1702 */     if (utf16 && (fieldSize & 0x1) != 0) {
/* 1703 */       throw new IllegalArgumentException("fieldSize is not even.");
/*      */     }
/*      */     
/* 1706 */     int oldPos = position();
/* 1707 */     int oldLimit = limit();
/* 1708 */     int end = oldPos + fieldSize;
/*      */     
/* 1710 */     if (oldLimit < end) {
/* 1711 */       throw new BufferUnderflowException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1716 */     if (!utf16) {
/* 1717 */       int i; for (i = oldPos; i < end && 
/* 1718 */         get(i) != 0; i++);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1723 */       if (i == end) {
/* 1724 */         limit(end);
/*      */       } else {
/* 1726 */         limit(i);
/*      */       } 
/*      */     } else {
/* 1729 */       int i; for (i = oldPos; i < end && (
/* 1730 */         get(i) != 0 || get(i + 1) != 0); i += 2);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1735 */       if (i == end) {
/* 1736 */         limit(end);
/*      */       } else {
/* 1738 */         limit(i);
/*      */       } 
/*      */     } 
/*      */     
/* 1742 */     if (!hasRemaining()) {
/* 1743 */       limit(oldLimit);
/* 1744 */       position(end);
/* 1745 */       return "";
/*      */     } 
/* 1747 */     decoder.reset();
/*      */     
/* 1749 */     int expectedLength = (int)(remaining() * decoder.averageCharsPerByte()) + 1;
/* 1750 */     CharBuffer out = CharBuffer.allocate(expectedLength);
/*      */     while (true) {
/*      */       CoderResult cr;
/* 1753 */       if (hasRemaining()) {
/* 1754 */         cr = decoder.decode(buf(), out, true);
/*      */       } else {
/* 1756 */         cr = decoder.flush(out);
/*      */       } 
/*      */       
/* 1759 */       if (cr.isUnderflow()) {
/*      */         break;
/*      */       }
/*      */       
/* 1763 */       if (cr.isOverflow()) {
/* 1764 */         CharBuffer o = CharBuffer.allocate(out.capacity() + expectedLength);
/* 1765 */         out.flip();
/* 1766 */         o.put(out);
/* 1767 */         out = o;
/*      */         
/*      */         continue;
/*      */       } 
/* 1771 */       if (cr.isError()) {
/*      */         
/* 1773 */         limit(oldLimit);
/* 1774 */         position(oldPos);
/* 1775 */         cr.throwException();
/*      */       } 
/*      */     } 
/*      */     
/* 1779 */     limit(oldLimit);
/* 1780 */     position(end);
/* 1781 */     return out.flip().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putString(CharSequence val, CharsetEncoder encoder) throws CharacterCodingException {
/* 1789 */     if (val.length() == 0) {
/* 1790 */       return this;
/*      */     }
/*      */     
/* 1793 */     CharBuffer in = CharBuffer.wrap(val);
/* 1794 */     encoder.reset();
/*      */     
/* 1796 */     int expandedState = 0;
/*      */     
/*      */     while (true) {
/*      */       CoderResult cr;
/* 1800 */       if (in.hasRemaining()) {
/* 1801 */         cr = encoder.encode(in, buf(), true);
/*      */       } else {
/* 1803 */         cr = encoder.flush(buf());
/*      */       } 
/*      */       
/* 1806 */       if (cr.isUnderflow()) {
/*      */         break;
/*      */       }
/* 1809 */       if (cr.isOverflow()) {
/* 1810 */         if (isAutoExpand()) {
/* 1811 */           switch (expandedState) {
/*      */             case 0:
/* 1813 */               autoExpand((int)Math.ceil((in.remaining() * encoder.averageBytesPerChar())));
/* 1814 */               expandedState++;
/*      */               continue;
/*      */             case 1:
/* 1817 */               autoExpand((int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())));
/* 1818 */               expandedState++;
/*      */               continue;
/*      */           } 
/* 1821 */           throw new RuntimeException("Expanded by " + (int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())) + " but that wasn't enough for '" + val + "'");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1828 */         expandedState = 0;
/*      */       } 
/* 1830 */       cr.throwException();
/*      */     } 
/* 1832 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putString(CharSequence val, int fieldSize, CharsetEncoder encoder) throws CharacterCodingException {
/* 1840 */     checkFieldSize(fieldSize);
/*      */     
/* 1842 */     if (fieldSize == 0) {
/* 1843 */       return this;
/*      */     }
/*      */     
/* 1846 */     autoExpand(fieldSize);
/*      */     
/* 1848 */     boolean utf16 = encoder.charset().name().startsWith("UTF-16");
/*      */     
/* 1850 */     if (utf16 && (fieldSize & 0x1) != 0) {
/* 1851 */       throw new IllegalArgumentException("fieldSize is not even.");
/*      */     }
/*      */     
/* 1854 */     int oldLimit = limit();
/* 1855 */     int end = position() + fieldSize;
/*      */     
/* 1857 */     if (oldLimit < end) {
/* 1858 */       throw new BufferOverflowException();
/*      */     }
/*      */     
/* 1861 */     if (val.length() == 0) {
/* 1862 */       if (!utf16) {
/* 1863 */         put((byte)0);
/*      */       } else {
/* 1865 */         put((byte)0);
/* 1866 */         put((byte)0);
/*      */       } 
/* 1868 */       position(end);
/* 1869 */       return this;
/*      */     } 
/*      */     
/* 1872 */     CharBuffer in = CharBuffer.wrap(val);
/* 1873 */     limit(end);
/* 1874 */     encoder.reset();
/*      */     
/*      */     while (true) {
/*      */       CoderResult cr;
/* 1878 */       if (in.hasRemaining()) {
/* 1879 */         cr = encoder.encode(in, buf(), true);
/*      */       } else {
/* 1881 */         cr = encoder.flush(buf());
/*      */       } 
/*      */       
/* 1884 */       if (cr.isUnderflow() || cr.isOverflow()) {
/*      */         break;
/*      */       }
/* 1887 */       cr.throwException();
/*      */     } 
/*      */     
/* 1890 */     limit(oldLimit);
/*      */     
/* 1892 */     if (position() < end) {
/* 1893 */       if (!utf16) {
/* 1894 */         put((byte)0);
/*      */       } else {
/* 1896 */         put((byte)0);
/* 1897 */         put((byte)0);
/*      */       } 
/*      */     }
/*      */     
/* 1901 */     position(end);
/* 1902 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPrefixedString(CharsetDecoder decoder) throws CharacterCodingException {
/* 1910 */     return getPrefixedString(2, decoder);
/*      */   }
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
/*      */   public String getPrefixedString(int prefixLength, CharsetDecoder decoder) throws CharacterCodingException {
/* 1925 */     if (!prefixedDataAvailable(prefixLength)) {
/* 1926 */       throw new BufferUnderflowException();
/*      */     }
/*      */     
/* 1929 */     int fieldSize = 0;
/*      */     
/* 1931 */     switch (prefixLength) {
/*      */       case 1:
/* 1933 */         fieldSize = getUnsigned();
/*      */         break;
/*      */       case 2:
/* 1936 */         fieldSize = getUnsignedShort();
/*      */         break;
/*      */       case 4:
/* 1939 */         fieldSize = getInt();
/*      */         break;
/*      */     } 
/*      */     
/* 1943 */     if (fieldSize == 0) {
/* 1944 */       return "";
/*      */     }
/*      */     
/* 1947 */     boolean utf16 = decoder.charset().name().startsWith("UTF-16");
/*      */     
/* 1949 */     if (utf16 && (fieldSize & 0x1) != 0) {
/* 1950 */       throw new BufferDataException("fieldSize is not even for a UTF-16 string.");
/*      */     }
/*      */     
/* 1953 */     int oldLimit = limit();
/* 1954 */     int end = position() + fieldSize;
/*      */     
/* 1956 */     if (oldLimit < end) {
/* 1957 */       throw new BufferUnderflowException();
/*      */     }
/*      */     
/* 1960 */     limit(end);
/* 1961 */     decoder.reset();
/*      */     
/* 1963 */     int expectedLength = (int)(remaining() * decoder.averageCharsPerByte()) + 1;
/* 1964 */     CharBuffer out = CharBuffer.allocate(expectedLength);
/*      */     while (true) {
/*      */       CoderResult cr;
/* 1967 */       if (hasRemaining()) {
/* 1968 */         cr = decoder.decode(buf(), out, true);
/*      */       } else {
/* 1970 */         cr = decoder.flush(out);
/*      */       } 
/*      */       
/* 1973 */       if (cr.isUnderflow()) {
/*      */         break;
/*      */       }
/*      */       
/* 1977 */       if (cr.isOverflow()) {
/* 1978 */         CharBuffer o = CharBuffer.allocate(out.capacity() + expectedLength);
/* 1979 */         out.flip();
/* 1980 */         o.put(out);
/* 1981 */         out = o;
/*      */         
/*      */         continue;
/*      */       } 
/* 1985 */       cr.throwException();
/*      */     } 
/*      */     
/* 1988 */     limit(oldLimit);
/* 1989 */     position(end);
/* 1990 */     return out.flip().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence in, CharsetEncoder encoder) throws CharacterCodingException {
/* 1998 */     return putPrefixedString(in, 2, 0, encoder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence in, int prefixLength, CharsetEncoder encoder) throws CharacterCodingException {
/* 2007 */     return putPrefixedString(in, prefixLength, 0, encoder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence in, int prefixLength, int padding, CharsetEncoder encoder) throws CharacterCodingException {
/* 2016 */     return putPrefixedString(in, prefixLength, padding, (byte)0, encoder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putPrefixedString(CharSequence val, int prefixLength, int padding, byte padValue, CharsetEncoder encoder) throws CharacterCodingException {
/*      */     int maxLength, padMask;
/* 2026 */     switch (prefixLength) {
/*      */       case 1:
/* 2028 */         maxLength = 255;
/*      */         break;
/*      */       case 2:
/* 2031 */         maxLength = 65535;
/*      */         break;
/*      */       case 4:
/* 2034 */         maxLength = Integer.MAX_VALUE;
/*      */         break;
/*      */       default:
/* 2037 */         throw new IllegalArgumentException("prefixLength: " + prefixLength);
/*      */     } 
/*      */     
/* 2040 */     if (val.length() > maxLength) {
/* 2041 */       throw new IllegalArgumentException("The specified string is too long.");
/*      */     }
/* 2043 */     if (val.length() == 0) {
/* 2044 */       switch (prefixLength) {
/*      */         case 1:
/* 2046 */           put((byte)0);
/*      */           break;
/*      */         case 2:
/* 2049 */           putShort((short)0);
/*      */           break;
/*      */         case 4:
/* 2052 */           putInt(0);
/*      */           break;
/*      */       } 
/* 2055 */       return this;
/*      */     } 
/*      */ 
/*      */     
/* 2059 */     switch (padding) {
/*      */       case 0:
/*      */       case 1:
/* 2062 */         padMask = 0;
/*      */         break;
/*      */       case 2:
/* 2065 */         padMask = 1;
/*      */         break;
/*      */       case 4:
/* 2068 */         padMask = 3;
/*      */         break;
/*      */       default:
/* 2071 */         throw new IllegalArgumentException("padding: " + padding);
/*      */     } 
/*      */     
/* 2074 */     CharBuffer in = CharBuffer.wrap(val);
/* 2075 */     skip(prefixLength);
/* 2076 */     int oldPos = position();
/* 2077 */     encoder.reset();
/*      */     
/* 2079 */     int expandedState = 0;
/*      */     
/*      */     while (true) {
/*      */       CoderResult cr;
/* 2083 */       if (in.hasRemaining()) {
/* 2084 */         cr = encoder.encode(in, buf(), true);
/*      */       } else {
/* 2086 */         cr = encoder.flush(buf());
/*      */       } 
/*      */       
/* 2089 */       if (position() - oldPos > maxLength) {
/* 2090 */         throw new IllegalArgumentException("The specified string is too long.");
/*      */       }
/*      */       
/* 2093 */       if (cr.isUnderflow()) {
/*      */         break;
/*      */       }
/* 2096 */       if (cr.isOverflow()) {
/* 2097 */         if (isAutoExpand()) {
/* 2098 */           switch (expandedState) {
/*      */             case 0:
/* 2100 */               autoExpand((int)Math.ceil((in.remaining() * encoder.averageBytesPerChar())));
/* 2101 */               expandedState++;
/*      */               continue;
/*      */             case 1:
/* 2104 */               autoExpand((int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())));
/* 2105 */               expandedState++;
/*      */               continue;
/*      */           } 
/* 2108 */           throw new RuntimeException("Expanded by " + (int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())) + " but that wasn't enough for '" + val + "'");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2115 */         expandedState = 0;
/*      */       } 
/* 2117 */       cr.throwException();
/*      */     } 
/*      */ 
/*      */     
/* 2121 */     fill(padValue, padding - (position() - oldPos & padMask));
/* 2122 */     int length = position() - oldPos;
/* 2123 */     switch (prefixLength) {
/*      */       case 1:
/* 2125 */         put(oldPos - 1, (byte)length);
/*      */         break;
/*      */       case 2:
/* 2128 */         putShort(oldPos - 2, (short)length);
/*      */         break;
/*      */       case 4:
/* 2131 */         putInt(oldPos - 4, length);
/*      */         break;
/*      */     } 
/* 2134 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject() throws ClassNotFoundException {
/* 2142 */     return getObject(Thread.currentThread().getContextClassLoader());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(final ClassLoader classLoader) throws ClassNotFoundException {
/* 2150 */     if (!prefixedDataAvailable(4)) {
/* 2151 */       throw new BufferUnderflowException();
/*      */     }
/*      */     
/* 2154 */     int length = getInt();
/* 2155 */     if (length <= 4) {
/* 2156 */       throw new BufferDataException("Object length should be greater than 4: " + length);
/*      */     }
/*      */     
/* 2159 */     int oldLimit = limit();
/* 2160 */     limit(position() + length);
/*      */     try {
/* 2162 */       ObjectInputStream in = new ObjectInputStream(asInputStream()) { protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
/*      */             String className;
/*      */             Class<?> clazz;
/* 2165 */             int type = read();
/* 2166 */             if (type < 0) {
/* 2167 */               throw new EOFException();
/*      */             }
/* 2169 */             switch (type) {
/*      */               case 0:
/* 2171 */                 return super.readClassDescriptor();
/*      */               case 1:
/* 2173 */                 className = readUTF();
/* 2174 */                 clazz = Class.forName(className, true, classLoader);
/* 2175 */                 return ObjectStreamClass.lookup(clazz);
/*      */             } 
/* 2177 */             throw new StreamCorruptedException("Unexpected class descriptor type: " + type);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
/* 2183 */             Class<?> clazz = desc.forClass();
/*      */             
/* 2185 */             if (clazz == null) {
/* 2186 */               String name = desc.getName();
/*      */               try {
/* 2188 */                 return Class.forName(name, false, classLoader);
/* 2189 */               } catch (ClassNotFoundException ex) {
/* 2190 */                 return super.resolveClass(desc);
/*      */               } 
/*      */             } 
/* 2193 */             return clazz;
/*      */           } }
/*      */         ;
/*      */       
/* 2197 */       return in.readObject();
/* 2198 */     } catch (IOException e) {
/* 2199 */       throw new BufferDataException(e);
/*      */     } finally {
/* 2201 */       limit(oldLimit);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putObject(Object o) {
/* 2210 */     int oldPos = position();
/* 2211 */     skip(4);
/*      */     try {
/* 2213 */       ObjectOutputStream out = new ObjectOutputStream(asOutputStream())
/*      */         {
/*      */           protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
/* 2216 */             Class<?> clazz = desc.forClass();
/*      */             
/* 2218 */             if (clazz.isArray() || clazz.isPrimitive() || !Serializable.class.isAssignableFrom(clazz)) {
/* 2219 */               write(0);
/* 2220 */               super.writeClassDescriptor(desc);
/*      */             } else {
/*      */               
/* 2223 */               write(1);
/* 2224 */               writeUTF(desc.getName());
/*      */             } 
/*      */           }
/*      */         };
/* 2228 */       out.writeObject(o);
/* 2229 */       out.flush();
/* 2230 */     } catch (IOException e) {
/* 2231 */       throw new BufferDataException(e);
/*      */     } 
/*      */ 
/*      */     
/* 2235 */     int newPos = position();
/* 2236 */     position(oldPos);
/* 2237 */     putInt(newPos - oldPos - 4);
/* 2238 */     position(newPos);
/* 2239 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean prefixedDataAvailable(int prefixLength) {
/* 2247 */     return prefixedDataAvailable(prefixLength, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean prefixedDataAvailable(int prefixLength, int maxDataLength) {
/*      */     int dataLength;
/* 2255 */     if (remaining() < prefixLength) {
/* 2256 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2260 */     switch (prefixLength) {
/*      */       case 1:
/* 2262 */         dataLength = getUnsigned(position());
/*      */         break;
/*      */       case 2:
/* 2265 */         dataLength = getUnsignedShort(position());
/*      */         break;
/*      */       case 4:
/* 2268 */         dataLength = getInt(position());
/*      */         break;
/*      */       default:
/* 2271 */         throw new IllegalArgumentException("prefixLength: " + prefixLength);
/*      */     } 
/*      */     
/* 2274 */     if (dataLength < 0 || dataLength > maxDataLength) {
/* 2275 */       throw new BufferDataException("dataLength: " + dataLength);
/*      */     }
/*      */     
/* 2278 */     return (remaining() - prefixLength >= dataLength);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(byte b) {
/* 2286 */     if (hasArray()) {
/* 2287 */       int arrayOffset = arrayOffset();
/* 2288 */       int beginPos = arrayOffset + position();
/* 2289 */       int limit = arrayOffset + limit();
/* 2290 */       byte[] array = array();
/*      */       
/* 2292 */       for (int i = beginPos; i < limit; i++) {
/* 2293 */         if (array[i] == b) {
/* 2294 */           return i - arrayOffset;
/*      */         }
/*      */       } 
/*      */     } else {
/* 2298 */       int beginPos = position();
/* 2299 */       int limit = limit();
/*      */       
/* 2301 */       for (int i = beginPos; i < limit; i++) {
/* 2302 */         if (get(i) == b) {
/* 2303 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2308 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer skip(int size) {
/* 2316 */     autoExpand(size);
/* 2317 */     return position(position() + size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer fill(byte value, int size) {
/* 2325 */     autoExpand(size);
/* 2326 */     int q = size >>> 3;
/* 2327 */     int r = size & 0x7;
/*      */     
/* 2329 */     if (q > 0) {
/* 2330 */       int intValue = value | value << 8 | value << 16 | value << 24;
/* 2331 */       long longValue = intValue;
/* 2332 */       longValue <<= 32L;
/* 2333 */       longValue |= intValue;
/*      */       
/* 2335 */       for (int i = q; i > 0; i--) {
/* 2336 */         putLong(longValue);
/*      */       }
/*      */     } 
/*      */     
/* 2340 */     q = r >>> 2;
/* 2341 */     r &= 0x3;
/*      */     
/* 2343 */     if (q > 0) {
/* 2344 */       int intValue = value | value << 8 | value << 16 | value << 24;
/* 2345 */       putInt(intValue);
/*      */     } 
/*      */     
/* 2348 */     q = r >> 1;
/* 2349 */     r &= 0x1;
/*      */     
/* 2351 */     if (q > 0) {
/* 2352 */       short shortValue = (short)(value | value << 8);
/* 2353 */       putShort(shortValue);
/*      */     } 
/*      */     
/* 2356 */     if (r > 0) {
/* 2357 */       put(value);
/*      */     }
/*      */     
/* 2360 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer fillAndReset(byte value, int size) {
/* 2368 */     autoExpand(size);
/* 2369 */     int pos = position();
/*      */     try {
/* 2371 */       fill(value, size);
/*      */     } finally {
/* 2373 */       position(pos);
/*      */     } 
/* 2375 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer fill(int size) {
/* 2383 */     autoExpand(size);
/* 2384 */     int q = size >>> 3;
/* 2385 */     int r = size & 0x7;
/*      */     
/* 2387 */     for (int i = q; i > 0; i--) {
/* 2388 */       putLong(0L);
/*      */     }
/*      */     
/* 2391 */     q = r >>> 2;
/* 2392 */     r &= 0x3;
/*      */     
/* 2394 */     if (q > 0) {
/* 2395 */       putInt(0);
/*      */     }
/*      */     
/* 2398 */     q = r >> 1;
/* 2399 */     r &= 0x1;
/*      */     
/* 2401 */     if (q > 0) {
/* 2402 */       putShort((short)0);
/*      */     }
/*      */     
/* 2405 */     if (r > 0) {
/* 2406 */       put((byte)0);
/*      */     }
/*      */     
/* 2409 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer fillAndReset(int size) {
/* 2417 */     autoExpand(size);
/* 2418 */     int pos = position();
/*      */     try {
/* 2420 */       fill(size);
/*      */     } finally {
/* 2422 */       position(pos);
/*      */     } 
/*      */     
/* 2425 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnum(Class<E> enumClass) {
/* 2433 */     return (E)toEnum(enumClass, getUnsigned());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnum(int index, Class<E> enumClass) {
/* 2441 */     return (E)toEnum(enumClass, getUnsigned(index));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumShort(Class<E> enumClass) {
/* 2449 */     return (E)toEnum(enumClass, getUnsignedShort());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumShort(int index, Class<E> enumClass) {
/* 2457 */     return (E)toEnum(enumClass, getUnsignedShort(index));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumInt(Class<E> enumClass) {
/* 2465 */     return (E)toEnum(enumClass, getInt());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnumInt(int index, Class<E> enumClass) {
/* 2472 */     return (E)toEnum(enumClass, getInt(index));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putEnum(Enum<?> e) {
/* 2480 */     if (e.ordinal() > 255L) {
/* 2481 */       throw new IllegalArgumentException(enumConversionErrorMessage(e, "byte"));
/*      */     }
/* 2483 */     return put((byte)e.ordinal());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putEnum(int index, Enum<?> e) {
/* 2491 */     if (e.ordinal() > 255L) {
/* 2492 */       throw new IllegalArgumentException(enumConversionErrorMessage(e, "byte"));
/*      */     }
/* 2494 */     return put(index, (byte)e.ordinal());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumShort(Enum<?> e) {
/* 2502 */     if (e.ordinal() > 65535L) {
/* 2503 */       throw new IllegalArgumentException(enumConversionErrorMessage(e, "short"));
/*      */     }
/* 2505 */     return putShort((short)e.ordinal());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumShort(int index, Enum<?> e) {
/* 2513 */     if (e.ordinal() > 65535L) {
/* 2514 */       throw new IllegalArgumentException(enumConversionErrorMessage(e, "short"));
/*      */     }
/* 2516 */     return putShort(index, (short)e.ordinal());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumInt(Enum<?> e) {
/* 2524 */     return putInt(e.ordinal());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer putEnumInt(int index, Enum<?> e) {
/* 2532 */     return putInt(index, e.ordinal());
/*      */   }
/*      */   
/*      */   private <E> E toEnum(Class<E> enumClass, int i) {
/* 2536 */     E[] enumConstants = enumClass.getEnumConstants();
/* 2537 */     if (i > enumConstants.length) {
/* 2538 */       throw new IndexOutOfBoundsException(String.format("%d is too large of an ordinal to convert to the enum %s", new Object[] { Integer.valueOf(i), enumClass.getName() }));
/*      */     }
/*      */     
/* 2541 */     return enumConstants[i];
/*      */   }
/*      */   
/*      */   private String enumConversionErrorMessage(Enum<?> e, String type) {
/* 2545 */     return String.format("%s.%s has an ordinal value too large for a %s", new Object[] { e.getClass().getName(), e.name(), type });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSet(Class<E> enumClass) {
/* 2553 */     return toEnumSet(enumClass, get() & 0xFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSet(int index, Class<E> enumClass) {
/* 2561 */     return toEnumSet(enumClass, get(index) & 0xFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetShort(Class<E> enumClass) {
/* 2569 */     return toEnumSet(enumClass, getShort() & 0xFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetShort(int index, Class<E> enumClass) {
/* 2577 */     return toEnumSet(enumClass, getShort(index) & 0xFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetInt(Class<E> enumClass) {
/* 2585 */     return toEnumSet(enumClass, getInt() & 0xFFFFFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetInt(int index, Class<E> enumClass) {
/* 2593 */     return toEnumSet(enumClass, getInt(index) & 0xFFFFFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetLong(Class<E> enumClass) {
/* 2601 */     return toEnumSet(enumClass, getLong());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> getEnumSetLong(int index, Class<E> enumClass) {
/* 2609 */     return toEnumSet(enumClass, getLong(index));
/*      */   }
/*      */   
/*      */   private <E extends Enum<E>> EnumSet<E> toEnumSet(Class<E> clazz, long vector) {
/* 2613 */     EnumSet<E> set = EnumSet.noneOf(clazz);
/* 2614 */     long mask = 1L;
/* 2615 */     for (Enum enum_ : (Enum[])clazz.getEnumConstants()) {
/* 2616 */       if ((mask & vector) == mask) {
/* 2617 */         set.add((E)enum_);
/*      */       }
/* 2619 */       mask <<= 1L;
/*      */     } 
/* 2621 */     return set;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSet(Set<E> set) {
/* 2629 */     long vector = toLong(set);
/* 2630 */     if ((vector & 0xFFFFFFFFFFFFFF00L) != 0L) {
/* 2631 */       throw new IllegalArgumentException("The enum set is too large to fit in a byte: " + set);
/*      */     }
/* 2633 */     return put((byte)(int)vector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSet(int index, Set<E> set) {
/* 2641 */     long vector = toLong(set);
/* 2642 */     if ((vector & 0xFFFFFFFFFFFFFF00L) != 0L) {
/* 2643 */       throw new IllegalArgumentException("The enum set is too large to fit in a byte: " + set);
/*      */     }
/* 2645 */     return put(index, (byte)(int)vector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetShort(Set<E> set) {
/* 2653 */     long vector = toLong(set);
/* 2654 */     if ((vector & 0xFFFFFFFFFFFF0000L) != 0L) {
/* 2655 */       throw new IllegalArgumentException("The enum set is too large to fit in a short: " + set);
/*      */     }
/* 2657 */     return putShort((short)(int)vector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetShort(int index, Set<E> set) {
/* 2665 */     long vector = toLong(set);
/* 2666 */     if ((vector & 0xFFFFFFFFFFFF0000L) != 0L) {
/* 2667 */       throw new IllegalArgumentException("The enum set is too large to fit in a short: " + set);
/*      */     }
/* 2669 */     return putShort(index, (short)(int)vector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetInt(Set<E> set) {
/* 2677 */     long vector = toLong(set);
/* 2678 */     if ((vector & 0xFFFFFFFF00000000L) != 0L) {
/* 2679 */       throw new IllegalArgumentException("The enum set is too large to fit in an int: " + set);
/*      */     }
/* 2681 */     return putInt((int)vector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetInt(int index, Set<E> set) {
/* 2689 */     long vector = toLong(set);
/* 2690 */     if ((vector & 0xFFFFFFFF00000000L) != 0L) {
/* 2691 */       throw new IllegalArgumentException("The enum set is too large to fit in an int: " + set);
/*      */     }
/* 2693 */     return putInt(index, (int)vector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetLong(Set<E> set) {
/* 2701 */     return putLong(toLong(set));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> IoBuffer putEnumSetLong(int index, Set<E> set) {
/* 2709 */     return putLong(index, toLong(set));
/*      */   }
/*      */   
/*      */   private <E extends Enum<E>> long toLong(Set<E> set) {
/* 2713 */     long vector = 0L;
/* 2714 */     for (Enum enum_ : set) {
/* 2715 */       if (enum_.ordinal() >= 64) {
/* 2716 */         throw new IllegalArgumentException("The enum set is too large to fit in a bit vector: " + set);
/*      */       }
/* 2718 */       vector |= 1L << enum_.ordinal();
/*      */     } 
/* 2720 */     return vector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IoBuffer autoExpand(int expectedRemaining) {
/* 2728 */     if (isAutoExpand()) {
/* 2729 */       expand(expectedRemaining, true);
/*      */     }
/* 2731 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IoBuffer autoExpand(int pos, int expectedRemaining) {
/* 2739 */     if (isAutoExpand()) {
/* 2740 */       expand(pos, expectedRemaining, true);
/*      */     }
/* 2742 */     return this;
/*      */   }
/*      */   
/*      */   private static void checkFieldSize(int fieldSize) {
/* 2746 */     if (fieldSize < 0)
/* 2747 */       throw new IllegalArgumentException("fieldSize cannot be negative: " + fieldSize); 
/*      */   }
/*      */   
/*      */   protected abstract void buf(ByteBuffer paramByteBuffer);
/*      */   
/*      */   protected abstract IoBuffer asReadOnlyBuffer0();
/*      */   
/*      */   protected abstract IoBuffer duplicate0();
/*      */   
/*      */   protected abstract IoBuffer slice0();
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/buffer/AbstractIoBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */