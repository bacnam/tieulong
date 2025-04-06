/*    */ package jsc.swt.plot2d;
/*    */ 
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.ButtonGroup;
/*    */ import javax.swing.JMenu;
/*    */ import javax.swing.JRadioButtonMenuItem;
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
/*    */ public class MarkerTypeMenu
/*    */   extends JMenu
/*    */ {
/*    */   public MarkerTypeMenu(String paramString, int paramInt, ActionListener paramActionListener) {
/* 38 */     super(paramString);
/* 39 */     ButtonGroup buttonGroup = new ButtonGroup();
/* 40 */     int i = StandardMarker.getTypeCount();
/* 41 */     JRadioButtonMenuItem[] arrayOfJRadioButtonMenuItem = new JRadioButtonMenuItem[i];
/* 42 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 44 */       arrayOfJRadioButtonMenuItem[b] = new JRadioButtonMenuItem(StandardMarker.getTypeName(b));
/* 45 */       arrayOfJRadioButtonMenuItem[b].addActionListener(paramActionListener);
/* 46 */       buttonGroup.add(arrayOfJRadioButtonMenuItem[b]);
/* 47 */       add(arrayOfJRadioButtonMenuItem[b]);
/*    */     } 
/* 49 */     if (paramInt >= 0 && paramInt < i) arrayOfJRadioButtonMenuItem[paramInt].setSelected(true); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/MarkerTypeMenu.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */