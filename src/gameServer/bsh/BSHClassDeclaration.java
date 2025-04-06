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
/*     */ class BSHClassDeclaration
/*     */   extends SimpleNode
/*     */ {
/*     */   static final String CLASSINITNAME = "_bshClassInit";
/*     */   String name;
/*     */   Modifiers modifiers;
/*     */   int numInterfaces;
/*     */   boolean extend;
/*     */   boolean isInterface;
/*     */   
/*     */   BSHClassDeclaration(int id) {
/*  56 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*     */     BSHBlock block;
/*  63 */     int child = 0;
/*     */ 
/*     */     
/*  66 */     Class superClass = null;
/*  67 */     if (this.extend) {
/*     */       
/*  69 */       BSHAmbiguousName superNode = (BSHAmbiguousName)jjtGetChild(child++);
/*  70 */       superClass = superNode.toClass(callstack, interpreter);
/*     */     } 
/*     */ 
/*     */     
/*  74 */     Class[] interfaces = new Class[this.numInterfaces];
/*  75 */     for (int i = 0; i < this.numInterfaces; i++) {
/*  76 */       BSHAmbiguousName node = (BSHAmbiguousName)jjtGetChild(child++);
/*  77 */       interfaces[i] = node.toClass(callstack, interpreter);
/*  78 */       if (!interfaces[i].isInterface()) {
/*  79 */         throw new EvalError("Type: " + node.text + " is not an interface!", this, callstack);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     if (child < jjtGetNumChildren()) {
/*  87 */       block = (BSHBlock)jjtGetChild(child);
/*     */     } else {
/*  89 */       block = new BSHBlock(25);
/*     */     } 
/*     */     try {
/*  92 */       return ClassGenerator.getClassGenerator().generateClass(this.name, this.modifiers, interfaces, superClass, block, this.isInterface, callstack, interpreter);
/*     */     
/*     */     }
/*  95 */     catch (UtilEvalError e) {
/*  96 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return "ClassDeclaration: " + this.name;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHClassDeclaration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */