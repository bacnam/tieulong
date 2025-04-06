/*    */ package com.mchange.v2.beans.swing;
/*    */ 
/*    */ import java.beans.IntrospectionException;
/*    */ import javax.swing.AbstractButton;
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
/*    */ public final class BoundButtonUtils
/*    */ {
/*    */   public static void bindToSetProperty(AbstractButton[] paramArrayOfAbstractButton, Object[] paramArrayOfObject, Object paramObject, String paramString) throws IntrospectionException {
/* 44 */     SetPropertyElementBoundButtonModel.bind(paramArrayOfAbstractButton, paramArrayOfObject, paramObject, paramString);
/*    */   }
/*    */   
/*    */   public static void bindAsRadioButtonsToProperty(AbstractButton[] paramArrayOfAbstractButton, Object[] paramArrayOfObject, Object paramObject, String paramString) throws IntrospectionException {
/* 48 */     PropertyBoundButtonGroup propertyBoundButtonGroup = new PropertyBoundButtonGroup(paramObject, paramString);
/* 49 */     for (byte b = 0; b < paramArrayOfAbstractButton.length; b++)
/* 50 */       propertyBoundButtonGroup.add(paramArrayOfAbstractButton[b], paramArrayOfObject[b]); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/BoundButtonUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */