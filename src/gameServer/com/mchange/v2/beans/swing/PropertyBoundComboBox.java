/*     */ package com.mchange.v2.beans.swing;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
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
/*     */ public class PropertyBoundComboBox
/*     */   extends JComboBox
/*     */ {
/*     */   PropertyComponentBindingUtility pcbu;
/*     */   MyHbi myHbi;
/*  52 */   Object itemsSrc = null;
/*  53 */   Object nullObject = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyBoundComboBox(Object paramObject1, String paramString, Object paramObject2, Object paramObject3) throws IntrospectionException {
/*  64 */     this.myHbi = new MyHbi();
/*  65 */     this.pcbu = new PropertyComponentBindingUtility(this.myHbi, paramObject1, paramString, false);
/*     */     
/*  67 */     this.nullObject = paramObject3;
/*  68 */     setItemsSrc(paramObject2);
/*     */   }
/*     */   
/*     */   public Object getItemsSrc() {
/*  72 */     return this.itemsSrc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemsSrc(Object paramObject) {
/*  78 */     this.myHbi.suspendNotifications();
/*     */     
/*  80 */     removeAllItems();
/*  81 */     if (paramObject instanceof Object[]) {
/*     */       
/*  83 */       Object[] arrayOfObject = (Object[])paramObject; byte b; int i;
/*  84 */       for (b = 0, i = arrayOfObject.length; b < i; b++) {
/*  85 */         addItem((E)arrayOfObject[b]);
/*     */       }
/*  87 */     } else if (paramObject instanceof Collection) {
/*     */       
/*  89 */       Collection collection = (Collection)paramObject;
/*  90 */       for (Iterator<E> iterator = collection.iterator(); iterator.hasNext();) {
/*  91 */         addItem(iterator.next());
/*     */       }
/*  93 */     } else if (paramObject instanceof ComboBoxModel) {
/*  94 */       setModel((ComboBoxModel<E>)paramObject);
/*     */     } else {
/*  96 */       throw new IllegalArgumentException("itemsSrc must be an Object[], a Collection, or a ComboBoxModel");
/*     */     } 
/*  98 */     this.itemsSrc = paramObject;
/*     */     
/* 100 */     this.pcbu.resync();
/*     */     
/* 102 */     this.myHbi.resumeNotifications();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNullObject(Object paramObject) {
/* 107 */     this.nullObject = null;
/* 108 */     this.pcbu.resync();
/*     */   }
/*     */   
/*     */   public Object getNullObject() {
/* 112 */     return this.nullObject;
/*     */   }
/*     */   
/*     */   class MyHbi implements HostBindingInterface {
/*     */     boolean suspend_notice = false;
/*     */     
/*     */     public void suspendNotifications() {
/* 119 */       this.suspend_notice = true;
/*     */     }
/*     */     public void resumeNotifications() {
/* 122 */       this.suspend_notice = false;
/*     */     }
/*     */     
/*     */     public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
/* 126 */       if (param1Object == null) {
/* 127 */         PropertyBoundComboBox.this.setSelectedItem(PropertyBoundComboBox.this.nullObject);
/*     */       } else {
/* 129 */         PropertyBoundComboBox.this.setSelectedItem(param1Object);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addUserModificationListeners() {
/* 134 */       ItemListener itemListener = new ItemListener()
/*     */         {
/*     */           public void itemStateChanged(ItemEvent param2ItemEvent)
/*     */           {
/* 138 */             if (!PropertyBoundComboBox.MyHbi.this.suspend_notice)
/* 139 */               PropertyBoundComboBox.this.pcbu.userModification(); 
/*     */           }
/*     */         };
/* 142 */       PropertyBoundComboBox.this.addItemListener(itemListener);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
/* 147 */       Object object = PropertyBoundComboBox.this.getSelectedItem();
/* 148 */       if (PropertyBoundComboBox.this.nullObject != null && PropertyBoundComboBox.this.nullObject.equals(object))
/* 149 */         object = null; 
/* 150 */       return object;
/*     */     }
/*     */     
/*     */     public void alertErroneousInput() {
/* 154 */       PropertyBoundComboBox.this.getToolkit().beep();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 161 */       TestBean testBean = new TestBean();
/* 162 */       PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
/*     */         {
/*     */           public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
/* 165 */             BeansUtils.debugShowPropertyChange(param1PropertyChangeEvent); }
/*     */         };
/* 167 */       testBean.addPropertyChangeListener(propertyChangeListener);
/*     */       
/* 169 */       PropertyBoundComboBox propertyBoundComboBox = new PropertyBoundComboBox(testBean, "theString", new String[] { "SELECT", "Frog", "Fish", "Puppy" }, "SELECT");
/* 170 */       PropertyBoundTextField propertyBoundTextField1 = new PropertyBoundTextField(testBean, "theInt", 5);
/* 171 */       PropertyBoundTextField propertyBoundTextField2 = new PropertyBoundTextField(testBean, "theFloat", 5);
/* 172 */       JFrame jFrame = new JFrame();
/* 173 */       BoxLayout boxLayout = new BoxLayout(jFrame.getContentPane(), 1);
/* 174 */       jFrame.getContentPane().setLayout(boxLayout);
/* 175 */       jFrame.getContentPane().add(propertyBoundComboBox);
/* 176 */       jFrame.getContentPane().add(propertyBoundTextField1);
/* 177 */       jFrame.getContentPane().add(propertyBoundTextField2);
/* 178 */       jFrame.pack();
/* 179 */       jFrame.show();
/*     */     }
/* 181 */     catch (Exception exception) {
/* 182 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/PropertyBoundComboBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */