/*    */ package javolution.util.internal.table;
/*    */ 
/*    */ import java.util.ListIterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import javolution.util.service.TableService;
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
/*    */ public final class TableIteratorImpl<E>
/*    */   implements ListIterator<E>
/*    */ {
/* 21 */   private int currentIndex = -1;
/*    */   private int end;
/*    */   private int nextIndex;
/*    */   private final TableService<E> table;
/*    */   
/*    */   public TableIteratorImpl(TableService<E> table, int index) {
/* 27 */     this.table = table;
/* 28 */     this.nextIndex = index;
/* 29 */     this.end = table.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(E e) {
/* 34 */     this.table.add(this.nextIndex++, e);
/* 35 */     this.end++;
/* 36 */     this.currentIndex = -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 41 */     return (this.nextIndex < this.end);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPrevious() {
/* 46 */     return (this.nextIndex > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public E next() {
/* 51 */     if (this.nextIndex >= this.end) throw new NoSuchElementException(); 
/* 52 */     this.currentIndex = this.nextIndex++;
/* 53 */     return (E)this.table.get(this.currentIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public int nextIndex() {
/* 58 */     return this.nextIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public E previous() {
/* 63 */     if (this.nextIndex <= 0) throw new NoSuchElementException(); 
/* 64 */     this.currentIndex = --this.nextIndex;
/* 65 */     return (E)this.table.get(this.currentIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public int previousIndex() {
/* 70 */     return this.nextIndex - 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 75 */     if (this.currentIndex < 0) throw new IllegalStateException(); 
/* 76 */     this.table.remove(this.currentIndex);
/* 77 */     this.end--;
/* 78 */     if (this.currentIndex < this.nextIndex) {
/* 79 */       this.nextIndex--;
/*    */     }
/* 81 */     this.currentIndex = -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(E e) {
/* 86 */     if (this.currentIndex >= 0) {
/* 87 */       this.table.set(this.currentIndex, e);
/*    */     } else {
/* 89 */       throw new IllegalStateException();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/TableIteratorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */