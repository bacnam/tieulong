/*    */ package com.mchange.v3.filecache;
/*    */ 
/*    */ import java.io.FileNotFoundException;
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
/*    */ public class FileNotCachedException
/*    */   extends FileNotFoundException
/*    */ {
/*    */   FileNotCachedException(String paramString) {
/* 43 */     super(paramString);
/*    */   }
/*    */   
/*    */   FileNotCachedException() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/filecache/FileNotCachedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */