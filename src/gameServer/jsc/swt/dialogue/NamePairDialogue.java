/*     */ package jsc.swt.dialogue;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.util.Vector;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import jsc.Utilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NamePairDialogue
/*     */   extends Dialogue
/*     */ {
/*  25 */   private Color focusColour = Color.red;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JList nameList1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JList nameList2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NamePairDialogue(Component paramComponent, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString1, int paramInt1, String paramString4, String[] paramArrayOfString2, int paramInt2) {
/*  49 */     this(paramComponent, paramString1, paramString2, paramString3, Utilities.toVector(paramArrayOfString1), paramInt1, paramString4, Utilities.toVector(paramArrayOfString2), paramInt2);
/*     */   }
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
/*     */   public NamePairDialogue(Component paramComponent, String paramString1, String paramString2, String paramString3, Vector paramVector1, int paramInt1, String paramString4, Vector paramVector2, int paramInt2) {
/*  74 */     super(paramComponent, paramString1, paramString2, -1, 2);
/*     */ 
/*     */     
/*  77 */     setDefaultButtonEnabled(false);
/*  78 */     ListListener listListener = new ListListener(this);
/*     */ 
/*     */     
/*  81 */     this.nameList1 = new JList(paramVector1);
/*  82 */     this.nameList1.setSelectionMode(paramInt1);
/*  83 */     this.nameList1.addListSelectionListener(listListener);
/*  84 */     JScrollPane jScrollPane1 = new JScrollPane(this.nameList1);
/*  85 */     this.nameList2 = new JList(paramVector2);
/*  86 */     this.nameList2.setSelectionMode(paramInt2);
/*  87 */     this.nameList2.addListSelectionListener(listListener);
/*  88 */     this.nameList1.addFocusListener(new KeyboardFocusListener(this, this.nameList1));
/*  89 */     this.nameList2.addFocusListener(new KeyboardFocusListener(this, this.nameList2));
/*  90 */     JScrollPane jScrollPane2 = new JScrollPane(this.nameList2);
/*     */ 
/*     */     
/*  93 */     Font font = new Font("SansSerif", 0, 12);
/*  94 */     JLabel jLabel1 = new JLabel(paramString3, 0);
/*  95 */     jLabel1.setFont(font);
/*  96 */     JViewport jViewport1 = new JViewport();
/*  97 */     jViewport1.setView(jLabel1);
/*  98 */     jScrollPane1.setColumnHeader(jViewport1);
/*  99 */     JLabel jLabel2 = new JLabel(paramString4, 0);
/* 100 */     jLabel2.setFont(font);
/* 101 */     JViewport jViewport2 = new JViewport();
/* 102 */     jViewport2.setView(jLabel2);
/* 103 */     jScrollPane2.setColumnHeader(jViewport2);
/*     */     
/* 105 */     JPanel jPanel = new JPanel(new GridLayout(1, 2, 1, 1));
/* 106 */     jPanel.add(jScrollPane1);
/* 107 */     jPanel.add(jScrollPane2);
/*     */     
/* 109 */     add(jPanel, "Center");
/* 110 */     setSize(500, 200);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIndices1() {
/* 118 */     return this.nameList1.getSelectedIndices();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIndices2() {
/* 125 */     return this.nameList2.getSelectedIndices();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getNames1() {
/* 137 */     if (this.nameList1.isSelectionEmpty()) return null; 
/* 138 */     Object[] arrayOfObject = this.nameList1.getSelectedValues();
/* 139 */     String[] arrayOfString = new String[arrayOfObject.length];
/* 140 */     for (byte b = 0; b < arrayOfObject.length; ) { arrayOfString[b] = (String)arrayOfObject[b]; b++; }
/* 141 */      return arrayOfString;
/*     */   }
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
/*     */   public String[] getNames2() {
/* 154 */     if (this.nameList2.isSelectionEmpty()) return null; 
/* 155 */     Object[] arrayOfObject = this.nameList2.getSelectedValues();
/* 156 */     String[] arrayOfString = new String[arrayOfObject.length];
/* 157 */     for (byte b = 0; b < arrayOfObject.length; ) { arrayOfString[b] = (String)arrayOfObject[b]; b++; }
/* 158 */      return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocusColour(Color paramColor) {
/* 167 */     this.focusColour = paramColor;
/*     */   }
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
/*     */   public String[][] showNames() {
/* 184 */     if (show() == null) return null; 
/* 185 */     if (this.nameList1.isSelectionEmpty() || this.nameList2.isSelectionEmpty()) return null;
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
/* 197 */     String[] arrayOfString1 = getNames1();
/* 198 */     String[] arrayOfString2 = getNames2();
/* 199 */     String[][] arrayOfString = new String[2][];
/* 200 */     arrayOfString[0] = arrayOfString1;
/* 201 */     arrayOfString[1] = arrayOfString2;
/* 202 */     return arrayOfString;
/*     */   }
/*     */   
/*     */   class KeyboardFocusListener extends FocusAdapter { JList list;
/*     */     private final NamePairDialogue this$0;
/*     */     
/*     */     public KeyboardFocusListener(NamePairDialogue this$0, JList param1JList) {
/* 209 */       this.this$0 = this$0; this.list = param1JList;
/*     */     } public void focusGained(FocusEvent param1FocusEvent) {
/* 211 */       this.list.setBorder(BorderFactory.createLineBorder(this.this$0.focusColour, 1));
/*     */     } public void focusLost(FocusEvent param1FocusEvent) {
/* 213 */       this.list.setBorder(BorderFactory.createLineBorder(Color.black, 1));
/*     */     } }
/*     */   class ListListener implements ListSelectionListener { ListListener(NamePairDialogue this$0) {
/* 216 */       this.this$0 = this$0;
/*     */     }
/*     */     private final NamePairDialogue this$0;
/*     */     public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
/* 220 */       if (this.this$0.nameList1.isSelectionEmpty() || this.this$0.nameList2.isSelectionEmpty()) {
/* 221 */         this.this$0.setDefaultButtonEnabled(false);
/*     */       } else {
/* 223 */         this.this$0.setDefaultButtonEnabled(true);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/dialogue/NamePairDialogue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */