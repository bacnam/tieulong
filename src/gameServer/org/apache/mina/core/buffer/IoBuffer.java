/*     */ package org.apache.mina.core.buffer;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IoBuffer
/*     */   implements Comparable<IoBuffer>
/*     */ {
/* 154 */   private static IoBufferAllocator allocator = new SimpleBufferAllocator();
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean useDirectBuffer = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public static IoBufferAllocator getAllocator() {
/* 163 */     return allocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setAllocator(IoBufferAllocator newAllocator) {
/* 170 */     if (newAllocator == null) {
/* 171 */       throw new IllegalArgumentException("allocator");
/*     */     }
/*     */     
/* 174 */     IoBufferAllocator oldAllocator = allocator;
/*     */     
/* 176 */     allocator = newAllocator;
/*     */     
/* 178 */     if (null != oldAllocator) {
/* 179 */       oldAllocator.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUseDirectBuffer() {
/* 189 */     return useDirectBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUseDirectBuffer(boolean useDirectBuffer) {
/* 197 */     IoBuffer.useDirectBuffer = useDirectBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IoBuffer allocate(int capacity) {
/* 210 */     return allocate(capacity, useDirectBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IoBuffer allocate(int capacity, boolean direct) {
/* 223 */     if (capacity < 0) {
/* 224 */       throw new IllegalArgumentException("capacity: " + capacity);
/*     */     }
/*     */     
/* 227 */     return allocator.allocate(capacity, direct);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IoBuffer wrap(ByteBuffer nioBuffer) {
/* 234 */     return allocator.wrap(nioBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IoBuffer wrap(byte[] byteArray) {
/* 241 */     return wrap(ByteBuffer.wrap(byteArray));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IoBuffer wrap(byte[] byteArray, int offset, int length) {
/* 248 */     return wrap(ByteBuffer.wrap(byteArray, offset, length));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int normalizeCapacity(int requestedCapacity) {
/* 258 */     if (requestedCapacity < 0) {
/* 259 */       return Integer.MAX_VALUE;
/*     */     }
/*     */     
/* 262 */     int newCapacity = Integer.highestOneBit(requestedCapacity);
/* 263 */     newCapacity <<= (newCapacity < requestedCapacity) ? 1 : 0;
/* 264 */     return (newCapacity < 0) ? Integer.MAX_VALUE : newCapacity;
/*     */   }
/*     */   
/*     */   public abstract void free();
/*     */   
/*     */   public abstract ByteBuffer buf();
/*     */   
/*     */   public abstract boolean isDirect();
/*     */   
/*     */   public abstract boolean isDerived();
/*     */   
/*     */   public abstract boolean isReadOnly();
/*     */   
/*     */   public abstract int minimumCapacity();
/*     */   
/*     */   public abstract IoBuffer minimumCapacity(int paramInt);
/*     */   
/*     */   public abstract int capacity();
/*     */   
/*     */   public abstract IoBuffer capacity(int paramInt);
/*     */   
/*     */   public abstract boolean isAutoExpand();
/*     */   
/*     */   public abstract IoBuffer setAutoExpand(boolean paramBoolean);
/*     */   
/*     */   public abstract boolean isAutoShrink();
/*     */   
/*     */   public abstract IoBuffer setAutoShrink(boolean paramBoolean);
/*     */   
/*     */   public abstract IoBuffer expand(int paramInt);
/*     */   
/*     */   public abstract IoBuffer expand(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer shrink();
/*     */   
/*     */   public abstract int position();
/*     */   
/*     */   public abstract IoBuffer position(int paramInt);
/*     */   
/*     */   public abstract int limit();
/*     */   
/*     */   public abstract IoBuffer limit(int paramInt);
/*     */   
/*     */   public abstract IoBuffer mark();
/*     */   
/*     */   public abstract int markValue();
/*     */   
/*     */   public abstract IoBuffer reset();
/*     */   
/*     */   public abstract IoBuffer clear();
/*     */   
/*     */   public abstract IoBuffer sweep();
/*     */   
/*     */   public abstract IoBuffer sweep(byte paramByte);
/*     */   
/*     */   public abstract IoBuffer flip();
/*     */   
/*     */   public abstract IoBuffer rewind();
/*     */   
/*     */   public abstract int remaining();
/*     */   
/*     */   public abstract boolean hasRemaining();
/*     */   
/*     */   public abstract IoBuffer duplicate();
/*     */   
/*     */   public abstract IoBuffer slice();
/*     */   
/*     */   public abstract IoBuffer asReadOnlyBuffer();
/*     */   
/*     */   public abstract boolean hasArray();
/*     */   
/*     */   public abstract byte[] array();
/*     */   
/*     */   public abstract int arrayOffset();
/*     */   
/*     */   public abstract byte get();
/*     */   
/*     */   public abstract short getUnsigned();
/*     */   
/*     */   public abstract IoBuffer put(byte paramByte);
/*     */   
/*     */   public abstract byte get(int paramInt);
/*     */   
/*     */   public abstract short getUnsigned(int paramInt);
/*     */   
/*     */   public abstract IoBuffer put(int paramInt, byte paramByte);
/*     */   
/*     */   public abstract IoBuffer get(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer get(byte[] paramArrayOfbyte);
/*     */   
/*     */   public abstract IoBuffer getSlice(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer getSlice(int paramInt);
/*     */   
/*     */   public abstract IoBuffer put(ByteBuffer paramByteBuffer);
/*     */   
/*     */   public abstract IoBuffer put(IoBuffer paramIoBuffer);
/*     */   
/*     */   public abstract IoBuffer put(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer put(byte[] paramArrayOfbyte);
/*     */   
/*     */   public abstract IoBuffer compact();
/*     */   
/*     */   public abstract ByteOrder order();
/*     */   
/*     */   public abstract IoBuffer order(ByteOrder paramByteOrder);
/*     */   
/*     */   public abstract char getChar();
/*     */   
/*     */   public abstract IoBuffer putChar(char paramChar);
/*     */   
/*     */   public abstract char getChar(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putChar(int paramInt, char paramChar);
/*     */   
/*     */   public abstract CharBuffer asCharBuffer();
/*     */   
/*     */   public abstract short getShort();
/*     */   
/*     */   public abstract int getUnsignedShort();
/*     */   
/*     */   public abstract IoBuffer putShort(short paramShort);
/*     */   
/*     */   public abstract short getShort(int paramInt);
/*     */   
/*     */   public abstract int getUnsignedShort(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putShort(int paramInt, short paramShort);
/*     */   
/*     */   public abstract ShortBuffer asShortBuffer();
/*     */   
/*     */   public abstract int getInt();
/*     */   
/*     */   public abstract long getUnsignedInt();
/*     */   
/*     */   public abstract int getMediumInt();
/*     */   
/*     */   public abstract int getUnsignedMediumInt();
/*     */   
/*     */   public abstract int getMediumInt(int paramInt);
/*     */   
/*     */   public abstract int getUnsignedMediumInt(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putMediumInt(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putMediumInt(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer putInt(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(byte paramByte);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(int paramInt, byte paramByte);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(short paramShort);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(int paramInt, short paramShort);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(long paramLong);
/*     */   
/*     */   public abstract IoBuffer putUnsigned(int paramInt, long paramLong);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(byte paramByte);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(int paramInt, byte paramByte);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(short paramShort);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(int paramInt, short paramShort);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(long paramLong);
/*     */   
/*     */   public abstract IoBuffer putUnsignedInt(int paramInt, long paramLong);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(byte paramByte);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(int paramInt, byte paramByte);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(short paramShort);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(int paramInt, short paramShort);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(long paramLong);
/*     */   
/*     */   public abstract IoBuffer putUnsignedShort(int paramInt, long paramLong);
/*     */   
/*     */   public abstract int getInt(int paramInt);
/*     */   
/*     */   public abstract long getUnsignedInt(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putInt(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract IntBuffer asIntBuffer();
/*     */   
/*     */   public abstract long getLong();
/*     */   
/*     */   public abstract IoBuffer putLong(long paramLong);
/*     */   
/*     */   public abstract long getLong(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putLong(int paramInt, long paramLong);
/*     */   
/*     */   public abstract LongBuffer asLongBuffer();
/*     */   
/*     */   public abstract float getFloat();
/*     */   
/*     */   public abstract IoBuffer putFloat(float paramFloat);
/*     */   
/*     */   public abstract float getFloat(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putFloat(int paramInt, float paramFloat);
/*     */   
/*     */   public abstract FloatBuffer asFloatBuffer();
/*     */   
/*     */   public abstract double getDouble();
/*     */   
/*     */   public abstract IoBuffer putDouble(double paramDouble);
/*     */   
/*     */   public abstract double getDouble(int paramInt);
/*     */   
/*     */   public abstract IoBuffer putDouble(int paramInt, double paramDouble);
/*     */   
/*     */   public abstract DoubleBuffer asDoubleBuffer();
/*     */   
/*     */   public abstract InputStream asInputStream();
/*     */   
/*     */   public abstract OutputStream asOutputStream();
/*     */   
/*     */   public abstract String getHexDump();
/*     */   
/*     */   public abstract String getHexDump(int paramInt);
/*     */   
/*     */   public abstract String getString(CharsetDecoder paramCharsetDecoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract String getString(int paramInt, CharsetDecoder paramCharsetDecoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract IoBuffer putString(CharSequence paramCharSequence, CharsetEncoder paramCharsetEncoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract IoBuffer putString(CharSequence paramCharSequence, int paramInt, CharsetEncoder paramCharsetEncoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract String getPrefixedString(CharsetDecoder paramCharsetDecoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract String getPrefixedString(int paramInt, CharsetDecoder paramCharsetDecoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract IoBuffer putPrefixedString(CharSequence paramCharSequence, CharsetEncoder paramCharsetEncoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract IoBuffer putPrefixedString(CharSequence paramCharSequence, int paramInt, CharsetEncoder paramCharsetEncoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract IoBuffer putPrefixedString(CharSequence paramCharSequence, int paramInt1, int paramInt2, CharsetEncoder paramCharsetEncoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract IoBuffer putPrefixedString(CharSequence paramCharSequence, int paramInt1, int paramInt2, byte paramByte, CharsetEncoder paramCharsetEncoder) throws CharacterCodingException;
/*     */   
/*     */   public abstract Object getObject() throws ClassNotFoundException;
/*     */   
/*     */   public abstract Object getObject(ClassLoader paramClassLoader) throws ClassNotFoundException;
/*     */   
/*     */   public abstract IoBuffer putObject(Object paramObject);
/*     */   
/*     */   public abstract boolean prefixedDataAvailable(int paramInt);
/*     */   
/*     */   public abstract boolean prefixedDataAvailable(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract int indexOf(byte paramByte);
/*     */   
/*     */   public abstract IoBuffer skip(int paramInt);
/*     */   
/*     */   public abstract IoBuffer fill(byte paramByte, int paramInt);
/*     */   
/*     */   public abstract IoBuffer fillAndReset(byte paramByte, int paramInt);
/*     */   
/*     */   public abstract IoBuffer fill(int paramInt);
/*     */   
/*     */   public abstract IoBuffer fillAndReset(int paramInt);
/*     */   
/*     */   public abstract <E extends Enum<E>> E getEnum(Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> E getEnum(int paramInt, Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> E getEnumShort(Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> E getEnumShort(int paramInt, Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> E getEnumInt(Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> E getEnumInt(int paramInt, Class<E> paramClass);
/*     */   
/*     */   public abstract IoBuffer putEnum(Enum<?> paramEnum);
/*     */   
/*     */   public abstract IoBuffer putEnum(int paramInt, Enum<?> paramEnum);
/*     */   
/*     */   public abstract IoBuffer putEnumShort(Enum<?> paramEnum);
/*     */   
/*     */   public abstract IoBuffer putEnumShort(int paramInt, Enum<?> paramEnum);
/*     */   
/*     */   public abstract IoBuffer putEnumInt(Enum<?> paramEnum);
/*     */   
/*     */   public abstract IoBuffer putEnumInt(int paramInt, Enum<?> paramEnum);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSet(Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSet(int paramInt, Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSetShort(Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSetShort(int paramInt, Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSetInt(Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSetInt(int paramInt, Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSetLong(Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> EnumSet<E> getEnumSetLong(int paramInt, Class<E> paramClass);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSet(Set<E> paramSet);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSet(int paramInt, Set<E> paramSet);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSetShort(Set<E> paramSet);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSetShort(int paramInt, Set<E> paramSet);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSetInt(Set<E> paramSet);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSetInt(int paramInt, Set<E> paramSet);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSetLong(Set<E> paramSet);
/*     */   
/*     */   public abstract <E extends Enum<E>> IoBuffer putEnumSetLong(int paramInt, Set<E> paramSet);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/buffer/IoBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */