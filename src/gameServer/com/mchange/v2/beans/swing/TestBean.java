/*    */ package com.mchange.v2.beans.swing;
/*    */ 
/*    */ import java.beans.PropertyChangeListener;
/*    */ import java.beans.PropertyChangeSupport;
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
/*    */ public class TestBean
/*    */ {
/*    */   String s;
/*    */   int i;
/*    */   float f;
/* 46 */   PropertyChangeSupport pcs = new PropertyChangeSupport(this);
/*    */   
/*    */   public String getTheString() {
/* 49 */     return this.s;
/*    */   }
/*    */   public int getTheInt() {
/* 52 */     return this.i;
/*    */   }
/*    */   public float getTheFloat() {
/* 55 */     return this.f;
/*    */   }
/*    */   
/*    */   public void setTheString(String paramString) {
/* 59 */     if (!eqOrBothNull(paramString, this.s)) {
/*    */       
/* 61 */       String str = this.s;
/* 62 */       this.s = paramString;
/* 63 */       this.pcs.firePropertyChange("theString", str, this.s);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTheInt(int paramInt) {
/* 69 */     if (paramInt != this.i) {
/*    */       
/* 71 */       int i = this.i;
/* 72 */       this.i = paramInt;
/* 73 */       this.pcs.firePropertyChange("theInt", i, this.i);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTheFloat(float paramFloat) {
/* 79 */     if (paramFloat != this.f) {
/*    */       
/* 81 */       float f = this.f;
/* 82 */       this.f = paramFloat;
/* 83 */       this.pcs.firePropertyChange("theFloat", new Float(f), new Float(this.f));
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 88 */     this.pcs.addPropertyChangeListener(paramPropertyChangeListener);
/*    */   }
/*    */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 91 */     this.pcs.removePropertyChangeListener(paramPropertyChangeListener);
/*    */   }
/*    */   
/*    */   private boolean eqOrBothNull(Object paramObject1, Object paramObject2) {
/* 95 */     return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/TestBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */