/*     */ package bsh;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CallStack
/*     */ {
/*  64 */   private Vector stack = new Vector(2);
/*     */   
/*     */   public CallStack() {}
/*     */   
/*     */   public CallStack(NameSpace namespace) {
/*  69 */     push(namespace);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  73 */     this.stack.removeAllElements();
/*     */   }
/*     */   
/*     */   public void push(NameSpace ns) {
/*  77 */     this.stack.insertElementAt(ns, 0);
/*     */   }
/*     */   
/*     */   public NameSpace top() {
/*  81 */     return get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameSpace get(int depth) {
/*  88 */     if (depth >= depth()) {
/*  89 */       return NameSpace.JAVACODE;
/*     */     }
/*  91 */     return this.stack.elementAt(depth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int depth, NameSpace ns) {
/*  99 */     this.stack.setElementAt(ns, depth);
/*     */   }
/*     */   
/*     */   public NameSpace pop() {
/* 103 */     if (depth() < 1)
/* 104 */       throw new InterpreterError("pop on empty CallStack"); 
/* 105 */     NameSpace top = top();
/* 106 */     this.stack.removeElementAt(0);
/* 107 */     return top;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameSpace swap(NameSpace newTop) {
/* 115 */     NameSpace oldTop = this.stack.elementAt(0);
/* 116 */     this.stack.setElementAt(newTop, 0);
/* 117 */     return oldTop;
/*     */   }
/*     */   
/*     */   public int depth() {
/* 121 */     return this.stack.size();
/*     */   }
/*     */   
/*     */   public NameSpace[] toArray() {
/* 125 */     NameSpace[] nsa = new NameSpace[depth()];
/* 126 */     this.stack.copyInto((Object[])nsa);
/* 127 */     return nsa;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 131 */     StringBuffer sb = new StringBuffer();
/* 132 */     sb.append("CallStack:\n");
/* 133 */     NameSpace[] nsa = toArray();
/* 134 */     for (int i = 0; i < nsa.length; i++) {
/* 135 */       sb.append("\t" + nsa[i] + "\n");
/*     */     }
/* 137 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallStack copy() {
/* 145 */     CallStack cs = new CallStack();
/* 146 */     cs.stack = (Vector)this.stack.clone();
/* 147 */     return cs;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/CallStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */