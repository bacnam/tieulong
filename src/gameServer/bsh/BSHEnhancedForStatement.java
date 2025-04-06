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
/*     */ class BSHEnhancedForStatement
/*     */   extends SimpleNode
/*     */   implements ParserConstants
/*     */ {
/*     */   String varName;
/*     */   
/*     */   BSHEnhancedForStatement(int id) {
/*  20 */     super(id);
/*     */   }
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*     */     SimpleNode expression;
/*  25 */     Class elementType = null;
/*  26 */     SimpleNode statement = null;
/*     */     
/*  28 */     NameSpace enclosingNameSpace = callstack.top();
/*  29 */     SimpleNode firstNode = (SimpleNode)jjtGetChild(0);
/*  30 */     int nodeCount = jjtGetNumChildren();
/*     */     
/*  32 */     if (firstNode instanceof BSHType) {
/*     */       
/*  34 */       elementType = ((BSHType)firstNode).getType(callstack, interpreter);
/*  35 */       expression = (SimpleNode)jjtGetChild(1);
/*  36 */       if (nodeCount > 2) {
/*  37 */         statement = (SimpleNode)jjtGetChild(2);
/*     */       }
/*     */     } else {
/*  40 */       expression = firstNode;
/*  41 */       if (nodeCount > 1) {
/*  42 */         statement = (SimpleNode)jjtGetChild(1);
/*     */       }
/*     */     } 
/*  45 */     BlockNameSpace eachNameSpace = new BlockNameSpace(enclosingNameSpace);
/*  46 */     callstack.swap(eachNameSpace);
/*     */     
/*  48 */     Object iteratee = expression.eval(callstack, interpreter);
/*     */     
/*  50 */     if (iteratee == Primitive.NULL) {
/*  51 */       throw new EvalError("The collection, array, map, iterator, or enumeration portion of a for statement cannot be null.", this, callstack);
/*     */     }
/*     */ 
/*     */     
/*  55 */     CollectionManager cm = CollectionManager.getCollectionManager();
/*  56 */     if (!cm.isBshIterable(iteratee)) {
/*  57 */       throw new EvalError("Can't iterate over type: " + iteratee.getClass(), this, callstack);
/*     */     }
/*  59 */     BshIterator iterator = cm.getBshIterator(iteratee);
/*     */     
/*  61 */     Object returnControl = Primitive.VOID;
/*  62 */     while (iterator.hasNext()) {
/*     */       
/*     */       try {
/*  65 */         if (elementType != null)
/*  66 */         { eachNameSpace.setTypedVariable(this.varName, elementType, iterator.next(), new Modifiers()); }
/*     */         
/*     */         else
/*     */         
/*  70 */         { eachNameSpace.setVariable(this.varName, iterator.next(), false); } 
/*  71 */       } catch (UtilEvalError e) {
/*  72 */         throw e.toEvalError("for loop iterator variable:" + this.varName, this, callstack);
/*     */       } 
/*     */ 
/*     */       
/*  76 */       boolean breakout = false;
/*  77 */       if (statement != null) {
/*     */         
/*  79 */         Object ret = statement.eval(callstack, interpreter);
/*     */         
/*  81 */         if (ret instanceof ReturnControl)
/*     */         {
/*  83 */           switch (((ReturnControl)ret).kind) {
/*     */             
/*     */             case 46:
/*  86 */               returnControl = ret;
/*  87 */               breakout = true;
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 12:
/*  94 */               breakout = true;
/*     */               break;
/*     */           } 
/*     */         
/*     */         }
/*     */       } 
/* 100 */       if (breakout) {
/*     */         break;
/*     */       }
/*     */     } 
/* 104 */     callstack.swap(enclosingNameSpace);
/* 105 */     return returnControl;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHEnhancedForStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */