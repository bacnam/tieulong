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
/*     */ class BSHFormalParameters
/*     */   extends SimpleNode
/*     */ {
/*     */   private String[] paramNames;
/*     */   Class[] paramTypes;
/*     */   int numArgs;
/*     */   String[] typeDescriptors;
/*     */   
/*     */   BSHFormalParameters(int id) {
/*  47 */     super(id);
/*     */   }
/*     */   
/*     */   void insureParsed() {
/*  51 */     if (this.paramNames != null) {
/*     */       return;
/*     */     }
/*  54 */     this.numArgs = jjtGetNumChildren();
/*  55 */     String[] paramNames = new String[this.numArgs];
/*     */     
/*  57 */     for (int i = 0; i < this.numArgs; i++) {
/*     */       
/*  59 */       BSHFormalParameter param = (BSHFormalParameter)jjtGetChild(i);
/*  60 */       paramNames[i] = param.name;
/*     */     } 
/*     */     
/*  63 */     this.paramNames = paramNames;
/*     */   }
/*     */   
/*     */   public String[] getParamNames() {
/*  67 */     insureParsed();
/*  68 */     return this.paramNames;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getTypeDescriptors(CallStack callstack, Interpreter interpreter, String defaultPackage) {
/*  74 */     if (this.typeDescriptors != null) {
/*  75 */       return this.typeDescriptors;
/*     */     }
/*  77 */     insureParsed();
/*  78 */     String[] typeDesc = new String[this.numArgs];
/*     */     
/*  80 */     for (int i = 0; i < this.numArgs; i++) {
/*     */       
/*  82 */       BSHFormalParameter param = (BSHFormalParameter)jjtGetChild(i);
/*  83 */       typeDesc[i] = param.getTypeDescriptor(callstack, interpreter, defaultPackage);
/*     */     } 
/*     */ 
/*     */     
/*  87 */     this.typeDescriptors = typeDesc;
/*  88 */     return typeDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  98 */     if (this.paramTypes != null) {
/*  99 */       return this.paramTypes;
/*     */     }
/* 101 */     insureParsed();
/* 102 */     Class[] paramTypes = new Class[this.numArgs];
/*     */     
/* 104 */     for (int i = 0; i < this.numArgs; i++) {
/*     */       
/* 106 */       BSHFormalParameter param = (BSHFormalParameter)jjtGetChild(i);
/* 107 */       paramTypes[i] = (Class)param.eval(callstack, interpreter);
/*     */     } 
/*     */     
/* 110 */     this.paramTypes = paramTypes;
/*     */     
/* 112 */     return paramTypes;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHFormalParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */