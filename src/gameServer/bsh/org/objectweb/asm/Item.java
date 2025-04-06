/*     */ package bsh.org.objectweb.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Item
/*     */ {
/*     */   short index;
/*     */   int type;
/*     */   int intVal;
/*     */   long longVal;
/*     */   float floatVal;
/*     */   double doubleVal;
/*     */   String strVal1;
/*     */   String strVal2;
/*     */   String strVal3;
/*     */   int hashCode;
/*     */   Item next;
/*     */   
/*     */   Item() {}
/*     */   
/*     */   Item(short index, Item i) {
/* 122 */     this.index = index;
/* 123 */     this.type = i.type;
/* 124 */     this.intVal = i.intVal;
/* 125 */     this.longVal = i.longVal;
/* 126 */     this.floatVal = i.floatVal;
/* 127 */     this.doubleVal = i.doubleVal;
/* 128 */     this.strVal1 = i.strVal1;
/* 129 */     this.strVal2 = i.strVal2;
/* 130 */     this.strVal3 = i.strVal3;
/* 131 */     this.hashCode = i.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(int intVal) {
/* 141 */     this.type = 3;
/* 142 */     this.intVal = intVal;
/* 143 */     this.hashCode = this.type + intVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(long longVal) {
/* 153 */     this.type = 5;
/* 154 */     this.longVal = longVal;
/* 155 */     this.hashCode = this.type + (int)longVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(float floatVal) {
/* 165 */     this.type = 4;
/* 166 */     this.floatVal = floatVal;
/* 167 */     this.hashCode = this.type + (int)floatVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(double doubleVal) {
/* 177 */     this.type = 6;
/* 178 */     this.doubleVal = doubleVal;
/* 179 */     this.hashCode = this.type + (int)doubleVal;
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
/*     */   void set(int type, String strVal1, String strVal2, String strVal3) {
/* 197 */     this.type = type;
/* 198 */     this.strVal1 = strVal1;
/* 199 */     this.strVal2 = strVal2;
/* 200 */     this.strVal3 = strVal3;
/* 201 */     switch (type) {
/*     */       case 1:
/*     */       case 7:
/*     */       case 8:
/* 205 */         this.hashCode = type + strVal1.hashCode();
/*     */         return;
/*     */       case 12:
/* 208 */         this.hashCode = type + strVal1.hashCode() * strVal2.hashCode();
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 214 */     this.hashCode = type + strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode();
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
/*     */   boolean isEqualTo(Item i) {
/* 228 */     if (i.type == this.type) {
/* 229 */       switch (this.type) {
/*     */         case 3:
/* 231 */           return (i.intVal == this.intVal);
/*     */         case 5:
/* 233 */           return (i.longVal == this.longVal);
/*     */         case 4:
/* 235 */           return (i.floatVal == this.floatVal);
/*     */         case 6:
/* 237 */           return (i.doubleVal == this.doubleVal);
/*     */         case 1:
/*     */         case 7:
/*     */         case 8:
/* 241 */           return i.strVal1.equals(this.strVal1);
/*     */         case 12:
/* 243 */           return (i.strVal1.equals(this.strVal1) && i.strVal2.equals(this.strVal2));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 249 */       return (i.strVal1.equals(this.strVal1) && i.strVal2.equals(this.strVal2) && i.strVal3.equals(this.strVal3));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 254 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/org/objectweb/asm/Item.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */