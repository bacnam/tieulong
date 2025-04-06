/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Vector;
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
/*    */ public class SomethingChangedEventSupport
/*    */ {
/*    */   Object source;
/* 43 */   Vector listeners = new Vector();
/*    */   
/*    */   public SomethingChangedEventSupport(Object paramObject) {
/* 46 */     this.source = paramObject;
/*    */   }
/*    */   
/*    */   public synchronized void addSomethingChangedListener(SomethingChangedListener paramSomethingChangedListener) {
/* 50 */     if (!this.listeners.contains(paramSomethingChangedListener))
/* 51 */       this.listeners.addElement(paramSomethingChangedListener); 
/*    */   }
/*    */   
/*    */   public synchronized void removeSomethingChangedListener(SomethingChangedListener paramSomethingChangedListener) {
/* 55 */     this.listeners.removeElement(paramSomethingChangedListener);
/*    */   }
/*    */   
/*    */   public synchronized void fireSomethingChanged() {
/* 59 */     SomethingChangedEvent somethingChangedEvent = new SomethingChangedEvent(this.source);
/* 60 */     for (Enumeration<SomethingChangedListener> enumeration = this.listeners.elements(); enumeration.hasMoreElements(); ) {
/*    */       
/* 62 */       SomethingChangedListener somethingChangedListener = enumeration.nextElement();
/* 63 */       somethingChangedListener.somethingChanged(somethingChangedEvent);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/SomethingChangedEventSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */