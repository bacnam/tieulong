/*      */ package javolution.io;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import javolution.context.LocalContext;
/*      */ import javolution.lang.MathLib;
/*      */ import javolution.lang.Realtime;
/*      */ import javolution.text.TextBuilder;
/*      */ import sun.nio.ch.DirectBuffer;
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
/*      */ 
/*      */ @Realtime
/*      */ public class Struct
/*      */ {
/*  158 */   public static final LocalContext.Parameter<Integer> MAXIMUM_ALIGNMENT = new LocalContext.Parameter<Integer>()
/*      */     {
/*      */       protected Integer getDefault() {
/*  161 */         return Integer.valueOf(4);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Struct _outer;
/*      */ 
/*      */ 
/*      */   
/*      */   ByteBuffer _byteBuffer;
/*      */ 
/*      */ 
/*      */   
/*      */   int _outerOffset;
/*      */ 
/*      */ 
/*      */   
/*  181 */   int _alignment = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int _length;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int _index;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int _wordSize;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int _bitsUsed;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _resetIndex;
/*      */ 
/*      */ 
/*      */   
/*      */   byte[] _bytes;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Struct() {
/*  215 */     this._resetIndex = isUnion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int size() {
/*  226 */     return (this._alignment <= 1) ? this._length : ((this._length + this._alignment - 1) / this._alignment * this._alignment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Struct outer() {
/*  237 */     return this._outer;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public final ByteBuffer getByteBuffer() {
/*  255 */     if (this._outer != null) return this._outer.getByteBuffer(); 
/*  256 */     return (this._byteBuffer != null) ? this._byteBuffer : newBuffer();
/*      */   }
/*      */   
/*      */   private synchronized ByteBuffer newBuffer() {
/*  260 */     if (this._byteBuffer != null) return this._byteBuffer; 
/*  261 */     ByteBuffer bf = ByteBuffer.allocateDirect(size());
/*  262 */     bf.order(byteOrder());
/*  263 */     setByteBuffer(bf, 0);
/*  264 */     return this._byteBuffer;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Struct setByteBuffer(ByteBuffer byteBuffer, int position) {
/*  284 */     if (byteBuffer.order() != byteOrder()) throw new IllegalArgumentException("The byte order of the specified byte buffer is different from this struct byte order");
/*      */ 
/*      */     
/*  287 */     if (this._outer != null) throw new UnsupportedOperationException("Inner struct byte buffer is inherited from outer");
/*      */     
/*  289 */     this._byteBuffer = byteBuffer;
/*  290 */     this._outerOffset = position;
/*  291 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Struct setByteBufferPosition(int position) {
/*  302 */     return setByteBuffer(getByteBuffer(), position);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getByteBufferPosition() {
/*  313 */     return (this._outer != null) ? (this._outer.getByteBufferPosition() + this._outerOffset) : this._outerOffset;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read(InputStream in) throws IOException {
/*  333 */     ByteBuffer buffer = getByteBuffer();
/*  334 */     int size = size();
/*  335 */     int remaining = size - buffer.position();
/*  336 */     if (remaining == 0) remaining = size; 
/*  337 */     int alreadyRead = size - remaining;
/*  338 */     if (buffer.hasArray()) {
/*  339 */       int offset = buffer.arrayOffset() + getByteBufferPosition();
/*  340 */       int bytesRead = in.read(buffer.array(), offset + alreadyRead, remaining);
/*      */       
/*  342 */       buffer.position(getByteBufferPosition() + alreadyRead + bytesRead - offset);
/*      */       
/*  344 */       return bytesRead;
/*      */     } 
/*  346 */     synchronized (buffer) {
/*  347 */       if (this._bytes == null) {
/*  348 */         this._bytes = new byte[size()];
/*      */       }
/*  350 */       int bytesRead = in.read(this._bytes, 0, remaining);
/*  351 */       buffer.position(getByteBufferPosition() + alreadyRead);
/*  352 */       buffer.put(this._bytes, 0, bytesRead);
/*  353 */       return bytesRead;
/*      */     } 
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
/*      */   public void write(OutputStream out) throws IOException {
/*  367 */     ByteBuffer buffer = getByteBuffer();
/*  368 */     if (buffer.hasArray()) {
/*  369 */       int offset = buffer.arrayOffset() + getByteBufferPosition();
/*  370 */       out.write(buffer.array(), offset, size());
/*      */     } else {
/*  372 */       synchronized (buffer) {
/*  373 */         if (this._bytes == null) {
/*  374 */           this._bytes = new byte[size()];
/*      */         }
/*  376 */         buffer.position(getByteBufferPosition());
/*  377 */         buffer.get(this._bytes);
/*  378 */         out.write(this._bytes);
/*      */       } 
/*      */     } 
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
/*      */   public final long address() {
/*  394 */     ByteBuffer thisBuffer = getByteBuffer();
/*  395 */     if (thisBuffer instanceof DirectBuffer)
/*  396 */       return ((DirectBuffer)thisBuffer).address(); 
/*  397 */     throw new UnsupportedOperationException();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  421 */     TextBuilder tmp = new TextBuilder();
/*  422 */     int size = size();
/*  423 */     ByteBuffer buffer = getByteBuffer();
/*  424 */     int start = getByteBufferPosition();
/*  425 */     for (int i = 0; i < size; i++) {
/*  426 */       int b = buffer.get(start + i) & 0xFF;
/*  427 */       tmp.append(HEXA[b >> 4]);
/*  428 */       tmp.append(HEXA[b & 0xF]);
/*  429 */       tmp.append(((i & 0xF) == 15) ? 10 : 32);
/*      */     } 
/*  431 */     return tmp.toString();
/*      */   }
/*      */   
/*  434 */   private static final char[] HEXA = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
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
/*      */   public boolean isUnion() {
/*  462 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteOrder byteOrder() {
/*  481 */     return (this._outer != null) ? this._outer.byteOrder() : ByteOrder.BIG_ENDIAN;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPacked() {
/*  502 */     return false;
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
/*      */   protected <S extends Struct> S inner(S struct) {
/*  514 */     if (((Struct)struct)._outer != null) throw new IllegalArgumentException("struct: Already an inner struct");
/*      */     
/*  516 */     Member inner = new Member(struct.size() << 3, ((Struct)struct)._alignment);
/*  517 */     ((Struct)struct)._outer = this;
/*  518 */     ((Struct)struct)._outerOffset = inner.offset();
/*  519 */     return struct;
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
/*      */   protected <S extends Struct> S[] array(S[] structs) {
/*  533 */     Class<?> structClass = null;
/*  534 */     boolean resetIndexSaved = this._resetIndex;
/*  535 */     if (this._resetIndex) {
/*  536 */       this._index = 0;
/*  537 */       this._resetIndex = false;
/*      */     } 
/*  539 */     for (int i = 0; i < structs.length; ) {
/*  540 */       Struct struct1; S struct = structs[i];
/*  541 */       if (struct == null) {
/*      */         try {
/*  543 */           if (structClass == null) {
/*  544 */             String arrayName = structs.getClass().getName();
/*  545 */             String structName = arrayName.substring(2, arrayName.length() - 1);
/*      */             
/*  547 */             structClass = Class.forName(structName);
/*  548 */             if (structClass == null) throw new IllegalArgumentException("Struct class: " + structName + " not found");
/*      */           
/*      */           } 
/*  551 */           struct1 = (Struct)structClass.newInstance();
/*  552 */         } catch (Exception e) {
/*  553 */           throw new RuntimeException(e.getMessage());
/*      */         } 
/*      */       }
/*  556 */       structs[i++] = inner((S)struct1);
/*      */     } 
/*  558 */     this._resetIndex = resetIndexSaved;
/*  559 */     return structs;
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
/*      */   protected <S extends Struct> S[][] array(S[][] structs) {
/*  573 */     boolean resetIndexSaved = this._resetIndex;
/*  574 */     if (this._resetIndex) {
/*  575 */       this._index = 0;
/*  576 */       this._resetIndex = false;
/*      */     } 
/*  578 */     for (int i = 0; i < structs.length; i++) {
/*  579 */       array(structs[i]);
/*      */     }
/*  581 */     this._resetIndex = resetIndexSaved;
/*  582 */     return structs;
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
/*      */   protected <S extends Struct> S[][][] array(S[][][] structs) {
/*  596 */     boolean resetIndexSaved = this._resetIndex;
/*  597 */     if (this._resetIndex) {
/*  598 */       this._index = 0;
/*  599 */       this._resetIndex = false;
/*      */     } 
/*  601 */     for (int i = 0; i < structs.length; i++) {
/*  602 */       array(structs[i]);
/*      */     }
/*  604 */     this._resetIndex = resetIndexSaved;
/*  605 */     return structs;
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
/*      */   protected <M extends Member> M[] array(M[] arrayMember) {
/*  619 */     boolean resetIndexSaved = this._resetIndex;
/*  620 */     if (this._resetIndex) {
/*  621 */       this._index = 0;
/*  622 */       this._resetIndex = false;
/*      */     } 
/*  624 */     if (BOOL.isInstance(arrayMember)) {
/*  625 */       for (int i = 0; i < arrayMember.length;) {
/*  626 */         arrayMember[i++] = (M)new Bool();
/*      */       }
/*  628 */     } else if (SIGNED_8.isInstance(arrayMember)) {
/*  629 */       for (int i = 0; i < arrayMember.length;) {
/*  630 */         arrayMember[i++] = (M)new Signed8();
/*      */       }
/*  632 */     } else if (UNSIGNED_8.isInstance(arrayMember)) {
/*  633 */       for (int i = 0; i < arrayMember.length;) {
/*  634 */         arrayMember[i++] = (M)new Unsigned8();
/*      */       }
/*  636 */     } else if (SIGNED_16.isInstance(arrayMember)) {
/*  637 */       for (int i = 0; i < arrayMember.length;) {
/*  638 */         arrayMember[i++] = (M)new Signed16();
/*      */       }
/*  640 */     } else if (UNSIGNED_16.isInstance(arrayMember)) {
/*  641 */       for (int i = 0; i < arrayMember.length;) {
/*  642 */         arrayMember[i++] = (M)new Unsigned16();
/*      */       }
/*  644 */     } else if (SIGNED_32.isInstance(arrayMember)) {
/*  645 */       for (int i = 0; i < arrayMember.length;) {
/*  646 */         arrayMember[i++] = (M)new Signed32();
/*      */       }
/*  648 */     } else if (UNSIGNED_32.isInstance(arrayMember)) {
/*  649 */       for (int i = 0; i < arrayMember.length;) {
/*  650 */         arrayMember[i++] = (M)new Unsigned32();
/*      */       }
/*  652 */     } else if (SIGNED_64.isInstance(arrayMember)) {
/*  653 */       for (int i = 0; i < arrayMember.length;) {
/*  654 */         arrayMember[i++] = (M)new Signed64();
/*      */       }
/*  656 */     } else if (FLOAT_32.isInstance(arrayMember)) {
/*  657 */       for (int i = 0; i < arrayMember.length;) {
/*  658 */         arrayMember[i++] = (M)new Float32();
/*      */       }
/*  660 */     } else if (FLOAT_64.isInstance(arrayMember)) {
/*  661 */       for (int i = 0; i < arrayMember.length;) {
/*  662 */         arrayMember[i++] = (M)new Float64();
/*      */       }
/*      */     } else {
/*  665 */       throw new UnsupportedOperationException("Cannot create member elements, the arrayMember should contain the member instances instead of null");
/*      */     } 
/*      */ 
/*      */     
/*  669 */     this._resetIndex = resetIndexSaved;
/*  670 */     return arrayMember;
/*      */   }
/*      */   
/*  673 */   private static final Class<? extends Bool[]> BOOL = (Class)(new Bool[0]).getClass();
/*  674 */   private static final Class<? extends Signed8[]> SIGNED_8 = (Class)(new Signed8[0]).getClass();
/*      */   
/*  676 */   private static final Class<? extends Unsigned8[]> UNSIGNED_8 = (Class)(new Unsigned8[0]).getClass();
/*      */   
/*  678 */   private static final Class<? extends Signed16[]> SIGNED_16 = (Class)(new Signed16[0]).getClass();
/*      */   
/*  680 */   private static final Class<? extends Unsigned16[]> UNSIGNED_16 = (Class)(new Unsigned16[0]).getClass();
/*      */   
/*  682 */   private static final Class<? extends Signed32[]> SIGNED_32 = (Class)(new Signed32[0]).getClass();
/*      */   
/*  684 */   private static final Class<? extends Unsigned32[]> UNSIGNED_32 = (Class)(new Unsigned32[0]).getClass();
/*      */   
/*  686 */   private static final Class<? extends Signed64[]> SIGNED_64 = (Class)(new Signed64[0]).getClass();
/*      */   
/*  688 */   private static final Class<? extends Float32[]> FLOAT_32 = (Class)(new Float32[0]).getClass();
/*      */   
/*  690 */   private static final Class<? extends Float64[]> FLOAT_64 = (Class)(new Float64[0]).getClass();
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
/*      */   protected <M extends Member> M[][] array(M[][] arrayMember) {
/*  704 */     boolean resetIndexSaved = this._resetIndex;
/*  705 */     if (this._resetIndex) {
/*  706 */       this._index = 0;
/*  707 */       this._resetIndex = false;
/*      */     } 
/*  709 */     for (int i = 0; i < arrayMember.length; i++) {
/*  710 */       array(arrayMember[i]);
/*      */     }
/*  712 */     this._resetIndex = resetIndexSaved;
/*  713 */     return arrayMember;
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
/*      */   protected <M extends Member> M[][][] array(M[][][] arrayMember) {
/*  727 */     boolean resetIndexSaved = this._resetIndex;
/*  728 */     if (this._resetIndex) {
/*  729 */       this._index = 0;
/*  730 */       this._resetIndex = false;
/*      */     } 
/*  732 */     for (int i = 0; i < arrayMember.length; i++) {
/*  733 */       array(arrayMember[i]);
/*      */     }
/*  735 */     this._resetIndex = resetIndexSaved;
/*  736 */     return arrayMember;
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
/*      */   protected UTF8String[] array(UTF8String[] array, int stringLength) {
/*  748 */     boolean resetIndexSaved = this._resetIndex;
/*  749 */     if (this._resetIndex) {
/*  750 */       this._index = 0;
/*  751 */       this._resetIndex = false;
/*      */     } 
/*  753 */     for (int i = 0; i < array.length; i++) {
/*  754 */       array[i] = new UTF8String(stringLength);
/*      */     }
/*  756 */     this._resetIndex = resetIndexSaved;
/*  757 */     return array;
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
/*      */   public long readBits(int bitOffset, int bitSize) {
/*  771 */     if (bitOffset + bitSize - 1 >> 3 >= size()) throw new IllegalArgumentException("Attempt to read outside the Struct");
/*      */     
/*  773 */     int offset = bitOffset >> 3;
/*  774 */     int bitStart = bitOffset - (offset << 3);
/*  775 */     bitStart = (byteOrder() == ByteOrder.BIG_ENDIAN) ? bitStart : (64 - bitSize - bitStart);
/*      */     
/*  777 */     int index = getByteBufferPosition() + offset;
/*  778 */     long value = readByteBufferLong(index);
/*  779 */     value <<= bitStart;
/*  780 */     value >>= 64 - bitSize;
/*  781 */     return value;
/*      */   }
/*      */   
/*      */   private long readByteBufferLong(int index) {
/*  785 */     ByteBuffer byteBuffer = getByteBuffer();
/*  786 */     if (index + 8 < byteBuffer.limit()) return byteBuffer.getLong(index);
/*      */     
/*  788 */     if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
/*  789 */       return ((readByte(index, byteBuffer) & 0xFF) + ((readByte(++index, byteBuffer) & 0xFF) << 8) + ((readByte(++index, byteBuffer) & 0xFF) << 16)) + ((readByte(++index, byteBuffer) & 0xFFL) << 24L) + ((readByte(++index, byteBuffer) & 0xFFL) << 32L) + ((readByte(++index, byteBuffer) & 0xFFL) << 40L) + ((readByte(++index, byteBuffer) & 0xFFL) << 48L) + ((readByte(++index, byteBuffer) & 0xFFL) << 56L);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  798 */     return (readByte(index, byteBuffer) << 56L) + ((readByte(++index, byteBuffer) & 0xFFL) << 48L) + ((readByte(++index, byteBuffer) & 0xFFL) << 40L) + ((readByte(++index, byteBuffer) & 0xFFL) << 32L) + ((readByte(++index, byteBuffer) & 0xFFL) << 24L) + ((readByte(++index, byteBuffer) & 0xFF) << 16) + ((readByte(++index, byteBuffer) & 0xFF) << 8) + (readByte(++index, byteBuffer) & 0xFFL);
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
/*      */   private static byte readByte(int index, ByteBuffer byteBuffer) {
/*  810 */     return (index < byteBuffer.limit()) ? byteBuffer.get(index) : 0;
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
/*      */   public void writeBits(long value, int bitOffset, int bitSize) {
/*  823 */     if (bitOffset + bitSize - 1 >> 3 >= size()) throw new IllegalArgumentException("Attempt to write outside the Struct");
/*      */     
/*  825 */     int offset = bitOffset >> 3;
/*  826 */     int bitStart = (byteOrder() == ByteOrder.BIG_ENDIAN) ? (bitOffset - (offset << 3)) : (64 - bitSize - bitOffset - (offset << 3));
/*      */     
/*  828 */     long mask = -1L;
/*  829 */     mask <<= bitStart;
/*  830 */     mask >>>= 64 - bitSize;
/*  831 */     mask <<= 64 - bitSize - bitStart;
/*  832 */     value <<= 64 - bitSize - bitStart;
/*  833 */     value &= mask;
/*  834 */     int index = getByteBufferPosition() + offset;
/*  835 */     long oldValue = readByteBufferLong(index);
/*  836 */     long resetValue = oldValue & (mask ^ 0xFFFFFFFFFFFFFFFFL);
/*  837 */     long newValue = resetValue | value;
/*  838 */     writeByteBufferLong(index, newValue);
/*      */   }
/*      */   
/*      */   private void writeByteBufferLong(int index, long value) {
/*  842 */     ByteBuffer byteBuffer = getByteBuffer();
/*  843 */     if (index + 8 < byteBuffer.limit()) {
/*  844 */       byteBuffer.putLong(index, value);
/*      */       
/*      */       return;
/*      */     } 
/*  848 */     if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
/*  849 */       writeByte(index, byteBuffer, (byte)(int)value);
/*  850 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 8L));
/*  851 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 16L));
/*  852 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 24L));
/*  853 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 32L));
/*  854 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 40L));
/*  855 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 48L));
/*  856 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 56L));
/*      */     } else {
/*  858 */       writeByte(index, byteBuffer, (byte)(int)(value >> 56L));
/*  859 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 48L));
/*  860 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 40L));
/*  861 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 32L));
/*  862 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 24L));
/*  863 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 16L));
/*  864 */       writeByte(++index, byteBuffer, (byte)(int)(value >> 8L));
/*  865 */       writeByte(++index, byteBuffer, (byte)(int)value);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void writeByte(int index, ByteBuffer byteBuffer, byte value) {
/*  870 */     if (index < byteBuffer.limit()) {
/*  871 */       byteBuffer.put(index, value);
/*      */     }
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
/*      */   protected class Member
/*      */   {
/*      */     private final int _offset;
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
/*      */     private final int _bitIndex;
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
/*      */     private final int _bitLength;
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
/*      */     protected Member(int bitLength, int wordSize) {
/*  923 */       this._bitLength = bitLength;
/*      */ 
/*      */       
/*  926 */       if (Struct.this._resetIndex) {
/*  927 */         Struct.this._index = 0;
/*      */       }
/*      */ 
/*      */       
/*  931 */       if (wordSize == 0 || (bitLength != 0 && wordSize == Struct.this._wordSize && Struct.this._bitsUsed + bitLength <= wordSize << 3)) {
/*      */ 
/*      */         
/*  934 */         this._offset = Struct.this._index - Struct.this._wordSize;
/*  935 */         this._bitIndex = Struct.this._bitsUsed;
/*  936 */         Struct.this._bitsUsed += bitLength;
/*      */ 
/*      */         
/*  939 */         while (Struct.this._bitsUsed > Struct.this._wordSize << 3) {
/*  940 */           Struct.this._index++;
/*  941 */           Struct.this._wordSize++;
/*  942 */           Struct.this._length = MathLib.max(Struct.this._length, Struct.this._index);
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  948 */       if (!Struct.this.isPacked()) {
/*      */ 
/*      */         
/*  951 */         if (Struct.this._alignment < wordSize) {
/*  952 */           Struct.this._alignment = wordSize;
/*      */         }
/*      */ 
/*      */         
/*  956 */         int misaligned = Struct.this._index % wordSize;
/*  957 */         if (misaligned != 0) {
/*  958 */           Struct.this._index += wordSize - misaligned;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  963 */       this._offset = Struct.this._index;
/*  964 */       this._bitIndex = 0;
/*      */ 
/*      */       
/*  967 */       Struct.this._index += MathLib.max(wordSize, bitLength + 7 >> 3);
/*  968 */       Struct.this._wordSize = wordSize;
/*  969 */       Struct.this._bitsUsed = bitLength;
/*  970 */       Struct.this._length = MathLib.max(Struct.this._length, Struct.this._index);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final Struct struct() {
/*  980 */       return Struct.this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int offset() {
/*  990 */       return this._offset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int bitIndex() {
/*  999 */       return this._bitIndex;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int bitLength() {
/* 1009 */       return this._bitLength;
/*      */     }
/*      */ 
/*      */     
/*      */     final int get(int wordSize, int word) {
/* 1014 */       int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();
/*      */ 
/*      */       
/* 1017 */       word >>= shift;
/* 1018 */       int mask = -1 >>> 32 - bitLength();
/* 1019 */       return word & mask;
/*      */     }
/*      */ 
/*      */     
/*      */     final int set(int value, int wordSize, int word) {
/* 1024 */       int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();
/*      */ 
/*      */       
/* 1027 */       int mask = -1 >>> 32 - bitLength();
/* 1028 */       mask <<= shift;
/* 1029 */       value <<= shift;
/* 1030 */       return word & (mask ^ 0xFFFFFFFF) | value & mask;
/*      */     }
/*      */ 
/*      */     
/*      */     final long get(int wordSize, long word) {
/* 1035 */       int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();
/*      */ 
/*      */       
/* 1038 */       word >>= shift;
/* 1039 */       long mask = -1L >>> 64 - bitLength();
/* 1040 */       return word & mask;
/*      */     }
/*      */ 
/*      */     
/*      */     final long set(long value, int wordSize, long word) {
/* 1045 */       int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();
/*      */ 
/*      */       
/* 1048 */       long mask = -1L >>> 64 - bitLength();
/* 1049 */       mask <<= shift;
/* 1050 */       value <<= shift;
/* 1051 */       return word & (mask ^ 0xFFFFFFFFFFFFFFFFL) | value & mask;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class UTF8String
/*      */     extends Member
/*      */   {
/* 1064 */     private final UTF8ByteBufferWriter _writer = new UTF8ByteBufferWriter();
/* 1065 */     private final UTF8ByteBufferReader _reader = new UTF8ByteBufferReader();
/*      */     private final int _length;
/*      */     
/*      */     public UTF8String(int length) {
/* 1069 */       super(length << 3, 1);
/* 1070 */       this._length = length;
/*      */     }
/*      */     
/*      */     public void set(String string) {
/* 1074 */       ByteBuffer buffer = Struct.this.getByteBuffer();
/* 1075 */       synchronized (buffer) {
/*      */         try {
/* 1077 */           int index = Struct.this.getByteBufferPosition() + offset();
/* 1078 */           buffer.position(index);
/* 1079 */           this._writer.setOutput(buffer);
/* 1080 */           if (string.length() < this._length) {
/* 1081 */             this._writer.write(string);
/* 1082 */             this._writer.write(0);
/* 1083 */           } else if (string.length() > this._length) {
/* 1084 */             this._writer.write(string.substring(0, this._length));
/*      */           } else {
/* 1086 */             this._writer.write(string);
/*      */           } 
/* 1088 */         } catch (IOException e) {
/* 1089 */           throw new Error(e.getMessage());
/*      */         } finally {
/* 1091 */           this._writer.reset();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public String get() {
/* 1097 */       ByteBuffer buffer = Struct.this.getByteBuffer();
/* 1098 */       synchronized (buffer) {
/* 1099 */         TextBuilder tmp = new TextBuilder();
/*      */         try {
/* 1101 */           int index = Struct.this.getByteBufferPosition() + offset();
/* 1102 */           buffer.position(index);
/* 1103 */           this._reader.setInput(buffer);
/* 1104 */           for (int i = 0; i < this._length; i++) {
/* 1105 */             char c = (char)this._reader.read();
/* 1106 */             if (c == '\000') {
/* 1107 */               return tmp.toString();
/*      */             }
/* 1109 */             tmp.append(c);
/*      */           } 
/*      */           
/* 1112 */           return tmp.toString();
/* 1113 */         } catch (IOException e) {
/* 1114 */           throw new Error(e.getMessage());
/*      */         } finally {
/* 1116 */           this._reader.reset();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1122 */       return get();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class Bool
/*      */     extends Member
/*      */   {
/*      */     public Bool() {
/* 1133 */       super(8, 1);
/*      */     }
/*      */     
/*      */     public Bool(int nbrOfBits) {
/* 1137 */       super(nbrOfBits, 1);
/*      */     }
/*      */     
/*      */     public boolean get() {
/* 1141 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1142 */       int word = Struct.this.getByteBuffer().get(index);
/* 1143 */       word = (bitLength() == 8) ? word : get(1, word);
/* 1144 */       return (word != 0);
/*      */     }
/*      */     
/*      */     public void set(boolean value) {
/* 1148 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1149 */       if (bitLength() == 8) {
/* 1150 */         Struct.this.getByteBuffer().put(index, (byte)(value ? -1 : 0));
/*      */       } else {
/* 1152 */         Struct.this.getByteBuffer().put(index, (byte)set(value ? -1 : 0, 1, Struct.this.getByteBuffer().get(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1160 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Signed8
/*      */     extends Member
/*      */   {
/*      */     public Signed8() {
/* 1170 */       super(8, 1);
/*      */     }
/*      */     
/*      */     public Signed8(int nbrOfBits) {
/* 1174 */       super(nbrOfBits, 1);
/*      */     }
/*      */     
/*      */     public byte get() {
/* 1178 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1179 */       int word = Struct.this.getByteBuffer().get(index);
/* 1180 */       return (byte)((bitLength() == 8) ? word : get(1, word));
/*      */     }
/*      */     
/*      */     public void set(byte value) {
/* 1184 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1185 */       if (bitLength() == 8) {
/* 1186 */         Struct.this.getByteBuffer().put(index, value);
/*      */       } else {
/* 1188 */         Struct.this.getByteBuffer().put(index, (byte)set(value, 1, Struct.this.getByteBuffer().get(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1194 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Unsigned8
/*      */     extends Member
/*      */   {
/*      */     public Unsigned8() {
/* 1204 */       super(8, 1);
/*      */     }
/*      */     
/*      */     public Unsigned8(int nbrOfBits) {
/* 1208 */       super(nbrOfBits, 1);
/*      */     }
/*      */     
/*      */     public short get() {
/* 1212 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1213 */       int word = Struct.this.getByteBuffer().get(index);
/* 1214 */       return (short)(0xFF & ((bitLength() == 8) ? word : get(1, word)));
/*      */     }
/*      */     
/*      */     public void set(short value) {
/* 1218 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1219 */       if (bitLength() == 8) {
/* 1220 */         Struct.this.getByteBuffer().put(index, (byte)value);
/*      */       } else {
/* 1222 */         Struct.this.getByteBuffer().put(index, (byte)set(value, 1, Struct.this.getByteBuffer().get(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1228 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Signed16
/*      */     extends Member
/*      */   {
/*      */     public Signed16() {
/* 1238 */       super(16, 2);
/*      */     }
/*      */     
/*      */     public Signed16(int nbrOfBits) {
/* 1242 */       super(nbrOfBits, 2);
/*      */     }
/*      */     
/*      */     public short get() {
/* 1246 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1247 */       int word = Struct.this.getByteBuffer().getShort(index);
/* 1248 */       return (short)((bitLength() == 16) ? word : get(2, word));
/*      */     }
/*      */     
/*      */     public void set(short value) {
/* 1252 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1253 */       if (bitLength() == 16) {
/* 1254 */         Struct.this.getByteBuffer().putShort(index, value);
/*      */       } else {
/* 1256 */         Struct.this.getByteBuffer().putShort(index, (short)set(value, 2, Struct.this.getByteBuffer().getShort(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1262 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Unsigned16
/*      */     extends Member
/*      */   {
/*      */     public Unsigned16() {
/* 1272 */       super(16, 2);
/*      */     }
/*      */     
/*      */     public Unsigned16(int nbrOfBits) {
/* 1276 */       super(nbrOfBits, 2);
/*      */     }
/*      */     
/*      */     public int get() {
/* 1280 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1281 */       int word = Struct.this.getByteBuffer().getShort(index);
/* 1282 */       return 0xFFFF & ((bitLength() == 16) ? word : get(2, word));
/*      */     }
/*      */     
/*      */     public void set(int value) {
/* 1286 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1287 */       if (bitLength() == 16) {
/* 1288 */         Struct.this.getByteBuffer().putShort(index, (short)value);
/*      */       } else {
/* 1290 */         Struct.this.getByteBuffer().putShort(index, (short)set(value, 2, Struct.this.getByteBuffer().getShort(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1296 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Signed32
/*      */     extends Member
/*      */   {
/*      */     public Signed32() {
/* 1306 */       super(32, 4);
/*      */     }
/*      */     
/*      */     public Signed32(int nbrOfBits) {
/* 1310 */       super(nbrOfBits, 4);
/*      */     }
/*      */     
/*      */     public int get() {
/* 1314 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1315 */       int word = Struct.this.getByteBuffer().getInt(index);
/* 1316 */       return (bitLength() == 32) ? word : get(4, word);
/*      */     }
/*      */     
/*      */     public void set(int value) {
/* 1320 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1321 */       if (bitLength() == 32) {
/* 1322 */         Struct.this.getByteBuffer().putInt(index, value);
/*      */       } else {
/* 1324 */         Struct.this.getByteBuffer().putInt(index, set(value, 4, Struct.this.getByteBuffer().getInt(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1330 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Unsigned32
/*      */     extends Member
/*      */   {
/*      */     public Unsigned32() {
/* 1340 */       super(32, 4);
/*      */     }
/*      */     
/*      */     public Unsigned32(int nbrOfBits) {
/* 1344 */       super(nbrOfBits, 4);
/*      */     }
/*      */     
/*      */     public long get() {
/* 1348 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1349 */       int word = Struct.this.getByteBuffer().getInt(index);
/* 1350 */       return 0xFFFFFFFFL & ((bitLength() == 32) ? word : get(4, word));
/*      */     }
/*      */     
/*      */     public void set(long value) {
/* 1354 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1355 */       if (bitLength() == 32) {
/* 1356 */         Struct.this.getByteBuffer().putInt(index, (int)value);
/*      */       } else {
/* 1358 */         Struct.this.getByteBuffer().putInt(index, set((int)value, 4, Struct.this.getByteBuffer().getInt(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1364 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Signed64
/*      */     extends Member
/*      */   {
/*      */     public Signed64() {
/* 1374 */       super(64, 8);
/*      */     }
/*      */     
/*      */     public Signed64(int nbrOfBits) {
/* 1378 */       super(nbrOfBits, 8);
/*      */     }
/*      */     
/*      */     public long get() {
/* 1382 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1383 */       long word = Struct.this.getByteBuffer().getLong(index);
/* 1384 */       return (bitLength() == 64) ? word : get(8, word);
/*      */     }
/*      */     
/*      */     public void set(long value) {
/* 1388 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1389 */       if (bitLength() == 64) {
/* 1390 */         Struct.this.getByteBuffer().putLong(index, value);
/*      */       } else {
/* 1392 */         Struct.this.getByteBuffer().putLong(index, set(value, 8, Struct.this.getByteBuffer().getLong(index)));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1398 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class BitField
/*      */     extends Member
/*      */   {
/*      */     public BitField(int nbrOfBits) {
/* 1409 */       super(nbrOfBits, 0);
/*      */     }
/*      */     
/*      */     public long longValue() {
/* 1413 */       long signedValue = Struct.this.readBits(bitIndex() + (offset() << 3), bitLength());
/*      */       
/* 1415 */       return (-1L << bitLength() ^ 0xFFFFFFFFFFFFFFFFL) & signedValue;
/*      */     }
/*      */     
/*      */     public int intValue() {
/* 1419 */       return (int)longValue();
/*      */     }
/*      */     
/*      */     public short shortValue() {
/* 1423 */       return (short)(int)longValue();
/*      */     }
/*      */     
/*      */     public byte byteValue() {
/* 1427 */       return (byte)(int)longValue();
/*      */     }
/*      */     
/*      */     public void set(long value) {
/* 1431 */       Struct.this.writeBits(value, bitIndex() + (offset() << 3), bitLength());
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1435 */       return String.valueOf(longValue());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Float32
/*      */     extends Member
/*      */   {
/*      */     public Float32() {
/* 1445 */       super(32, 4);
/*      */     }
/*      */     
/*      */     public float get() {
/* 1449 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1450 */       return Struct.this.getByteBuffer().getFloat(index);
/*      */     }
/*      */     
/*      */     public void set(float value) {
/* 1454 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1455 */       Struct.this.getByteBuffer().putFloat(index, value);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1459 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Float64
/*      */     extends Member
/*      */   {
/*      */     public Float64() {
/* 1469 */       super(64, 8);
/*      */     }
/*      */     
/*      */     public double get() {
/* 1473 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1474 */       return Struct.this.getByteBuffer().getDouble(index);
/*      */     }
/*      */     
/*      */     public void set(double value) {
/* 1478 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1479 */       Struct.this.getByteBuffer().putDouble(index, value);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1483 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class Reference32<S extends Struct>
/*      */     extends Member
/*      */   {
/*      */     private S _struct;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Reference32() {
/* 1502 */       super(32, 4);
/*      */     }
/*      */     
/*      */     public void set(S struct) {
/* 1506 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1507 */       if (struct != null) {
/* 1508 */         Struct.this.getByteBuffer().putInt(index, (int)struct.address());
/*      */       } else {
/* 1510 */         Struct.this.getByteBuffer().putInt(index, 0);
/*      */       } 
/* 1512 */       this._struct = struct;
/*      */     }
/*      */     
/*      */     public S get() {
/* 1516 */       return this._struct;
/*      */     }
/*      */     
/*      */     public int value() {
/* 1520 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1521 */       return Struct.this.getByteBuffer().getInt(index);
/*      */     }
/*      */     
/*      */     public boolean isUpToDate() {
/* 1525 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1526 */       if (this._struct != null) {
/* 1527 */         return (Struct.this.getByteBuffer().getInt(index) == (int)this._struct.address());
/*      */       }
/* 1529 */       return (Struct.this.getByteBuffer().getInt(index) == 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class Reference64<S extends Struct>
/*      */     extends Member
/*      */   {
/*      */     private S _struct;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Reference64() {
/* 1549 */       super(64, 8);
/*      */     }
/*      */     
/*      */     public void set(S struct) {
/* 1553 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1554 */       if (struct != null) {
/* 1555 */         Struct.this.getByteBuffer().putLong(index, struct.address());
/* 1556 */       } else if (struct == null) {
/* 1557 */         Struct.this.getByteBuffer().putLong(index, 0L);
/*      */       } 
/* 1559 */       this._struct = struct;
/*      */     }
/*      */     
/*      */     public S get() {
/* 1563 */       return this._struct;
/*      */     }
/*      */     
/*      */     public long value() {
/* 1567 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1568 */       return Struct.this.getByteBuffer().getLong(index);
/*      */     }
/*      */     
/*      */     public boolean isUpToDate() {
/* 1572 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1573 */       if (this._struct != null) {
/* 1574 */         return (Struct.this.getByteBuffer().getLong(index) == this._struct.address());
/*      */       }
/* 1576 */       return (Struct.this.getByteBuffer().getLong(index) == 0L);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class Enum8<T extends Enum<T>>
/*      */     extends Member
/*      */   {
/*      */     private final T[] _values;
/*      */ 
/*      */     
/*      */     public Enum8(T[] values) {
/* 1589 */       super(8, 1);
/* 1590 */       this._values = values;
/*      */     }
/*      */     
/*      */     public Enum8(T[] values, int nbrOfBits) {
/* 1594 */       super(nbrOfBits, 1);
/* 1595 */       this._values = values;
/*      */     }
/*      */     
/*      */     public T get() {
/* 1599 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1600 */       int word = Struct.this.getByteBuffer().get(index);
/* 1601 */       return this._values[0xFF & get(1, word)];
/*      */     }
/*      */     
/*      */     public void set(T e) {
/* 1605 */       int value = e.ordinal();
/* 1606 */       if (this._values[value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");
/*      */ 
/*      */ 
/*      */       
/* 1610 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1611 */       int word = Struct.this.getByteBuffer().get(index);
/* 1612 */       Struct.this.getByteBuffer().put(index, (byte)set(value, 1, word));
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1616 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class Enum16<T extends Enum<T>>
/*      */     extends Member
/*      */   {
/*      */     private final T[] _values;
/*      */ 
/*      */     
/*      */     public Enum16(T[] values) {
/* 1628 */       super(16, 2);
/* 1629 */       this._values = values;
/*      */     }
/*      */     
/*      */     public Enum16(T[] values, int nbrOfBits) {
/* 1633 */       super(nbrOfBits, 2);
/* 1634 */       this._values = values;
/*      */     }
/*      */     
/*      */     public T get() {
/* 1638 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1639 */       int word = Struct.this.getByteBuffer().getShort(index);
/* 1640 */       return this._values[0xFFFF & get(2, word)];
/*      */     }
/*      */     
/*      */     public void set(T e) {
/* 1644 */       int value = e.ordinal();
/* 1645 */       if (this._values[value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");
/*      */ 
/*      */ 
/*      */       
/* 1649 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1650 */       int word = Struct.this.getByteBuffer().getShort(index);
/* 1651 */       Struct.this.getByteBuffer().putShort(index, (short)set(value, 2, word));
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1655 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class Enum32<T extends Enum<T>>
/*      */     extends Member
/*      */   {
/*      */     private final T[] _values;
/*      */ 
/*      */     
/*      */     public Enum32(T[] values) {
/* 1667 */       super(32, 4);
/* 1668 */       this._values = values;
/*      */     }
/*      */     
/*      */     public Enum32(T[] values, int nbrOfBits) {
/* 1672 */       super(nbrOfBits, 4);
/* 1673 */       this._values = values;
/*      */     }
/*      */     
/*      */     public T get() {
/* 1677 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1678 */       int word = Struct.this.getByteBuffer().getInt(index);
/* 1679 */       return this._values[get(4, word)];
/*      */     }
/*      */     
/*      */     public void set(T e) {
/* 1683 */       int value = e.ordinal();
/* 1684 */       if (this._values[value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");
/*      */ 
/*      */ 
/*      */       
/* 1688 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1689 */       int word = Struct.this.getByteBuffer().getInt(index);
/* 1690 */       Struct.this.getByteBuffer().putInt(index, set(value, 4, word));
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1694 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class Enum64<T extends Enum<T>>
/*      */     extends Member
/*      */   {
/*      */     private final T[] _values;
/*      */ 
/*      */     
/*      */     public Enum64(T[] values) {
/* 1706 */       super(64, 8);
/* 1707 */       this._values = values;
/*      */     }
/*      */     
/*      */     public Enum64(T[] values, int nbrOfBits) {
/* 1711 */       super(nbrOfBits, 8);
/* 1712 */       this._values = values;
/*      */     }
/*      */     
/*      */     public T get() {
/* 1716 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1717 */       long word = Struct.this.getByteBuffer().getLong(index);
/* 1718 */       return this._values[(int)get(8, word)];
/*      */     }
/*      */     
/*      */     public void set(T e) {
/* 1722 */       long value = e.ordinal();
/* 1723 */       if (this._values[(int)value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");
/*      */ 
/*      */ 
/*      */       
/* 1727 */       int index = Struct.this.getByteBufferPosition() + offset();
/* 1728 */       long word = Struct.this.getByteBuffer().getLong(index);
/* 1729 */       Struct.this.getByteBuffer().putLong(index, set(value, 8, word));
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1733 */       return String.valueOf(get());
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/io/Struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */