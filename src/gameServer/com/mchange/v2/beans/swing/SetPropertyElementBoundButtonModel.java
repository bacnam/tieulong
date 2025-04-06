/*     */ package com.mchange.v2.beans.swing;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.event.ChangeListener;
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
/*     */ class SetPropertyElementBoundButtonModel
/*     */   implements ButtonModel
/*     */ {
/*     */   Object putativeElement;
/*     */   ButtonModel inner;
/*     */   PropertyComponentBindingUtility pcbu;
/*     */   
/*     */   public static void bind(AbstractButton[] paramArrayOfAbstractButton, Object[] paramArrayOfObject, Object paramObject, String paramString) throws IntrospectionException {
/*     */     byte b;
/*     */     int i;
/*  54 */     for (b = 0, i = paramArrayOfAbstractButton.length; b < i; b++) {
/*     */       
/*  56 */       AbstractButton abstractButton = paramArrayOfAbstractButton[b];
/*  57 */       abstractButton.setModel(new SetPropertyElementBoundButtonModel(abstractButton.getModel(), paramObject, paramString, paramArrayOfObject[b]));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SetPropertyElementBoundButtonModel(ButtonModel paramButtonModel, Object paramObject1, String paramString, Object paramObject2) throws IntrospectionException {
/*  64 */     this.inner = paramButtonModel;
/*  65 */     this.putativeElement = paramObject2;
/*  66 */     this.pcbu = new PropertyComponentBindingUtility(new MyHbi(), paramObject1, paramString, false);
/*  67 */     this.pcbu.resync();
/*     */   }
/*     */   
/*     */   public boolean isArmed() {
/*  71 */     return this.inner.isArmed();
/*     */   }
/*     */   public boolean isSelected() {
/*  74 */     return this.inner.isSelected();
/*     */   }
/*     */   public boolean isEnabled() {
/*  77 */     return this.inner.isEnabled();
/*     */   }
/*     */   public boolean isPressed() {
/*  80 */     return this.inner.isPressed();
/*     */   }
/*     */   public boolean isRollover() {
/*  83 */     return this.inner.isRollover();
/*     */   }
/*     */   public void setArmed(boolean paramBoolean) {
/*  86 */     this.inner.setArmed(paramBoolean);
/*     */   }
/*     */   public void setSelected(boolean paramBoolean) {
/*  89 */     this.inner.setSelected(paramBoolean);
/*     */   }
/*     */   public void setEnabled(boolean paramBoolean) {
/*  92 */     this.inner.setEnabled(paramBoolean);
/*     */   }
/*     */   public void setPressed(boolean paramBoolean) {
/*  95 */     this.inner.setPressed(paramBoolean);
/*     */   }
/*     */   public void setRollover(boolean paramBoolean) {
/*  98 */     this.inner.setRollover(paramBoolean);
/*     */   }
/*     */   public void setMnemonic(int paramInt) {
/* 101 */     this.inner.setMnemonic(paramInt);
/*     */   }
/*     */   public int getMnemonic() {
/* 104 */     return this.inner.getMnemonic();
/*     */   }
/*     */   public void setActionCommand(String paramString) {
/* 107 */     this.inner.setActionCommand(paramString);
/*     */   }
/*     */   public String getActionCommand() {
/* 110 */     return this.inner.getActionCommand();
/*     */   }
/*     */   public void setGroup(ButtonGroup paramButtonGroup) {
/* 113 */     this.inner.setGroup(paramButtonGroup);
/*     */   }
/*     */   public Object[] getSelectedObjects() {
/* 116 */     return this.inner.getSelectedObjects();
/*     */   }
/*     */   public void addActionListener(ActionListener paramActionListener) {
/* 119 */     this.inner.addActionListener(paramActionListener);
/*     */   }
/*     */   public void removeActionListener(ActionListener paramActionListener) {
/* 122 */     this.inner.removeActionListener(paramActionListener);
/*     */   }
/*     */   public void addItemListener(ItemListener paramItemListener) {
/* 125 */     this.inner.addItemListener(paramItemListener);
/*     */   }
/*     */   public void removeItemListener(ItemListener paramItemListener) {
/* 128 */     this.inner.removeItemListener(paramItemListener);
/*     */   }
/*     */   public void addChangeListener(ChangeListener paramChangeListener) {
/* 131 */     this.inner.addChangeListener(paramChangeListener);
/*     */   }
/*     */   public void removeChangeListener(ChangeListener paramChangeListener) {
/* 134 */     this.inner.removeChangeListener(paramChangeListener);
/*     */   }
/*     */   
/*     */   class MyHbi
/*     */     implements HostBindingInterface {
/*     */     public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
/* 140 */       if (param1Object == null) {
/* 141 */         SetPropertyElementBoundButtonModel.this.setSelected(false);
/*     */       } else {
/* 143 */         SetPropertyElementBoundButtonModel.this.setSelected(((Set)param1Object).contains(SetPropertyElementBoundButtonModel.this.putativeElement));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addUserModificationListeners() {
/* 148 */       ActionListener actionListener = new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent param2ActionEvent) {
/* 151 */             SetPropertyElementBoundButtonModel.this.pcbu.userModification(); }
/*     */         };
/* 153 */       SetPropertyElementBoundButtonModel.this.addActionListener(actionListener);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
/*     */       HashSet<Object> hashSet;
/* 159 */       if (param1Object == null) {
/*     */         
/* 161 */         if (!SetPropertyElementBoundButtonModel.this.isSelected()) {
/* 162 */           return null;
/*     */         }
/* 164 */         hashSet = new HashSet();
/*     */       } else {
/*     */         
/* 167 */         hashSet = new HashSet((Set)param1Object);
/*     */       } 
/* 169 */       if (SetPropertyElementBoundButtonModel.this.isSelected()) {
/* 170 */         hashSet.add(SetPropertyElementBoundButtonModel.this.putativeElement);
/*     */       } else {
/* 172 */         hashSet.remove(SetPropertyElementBoundButtonModel.this.putativeElement);
/*     */       } 
/* 174 */       return hashSet;
/*     */     }
/*     */     
/*     */     public void alertErroneousInput() {
/* 178 */       Toolkit.getDefaultToolkit().beep();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/SetPropertyElementBoundButtonModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */