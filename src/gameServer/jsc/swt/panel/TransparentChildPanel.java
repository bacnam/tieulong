/*    */ package jsc.swt.panel;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.LayoutManager;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JPanel;
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
/*    */ public class TransparentChildPanel
/*    */   extends JPanel
/*    */ {
/*    */   public TransparentChildPanel(LayoutManager paramLayoutManager) {
/* 25 */     super(paramLayoutManager);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void transparentComponents() {
/* 35 */     Component[] arrayOfComponent = getComponents();
/* 36 */     for (byte b = 0; b < arrayOfComponent.length; b++) {
/*    */       
/* 38 */       JComponent jComponent = (JComponent)arrayOfComponent[b];
/* 39 */       jComponent.setOpaque(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/panel/TransparentChildPanel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */