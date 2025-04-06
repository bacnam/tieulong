/*     */ package com.mchange.v2.beans.swing;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PropertyBoundButtonGroup
/*     */   extends ButtonGroup
/*     */ {
/*     */   PropertyComponentBindingUtility pcbu;
/*     */   HostBindingInterface myHbi;
/*  50 */   WeChangedListener wcl = new WeChangedListener();
/*     */   
/*  52 */   Map buttonsModelsToValues = new HashMap<Object, Object>();
/*  53 */   Map valuesToButtonModels = new HashMap<Object, Object>();
/*  54 */   JButton fakeButton = new JButton();
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyBoundButtonGroup(Object paramObject, String paramString) throws IntrospectionException {
/*  59 */     this.myHbi = new MyHbi();
/*  60 */     this.pcbu = new PropertyComponentBindingUtility(this.myHbi, paramObject, paramString, false);
/*  61 */     add(this.fakeButton, (Object)null);
/*  62 */     this.pcbu.resync();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(AbstractButton paramAbstractButton, Object paramObject) {
/*  67 */     super.add(paramAbstractButton);
/*  68 */     this.buttonsModelsToValues.put(paramAbstractButton.getModel(), paramObject);
/*  69 */     this.valuesToButtonModels.put(paramObject, paramAbstractButton.getModel());
/*     */     
/*  71 */     paramAbstractButton.addActionListener(this.wcl);
/*  72 */     this.pcbu.resync();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(AbstractButton paramAbstractButton) {
/*  77 */     System.err.println(this + "Warning: The button '" + paramAbstractButton + "' has been implicitly associated with a null value!");
/*  78 */     System.err.println("To avoid this warning, please use public void add(AbstractButton button, Object associatedValue)");
/*  79 */     System.err.println("instead of the single-argument add method.");
/*  80 */     super.add(paramAbstractButton);
/*     */     
/*  82 */     paramAbstractButton.addActionListener(this.wcl);
/*  83 */     this.pcbu.resync();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(AbstractButton paramAbstractButton) {
/*  88 */     paramAbstractButton.removeActionListener(this.wcl);
/*  89 */     super.remove(paramAbstractButton);
/*     */   }
/*     */   
/*     */   class MyHbi
/*     */     implements HostBindingInterface
/*     */   {
/*     */     public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
/*  96 */       ButtonModel buttonModel = (ButtonModel)PropertyBoundButtonGroup.this.valuesToButtonModels.get(param1Object);
/*  97 */       if (buttonModel != null) {
/*  98 */         PropertyBoundButtonGroup.this.setSelected(buttonModel, true);
/*     */       } else {
/* 100 */         PropertyBoundButtonGroup.this.setSelected(PropertyBoundButtonGroup.this.fakeButton.getModel(), true);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void addUserModificationListeners() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
/* 111 */       ButtonModel buttonModel = PropertyBoundButtonGroup.this.getSelection();
/* 112 */       return PropertyBoundButtonGroup.this.buttonsModelsToValues.get(buttonModel);
/*     */     }
/*     */     
/*     */     public void alertErroneousInput() {
/* 116 */       Toolkit.getDefaultToolkit().beep();
/*     */     }
/*     */   }
/*     */   
/*     */   class WeChangedListener implements ActionListener {
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 122 */       PropertyBoundButtonGroup.this.pcbu.userModification();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/PropertyBoundButtonGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */