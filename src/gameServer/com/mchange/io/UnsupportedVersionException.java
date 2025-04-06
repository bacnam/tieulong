/*    */ package com.mchange.io;
/*    */ 
/*    */ import java.io.InvalidClassException;
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
/*    */ public class UnsupportedVersionException
/*    */   extends InvalidClassException
/*    */ {
/*    */   public UnsupportedVersionException(String paramString) {
/* 47 */     super(paramString);
/*    */   }
/*    */   public UnsupportedVersionException(Object paramObject, int paramInt) {
/* 50 */     this(paramObject.getClass().getName() + " -- unsupported version: " + paramInt);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/UnsupportedVersionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */