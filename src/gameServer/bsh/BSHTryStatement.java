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
/*     */ class BSHTryStatement
/*     */   extends SimpleNode
/*     */ {
/*     */   BSHTryStatement(int id) {
/*  43 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  49 */     BSHBlock tryBlock = (BSHBlock)jjtGetChild(0);
/*     */     
/*  51 */     Vector<Node> catchParams = new Vector();
/*  52 */     Vector<Node> catchBlocks = new Vector();
/*     */     
/*  54 */     int nchild = jjtGetNumChildren();
/*  55 */     Node node = null;
/*  56 */     int i = 1;
/*  57 */     while (i < nchild && node = jjtGetChild(i++) instanceof BSHFormalParameter) {
/*     */       
/*  59 */       catchParams.addElement(node);
/*  60 */       catchBlocks.addElement(jjtGetChild(i++));
/*  61 */       node = null;
/*     */     } 
/*     */     
/*  64 */     BSHBlock finallyBlock = null;
/*  65 */     if (node != null) {
/*  66 */       finallyBlock = (BSHBlock)node;
/*     */     }
/*     */ 
/*     */     
/*  70 */     TargetError target = null;
/*  71 */     Throwable thrown = null;
/*  72 */     Object ret = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     int callstackDepth = callstack.depth();
/*     */     try {
/*  86 */       ret = tryBlock.eval(callstack, interpreter);
/*     */     }
/*  88 */     catch (TargetError e) {
/*  89 */       target = e;
/*  90 */       String stackInfo = "Bsh Stack: ";
/*  91 */       while (callstack.depth() > callstackDepth) {
/*  92 */         stackInfo = stackInfo + "\t" + callstack.pop() + "\n";
/*     */       }
/*     */     } 
/*     */     
/*  96 */     if (target != null) {
/*  97 */       thrown = target.getTarget();
/*     */     }
/*     */ 
/*     */     
/* 101 */     if (thrown != null) {
/*     */       
/* 103 */       int n = catchParams.size();
/* 104 */       for (i = 0; i < n; ) {
/*     */ 
/*     */         
/* 107 */         BSHFormalParameter fp = (BSHFormalParameter)catchParams.elementAt(i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 114 */         fp.eval(callstack, interpreter);
/*     */         
/* 116 */         if (fp.type == null && interpreter.getStrictJava()) {
/* 117 */           throw new EvalError("(Strict Java) Untyped catch block", this, callstack);
/*     */         }
/*     */ 
/*     */         
/* 121 */         if (fp.type != null) {
/*     */           try {
/* 123 */             thrown = (Throwable)Types.castObject(thrown, fp.type, 1);
/*     */           }
/* 125 */           catch (UtilEvalError e) {}
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 137 */         BSHBlock cb = (BSHBlock)catchBlocks.elementAt(i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 143 */         NameSpace enclosingNameSpace = callstack.top();
/* 144 */         BlockNameSpace cbNameSpace = new BlockNameSpace(enclosingNameSpace);
/*     */ 
/*     */         
/*     */         try {
/* 148 */           if (fp.type == BSHFormalParameter.UNTYPED)
/*     */           {
/* 150 */             cbNameSpace.setBlockVariable(fp.name, thrown);
/*     */           }
/*     */           else
/*     */           {
/* 154 */             Modifiers modifiers = new Modifiers();
/* 155 */             cbNameSpace.setTypedVariable(fp.name, fp.type, thrown, new Modifiers());
/*     */           }
/*     */         
/* 158 */         } catch (UtilEvalError e) {
/* 159 */           throw new InterpreterError("Unable to set var in catch block namespace.");
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 164 */         callstack.swap(cbNameSpace);
/*     */         try {
/* 166 */           ret = cb.eval(callstack, interpreter);
/*     */         } finally {
/*     */           
/* 169 */           callstack.swap(enclosingNameSpace);
/*     */         } 
/*     */         
/* 172 */         target = null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 178 */     if (finallyBlock != null) {
/* 179 */       ret = finallyBlock.eval(callstack, interpreter);
/*     */     }
/*     */     
/* 182 */     if (target != null) {
/* 183 */       throw target;
/*     */     }
/* 185 */     if (ret instanceof ReturnControl) {
/* 186 */       return ret;
/*     */     }
/* 188 */     return Primitive.VOID;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHTryStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */