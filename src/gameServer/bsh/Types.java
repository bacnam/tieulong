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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Types
/*     */ {
/*     */   static final int CAST = 0;
/*     */   static final int ASSIGNMENT = 1;
/*     */   static final int JAVA_BASE_ASSIGNABLE = 1;
/*     */   static final int JAVA_BOX_TYPES_ASSIGABLE = 2;
/*     */   static final int JAVA_VARARGS_ASSIGNABLE = 3;
/*     */   static final int BSH_ASSIGNABLE = 4;
/*     */   static final int FIRST_ROUND_ASSIGNABLE = 1;
/*     */   static final int LAST_ROUND_ASSIGNABLE = 4;
/*  69 */   static Primitive VALID_CAST = new Primitive(1);
/*  70 */   static Primitive INVALID_CAST = new Primitive(-1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class[] getTypes(Object[] args) {
/*  77 */     if (args == null) {
/*  78 */       return new Class[0];
/*     */     }
/*  80 */     Class[] types = new Class[args.length];
/*     */     
/*  82 */     for (int i = 0; i < args.length; i++) {
/*     */       
/*  84 */       if (args[i] == null) {
/*  85 */         types[i] = null;
/*     */       }
/*  87 */       else if (args[i] instanceof Primitive) {
/*  88 */         types[i] = ((Primitive)args[i]).getType();
/*     */       } else {
/*  90 */         types[i] = args[i].getClass();
/*     */       } 
/*     */     } 
/*  93 */     return types;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isSignatureAssignable(Class[] from, Class[] to, int round) {
/*     */     int i;
/* 105 */     if (round != 3 && from.length != to.length) {
/* 106 */       return false;
/*     */     }
/* 108 */     switch (round) {
/*     */       
/*     */       case 1:
/* 111 */         for (i = 0; i < from.length; i++) {
/* 112 */           if (!isJavaBaseAssignable(to[i], from[i]))
/* 113 */             return false; 
/* 114 */         }  return true;
/*     */       case 2:
/* 116 */         for (i = 0; i < from.length; i++) {
/* 117 */           if (!isJavaBoxTypesAssignable(to[i], from[i]))
/* 118 */             return false; 
/* 119 */         }  return true;
/*     */       case 3:
/* 121 */         return isSignatureVarargsAssignable(from, to);
/*     */       case 4:
/* 123 */         for (i = 0; i < from.length; i++) {
/* 124 */           if (!isBshAssignable(to[i], from[i]))
/* 125 */             return false; 
/* 126 */         }  return true;
/*     */     } 
/* 128 */     throw new InterpreterError("bad case");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSignatureVarargsAssignable(Class[] from, Class[] to) {
/* 135 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isJavaAssignable(Class lhsType, Class rhsType) {
/* 164 */     return (isJavaBaseAssignable(lhsType, rhsType) || isJavaBoxTypesAssignable(lhsType, rhsType));
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
/*     */   static boolean isJavaBaseAssignable(Class<short> lhsType, Class<byte> rhsType) {
/* 182 */     if (lhsType == null) {
/* 183 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 187 */     if (rhsType == null) {
/* 188 */       return !lhsType.isPrimitive();
/*     */     }
/* 190 */     if (lhsType.isPrimitive() && rhsType.isPrimitive()) {
/*     */       
/* 192 */       if (lhsType == rhsType) {
/* 193 */         return true;
/*     */       }
/*     */       
/* 196 */       if (rhsType == byte.class && (lhsType == short.class || lhsType == int.class || lhsType == long.class || lhsType == float.class || lhsType == double.class))
/*     */       {
/*     */ 
/*     */         
/* 200 */         return true;
/*     */       }
/* 202 */       if (rhsType == short.class && (lhsType == int.class || lhsType == long.class || lhsType == float.class || lhsType == double.class))
/*     */       {
/*     */         
/* 205 */         return true;
/*     */       }
/* 207 */       if (rhsType == char.class && (lhsType == int.class || lhsType == long.class || lhsType == float.class || lhsType == double.class))
/*     */       {
/*     */         
/* 210 */         return true;
/*     */       }
/* 212 */       if (rhsType == int.class && (lhsType == long.class || lhsType == float.class || lhsType == double.class))
/*     */       {
/*     */         
/* 215 */         return true;
/*     */       }
/* 217 */       if (rhsType == long.class && (lhsType == float.class || lhsType == double.class))
/*     */       {
/* 219 */         return true;
/*     */       }
/* 221 */       if (rhsType == float.class && lhsType == double.class) {
/* 222 */         return true;
/*     */       }
/*     */     }
/* 225 */     else if (lhsType.isAssignableFrom(rhsType)) {
/* 226 */       return true;
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isJavaBoxTypesAssignable(Class<Object> lhsType, Class<char> rhsType) {
/* 238 */     if (lhsType == null) {
/* 239 */       return false;
/*     */     }
/*     */     
/* 242 */     if (lhsType == Object.class) {
/* 243 */       return true;
/*     */     }
/*     */     
/* 246 */     if (lhsType == Number.class && rhsType != char.class && rhsType != boolean.class)
/*     */     {
/*     */ 
/*     */       
/* 250 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     if (Primitive.wrapperMap.get(lhsType) == rhsType) {
/* 257 */       return true;
/*     */     }
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isBshAssignable(Class toType, Class fromType) {
/*     */     try {
/* 269 */       return (castObject(toType, fromType, null, 1, true) == VALID_CAST);
/*     */ 
/*     */     
/*     */     }
/* 273 */     catch (UtilEvalError e) {
/*     */       
/* 275 */       throw new InterpreterError("err in cast check: " + e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object castObject(Object fromValue, Class toType, int operation) throws UtilEvalError {
/* 302 */     if (fromValue == null) {
/* 303 */       throw new InterpreterError("null fromValue");
/*     */     }
/* 305 */     Class fromType = (fromValue instanceof Primitive) ? ((Primitive)fromValue).getType() : fromValue.getClass();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     return castObject(toType, fromType, fromValue, operation, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object castObject(Class<void> toType, Class<Primitive> fromType, Object fromValue, int operation, boolean checkOnly) throws UtilEvalError {
/* 385 */     if (checkOnly && fromValue != null)
/* 386 */       throw new InterpreterError("bad cast params 1"); 
/* 387 */     if (!checkOnly && fromValue == null)
/* 388 */       throw new InterpreterError("bad cast params 2"); 
/* 389 */     if (fromType == Primitive.class)
/* 390 */       throw new InterpreterError("bad from Type, need to unwrap"); 
/* 391 */     if (fromValue == Primitive.NULL && fromType != null)
/* 392 */       throw new InterpreterError("inconsistent args 1"); 
/* 393 */     if (fromValue == Primitive.VOID && fromType != void.class)
/* 394 */       throw new InterpreterError("inconsistent args 2"); 
/* 395 */     if (toType == void.class) {
/* 396 */       throw new InterpreterError("loose toType should be null");
/*     */     }
/*     */     
/* 399 */     if (toType == null || toType == fromType) {
/* 400 */       return checkOnly ? VALID_CAST : fromValue;
/*     */     }
/*     */ 
/*     */     
/* 404 */     if (toType.isPrimitive()) {
/*     */       
/* 406 */       if (fromType == void.class || fromType == null || fromType.isPrimitive())
/*     */       {
/*     */ 
/*     */         
/* 410 */         return Primitive.castPrimitive(toType, fromType, (Primitive)fromValue, checkOnly, operation);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 415 */       if (Primitive.isWrapperType(fromType)) {
/*     */         Primitive primFromValue;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 421 */         Class unboxedFromType = Primitive.unboxType(fromType);
/*     */         
/* 423 */         if (checkOnly) {
/* 424 */           primFromValue = null;
/*     */         } else {
/* 426 */           primFromValue = (Primitive)Primitive.wrap(fromValue, unboxedFromType);
/*     */         } 
/*     */         
/* 429 */         return Primitive.castPrimitive(toType, unboxedFromType, primFromValue, checkOnly, operation);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 435 */       if (checkOnly) {
/* 436 */         return INVALID_CAST;
/*     */       }
/* 438 */       throw castError(toType, fromType, operation);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 446 */     if (fromType == void.class || fromType == null || fromType.isPrimitive()) {
/*     */ 
/*     */ 
/*     */       
/* 450 */       if (Primitive.isWrapperType(toType) && fromType != void.class && fromType != null)
/*     */       {
/*     */ 
/*     */         
/* 454 */         return checkOnly ? VALID_CAST : Primitive.castWrapper(Primitive.unboxType(toType), ((Primitive)fromValue).getValue());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 461 */       if (toType == Object.class && fromType != void.class && fromType != null)
/*     */       {
/*     */ 
/*     */         
/* 465 */         return checkOnly ? VALID_CAST : ((Primitive)fromValue).getValue();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 472 */       return Primitive.castPrimitive(toType, fromType, (Primitive)fromValue, checkOnly, operation);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 479 */     if (toType.isAssignableFrom(fromType)) {
/* 480 */       return checkOnly ? VALID_CAST : fromValue;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 485 */     if (toType.isInterface() && This.class.isAssignableFrom(fromType) && Capabilities.canGenerateInterfaces())
/*     */     {
/*     */ 
/*     */       
/* 489 */       return checkOnly ? VALID_CAST : ((This)fromValue).getInterface(toType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 494 */     if (Primitive.isWrapperType(toType) && Primitive.isWrapperType(fromType))
/*     */     {
/*     */       
/* 497 */       return checkOnly ? VALID_CAST : Primitive.castWrapper(toType, fromValue);
/*     */     }
/*     */     
/* 500 */     if (checkOnly) {
/* 501 */       return INVALID_CAST;
/*     */     }
/* 503 */     throw castError(toType, fromType, operation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static UtilEvalError castError(Class lhsType, Class rhsType, int operation) {
/* 513 */     return castError(Reflect.normalizeClassName(lhsType), Reflect.normalizeClassName(rhsType), operation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static UtilEvalError castError(String lhs, String rhs, int operation) {
/* 521 */     if (operation == 1) {
/* 522 */       return new UtilEvalError("Can't assign " + rhs + " to " + lhs);
/*     */     }
/*     */     
/* 525 */     Exception cce = new ClassCastException("Cannot cast " + rhs + " to " + lhs);
/*     */     
/* 527 */     return new UtilTargetError(cce);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Types.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */