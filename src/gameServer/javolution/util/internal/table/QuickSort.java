/*    */ package javolution.util.internal.table;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ 
/*    */ public class QuickSort<E>
/*    */ {
/*    */   private final Comparator<? super E> comparator;
/*    */   private final TableService<E> table;
/*    */   
/*    */   public QuickSort(TableService<E> table, Comparator<? super E> comparator) {
/* 25 */     this.table = table;
/* 26 */     this.comparator = comparator;
/*    */   }
/*    */   
/*    */   public void sort() {
/* 30 */     int size = this.table.size();
/* 31 */     if (size > 0) quicksort(0, this.table.size() - 1); 
/*    */   }
/*    */   
/*    */   public void sort(int first, int last) {
/* 35 */     if (first < last) {
/* 36 */       int pivIndex = partition(first, last);
/* 37 */       sort(first, pivIndex - 1);
/* 38 */       sort(pivIndex + 1, last);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void quicksort(int first, int last) {
/* 45 */     int pivIndex = 0;
/* 46 */     if (first < last) {
/* 47 */       pivIndex = partition(first, last);
/* 48 */       quicksort(first, pivIndex - 1);
/* 49 */       quicksort(pivIndex + 1, last);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private int partition(int f, int l) {
/* 55 */     E piv = (E)this.table.get(f);
/* 56 */     int up = f;
/* 57 */     int down = l;
/*    */     while (true) {
/* 59 */       if (this.comparator.compare((E)this.table.get(up), piv) <= 0 && up < l) {
/* 60 */         up++; continue;
/*    */       } 
/* 62 */       while (this.comparator.compare((E)this.table.get(down), piv) > 0 && down > f) {
/* 63 */         down--;
/*    */       }
/* 65 */       if (up < down) {
/* 66 */         E temp = (E)this.table.get(up);
/* 67 */         this.table.set(up, this.table.get(down));
/* 68 */         this.table.set(down, temp);
/*    */       } 
/* 70 */       if (down <= up) {
/* 71 */         this.table.set(f, this.table.get(down));
/* 72 */         this.table.set(down, piv);
/* 73 */         return down;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/QuickSort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */