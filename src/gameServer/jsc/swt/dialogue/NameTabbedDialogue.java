/*     */ package jsc.swt.dialogue;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
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
/*     */ public class NameTabbedDialogue
/*     */   extends Dialogue
/*     */ {
/*     */   int n;
/*     */   JList[] nameLists;
/*     */   boolean[] selectionRequired;
/*     */   
/*     */   public NameTabbedDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, Vector[] paramArrayOfVector, int[] paramArrayOfint, boolean[] paramArrayOfboolean) {
/*  45 */     super(paramComponent, paramString1, paramString2, -1, 2);
/*  46 */     createTabbedPane(paramArrayOfString, paramArrayOfVector, paramArrayOfint, paramArrayOfboolean);
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
/*     */   public NameTabbedDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, Vector paramVector, int paramInt, boolean[] paramArrayOfboolean) {
/*  69 */     super(paramComponent, paramString1, paramString2, -1, 2);
/*  70 */     this.n = paramArrayOfString.length;
/*  71 */     Vector[] arrayOfVector = new Vector[this.n];
/*  72 */     int[] arrayOfInt = new int[this.n];
/*  73 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/*  75 */       arrayOfVector[b] = paramVector;
/*  76 */       arrayOfInt[b] = paramInt;
/*     */     } 
/*  78 */     createTabbedPane(paramArrayOfString, arrayOfVector, arrayOfInt, paramArrayOfboolean);
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
/*     */   public NameTabbedDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, Vector paramVector, int[] paramArrayOfint, boolean[] paramArrayOfboolean) {
/* 101 */     super(paramComponent, paramString1, paramString2, -1, 2);
/* 102 */     this.n = paramArrayOfString.length;
/* 103 */     Vector[] arrayOfVector = new Vector[this.n];
/* 104 */     for (byte b = 0; b < this.n; ) { arrayOfVector[b] = paramVector; b++; }
/* 105 */      createTabbedPane(paramArrayOfString, arrayOfVector, paramArrayOfint, paramArrayOfboolean);
/*     */   }
/*     */ 
/*     */   
/*     */   private void createTabbedPane(String[] paramArrayOfString, Vector[] paramArrayOfVector, int[] paramArrayOfint, boolean[] paramArrayOfboolean) {
/* 110 */     this.n = paramArrayOfString.length;
/* 111 */     if (paramArrayOfVector.length != this.n || paramArrayOfint.length != this.n || paramArrayOfboolean.length != this.n) {
/* 112 */       throw new IllegalArgumentException("Arrays different lengths.");
/*     */     }
/* 114 */     this.selectionRequired = paramArrayOfboolean;
/*     */ 
/*     */     
/* 117 */     JTabbedPane jTabbedPane = new JTabbedPane();
/* 118 */     this.nameLists = new JList[this.n];
/* 119 */     Font font = new Font("SansSerif", 0, 12);
/* 120 */     ListListener listListener = new ListListener(this);
/* 121 */     for (byte b = 0; b < this.n; b++) {
/*     */ 
/*     */       
/* 124 */       this.nameLists[b] = new JList(paramArrayOfVector[b]);
/* 125 */       this.nameLists[b].setSelectionMode(paramArrayOfint[b]);
/* 126 */       this.nameLists[b].addListSelectionListener(listListener);
/* 127 */       JScrollPane jScrollPane = new JScrollPane(this.nameLists[b]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 137 */       jTabbedPane.addTab(paramArrayOfString[b], jScrollPane);
/* 138 */       if (paramArrayOfboolean[b]) setDefaultButtonEnabled(false);
/*     */     
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     add(jTabbedPane, "Center");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 153 */     return this.n;
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
/*     */   public String[] getNames(int paramInt) {
/* 166 */     if (this.nameLists[paramInt].isSelectionEmpty()) return null; 
/* 167 */     Object[] arrayOfObject = this.nameLists[paramInt].getSelectedValues();
/* 168 */     String[] arrayOfString = new String[arrayOfObject.length];
/* 169 */     for (byte b = 0; b < arrayOfObject.length; ) { arrayOfString[b] = (String)arrayOfObject[b]; b++; }
/* 170 */      return arrayOfString;
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
/* 187 */     if (show() == null) return null; 
/* 188 */     String[][] arrayOfString = new String[this.n][];
/* 189 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/* 191 */       String[] arrayOfString1 = getNames(b);
/* 192 */       arrayOfString[b] = arrayOfString1;
/*     */     } 
/* 194 */     return arrayOfString;
/*     */   }
/*     */   class ListListener implements ListSelectionListener { ListListener(NameTabbedDialogue this$0) {
/* 197 */       this.this$0 = this$0;
/*     */     }
/*     */     private final NameTabbedDialogue this$0;
/*     */     public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
/* 201 */       for (byte b = 0; b < this.this$0.n; b++) {
/* 202 */         if (this.this$0.selectionRequired[b] && this.this$0.nameLists[b].isSelectionEmpty())
/* 203 */         { this.this$0.setDefaultButtonEnabled(false); return; } 
/* 204 */       }  this.this$0.setDefaultButtonEnabled(true);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/dialogue/NameTabbedDialogue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */