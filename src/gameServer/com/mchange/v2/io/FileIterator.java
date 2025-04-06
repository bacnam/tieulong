/*    */ package com.mchange.v2.io;
/*    */ 
/*    */ import com.mchange.v1.util.UIterator;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public interface FileIterator
/*    */   extends UIterator
/*    */ {
/* 52 */   public static final FileIterator EMPTY_FILE_ITERATOR = new FileIterator() {
/*    */       public File nextFile() {
/* 54 */         throw new NoSuchElementException();
/* 55 */       } public boolean hasNext() { return false; }
/* 56 */       public Object next() { throw new NoSuchElementException(); } public void remove() {
/* 57 */         throw new IllegalStateException();
/*    */       }
/*    */       
/*    */       public void close() {}
/*    */     };
/*    */   
/*    */   File nextFile() throws IOException;
/*    */   
/*    */   boolean hasNext() throws IOException;
/*    */   
/*    */   Object next() throws IOException;
/*    */   
/*    */   void remove() throws IOException;
/*    */   
/*    */   void close() throws IOException;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/io/FileIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */