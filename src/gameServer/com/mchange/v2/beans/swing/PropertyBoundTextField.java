/*     */ package com.mchange.v2.beans.swing;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyEditor;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JTextField;
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
/*     */ 
/*     */ public class PropertyBoundTextField
/*     */   extends JTextField
/*     */ {
/*     */   PropertyComponentBindingUtility pcbu;
/*     */   HostBindingInterface myHbi;
/*     */   
/*     */   public PropertyBoundTextField(Object paramObject, String paramString, int paramInt) throws IntrospectionException {
/*  54 */     super(paramInt);
/*  55 */     this.myHbi = new MyHbi();
/*  56 */     this.pcbu = new PropertyComponentBindingUtility(this.myHbi, paramObject, paramString, true);
/*  57 */     this.pcbu.resync();
/*     */   }
/*     */   
/*     */   class MyHbi
/*     */     implements HostBindingInterface
/*     */   {
/*     */     public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
/*  64 */       if (param1Object == null) {
/*  65 */         PropertyBoundTextField.this.setText("");
/*     */       } else {
/*     */         
/*  68 */         param1PropertyEditor.setValue(param1Object);
/*  69 */         String str = param1PropertyEditor.getAsText();
/*  70 */         PropertyBoundTextField.this.setText(str);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void addUserModificationListeners() {
/*  76 */       PropertyBoundTextField.WeChangedListener weChangedListener = new PropertyBoundTextField.WeChangedListener();
/*  77 */       PropertyBoundTextField.this.addActionListener(weChangedListener);
/*  78 */       PropertyBoundTextField.this.addFocusListener(weChangedListener);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
/*  83 */       String str = PropertyBoundTextField.this.getText().trim();
/*  84 */       if ("".equals(str)) {
/*  85 */         return null;
/*     */       }
/*     */       
/*  88 */       param1PropertyEditor.setAsText(str);
/*  89 */       return param1PropertyEditor.getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public void alertErroneousInput() {
/*  94 */       PropertyBoundTextField.this.getToolkit().beep();
/*     */     }
/*     */   }
/*     */   
/*     */   class WeChangedListener
/*     */     implements ActionListener, FocusListener {
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 101 */       PropertyBoundTextField.this.pcbu.userModification();
/*     */     }
/*     */ 
/*     */     
/*     */     public void focusLost(FocusEvent param1FocusEvent) {
/* 106 */       PropertyBoundTextField.this.pcbu.userModification();
/*     */     }
/*     */     
/*     */     public void focusGained(FocusEvent param1FocusEvent) {}
/*     */   }
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 114 */       TestBean testBean = new TestBean();
/* 115 */       PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
/*     */         {
/*     */           public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
/* 118 */             BeansUtils.debugShowPropertyChange(param1PropertyChangeEvent); }
/*     */         };
/* 120 */       testBean.addPropertyChangeListener(propertyChangeListener);
/*     */       
/* 122 */       PropertyBoundTextField propertyBoundTextField1 = new PropertyBoundTextField(testBean, "theString", 20);
/* 123 */       PropertyBoundTextField propertyBoundTextField2 = new PropertyBoundTextField(testBean, "theInt", 5);
/* 124 */       PropertyBoundTextField propertyBoundTextField3 = new PropertyBoundTextField(testBean, "theFloat", 5);
/* 125 */       JFrame jFrame = new JFrame();
/* 126 */       BoxLayout boxLayout = new BoxLayout(jFrame.getContentPane(), 1);
/* 127 */       jFrame.getContentPane().setLayout(boxLayout);
/* 128 */       jFrame.getContentPane().add(propertyBoundTextField1);
/* 129 */       jFrame.getContentPane().add(propertyBoundTextField2);
/* 130 */       jFrame.getContentPane().add(propertyBoundTextField3);
/* 131 */       jFrame.pack();
/* 132 */       jFrame.show();
/*     */     }
/* 134 */     catch (Exception exception) {
/* 135 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/PropertyBoundTextField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */