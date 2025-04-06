/*     */ package bsh;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BSHLiteral
/*     */   extends SimpleNode
/*     */ {
/*     */   public Object value;
/*     */   
/*     */   BSHLiteral(int id) {
/*  41 */     super(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  46 */     if (this.value == null) {
/*  47 */       throw new InterpreterError("Null in bsh literal: " + this.value);
/*     */     }
/*  49 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   private char getEscapeChar(char ch) {
/*  54 */     switch (ch) {
/*     */       
/*     */       case 'b':
/*  57 */         ch = '\b';
/*     */         break;
/*     */       
/*     */       case 't':
/*  61 */         ch = '\t';
/*     */         break;
/*     */       
/*     */       case 'n':
/*  65 */         ch = '\n';
/*     */         break;
/*     */       
/*     */       case 'f':
/*  69 */         ch = '\f';
/*     */         break;
/*     */       
/*     */       case 'r':
/*  73 */         ch = '\r';
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     return ch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void charSetup(String str) {
/*  88 */     char ch = str.charAt(0);
/*  89 */     if (ch == '\\') {
/*     */ 
/*     */       
/*  92 */       ch = str.charAt(1);
/*     */       
/*  94 */       if (Character.isDigit(ch)) {
/*  95 */         ch = (char)Integer.parseInt(str.substring(1), 8);
/*     */       } else {
/*  97 */         ch = getEscapeChar(ch);
/*     */       } 
/*     */     } 
/* 100 */     this.value = new Primitive((new Character(ch)).charValue());
/*     */   }
/*     */ 
/*     */   
/*     */   void stringSetup(String str) {
/* 105 */     StringBuffer buffer = new StringBuffer();
/* 106 */     for (int i = 0; i < str.length(); i++) {
/*     */       
/* 108 */       char ch = str.charAt(i);
/* 109 */       if (ch == '\\') {
/*     */ 
/*     */         
/* 112 */         ch = str.charAt(++i);
/*     */         
/* 114 */         if (Character.isDigit(ch)) {
/*     */           
/* 116 */           int endPos = i;
/*     */ 
/*     */           
/* 119 */           while (endPos < i + 2) {
/*     */             
/* 121 */             if (Character.isDigit(str.charAt(endPos + 1))) {
/* 122 */               endPos++;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 127 */           ch = (char)Integer.parseInt(str.substring(i, endPos + 1), 8);
/* 128 */           i = endPos;
/*     */         } else {
/*     */           
/* 131 */           ch = getEscapeChar(ch);
/*     */         } 
/*     */       } 
/* 134 */       buffer.append(ch);
/*     */     } 
/*     */     
/* 137 */     this.value = buffer.toString().intern();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHLiteral.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */