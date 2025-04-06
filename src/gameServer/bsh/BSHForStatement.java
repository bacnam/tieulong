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
/*     */ class BSHForStatement
/*     */   extends SimpleNode
/*     */   implements ParserConstants
/*     */ {
/*     */   public boolean hasForInit;
/*     */   public boolean hasExpression;
/*     */   public boolean hasForUpdate;
/*     */   private SimpleNode forInit;
/*     */   private SimpleNode expression;
/*     */   private SimpleNode forUpdate;
/*     */   private SimpleNode statement;
/*     */   private boolean parsed;
/*     */   
/*     */   BSHForStatement(int id) {
/*  53 */     super(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  58 */     int i = 0;
/*  59 */     if (this.hasForInit)
/*  60 */       this.forInit = (SimpleNode)jjtGetChild(i++); 
/*  61 */     if (this.hasExpression)
/*  62 */       this.expression = (SimpleNode)jjtGetChild(i++); 
/*  63 */     if (this.hasForUpdate)
/*  64 */       this.forUpdate = (SimpleNode)jjtGetChild(i++); 
/*  65 */     if (i < jjtGetNumChildren()) {
/*  66 */       this.statement = (SimpleNode)jjtGetChild(i);
/*     */     }
/*  68 */     NameSpace enclosingNameSpace = callstack.top();
/*  69 */     BlockNameSpace forNameSpace = new BlockNameSpace(enclosingNameSpace);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     callstack.swap(forNameSpace);
/*     */ 
/*     */     
/*  92 */     if (this.hasForInit) {
/*  93 */       this.forInit.eval(callstack, interpreter);
/*     */     }
/*  95 */     Object returnControl = Primitive.VOID;
/*     */     
/*     */     while (true) {
/*  98 */       if (this.hasExpression) {
/*     */         
/* 100 */         boolean cond = BSHIfStatement.evaluateCondition(this.expression, callstack, interpreter);
/*     */ 
/*     */         
/* 103 */         if (!cond) {
/*     */           break;
/*     */         }
/*     */       } 
/* 107 */       boolean breakout = false;
/* 108 */       if (this.statement != null) {
/*     */ 
/*     */         
/* 111 */         Object ret = this.statement.eval(callstack, interpreter);
/*     */         
/* 113 */         if (ret instanceof ReturnControl)
/*     */         {
/* 115 */           switch (((ReturnControl)ret).kind) {
/*     */             
/*     */             case 46:
/* 118 */               returnControl = ret;
/* 119 */               breakout = true;
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 12:
/* 126 */               breakout = true;
/*     */               break;
/*     */           } 
/*     */         
/*     */         }
/*     */       } 
/* 132 */       if (breakout) {
/*     */         break;
/*     */       }
/* 135 */       if (this.hasForUpdate) {
/* 136 */         this.forUpdate.eval(callstack, interpreter);
/*     */       }
/*     */     } 
/* 139 */     callstack.swap(enclosingNameSpace);
/* 140 */     return returnControl;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHForStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */