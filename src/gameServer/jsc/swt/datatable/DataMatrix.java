/*     */ package jsc.swt.datatable;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
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
/*     */ public class DataMatrix
/*     */   implements Serializable
/*     */ {
/*     */   int columnCount;
/*     */   int rowCount;
/*     */   Class defaultClass;
/*     */   String defaultNamePrefix;
/*     */   Vector columnClasses;
/*     */   Vector columnData;
/*     */   Vector columnNames;
/*     */   
/*     */   public DataMatrix(int paramInt1, int paramInt2, String paramString, Class paramClass) {
/*  41 */     if (paramInt1 < 0 || paramInt2 < 1)
/*  42 */       throw new IllegalArgumentException("Invalid data matrix"); 
/*  43 */     this.rowCount = paramInt1;
/*  44 */     this.columnCount = paramInt2;
/*  45 */     this.defaultNamePrefix = paramString;
/*  46 */     this.defaultClass = paramClass;
/*  47 */     this.columnNames = new Vector(paramInt2);
/*  48 */     this.columnClasses = new Vector(paramInt2);
/*  49 */     this.columnData = new Vector(paramInt2);
/*     */     
/*  51 */     for (byte b = 1; b <= paramInt2; b++) {
/*     */       
/*  53 */       this.columnNames.addElement(paramString + b);
/*  54 */       this.columnClasses.addElement(paramClass); Vector vector;
/*  55 */       this.columnData.addElement(vector = new Vector(paramInt1));
/*  56 */       for (byte b1 = 0; b1 < paramInt1; ) { vector.addElement(null); b1++; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataMatrix(int paramInt1, int paramInt2) {
/*  68 */     this(paramInt1, paramInt2, "", Double.class);
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
/*     */   public DataMatrix(String paramString1, boolean paramBoolean, String paramString2, String paramString3, String paramString4) {
/*  92 */     this(0, 1, paramString4, String.class);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString1, paramString2);
/*  98 */     int i = stringTokenizer.countTokens();
/*  99 */     int j = 0; byte b1;
/* 100 */     for (b1 = 0; b1 < i; b1++) {
/*     */       
/* 102 */       StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), paramString3);
/* 103 */       int k = stringTokenizer1.countTokens();
/* 104 */       if (k > j) j = k;
/*     */     
/*     */     } 
/*     */     byte b2;
/* 108 */     for (b2 = 2; b2 <= j; ) { addColumn(); b2++; }
/*     */     
/* 110 */     stringTokenizer = new StringTokenizer(paramString1, paramString2);
/*     */ 
/*     */     
/* 113 */     byte b3 = 0;
/* 114 */     for (b1 = 0; b1 < i; b1++) {
/*     */       
/* 116 */       StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), paramString3);
/* 117 */       int k = stringTokenizer1.countTokens();
/* 118 */       if (b1 == 0 && paramBoolean) {
/*     */         
/* 120 */         b3 = 1;
/* 121 */         for (b2 = 0; b2 < k; b2++)
/*     */         {
/* 123 */           String str = stringTokenizer1.nextToken();
/* 124 */           setColumnName(b2, str);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 129 */         addRow();
/* 130 */         for (b2 = 0; b2 < k; b2++) {
/*     */           
/* 132 */           String str = stringTokenizer1.nextToken();
/* 133 */           setValueAt(str, b1 - b3, b2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 139 */     for (b2 = 0; b2 < this.columnCount; b2++) {
/*     */       
/* 141 */       if (isColumnDataInteger(b2)) {
/*     */         
/* 143 */         convertStringToInteger(b2);
/* 144 */         setColumnClass(b2, Integer.class);
/*     */       }
/* 146 */       else if (isColumnDataDouble(b2)) {
/*     */         
/* 148 */         convertStringToDouble(b2);
/* 149 */         setColumnClass(b2, Double.class);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int addColumn() {
/* 162 */     this.columnCount++;
/* 163 */     this.columnNames.addElement(this.defaultNamePrefix + this.columnCount);
/* 164 */     this.columnClasses.addElement(this.defaultClass);
/*     */     Vector vector;
/* 166 */     this.columnData.addElement(vector = new Vector(this.rowCount));
/* 167 */     for (byte b = 0; b < this.rowCount; ) { vector.addElement(null); b++; }
/* 168 */      return this.columnCount - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRow() {
/* 174 */     this.rowCount++;
/*     */     
/* 176 */     for (byte b = 0; b < this.columnCount; b++) {
/*     */       
/* 178 */       Vector vector = this.columnData.elementAt(b);
/* 179 */       vector.addElement(null);
/*     */     } 
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
/*     */   private void convertDoubleToInteger(int paramInt) {
/* 205 */     Vector vector = this.columnData.elementAt(paramInt);
/* 206 */     for (byte b = 0; b < this.rowCount; b++) {
/*     */       
/* 208 */       Double double_ = (Double)vector.elementAt(b);
/* 209 */       if (double_ != null) {
/*     */         
/* 211 */         Double double_1 = double_;
/* 212 */         int i = double_1.intValue();
/* 213 */         if (i < 0) i = 0; 
/* 214 */         vector.setElementAt(new Integer(i), b);
/*     */       } 
/*     */     } 
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
/*     */   
/*     */   private void convertStringToDouble(int paramInt) {
/* 242 */     Vector vector = this.columnData.elementAt(paramInt);
/* 243 */     for (byte b = 0; b < this.rowCount; b++) {
/*     */       
/* 245 */       Object object = vector.elementAt(b);
/* 246 */       if (object != null) {
/*     */         
/* 248 */         String str = object.toString();
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 253 */           vector.setElementAt(new Double(str), b);
/*     */         }
/*     */         catch (Exception exception) {
/*     */           
/* 257 */           vector.setElementAt(new Double(0.0D), b);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void convertStringToInteger(int paramInt) {
/* 266 */     convertStringToDouble(paramInt); convertDoubleToInteger(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyColumn(int paramInt1, int paramInt2) {
/* 276 */     setColumnName(paramInt2, getColumnName(paramInt1));
/* 277 */     setColumnClass(paramInt2, getColumnClass(paramInt1));
/* 278 */     Vector vector1 = this.columnData.elementAt(paramInt1);
/* 279 */     Vector vector2 = this.columnData.elementAt(paramInt2);
/* 280 */     for (byte b = 0; b < this.rowCount; b++) {
/* 281 */       vector2.setElementAt(vector1.elementAt(b), b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyRow(int paramInt1, int paramInt2) {
/* 293 */     for (byte b = 0; b < this.columnCount; b++) {
/*     */       
/* 295 */       Vector vector = this.columnData.elementAt(b);
/* 296 */       vector.setElementAt(vector.elementAt(paramInt1), paramInt2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getColumnClass(int paramInt) {
/* 306 */     return this.columnClasses.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 313 */     return this.columnCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getColumnData(int paramInt) {
/* 322 */     return this.columnData.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnName(int paramInt) {
/* 331 */     return this.columnNames.elementAt(paramInt);
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
/*     */   public Vector getColumnNames(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
/* 344 */     Vector vector = new Vector(this.columnCount);
/* 345 */     for (byte b = 0; b < this.columnCount; b++) {
/*     */       
/* 347 */       if (getColumnValueCount(b) >= paramInt) {
/*     */         
/* 349 */         Class clazz = getColumnClass(b);
/* 350 */         if ((paramBoolean1 && clazz == Double.class) || (paramBoolean2 && clazz == Integer.class) || (paramBoolean3 && clazz == String.class))
/*     */         {
/*     */           
/* 353 */           vector.addElement(getColumnName(b)); } 
/*     */       } 
/*     */     } 
/* 356 */     return vector;
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
/*     */   public int getColumnValueCount(int paramInt) {
/* 374 */     Vector vector = this.columnData.elementAt(paramInt);
/* 375 */     byte b1 = 0;
/* 376 */     for (byte b2 = 0; b2 < this.rowCount; b2++) {
/* 377 */       if (vector.elementAt(b2) != null) b1++; 
/* 378 */     }  return b1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getDefaultColumnClass() {
/* 387 */     return this.defaultClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultNamePrefix() {
/* 394 */     return this.defaultNamePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 401 */     return this.rowCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValueAt(int paramInt1, int paramInt2) {
/* 412 */     Vector vector = this.columnData.elementAt(paramInt2);
/*     */     
/* 414 */     return vector.elementAt(paramInt1);
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
/*     */   public void insertColumn(int paramInt) {
/* 426 */     this.columnCount++;
/*     */     
/* 428 */     if (paramInt > this.columnNames.size()) paramInt = this.columnNames.size(); 
/* 429 */     this.columnNames.insertElementAt(this.defaultNamePrefix + this.columnCount, paramInt);
/* 430 */     this.columnClasses.insertElementAt(this.defaultClass, paramInt);
/*     */     Vector vector;
/* 432 */     this.columnData.insertElementAt(vector = new Vector(this.rowCount), paramInt);
/* 433 */     for (byte b = 0; b < this.rowCount; ) { vector.addElement(null); b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertRow(int paramInt) {
/* 443 */     this.rowCount++;
/*     */     
/* 445 */     for (byte b = 0; b < this.columnCount; b++) {
/*     */       
/* 447 */       Vector vector = this.columnData.elementAt(b);
/* 448 */       vector.insertElementAt(null, paramInt);
/*     */     } 
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
/*     */   public void insertValue(Object paramObject, int paramInt1, int paramInt2) {
/* 487 */     Vector vector = this.columnData.elementAt(paramInt2);
/* 488 */     vector.insertElementAt(paramObject, paramInt1);
/* 489 */     vector.removeElementAt(vector.size() - 1);
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
/*     */   public boolean isColumnDataDouble(int paramInt) {
/* 501 */     Vector vector = this.columnData.elementAt(paramInt);
/* 502 */     for (byte b = 0; b < this.rowCount; b++) {
/*     */       
/* 504 */       Object object = vector.elementAt(b);
/* 505 */       if (object != null) {
/*     */         
/* 507 */         String str = object.toString();
/*     */         try {
/* 509 */           Double double_ = new Double(str);
/*     */         }
/*     */         catch (Exception exception) {
/*     */           
/* 513 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 518 */     return true;
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
/*     */   public boolean isColumnDataInteger(int paramInt) {
/* 530 */     Vector vector = this.columnData.elementAt(paramInt);
/* 531 */     for (byte b = 0; b < this.rowCount; b++) {
/*     */       
/* 533 */       Object object = vector.elementAt(b);
/* 534 */       if (object != null) {
/*     */         
/* 536 */         String str = object.toString();
/*     */         try {
/* 538 */           Integer integer = new Integer(str);
/* 539 */           if (integer.intValue() < 0) return false;
/*     */         
/*     */         } catch (Exception exception) {
/* 542 */           return false;
/*     */         } 
/*     */       } 
/* 545 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCell(int paramInt1, int paramInt2) {
/* 556 */     Vector vector = this.columnData.elementAt(paramInt2);
/* 557 */     vector.removeElementAt(paramInt1);
/* 558 */     vector.addElement(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeColumn(int paramInt) {
/* 568 */     this.columnCount--;
/* 569 */     this.columnNames.removeElementAt(paramInt);
/* 570 */     this.columnClasses.removeElementAt(paramInt);
/* 571 */     this.columnData.removeElementAt(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeColumn(String paramString) {
/* 581 */     int i = this.columnNames.indexOf(paramString);
/* 582 */     removeColumn(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeRow(int paramInt) {
/* 592 */     this.rowCount--;
/*     */     
/* 594 */     for (byte b = 0; b < this.columnCount; b++) {
/*     */       
/* 596 */       Vector vector = this.columnData.elementAt(b);
/* 597 */       vector.removeElementAt(paramInt);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumnClass(int paramInt, Class paramClass) {
/* 608 */     this.columnClasses.setElementAt(paramClass, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumnName(int paramInt, String paramString) {
/* 617 */     this.columnNames.setElementAt(paramString, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultColumnClass(Class paramClass) {
/* 626 */     this.defaultClass = paramClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
/* 637 */     Vector vector = this.columnData.elementAt(paramInt2);
/*     */     
/* 639 */     vector.setElementAt(paramObject, paramInt1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/DataMatrix.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */