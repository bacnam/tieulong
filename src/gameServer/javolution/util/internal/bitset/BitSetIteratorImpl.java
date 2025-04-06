/*    */ package javolution.util.internal.bitset;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import javolution.util.Index;
/*    */ import javolution.util.service.BitSetService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BitSetIteratorImpl
/*    */   implements Iterator<Index>
/*    */ {
/*    */   private final BitSetService that;
/*    */   private int nextIndex;
/* 26 */   private int currentIndex = -1;
/*    */   
/*    */   public BitSetIteratorImpl(BitSetService that, int index) {
/* 29 */     this.that = that;
/* 30 */     this.nextIndex = that.nextSetBit(index);
/*    */   }
/*    */   
/*    */   public boolean hasNext() {
/* 34 */     return (this.nextIndex >= 0);
/*    */   }
/*    */   
/*    */   public Index next() {
/* 38 */     if (this.nextIndex < 0)
/* 39 */       throw new NoSuchElementException(); 
/* 40 */     this.currentIndex = this.nextIndex;
/* 41 */     this.nextIndex = this.that.nextSetBit(this.nextIndex);
/* 42 */     return Index.valueOf(this.currentIndex);
/*    */   }
/*    */   
/*    */   public void remove() {
/* 46 */     if (this.currentIndex < 0)
/* 47 */       throw new IllegalStateException(); 
/* 48 */     this.that.clear(this.currentIndex);
/* 49 */     this.currentIndex = -1;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/bitset/BitSetIteratorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */