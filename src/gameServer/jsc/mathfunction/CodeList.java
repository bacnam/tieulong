/*     */ package jsc.mathfunction;
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
/*     */ public class CodeList
/*     */   implements Cloneable
/*     */ {
/*     */   private int nMax;
/*  50 */   int codeListSize = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CodeListLine[] codeList;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeList(int paramInt) {
/*  61 */     this.nMax = paramInt;
/*     */     
/*  63 */     this.codeList = new CodeListLine[this.nMax];
/*     */     
/*  65 */     for (byte b = 0; b < this.nMax; ) { this.codeList[b] = new CodeListLine(); b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  73 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeList copy() {
/*  82 */     CodeList codeList = new CodeList(this.nMax);
/*  83 */     codeList.codeListSize = this.codeListSize;
/*  84 */     for (byte b = 0; b < this.nMax; b++) {
/*     */       
/*  86 */       (codeList.codeList[b]).f = (this.codeList[b]).f;
/*  87 */       (codeList.codeList[b]).op = (this.codeList[b]).op;
/*  88 */       (codeList.codeList[b]).type = (this.codeList[b]).type;
/*  89 */       (codeList.codeList[b]).left = (this.codeList[b]).left;
/*  90 */       (codeList.codeList[b]).right = (this.codeList[b]).right;
/*     */     } 
/*  92 */     return codeList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int decrementSize() {
/* 100 */     return --this.codeListSize;
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
/*     */   public boolean equals(CodeList paramCodeList) {
/* 115 */     if (paramCodeList.size() != this.codeListSize) return false; 
/* 116 */     for (byte b = 1; b <= this.codeListSize; b++) {
/*     */       
/* 118 */       if ((paramCodeList.codeList[b]).f != (this.codeList[b]).f) return false; 
/* 119 */       if ((paramCodeList.codeList[b]).op != (this.codeList[b]).op) return false; 
/* 120 */       if ((paramCodeList.codeList[b]).type != (this.codeList[b]).type) return false; 
/* 121 */       if ((paramCodeList.codeList[b]).left != (this.codeList[b]).left) return false; 
/* 122 */       if ((paramCodeList.codeList[b]).right != (this.codeList[b]).right) return false; 
/*     */     } 
/* 124 */     return true;
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
/*     */   public int getCode(int paramInt) {
/* 141 */     return (this.codeList[paramInt]).op;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getLabelCode(int paramInt) {
/* 149 */     return -paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLabelLine(int paramInt) {
/* 158 */     return (paramInt < 0) ? -paramInt : paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLeft(int paramInt) {
/* 167 */     return (this.codeList[paramInt]).left;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLeftCode(int paramInt) {
/* 176 */     return (this.codeList[getLabelLine((this.codeList[paramInt]).left)]).op;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLeftType(int paramInt) {
/* 184 */     return (this.codeList[getLabelLine((this.codeList[paramInt]).left)]).type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLeftValue(int paramInt) {
/* 194 */     return (this.codeList[getLabelLine((this.codeList[paramInt]).left)]).f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRight(int paramInt) {
/* 203 */     return (this.codeList[paramInt]).right;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRightCode(int paramInt) {
/* 212 */     return (this.codeList[getLabelLine((this.codeList[paramInt]).right)]).op;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRightType(int paramInt) {
/* 220 */     return (this.codeList[getLabelLine((this.codeList[paramInt]).right)]).type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRightValue(int paramInt) {
/* 230 */     return (this.codeList[getLabelLine((this.codeList[paramInt]).right)]).f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType(int paramInt) {
/* 239 */     return (this.codeList[paramInt]).type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue() {
/* 247 */     return (this.codeList[this.codeListSize]).f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue(int paramInt) {
/* 256 */     return (this.codeList[paramInt]).f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int incrementSize() {
/* 263 */     return ++this.codeListSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCode(int paramInt1, int paramInt2) {
/* 272 */     (this.codeList[paramInt2]).op = paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLeft(int paramInt1, int paramInt2) {
/* 281 */     (this.codeList[paramInt2]).left = paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRight(int paramInt1, int paramInt2) {
/* 290 */     (this.codeList[paramInt2]).right = paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int paramInt1, int paramInt2) {
/* 299 */     (this.codeList[paramInt2]).type = paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int paramInt) {
/* 306 */     this.codeListSize = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(double paramDouble, int paramInt) {
/* 314 */     (this.codeList[paramInt]).f = paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 321 */     return this.codeListSize;
/*     */   } private class CodeListLine { private CodeListLine(CodeList this$0) {
/* 323 */       CodeList.this = CodeList.this;
/*     */     }
/*     */     
/*     */     double f;
/*     */     int op;
/*     */     int type;
/*     */     int left;
/*     */     int right;
/*     */     private final CodeList this$0; }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/CodeList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */