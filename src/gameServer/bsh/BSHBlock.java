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
/*     */ class BSHBlock
/*     */   extends SimpleNode
/*     */ {
/*     */   public boolean isSynchronized = false;
/*     */   
/*     */   BSHBlock(int id) {
/*  41 */     super(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  46 */     return eval(callstack, interpreter, false);
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
/*     */   public Object eval(CallStack callstack, Interpreter interpreter, boolean overrideNamespace) throws EvalError {
/*  64 */     Object ret, syncValue = null;
/*  65 */     if (this.isSynchronized) {
/*     */ 
/*     */       
/*  68 */       SimpleNode exp = (SimpleNode)jjtGetChild(0);
/*  69 */       syncValue = exp.eval(callstack, interpreter);
/*     */     } 
/*     */ 
/*     */     
/*  73 */     if (this.isSynchronized) {
/*  74 */       synchronized (syncValue) {
/*     */         
/*  76 */         ret = evalBlock(callstack, interpreter, overrideNamespace, (NodeFilter)null);
/*     */       } 
/*     */     } else {
/*     */       
/*  80 */       ret = evalBlock(callstack, interpreter, overrideNamespace, (NodeFilter)null);
/*     */     } 
/*     */     
/*  83 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object evalBlock(CallStack callstack, Interpreter interpreter, boolean overrideNamespace, NodeFilter nodeFilter) throws EvalError {
/*  91 */     Object ret = Primitive.VOID;
/*  92 */     NameSpace enclosingNameSpace = null;
/*  93 */     if (!overrideNamespace) {
/*     */       
/*  95 */       enclosingNameSpace = callstack.top();
/*  96 */       BlockNameSpace bodyNameSpace = new BlockNameSpace(enclosingNameSpace);
/*     */ 
/*     */       
/*  99 */       callstack.swap(bodyNameSpace);
/*     */     } 
/*     */     
/* 102 */     int startChild = this.isSynchronized ? 1 : 0;
/* 103 */     int numChildren = jjtGetNumChildren();
/*     */ 
/*     */     
/*     */     try {
/*     */       int i;
/*     */ 
/*     */       
/* 110 */       for (i = startChild; i < numChildren; i++) {
/*     */         
/* 112 */         SimpleNode node = (SimpleNode)jjtGetChild(i);
/*     */         
/* 114 */         if (nodeFilter == null || nodeFilter.isVisible(node))
/*     */         {
/*     */           
/* 117 */           if (node instanceof BSHClassDeclaration)
/* 118 */             node.eval(callstack, interpreter);  } 
/*     */       } 
/* 120 */       for (i = startChild; i < numChildren; i++) {
/*     */         
/* 122 */         SimpleNode node = (SimpleNode)jjtGetChild(i);
/* 123 */         if (!(node instanceof BSHClassDeclaration))
/*     */         {
/*     */ 
/*     */           
/* 127 */           if (nodeFilter == null || nodeFilter.isVisible(node)) {
/*     */ 
/*     */             
/* 130 */             ret = node.eval(callstack, interpreter);
/*     */ 
/*     */             
/* 133 */             if (ret instanceof ReturnControl)
/*     */               break; 
/*     */           }  } 
/*     */       } 
/*     */     } finally {
/* 138 */       if (!overrideNamespace)
/* 139 */         callstack.swap(enclosingNameSpace); 
/*     */     } 
/* 141 */     return ret;
/*     */   }
/*     */   
/*     */   public static interface NodeFilter {
/*     */     boolean isVisible(SimpleNode param1SimpleNode);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */