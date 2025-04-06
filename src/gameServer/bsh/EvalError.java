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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EvalError
/*     */   extends Exception
/*     */ {
/*     */   SimpleNode node;
/*     */   String message;
/*     */   CallStack callstack;
/*     */   
/*     */   public EvalError(String s, SimpleNode node, CallStack callstack) {
/*  57 */     setMessage(s);
/*  58 */     this.node = node;
/*     */     
/*  60 */     if (callstack != null) {
/*  61 */       this.callstack = callstack.copy();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     String trace;
/*  70 */     if (this.node != null) {
/*  71 */       trace = " : at Line: " + this.node.getLineNumber() + " : in file: " + this.node.getSourceFile() + " : " + this.node.getText();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  76 */       trace = ": <at unknown location>";
/*     */     } 
/*  78 */     if (this.callstack != null) {
/*  79 */       trace = trace + "\n" + getScriptStackTrace();
/*     */     }
/*  81 */     return getMessage() + trace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reThrow(String msg) throws EvalError {
/*  90 */     prependMessage(msg);
/*  91 */     throw this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SimpleNode getNode() {
/*  99 */     return this.node;
/*     */   }
/*     */   
/*     */   void setNode(SimpleNode node) {
/* 103 */     this.node = node;
/*     */   }
/*     */   
/*     */   public String getErrorText() {
/* 107 */     if (this.node != null) {
/* 108 */       return this.node.getText();
/*     */     }
/* 110 */     return "<unknown error>";
/*     */   }
/*     */   
/*     */   public int getErrorLineNumber() {
/* 114 */     if (this.node != null) {
/* 115 */       return this.node.getLineNumber();
/*     */     }
/* 117 */     return -1;
/*     */   }
/*     */   
/*     */   public String getErrorSourceFile() {
/* 121 */     if (this.node != null) {
/* 122 */       return this.node.getSourceFile();
/*     */     }
/* 124 */     return "<unknown file>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScriptStackTrace() {
/* 129 */     if (this.callstack == null) {
/* 130 */       return "<Unknown>";
/*     */     }
/* 132 */     String trace = "";
/* 133 */     CallStack stack = this.callstack.copy();
/* 134 */     while (stack.depth() > 0) {
/*     */       
/* 136 */       NameSpace ns = stack.pop();
/* 137 */       SimpleNode node = ns.getNode();
/* 138 */       if (ns.isMethod) {
/*     */         
/* 140 */         trace = trace + "\nCalled from method: " + ns.getName();
/* 141 */         if (node != null) {
/* 142 */           trace = trace + " : at Line: " + node.getLineNumber() + " : in file: " + node.getSourceFile() + " : " + node.getText();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 148 */     return trace;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 154 */     return this.message;
/*     */   } public void setMessage(String s) {
/* 156 */     this.message = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prependMessage(String s) {
/* 163 */     if (s == null) {
/*     */       return;
/*     */     }
/* 166 */     if (this.message == null) {
/* 167 */       this.message = s;
/*     */     } else {
/* 169 */       this.message = s + " : " + this.message;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/EvalError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */