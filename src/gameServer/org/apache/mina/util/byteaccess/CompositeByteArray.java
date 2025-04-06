/*      */ package org.apache.mina.util.byteaccess;
/*      */ 
/*      */ import java.nio.ByteOrder;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import org.apache.mina.core.buffer.IoBuffer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CompositeByteArray
/*      */   extends AbstractByteArray
/*      */ {
/*   72 */   private final ByteArrayList bas = new ByteArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteOrder order;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ByteArrayFactory byteArrayFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteArray() {
/*   88 */     this(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteArray(ByteArrayFactory byteArrayFactory) {
/*   99 */     this.byteArrayFactory = byteArrayFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray getFirst() {
/*  109 */     if (this.bas.isEmpty()) {
/*  110 */       return null;
/*      */     }
/*      */     
/*  113 */     return this.bas.getFirst().getByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFirst(ByteArray ba) {
/*  124 */     addHook(ba);
/*  125 */     this.bas.addFirst(ba);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray removeFirst() {
/*  135 */     ByteArrayList.Node node = this.bas.removeFirst();
/*  136 */     return (node == null) ? null : node.getByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray removeTo(int index) {
/*  147 */     if (index < first() || index > last()) {
/*  148 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */ 
/*      */     
/*  152 */     CompositeByteArray prefix = new CompositeByteArray(this.byteArrayFactory);
/*  153 */     int remaining = index - first();
/*      */     
/*  155 */     while (remaining > 0) {
/*  156 */       ByteArray component = removeFirst();
/*      */       
/*  158 */       if (component.last() <= remaining) {
/*      */         
/*  160 */         prefix.addLast(component);
/*  161 */         remaining -= component.last();
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  167 */       IoBuffer bb = component.getSingleIoBuffer();
/*      */       
/*  169 */       int originalLimit = bb.limit();
/*      */       
/*  171 */       bb.position(0);
/*      */       
/*  173 */       bb.limit(remaining);
/*      */       
/*  175 */       IoBuffer bb1 = bb.slice();
/*      */       
/*  177 */       bb.position(remaining);
/*      */       
/*  179 */       bb.limit(originalLimit);
/*      */       
/*  181 */       IoBuffer bb2 = bb.slice();
/*      */       
/*  183 */       ByteArray ba1 = new BufferByteArray(bb1)
/*      */         {
/*      */           public void free() {}
/*      */         };
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  191 */       prefix.addLast(ba1);
/*  192 */       remaining -= ba1.last();
/*      */ 
/*      */       
/*  195 */       final ByteArray componentFinal = component;
/*  196 */       ByteArray ba2 = new BufferByteArray(bb2)
/*      */         {
/*      */           public void free() {
/*  199 */             componentFinal.free();
/*      */           }
/*      */         };
/*      */       
/*  203 */       addFirst(ba2);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  208 */     return prefix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLast(ByteArray ba) {
/*  218 */     addHook(ba);
/*  219 */     this.bas.addLast(ba);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray removeLast() {
/*  229 */     ByteArrayList.Node node = this.bas.removeLast();
/*  230 */     return (node == null) ? null : node.getByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void free() {
/*  237 */     while (!this.bas.isEmpty()) {
/*  238 */       ByteArrayList.Node node = this.bas.getLast();
/*  239 */       node.getByteArray().free();
/*  240 */       this.bas.removeLast();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkBounds(int index, int accessSize) {
/*  245 */     int lower = index;
/*  246 */     int upper = index + accessSize;
/*      */     
/*  248 */     if (lower < first()) {
/*  249 */       throw new IndexOutOfBoundsException("Index " + lower + " less than start " + first() + ".");
/*      */     }
/*      */     
/*  252 */     if (upper > last()) {
/*  253 */       throw new IndexOutOfBoundsException("Index " + upper + " greater than length " + last() + ".");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<IoBuffer> getIoBuffers() {
/*  261 */     if (this.bas.isEmpty()) {
/*  262 */       return Collections.emptyList();
/*      */     }
/*      */     
/*  265 */     Collection<IoBuffer> result = new ArrayList<IoBuffer>();
/*  266 */     ByteArrayList.Node node = this.bas.getFirst();
/*      */     
/*  268 */     for (IoBuffer bb : node.getByteArray().getIoBuffers()) {
/*  269 */       result.add(bb);
/*      */     }
/*      */     
/*  272 */     while (node.hasNextNode()) {
/*  273 */       node = node.getNextNode();
/*      */       
/*  275 */       for (IoBuffer bb : node.getByteArray().getIoBuffers()) {
/*  276 */         result.add(bb);
/*      */       }
/*      */     } 
/*      */     
/*  280 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoBuffer getSingleIoBuffer() {
/*  287 */     if (this.byteArrayFactory == null) {
/*  288 */       throw new IllegalStateException("Can't get single buffer from CompositeByteArray unless it has a ByteArrayFactory.");
/*      */     }
/*      */ 
/*      */     
/*  292 */     if (this.bas.isEmpty()) {
/*  293 */       ByteArray byteArray = this.byteArrayFactory.create(1);
/*  294 */       return byteArray.getSingleIoBuffer();
/*      */     } 
/*      */     
/*  297 */     int actualLength = last() - first();
/*      */ 
/*      */     
/*  300 */     ByteArrayList.Node node = this.bas.getFirst();
/*  301 */     ByteArray ba = node.getByteArray();
/*      */     
/*  303 */     if (ba.last() == actualLength) {
/*  304 */       return ba.getSingleIoBuffer();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  309 */     ByteArray target = this.byteArrayFactory.create(actualLength);
/*  310 */     IoBuffer bb = target.getSingleIoBuffer();
/*  311 */     ByteArray.Cursor cursor = cursor();
/*  312 */     cursor.put(bb);
/*      */     
/*  314 */     while (!this.bas.isEmpty()) {
/*  315 */       ByteArrayList.Node node1 = this.bas.getLast();
/*  316 */       ByteArray component = node1.getByteArray();
/*  317 */       this.bas.removeLast();
/*  318 */       component.free();
/*      */     } 
/*      */     
/*  321 */     this.bas.addLast(target);
/*  322 */     return bb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray.Cursor cursor() {
/*  329 */     return new CursorImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray.Cursor cursor(int index) {
/*  336 */     return new CursorImpl(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray.Cursor cursor(CursorListener listener) {
/*  347 */     return new CursorImpl(listener);
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
/*      */   public ByteArray.Cursor cursor(int index, CursorListener listener) {
/*  359 */     return new CursorImpl(index, listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteArray slice(int index, int length) {
/*  366 */     return cursor(index).slice(length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte get(int index) {
/*  373 */     return cursor(index).get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(int index, byte b) {
/*  380 */     cursor(index).put(b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void get(int index, IoBuffer bb) {
/*  387 */     cursor(index).get(bb);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(int index, IoBuffer bb) {
/*  394 */     cursor(index).put(bb);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int first() {
/*  401 */     return this.bas.firstByte();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int last() {
/*  408 */     return this.bas.lastByte();
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
/*      */   private void addHook(ByteArray ba) {
/*  421 */     if (ba.first() != 0) {
/*  422 */       throw new IllegalArgumentException("Cannot add byte array that doesn't start from 0: " + ba.first());
/*      */     }
/*      */     
/*  425 */     if (this.order == null) {
/*  426 */       this.order = ba.order();
/*  427 */     } else if (!this.order.equals(ba.order())) {
/*  428 */       throw new IllegalArgumentException("Cannot add byte array with different byte order: " + ba.order());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*  436 */     if (this.order == null) {
/*  437 */       throw new IllegalStateException("Byte order not yet set.");
/*      */     }
/*  439 */     return this.order;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void order(ByteOrder order) {
/*  446 */     if (order == null || !order.equals(this.order)) {
/*  447 */       this.order = order;
/*      */       
/*  449 */       if (!this.bas.isEmpty()) {
/*  450 */         for (ByteArrayList.Node node = this.bas.getFirst(); node.hasNextNode(); node = node.getNextNode()) {
/*  451 */           node.getByteArray().order(order);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  461 */     return cursor(index).getShort();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putShort(int index, short s) {
/*  468 */     cursor(index).putShort(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  475 */     return cursor(index).getInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putInt(int index, int i) {
/*  482 */     cursor(index).putInt(i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  489 */     return cursor(index).getLong();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putLong(int index, long l) {
/*  496 */     cursor(index).putLong(l);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  503 */     return cursor(index).getFloat();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putFloat(int index, float f) {
/*  510 */     cursor(index).putFloat(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  517 */     return cursor(index).getDouble();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putDouble(int index, double d) {
/*  524 */     cursor(index).putDouble(d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  531 */     return cursor(index).getChar();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putChar(int index, char c) {
/*  538 */     cursor(index).putChar(c);
/*      */   }
/*      */   
/*      */   public static interface CursorListener {
/*      */     void enteredFirstComponent(int param1Int, ByteArray param1ByteArray);
/*      */     
/*      */     void enteredNextComponent(int param1Int, ByteArray param1ByteArray);
/*      */     
/*      */     void enteredPreviousComponent(int param1Int, ByteArray param1ByteArray);
/*      */     
/*      */     void enteredLastComponent(int param1Int, ByteArray param1ByteArray);
/*      */   }
/*      */   
/*      */   private class CursorImpl implements ByteArray.Cursor {
/*      */     private int index;
/*      */     private final CompositeByteArray.CursorListener listener;
/*      */     
/*      */     public CursorImpl() {
/*  556 */       this(0, (CompositeByteArray.CursorListener)null);
/*      */     }
/*      */     private ByteArrayList.Node componentNode; private int componentIndex; private ByteArray.Cursor componentCursor;
/*      */     public CursorImpl(int index) {
/*  560 */       this(index, (CompositeByteArray.CursorListener)null);
/*      */     }
/*      */     
/*      */     public CursorImpl(CompositeByteArray.CursorListener listener) {
/*  564 */       this(0, listener);
/*      */     }
/*      */     
/*      */     public CursorImpl(int index, CompositeByteArray.CursorListener listener) {
/*  568 */       this.index = index;
/*  569 */       this.listener = listener;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getIndex() {
/*  576 */       return this.index;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setIndex(int index) {
/*  583 */       CompositeByteArray.this.checkBounds(index, 0);
/*  584 */       this.index = index;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void skip(int length) {
/*  591 */       setIndex(this.index + length);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ByteArray slice(int length) {
/*  598 */       CompositeByteArray slice = new CompositeByteArray(CompositeByteArray.this.byteArrayFactory);
/*  599 */       int remaining = length;
/*  600 */       while (remaining > 0) {
/*  601 */         prepareForAccess(remaining);
/*  602 */         int componentSliceSize = Math.min(remaining, this.componentCursor.getRemaining());
/*  603 */         ByteArray componentSlice = this.componentCursor.slice(componentSliceSize);
/*  604 */         slice.addLast(componentSlice);
/*  605 */         this.index += componentSliceSize;
/*  606 */         remaining -= componentSliceSize;
/*      */       } 
/*  608 */       return slice;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ByteOrder order() {
/*  615 */       return CompositeByteArray.this.order();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void prepareForAccess(int accessSize) {
/*  621 */       if (this.componentNode != null && this.componentNode.isRemoved()) {
/*  622 */         this.componentNode = null;
/*  623 */         this.componentCursor = null;
/*      */       } 
/*      */ 
/*      */       
/*  627 */       CompositeByteArray.this.checkBounds(this.index, accessSize);
/*      */ 
/*      */ 
/*      */       
/*  631 */       ByteArrayList.Node oldComponentNode = this.componentNode;
/*      */ 
/*      */       
/*  634 */       if (this.componentNode == null) {
/*  635 */         int basMidpoint = (CompositeByteArray.this.last() - CompositeByteArray.this.first()) / 2 + CompositeByteArray.this.first();
/*  636 */         if (this.index <= basMidpoint) {
/*      */           
/*  638 */           this.componentNode = CompositeByteArray.this.bas.getFirst();
/*  639 */           this.componentIndex = CompositeByteArray.this.first();
/*  640 */           if (this.listener != null) {
/*  641 */             this.listener.enteredFirstComponent(this.componentIndex, this.componentNode.getByteArray());
/*      */           }
/*      */         } else {
/*      */           
/*  645 */           this.componentNode = CompositeByteArray.this.bas.getLast();
/*  646 */           this.componentIndex = CompositeByteArray.this.last() - this.componentNode.getByteArray().last();
/*  647 */           if (this.listener != null) {
/*  648 */             this.listener.enteredLastComponent(this.componentIndex, this.componentNode.getByteArray());
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  654 */       while (this.index < this.componentIndex) {
/*  655 */         this.componentNode = this.componentNode.getPreviousNode();
/*  656 */         this.componentIndex -= this.componentNode.getByteArray().last();
/*  657 */         if (this.listener != null) {
/*  658 */           this.listener.enteredPreviousComponent(this.componentIndex, this.componentNode.getByteArray());
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  663 */       while (this.index >= this.componentIndex + this.componentNode.getByteArray().length()) {
/*  664 */         this.componentIndex += this.componentNode.getByteArray().last();
/*  665 */         this.componentNode = this.componentNode.getNextNode();
/*  666 */         if (this.listener != null) {
/*  667 */           this.listener.enteredNextComponent(this.componentIndex, this.componentNode.getByteArray());
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  672 */       int internalComponentIndex = this.index - this.componentIndex;
/*  673 */       if (this.componentNode == oldComponentNode) {
/*      */         
/*  675 */         this.componentCursor.setIndex(internalComponentIndex);
/*      */       } else {
/*      */         
/*  678 */         this.componentCursor = this.componentNode.getByteArray().cursor(internalComponentIndex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getRemaining() {
/*  686 */       return CompositeByteArray.this.last() - this.index + 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasRemaining() {
/*  693 */       return (getRemaining() > 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte get() {
/*  700 */       prepareForAccess(1);
/*  701 */       byte b = this.componentCursor.get();
/*  702 */       this.index++;
/*  703 */       return b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void put(byte b) {
/*  710 */       prepareForAccess(1);
/*  711 */       this.componentCursor.put(b);
/*  712 */       this.index++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void get(IoBuffer bb) {
/*  719 */       while (bb.hasRemaining()) {
/*  720 */         int remainingBefore = bb.remaining();
/*  721 */         prepareForAccess(remainingBefore);
/*  722 */         this.componentCursor.get(bb);
/*  723 */         int remainingAfter = bb.remaining();
/*      */         
/*  725 */         int chunkSize = remainingBefore - remainingAfter;
/*  726 */         this.index += chunkSize;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void put(IoBuffer bb) {
/*  734 */       while (bb.hasRemaining()) {
/*  735 */         int remainingBefore = bb.remaining();
/*  736 */         prepareForAccess(remainingBefore);
/*  737 */         this.componentCursor.put(bb);
/*  738 */         int remainingAfter = bb.remaining();
/*      */         
/*  740 */         int chunkSize = remainingBefore - remainingAfter;
/*  741 */         this.index += chunkSize;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public short getShort() {
/*  749 */       prepareForAccess(2);
/*  750 */       if (this.componentCursor.getRemaining() >= 4) {
/*  751 */         short s = this.componentCursor.getShort();
/*  752 */         this.index += 2;
/*  753 */         return s;
/*      */       } 
/*  755 */       byte b0 = get();
/*  756 */       byte b1 = get();
/*  757 */       if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/*  758 */         return (short)(b0 << 8 | b1 << 0);
/*      */       }
/*  760 */       return (short)(b1 << 8 | b0 << 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void putShort(short s) {
/*  769 */       prepareForAccess(2);
/*  770 */       if (this.componentCursor.getRemaining() >= 4) {
/*  771 */         this.componentCursor.putShort(s);
/*  772 */         this.index += 2;
/*      */       } else {
/*      */         byte b0, b1;
/*      */         
/*  776 */         if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/*  777 */           b0 = (byte)(s >> 8 & 0xFF);
/*  778 */           b1 = (byte)(s >> 0 & 0xFF);
/*      */         } else {
/*  780 */           b0 = (byte)(s >> 0 & 0xFF);
/*  781 */           b1 = (byte)(s >> 8 & 0xFF);
/*      */         } 
/*  783 */         put(b0);
/*  784 */         put(b1);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getInt() {
/*  792 */       prepareForAccess(4);
/*  793 */       if (this.componentCursor.getRemaining() >= 4) {
/*  794 */         int i = this.componentCursor.getInt();
/*  795 */         this.index += 4;
/*  796 */         return i;
/*      */       } 
/*  798 */       byte b0 = get();
/*  799 */       byte b1 = get();
/*  800 */       byte b2 = get();
/*  801 */       byte b3 = get();
/*  802 */       if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/*  803 */         return b0 << 24 | b1 << 16 | b2 << 8 | b3 << 0;
/*      */       }
/*  805 */       return b3 << 24 | b2 << 16 | b1 << 8 | b0 << 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void putInt(int i) {
/*  814 */       prepareForAccess(4);
/*  815 */       if (this.componentCursor.getRemaining() >= 4) {
/*  816 */         this.componentCursor.putInt(i);
/*  817 */         this.index += 4;
/*      */       } else {
/*      */         byte b0, b1, b2, b3;
/*      */ 
/*      */ 
/*      */         
/*  823 */         if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/*  824 */           b0 = (byte)(i >> 24 & 0xFF);
/*  825 */           b1 = (byte)(i >> 16 & 0xFF);
/*  826 */           b2 = (byte)(i >> 8 & 0xFF);
/*  827 */           b3 = (byte)(i >> 0 & 0xFF);
/*      */         } else {
/*  829 */           b0 = (byte)(i >> 0 & 0xFF);
/*  830 */           b1 = (byte)(i >> 8 & 0xFF);
/*  831 */           b2 = (byte)(i >> 16 & 0xFF);
/*  832 */           b3 = (byte)(i >> 24 & 0xFF);
/*      */         } 
/*  834 */         put(b0);
/*  835 */         put(b1);
/*  836 */         put(b2);
/*  837 */         put(b3);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public long getLong() {
/*  845 */       prepareForAccess(8);
/*  846 */       if (this.componentCursor.getRemaining() >= 4) {
/*  847 */         long l = this.componentCursor.getLong();
/*  848 */         this.index += 8;
/*  849 */         return l;
/*      */       } 
/*  851 */       byte b0 = get();
/*  852 */       byte b1 = get();
/*  853 */       byte b2 = get();
/*  854 */       byte b3 = get();
/*  855 */       byte b4 = get();
/*  856 */       byte b5 = get();
/*  857 */       byte b6 = get();
/*  858 */       byte b7 = get();
/*  859 */       if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/*  860 */         return (b0 & 0xFFL) << 56L | (b1 & 0xFFL) << 48L | (b2 & 0xFFL) << 40L | (b3 & 0xFFL) << 32L | (b4 & 0xFFL) << 24L | (b5 & 0xFFL) << 16L | (b6 & 0xFFL) << 8L | (b7 & 0xFFL) << 0L;
/*      */       }
/*      */       
/*  863 */       return (b7 & 0xFFL) << 56L | (b6 & 0xFFL) << 48L | (b5 & 0xFFL) << 40L | (b4 & 0xFFL) << 32L | (b3 & 0xFFL) << 24L | (b2 & 0xFFL) << 16L | (b1 & 0xFFL) << 8L | (b0 & 0xFFL) << 0L;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void putLong(long l) {
/*  874 */       prepareForAccess(8);
/*  875 */       if (this.componentCursor.getRemaining() >= 4) {
/*  876 */         this.componentCursor.putLong(l);
/*  877 */         this.index += 8;
/*      */       } else {
/*      */         byte b0, b1, b2, b3, b4, b5, b6, b7;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  887 */         if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/*  888 */           b0 = (byte)(int)(l >> 56L & 0xFFL);
/*  889 */           b1 = (byte)(int)(l >> 48L & 0xFFL);
/*  890 */           b2 = (byte)(int)(l >> 40L & 0xFFL);
/*  891 */           b3 = (byte)(int)(l >> 32L & 0xFFL);
/*  892 */           b4 = (byte)(int)(l >> 24L & 0xFFL);
/*  893 */           b5 = (byte)(int)(l >> 16L & 0xFFL);
/*  894 */           b6 = (byte)(int)(l >> 8L & 0xFFL);
/*  895 */           b7 = (byte)(int)(l >> 0L & 0xFFL);
/*      */         } else {
/*  897 */           b0 = (byte)(int)(l >> 0L & 0xFFL);
/*  898 */           b1 = (byte)(int)(l >> 8L & 0xFFL);
/*  899 */           b2 = (byte)(int)(l >> 16L & 0xFFL);
/*  900 */           b3 = (byte)(int)(l >> 24L & 0xFFL);
/*  901 */           b4 = (byte)(int)(l >> 32L & 0xFFL);
/*  902 */           b5 = (byte)(int)(l >> 40L & 0xFFL);
/*  903 */           b6 = (byte)(int)(l >> 48L & 0xFFL);
/*  904 */           b7 = (byte)(int)(l >> 56L & 0xFFL);
/*      */         } 
/*  906 */         put(b0);
/*  907 */         put(b1);
/*  908 */         put(b2);
/*  909 */         put(b3);
/*  910 */         put(b4);
/*  911 */         put(b5);
/*  912 */         put(b6);
/*  913 */         put(b7);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float getFloat() {
/*  921 */       prepareForAccess(4);
/*  922 */       if (this.componentCursor.getRemaining() >= 4) {
/*  923 */         float f = this.componentCursor.getFloat();
/*  924 */         this.index += 4;
/*  925 */         return f;
/*      */       } 
/*  927 */       int i = getInt();
/*  928 */       return Float.intBitsToFloat(i);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void putFloat(float f) {
/*  936 */       prepareForAccess(4);
/*  937 */       if (this.componentCursor.getRemaining() >= 4) {
/*  938 */         this.componentCursor.putFloat(f);
/*  939 */         this.index += 4;
/*      */       } else {
/*  941 */         int i = Float.floatToIntBits(f);
/*  942 */         putInt(i);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public double getDouble() {
/*  950 */       prepareForAccess(8);
/*  951 */       if (this.componentCursor.getRemaining() >= 4) {
/*  952 */         double d = this.componentCursor.getDouble();
/*  953 */         this.index += 8;
/*  954 */         return d;
/*      */       } 
/*  956 */       long l = getLong();
/*  957 */       return Double.longBitsToDouble(l);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void putDouble(double d) {
/*  965 */       prepareForAccess(8);
/*  966 */       if (this.componentCursor.getRemaining() >= 4) {
/*  967 */         this.componentCursor.putDouble(d);
/*  968 */         this.index += 8;
/*      */       } else {
/*  970 */         long l = Double.doubleToLongBits(d);
/*  971 */         putLong(l);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char getChar() {
/*  979 */       prepareForAccess(2);
/*  980 */       if (this.componentCursor.getRemaining() >= 4) {
/*  981 */         char c = this.componentCursor.getChar();
/*  982 */         this.index += 2;
/*  983 */         return c;
/*      */       } 
/*  985 */       byte b0 = get();
/*  986 */       byte b1 = get();
/*  987 */       if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/*  988 */         return (char)(b0 << 8 | b1 << 0);
/*      */       }
/*  990 */       return (char)(b1 << 8 | b0 << 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void putChar(char c) {
/*  999 */       prepareForAccess(2);
/* 1000 */       if (this.componentCursor.getRemaining() >= 4) {
/* 1001 */         this.componentCursor.putChar(c);
/* 1002 */         this.index += 2;
/*      */       } else {
/*      */         byte b0, b1;
/*      */         
/* 1006 */         if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
/* 1007 */           b0 = (byte)(c >> 8 & 0xFF);
/* 1008 */           b1 = (byte)(c >> 0 & 0xFF);
/*      */         } else {
/* 1010 */           b0 = (byte)(c >> 0 & 0xFF);
/* 1011 */           b1 = (byte)(c >> 8 & 0xFF);
/*      */         } 
/* 1013 */         put(b0);
/* 1014 */         put(b1);
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/CompositeByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */