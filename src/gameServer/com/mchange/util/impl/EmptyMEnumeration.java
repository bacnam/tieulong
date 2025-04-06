/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.MEnumeration;
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
/*    */ public class EmptyMEnumeration
/*    */   implements MEnumeration
/*    */ {
/* 43 */   public static MEnumeration SINGLETON = new EmptyMEnumeration();
/*    */ 
/*    */   
/*    */   public Object nextElement() {
/* 47 */     throw new NoSuchElementException(); } public boolean hasMoreElements() {
/* 48 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/EmptyMEnumeration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */