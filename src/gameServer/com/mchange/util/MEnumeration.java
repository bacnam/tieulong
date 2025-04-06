/*    */ package com.mchange.util;
/*    */ 
/*    */ import com.mchange.io.IOEnumeration;
/*    */ import com.mchange.util.impl.EmptyMEnumeration;
/*    */ import java.util.Enumeration;
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
/*    */ public interface MEnumeration
/*    */   extends IOEnumeration, Enumeration
/*    */ {
/* 43 */   public static final MEnumeration EMPTY = EmptyMEnumeration.SINGLETON;
/*    */   
/*    */   Object nextElement();
/*    */   
/*    */   boolean hasMoreElements();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/MEnumeration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */