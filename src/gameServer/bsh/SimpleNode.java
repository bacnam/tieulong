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
/*     */ 
/*     */ 
/*     */ class SimpleNode
/*     */   implements Node
/*     */ {
/*  54 */   public static SimpleNode JAVACODE = new SimpleNode(-1)
/*     */     {
/*     */       public String getSourceFile() {
/*  57 */         return "<Called from Java Code>";
/*     */       }
/*     */       
/*     */       public int getLineNumber() {
/*  61 */         return -1;
/*     */       }
/*     */       
/*     */       public String getText() {
/*  65 */         return "<Compiled Java Code>";
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   protected Node parent;
/*     */   protected Node[] children;
/*     */   protected int id;
/*     */   Token firstToken;
/*     */   Token lastToken;
/*     */   String sourceFile;
/*     */   
/*     */   public SimpleNode(int i) {
/*  78 */     this.id = i;
/*     */   }
/*     */   
/*     */   public void jjtOpen() {}
/*     */   public void jjtClose() {}
/*     */   
/*  84 */   public void jjtSetParent(Node n) { this.parent = n; } public Node jjtGetParent() {
/*  85 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void jjtAddChild(Node n, int i) {
/*  90 */     if (this.children == null) {
/*  91 */       this.children = new Node[i + 1];
/*     */     }
/*  93 */     else if (i >= this.children.length) {
/*     */       
/*  95 */       Node[] c = new Node[i + 1];
/*  96 */       System.arraycopy(this.children, 0, c, 0, this.children.length);
/*  97 */       this.children = c;
/*     */     } 
/*     */     
/* 100 */     this.children[i] = n;
/*     */   }
/*     */   
/*     */   public Node jjtGetChild(int i) {
/* 104 */     return this.children[i];
/*     */   }
/*     */   public SimpleNode getChild(int i) {
/* 107 */     return (SimpleNode)jjtGetChild(i);
/*     */   }
/*     */   
/*     */   public int jjtGetNumChildren() {
/* 111 */     return (this.children == null) ? 0 : this.children.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 121 */     return ParserTreeConstants.jjtNodeName[this.id]; } public String toString(String prefix) {
/* 122 */     return prefix + toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(String prefix) {
/* 130 */     System.out.println(toString(prefix));
/* 131 */     if (this.children != null)
/*     */     {
/* 133 */       for (int i = 0; i < this.children.length; i++) {
/*     */         
/* 135 */         SimpleNode n = (SimpleNode)this.children[i];
/* 136 */         if (n != null)
/*     */         {
/* 138 */           n.dump(prefix + " ");
/*     */         }
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
/*     */   public void prune() {
/* 152 */     jjtSetParent(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 161 */     throw new InterpreterError("Unimplemented or inappropriate for " + getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceFile(String sourceFile) {
/* 170 */     this.sourceFile = sourceFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSourceFile() {
/* 181 */     if (this.sourceFile == null) {
/* 182 */       if (this.parent != null) {
/* 183 */         return ((SimpleNode)this.parent).getSourceFile();
/*     */       }
/* 185 */       return "<unknown file>";
/*     */     } 
/* 187 */     return this.sourceFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/* 194 */     return this.firstToken.beginLine;
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
/*     */   public String getText() {
/* 209 */     StringBuffer text = new StringBuffer();
/* 210 */     Token t = this.firstToken;
/* 211 */     while (t != null) {
/* 212 */       text.append(t.image);
/* 213 */       if (!t.image.equals("."))
/* 214 */         text.append(" "); 
/* 215 */       if (t == this.lastToken || t.image.equals("{") || t.image.equals(";")) {
/*     */         break;
/*     */       }
/* 218 */       t = t.next;
/*     */     } 
/*     */     
/* 221 */     return text.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/SimpleNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */