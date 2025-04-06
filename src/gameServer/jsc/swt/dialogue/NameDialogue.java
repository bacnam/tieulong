/*     */ package jsc.swt.dialogue;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JScrollPane;
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
/*     */ public class NameDialogue
/*     */   extends Dialogue
/*     */ {
/*     */   JList nameList;
/*     */   
/*     */   public NameDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, int paramInt) {
/*  37 */     super(paramComponent, paramString1, paramString2, -1, 2);
/*     */ 
/*     */ 
/*     */     
/*  41 */     this.nameList = new JList(paramArrayOfString);
/*  42 */     addList(paramInt);
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
/*     */   public NameDialogue(Component paramComponent, String paramString1, String paramString2, Vector paramVector, int paramInt) {
/*  58 */     super(paramComponent, paramString1, paramString2, -1, 2);
/*     */ 
/*     */ 
/*     */     
/*  62 */     this.nameList = new JList(paramVector);
/*  63 */     addList(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   void addList(int paramInt) {
/*  68 */     setDefaultButtonEnabled(false);
/*  69 */     this.nameList.setSelectionMode(paramInt);
/*  70 */     this.nameList.addListSelectionListener(new ListListener(this));
/*  71 */     JScrollPane jScrollPane = new JScrollPane(this.nameList);
/*  72 */     add(jScrollPane, "Center");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIndices() {
/*  80 */     return this.nameList.getSelectedIndices();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] showNames() {
/*  91 */     if (show() == null) return null; 
/*  92 */     if (this.nameList.isSelectionEmpty()) return null; 
/*  93 */     Object[] arrayOfObject = this.nameList.getSelectedValues();
/*  94 */     String[] arrayOfString = new String[arrayOfObject.length];
/*  95 */     for (byte b = 0; b < arrayOfObject.length; ) { arrayOfString[b] = (String)arrayOfObject[b]; b++; }
/*  96 */      return arrayOfString;
/*     */   }
/*     */   class ListListener implements ListSelectionListener { ListListener(NameDialogue this$0) {
/*  99 */       this.this$0 = this$0;
/*     */     }
/*     */     private final NameDialogue this$0;
/*     */     public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
/* 103 */       this.this$0.setDefaultButtonEnabled(!this.this$0.nameList.isSelectionEmpty());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/dialogue/NameDialogue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */