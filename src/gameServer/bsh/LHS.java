/*     */ package bsh;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LHS
/*     */   implements ParserConstants, Serializable
/*     */ {
/*     */   NameSpace nameSpace;
/*     */   boolean localVar;
/*     */   static final int VARIABLE = 0;
/*     */   static final int FIELD = 1;
/*     */   static final int PROPERTY = 2;
/*     */   static final int INDEX = 3;
/*     */   static final int METHOD_EVAL = 4;
/*     */   int type;
/*     */   String varName;
/*     */   String propName;
/*     */   Field field;
/*     */   Object object;
/*     */   int index;
/*     */   
/*     */   LHS(NameSpace nameSpace, String varName, boolean localVar) {
/*  85 */     this.type = 0;
/*  86 */     this.localVar = localVar;
/*  87 */     this.varName = varName;
/*  88 */     this.nameSpace = nameSpace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LHS(Field field) {
/*  97 */     this.type = 1;
/*  98 */     this.object = null;
/*  99 */     this.field = field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LHS(Object object, Field field) {
/* 107 */     if (object == null) {
/* 108 */       throw new NullPointerException("constructed empty LHS");
/*     */     }
/* 110 */     this.type = 1;
/* 111 */     this.object = object;
/* 112 */     this.field = field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LHS(Object object, String propName) {
/* 120 */     if (object == null) {
/* 121 */       throw new NullPointerException("constructed empty LHS");
/*     */     }
/* 123 */     this.type = 2;
/* 124 */     this.object = object;
/* 125 */     this.propName = propName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LHS(Object array, int index) {
/* 133 */     if (array == null) {
/* 134 */       throw new NullPointerException("constructed empty LHS");
/*     */     }
/* 136 */     this.type = 3;
/* 137 */     this.object = array;
/* 138 */     this.index = index;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue() throws UtilEvalError {
/* 143 */     if (this.type == 0) {
/* 144 */       return this.nameSpace.getVariable(this.varName);
/*     */     }
/* 146 */     if (this.type == 1) {
/*     */       try {
/* 148 */         Object o = this.field.get(this.object);
/* 149 */         return Primitive.wrap(o, this.field.getType());
/* 150 */       } catch (IllegalAccessException e2) {
/* 151 */         throw new UtilEvalError("Can't read field: " + this.field);
/*     */       } 
/*     */     }
/* 154 */     if (this.type == 2) {
/*     */ 
/*     */ 
/*     */       
/* 158 */       CollectionManager cm = CollectionManager.getCollectionManager();
/* 159 */       if (cm.isMap(this.object)) {
/* 160 */         return cm.getFromMap(this.object, this.propName);
/*     */       }
/*     */       try {
/* 163 */         return Reflect.getObjectProperty(this.object, this.propName);
/* 164 */       } catch (ReflectError e) {
/* 165 */         Interpreter.debug(e.getMessage());
/* 166 */         throw new UtilEvalError("No such property: " + this.propName);
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     if (this.type == 3) {
/*     */       try {
/* 172 */         return Reflect.getIndex(this.object, this.index);
/*     */       }
/* 174 */       catch (Exception e) {
/* 175 */         throw new UtilEvalError("Array access: " + e);
/*     */       } 
/*     */     }
/* 178 */     throw new InterpreterError("LHS type");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object assign(Object val, boolean strictJava) throws UtilEvalError {
/* 187 */     if (this.type == 0)
/*     */     
/*     */     { 
/* 190 */       if (this.localVar) {
/* 191 */         this.nameSpace.setLocalVariable(this.varName, val, strictJava);
/*     */       } else {
/* 193 */         this.nameSpace.setVariable(this.varName, val, strictJava);
/*     */       }  }
/* 195 */     else { if (this.type == 1) {
/*     */         
/*     */         try {
/*     */           
/* 199 */           ReflectManager.RMSetAccessible(this.field);
/* 200 */           this.field.set(this.object, Primitive.unwrap(val));
/* 201 */           return val;
/*     */         }
/* 203 */         catch (NullPointerException e) {
/* 204 */           throw new UtilEvalError("LHS (" + this.field.getName() + ") not a static field.");
/*     */         
/*     */         }
/* 207 */         catch (IllegalAccessException e2) {
/* 208 */           throw new UtilEvalError("LHS (" + this.field.getName() + ") can't access field: " + e2);
/*     */         
/*     */         }
/* 211 */         catch (IllegalArgumentException e3) {
/*     */           
/* 213 */           String type = (val instanceof Primitive) ? ((Primitive)val).getType().getName() : val.getClass().getName();
/*     */ 
/*     */           
/* 216 */           throw new UtilEvalError("Argument type mismatch. " + ((val == null) ? "null" : type) + " not assignable to field " + this.field.getName());
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 222 */       if (this.type == 2) {
/*     */         
/* 224 */         CollectionManager cm = CollectionManager.getCollectionManager();
/* 225 */         if (cm.isMap(this.object)) {
/* 226 */           cm.putInMap(this.object, this.propName, Primitive.unwrap(val));
/*     */         } else {
/*     */           try {
/* 229 */             Reflect.setObjectProperty(this.object, this.propName, val);
/*     */           }
/* 231 */           catch (ReflectError e) {
/* 232 */             Interpreter.debug("Assignment: " + e.getMessage());
/* 233 */             throw new UtilEvalError("No such property: " + this.propName);
/*     */           } 
/*     */         } 
/* 236 */       } else if (this.type == 3) {
/*     */         try {
/* 238 */           Reflect.setIndex(this.object, this.index, val);
/* 239 */         } catch (UtilTargetError e1) {
/* 240 */           throw e1;
/* 241 */         } catch (Exception e) {
/* 242 */           throw new UtilEvalError("Assignment: " + e.getMessage());
/*     */         } 
/*     */       } else {
/* 245 */         throw new InterpreterError("unknown lhs");
/*     */       }  }
/* 247 */      return val;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 251 */     return "LHS: " + ((this.field != null) ? ("field = " + this.field.toString()) : "") + ((this.varName != null) ? (" varName = " + this.varName) : "") + ((this.nameSpace != null) ? (" nameSpace = " + this.nameSpace.toString()) : "");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/LHS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */