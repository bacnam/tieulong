/*     */ package bsh;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BSHType
/*     */   extends SimpleNode
/*     */   implements BshClassManager.Listener
/*     */ {
/*     */   private Class baseType;
/*     */   private int arrayDims;
/*     */   private Class type;
/*     */   String descriptor;
/*     */   
/*     */   BSHType(int id) {
/*  62 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addArrayDimension() {
/*  70 */     this.arrayDims++;
/*     */   }
/*     */   
/*     */   SimpleNode getTypeNode() {
/*  74 */     return (SimpleNode)jjtGetChild(0);
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
/*     */   public String getTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
/*     */     String descriptor;
/*  88 */     if (this.descriptor != null) {
/*  89 */       return this.descriptor;
/*     */     }
/*     */ 
/*     */     
/*  93 */     SimpleNode node = getTypeNode();
/*  94 */     if (node instanceof BSHPrimitiveType) {
/*  95 */       descriptor = getTypeDescriptor(((BSHPrimitiveType)node).type);
/*     */     } else {
/*     */       
/*  98 */       String clasName = ((BSHAmbiguousName)node).text;
/*  99 */       BshClassManager bcm = interpreter.getClassManager();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       String definingClass = bcm.getClassBeingDefined(clasName);
/*     */       
/* 106 */       Class clas = null;
/* 107 */       if (definingClass == null) {
/*     */         
/*     */         try {
/* 110 */           clas = ((BSHAmbiguousName)node).toClass(callstack, interpreter);
/*     */         }
/* 112 */         catch (EvalError e) {}
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 118 */         clasName = definingClass;
/*     */       } 
/* 120 */       if (clas != null) {
/*     */ 
/*     */         
/* 123 */         descriptor = getTypeDescriptor(clas);
/*     */       
/*     */       }
/* 126 */       else if (defaultPackage == null || Name.isCompound(clasName)) {
/* 127 */         descriptor = "L" + clasName.replace('.', '/') + ";";
/*     */       } else {
/* 129 */         descriptor = "L" + defaultPackage.replace('.', '/') + "/" + clasName + ";";
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 134 */     for (int i = 0; i < this.arrayDims; i++) {
/* 135 */       descriptor = "[" + descriptor;
/*     */     }
/* 137 */     this.descriptor = descriptor;
/*     */     
/* 139 */     return descriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getType(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 146 */     if (this.type != null) {
/* 147 */       return this.type;
/*     */     }
/*     */     
/* 150 */     SimpleNode node = getTypeNode();
/* 151 */     if (node instanceof BSHPrimitiveType) {
/* 152 */       this.baseType = ((BSHPrimitiveType)node).getType();
/*     */     } else {
/* 154 */       this.baseType = ((BSHAmbiguousName)node).toClass(callstack, interpreter);
/*     */     } 
/*     */     
/* 157 */     if (this.arrayDims > 0) {
/*     */ 
/*     */       
/*     */       try {
/* 161 */         int[] dims = new int[this.arrayDims];
/* 162 */         Object obj = Array.newInstance(this.baseType, dims);
/* 163 */         this.type = obj.getClass();
/* 164 */       } catch (Exception e) {
/* 165 */         throw new EvalError("Couldn't construct array type", this, callstack);
/*     */       } 
/*     */     } else {
/*     */       
/* 169 */       this.type = this.baseType;
/*     */     } 
/*     */ 
/*     */     
/* 173 */     interpreter.getClassManager().addListener(this);
/*     */     
/* 175 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getBaseType() {
/* 184 */     return this.baseType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArrayDims() {
/* 191 */     return this.arrayDims;
/*     */   }
/*     */   
/*     */   public void classLoaderChanged() {
/* 195 */     this.type = null;
/* 196 */     this.baseType = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getTypeDescriptor(Class<boolean> clas) {
/* 201 */     if (clas == boolean.class) return "Z"; 
/* 202 */     if (clas == char.class) return "C"; 
/* 203 */     if (clas == byte.class) return "B"; 
/* 204 */     if (clas == short.class) return "S"; 
/* 205 */     if (clas == int.class) return "I"; 
/* 206 */     if (clas == long.class) return "J"; 
/* 207 */     if (clas == float.class) return "F"; 
/* 208 */     if (clas == double.class) return "D"; 
/* 209 */     if (clas == void.class) return "V";
/*     */     
/* 211 */     String name = clas.getName().replace('.', '/');
/*     */     
/* 213 */     if (name.startsWith("[") || name.endsWith(";")) {
/* 214 */       return name;
/*     */     }
/* 216 */     return "L" + name.replace('.', '/') + ";";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */