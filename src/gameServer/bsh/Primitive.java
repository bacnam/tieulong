/*      */ package bsh;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Primitive
/*      */   implements ParserConstants, Serializable
/*      */ {
/*   73 */   static Hashtable wrapperMap = new Hashtable<Object, Object>(); private Object value;
/*      */   static {
/*   75 */     wrapperMap.put(boolean.class, Boolean.class);
/*   76 */     wrapperMap.put(byte.class, Byte.class);
/*   77 */     wrapperMap.put(short.class, Short.class);
/*   78 */     wrapperMap.put(char.class, Character.class);
/*   79 */     wrapperMap.put(int.class, Integer.class);
/*   80 */     wrapperMap.put(long.class, Long.class);
/*   81 */     wrapperMap.put(float.class, Float.class);
/*   82 */     wrapperMap.put(double.class, Double.class);
/*   83 */     wrapperMap.put(Boolean.class, boolean.class);
/*   84 */     wrapperMap.put(Byte.class, byte.class);
/*   85 */     wrapperMap.put(Short.class, short.class);
/*   86 */     wrapperMap.put(Character.class, char.class);
/*   87 */     wrapperMap.put(Integer.class, int.class);
/*   88 */     wrapperMap.put(Long.class, long.class);
/*   89 */     wrapperMap.put(Float.class, float.class);
/*   90 */     wrapperMap.put(Double.class, double.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Special
/*      */     implements Serializable
/*      */   {
/*  100 */     public static final Special NULL_VALUE = new Special();
/*  101 */     public static final Special VOID_TYPE = new Special();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   public static final Primitive NULL = new Primitive(Special.NULL_VALUE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  115 */   public static final Primitive VOID = new Primitive(Special.VOID_TYPE);
/*      */ 
/*      */ 
/*      */   
/*      */   public Primitive(Object value) {
/*  120 */     if (value == null) {
/*  121 */       throw new InterpreterError("Use Primitve.NULL instead of Primitive(null)");
/*      */     }
/*      */     
/*  124 */     if (value != Special.NULL_VALUE && value != Special.VOID_TYPE && !isWrapperType(value.getClass()))
/*      */     {
/*      */ 
/*      */       
/*  128 */       throw new InterpreterError("Not a wrapper type: " + value.getClass());
/*      */     }
/*  130 */     this.value = value;
/*      */   }
/*      */   
/*  133 */   public Primitive(boolean value) { this(new Boolean(value)); }
/*  134 */   public Primitive(byte value) { this(new Byte(value)); }
/*  135 */   public Primitive(short value) { this(new Short(value)); }
/*  136 */   public Primitive(char value) { this(new Character(value)); }
/*  137 */   public Primitive(int value) { this(new Integer(value)); }
/*  138 */   public Primitive(long value) { this(new Long(value)); }
/*  139 */   public Primitive(float value) { this(new Float(value)); } public Primitive(double value) {
/*  140 */     this(new Double(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getValue() {
/*  147 */     if (this.value == Special.NULL_VALUE) {
/*  148 */       return null;
/*      */     }
/*  150 */     if (this.value == Special.VOID_TYPE) {
/*  151 */       throw new InterpreterError("attempt to unwrap void type");
/*      */     }
/*  153 */     return this.value;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  158 */     if (this.value == Special.NULL_VALUE)
/*  159 */       return "null"; 
/*  160 */     if (this.value == Special.VOID_TYPE) {
/*  161 */       return "void";
/*      */     }
/*  163 */     return this.value.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class getType() {
/*  173 */     if (this == VOID) {
/*  174 */       return void.class;
/*      */     }
/*      */ 
/*      */     
/*  178 */     if (this == NULL) {
/*  179 */       return null;
/*      */     }
/*  181 */     return unboxType(this.value.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object binaryOperation(Object obj1, Object obj2, int kind) throws UtilEvalError {
/*      */     Object result;
/*  196 */     if (obj1 == NULL || obj2 == NULL) {
/*  197 */       throw new UtilEvalError("Null value or 'null' literal in binary operation");
/*      */     }
/*  199 */     if (obj1 == VOID || obj2 == VOID) {
/*  200 */       throw new UtilEvalError("Undefined variable, class, or 'void' literal in binary operation");
/*      */     }
/*      */ 
/*      */     
/*  204 */     Class<?> lhsOrgType = obj1.getClass();
/*  205 */     Class<?> rhsOrgType = obj2.getClass();
/*      */ 
/*      */     
/*  208 */     if (obj1 instanceof Primitive)
/*  209 */       obj1 = ((Primitive)obj1).getValue(); 
/*  210 */     if (obj2 instanceof Primitive) {
/*  211 */       obj2 = ((Primitive)obj2).getValue();
/*      */     }
/*  213 */     Object[] operands = promotePrimitives(obj1, obj2);
/*  214 */     Object lhs = operands[0];
/*  215 */     Object rhs = operands[1];
/*      */     
/*  217 */     if (lhs.getClass() != rhs.getClass()) {
/*  218 */       throw new UtilEvalError("Type mismatch in operator.  " + lhs.getClass() + " cannot be used with " + rhs.getClass());
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  223 */       result = binaryOperationImpl(lhs, rhs, kind);
/*  224 */     } catch (ArithmeticException e) {
/*  225 */       throw new UtilTargetError("Arithemetic Exception in binary op", e);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  231 */     if ((lhsOrgType == Primitive.class && rhsOrgType == Primitive.class) || result instanceof Boolean)
/*      */     {
/*      */       
/*  234 */       return new Primitive(result);
/*      */     }
/*  236 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static Object binaryOperationImpl(Object lhs, Object rhs, int kind) throws UtilEvalError {
/*  242 */     if (lhs instanceof Boolean)
/*  243 */       return booleanBinaryOperation((Boolean)lhs, (Boolean)rhs, kind); 
/*  244 */     if (lhs instanceof Integer)
/*  245 */       return intBinaryOperation((Integer)lhs, (Integer)rhs, kind); 
/*  246 */     if (lhs instanceof Long)
/*  247 */       return longBinaryOperation((Long)lhs, (Long)rhs, kind); 
/*  248 */     if (lhs instanceof Float)
/*  249 */       return floatBinaryOperation((Float)lhs, (Float)rhs, kind); 
/*  250 */     if (lhs instanceof Double) {
/*  251 */       return doubleBinaryOperation((Double)lhs, (Double)rhs, kind);
/*      */     }
/*  253 */     throw new UtilEvalError("Invalid types in binary operator");
/*      */   }
/*      */ 
/*      */   
/*      */   static Boolean booleanBinaryOperation(Boolean B1, Boolean B2, int kind) {
/*  258 */     boolean lhs = B1.booleanValue();
/*  259 */     boolean rhs = B2.booleanValue();
/*      */     
/*  261 */     switch (kind) {
/*      */       
/*      */       case 90:
/*  264 */         return new Boolean((lhs == rhs));
/*      */       
/*      */       case 95:
/*  267 */         return new Boolean((lhs != rhs));
/*      */       
/*      */       case 96:
/*      */       case 97:
/*  271 */         return new Boolean((lhs || rhs));
/*      */       
/*      */       case 98:
/*      */       case 99:
/*  275 */         return new Boolean((lhs && rhs));
/*      */     } 
/*      */     
/*  278 */     throw new InterpreterError("unimplemented binary operator");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object longBinaryOperation(Long L1, Long L2, int kind) {
/*  285 */     long lhs = L1.longValue();
/*  286 */     long rhs = L2.longValue();
/*      */     
/*  288 */     switch (kind) {
/*      */ 
/*      */       
/*      */       case 84:
/*      */       case 85:
/*  293 */         return new Boolean((lhs < rhs));
/*      */       
/*      */       case 82:
/*      */       case 83:
/*  297 */         return new Boolean((lhs > rhs));
/*      */       
/*      */       case 90:
/*  300 */         return new Boolean((lhs == rhs));
/*      */       
/*      */       case 91:
/*      */       case 92:
/*  304 */         return new Boolean((lhs <= rhs));
/*      */       
/*      */       case 93:
/*      */       case 94:
/*  308 */         return new Boolean((lhs >= rhs));
/*      */       
/*      */       case 95:
/*  311 */         return new Boolean((lhs != rhs));
/*      */ 
/*      */       
/*      */       case 102:
/*  315 */         return new Long(lhs + rhs);
/*      */       
/*      */       case 103:
/*  318 */         return new Long(lhs - rhs);
/*      */       
/*      */       case 104:
/*  321 */         return new Long(lhs * rhs);
/*      */       
/*      */       case 105:
/*  324 */         return new Long(lhs / rhs);
/*      */       
/*      */       case 111:
/*  327 */         return new Long(lhs % rhs);
/*      */ 
/*      */       
/*      */       case 112:
/*      */       case 113:
/*  332 */         return new Long(lhs << (int)rhs);
/*      */       
/*      */       case 114:
/*      */       case 115:
/*  336 */         return new Long(lhs >> (int)rhs);
/*      */       
/*      */       case 116:
/*      */       case 117:
/*  340 */         return new Long(lhs >>> (int)rhs);
/*      */       
/*      */       case 106:
/*      */       case 107:
/*  344 */         return new Long(lhs & rhs);
/*      */       
/*      */       case 108:
/*      */       case 109:
/*  348 */         return new Long(lhs | rhs);
/*      */       
/*      */       case 110:
/*  351 */         return new Long(lhs ^ rhs);
/*      */     } 
/*      */     
/*  354 */     throw new InterpreterError("Unimplemented binary long operator");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object intBinaryOperation(Integer I1, Integer I2, int kind) {
/*  362 */     int lhs = I1.intValue();
/*  363 */     int rhs = I2.intValue();
/*      */     
/*  365 */     switch (kind) {
/*      */ 
/*      */       
/*      */       case 84:
/*      */       case 85:
/*  370 */         return new Boolean((lhs < rhs));
/*      */       
/*      */       case 82:
/*      */       case 83:
/*  374 */         return new Boolean((lhs > rhs));
/*      */       
/*      */       case 90:
/*  377 */         return new Boolean((lhs == rhs));
/*      */       
/*      */       case 91:
/*      */       case 92:
/*  381 */         return new Boolean((lhs <= rhs));
/*      */       
/*      */       case 93:
/*      */       case 94:
/*  385 */         return new Boolean((lhs >= rhs));
/*      */       
/*      */       case 95:
/*  388 */         return new Boolean((lhs != rhs));
/*      */ 
/*      */       
/*      */       case 102:
/*  392 */         return new Integer(lhs + rhs);
/*      */       
/*      */       case 103:
/*  395 */         return new Integer(lhs - rhs);
/*      */       
/*      */       case 104:
/*  398 */         return new Integer(lhs * rhs);
/*      */       
/*      */       case 105:
/*  401 */         return new Integer(lhs / rhs);
/*      */       
/*      */       case 111:
/*  404 */         return new Integer(lhs % rhs);
/*      */ 
/*      */       
/*      */       case 112:
/*      */       case 113:
/*  409 */         return new Integer(lhs << rhs);
/*      */       
/*      */       case 114:
/*      */       case 115:
/*  413 */         return new Integer(lhs >> rhs);
/*      */       
/*      */       case 116:
/*      */       case 117:
/*  417 */         return new Integer(lhs >>> rhs);
/*      */       
/*      */       case 106:
/*      */       case 107:
/*  421 */         return new Integer(lhs & rhs);
/*      */       
/*      */       case 108:
/*      */       case 109:
/*  425 */         return new Integer(lhs | rhs);
/*      */       
/*      */       case 110:
/*  428 */         return new Integer(lhs ^ rhs);
/*      */     } 
/*      */     
/*  431 */     throw new InterpreterError("Unimplemented binary integer operator");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object doubleBinaryOperation(Double D1, Double D2, int kind) throws UtilEvalError {
/*  440 */     double lhs = D1.doubleValue();
/*  441 */     double rhs = D2.doubleValue();
/*      */     
/*  443 */     switch (kind) {
/*      */ 
/*      */       
/*      */       case 84:
/*      */       case 85:
/*  448 */         return new Boolean((lhs < rhs));
/*      */       
/*      */       case 82:
/*      */       case 83:
/*  452 */         return new Boolean((lhs > rhs));
/*      */       
/*      */       case 90:
/*  455 */         return new Boolean((lhs == rhs));
/*      */       
/*      */       case 91:
/*      */       case 92:
/*  459 */         return new Boolean((lhs <= rhs));
/*      */       
/*      */       case 93:
/*      */       case 94:
/*  463 */         return new Boolean((lhs >= rhs));
/*      */       
/*      */       case 95:
/*  466 */         return new Boolean((lhs != rhs));
/*      */ 
/*      */       
/*      */       case 102:
/*  470 */         return new Double(lhs + rhs);
/*      */       
/*      */       case 103:
/*  473 */         return new Double(lhs - rhs);
/*      */       
/*      */       case 104:
/*  476 */         return new Double(lhs * rhs);
/*      */       
/*      */       case 105:
/*  479 */         return new Double(lhs / rhs);
/*      */       
/*      */       case 111:
/*  482 */         return new Double(lhs % rhs);
/*      */ 
/*      */       
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*  491 */         throw new UtilEvalError("Can't shift doubles");
/*      */     } 
/*      */     
/*  494 */     throw new InterpreterError("Unimplemented binary double operator");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object floatBinaryOperation(Float F1, Float F2, int kind) throws UtilEvalError {
/*  502 */     float lhs = F1.floatValue();
/*  503 */     float rhs = F2.floatValue();
/*      */     
/*  505 */     switch (kind) {
/*      */ 
/*      */       
/*      */       case 84:
/*      */       case 85:
/*  510 */         return new Boolean((lhs < rhs));
/*      */       
/*      */       case 82:
/*      */       case 83:
/*  514 */         return new Boolean((lhs > rhs));
/*      */       
/*      */       case 90:
/*  517 */         return new Boolean((lhs == rhs));
/*      */       
/*      */       case 91:
/*      */       case 92:
/*  521 */         return new Boolean((lhs <= rhs));
/*      */       
/*      */       case 93:
/*      */       case 94:
/*  525 */         return new Boolean((lhs >= rhs));
/*      */       
/*      */       case 95:
/*  528 */         return new Boolean((lhs != rhs));
/*      */ 
/*      */       
/*      */       case 102:
/*  532 */         return new Float(lhs + rhs);
/*      */       
/*      */       case 103:
/*  535 */         return new Float(lhs - rhs);
/*      */       
/*      */       case 104:
/*  538 */         return new Float(lhs * rhs);
/*      */       
/*      */       case 105:
/*  541 */         return new Float(lhs / rhs);
/*      */       
/*      */       case 111:
/*  544 */         return new Float(lhs % rhs);
/*      */ 
/*      */       
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*  553 */         throw new UtilEvalError("Can't shift floats ");
/*      */     } 
/*      */     
/*  556 */     throw new InterpreterError("Unimplemented binary float operator");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object promoteToInteger(Object wrapper) {
/*  566 */     if (wrapper instanceof Character)
/*  567 */       return new Integer(((Character)wrapper).charValue()); 
/*  568 */     if (wrapper instanceof Byte || wrapper instanceof Short) {
/*  569 */       return new Integer(((Number)wrapper).intValue());
/*      */     }
/*  571 */     return wrapper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[] promotePrimitives(Object lhs, Object rhs) {
/*  580 */     lhs = promoteToInteger(lhs);
/*  581 */     rhs = promoteToInteger(rhs);
/*      */     
/*  583 */     if (lhs instanceof Number && rhs instanceof Number) {
/*      */       
/*  585 */       Number lnum = (Number)lhs;
/*  586 */       Number rnum = (Number)rhs;
/*      */       
/*      */       boolean b;
/*      */       
/*  590 */       if ((b = lnum instanceof Double) || rnum instanceof Double) {
/*      */         
/*  592 */         if (b) {
/*  593 */           rhs = new Double(rnum.doubleValue());
/*      */         } else {
/*  595 */           lhs = new Double(lnum.doubleValue());
/*      */         } 
/*  597 */       } else if ((b = lnum instanceof Float) || rnum instanceof Float) {
/*      */         
/*  599 */         if (b) {
/*  600 */           rhs = new Float(rnum.floatValue());
/*      */         } else {
/*  602 */           lhs = new Float(lnum.floatValue());
/*      */         } 
/*  604 */       } else if ((b = lnum instanceof Long) || rnum instanceof Long) {
/*      */         
/*  606 */         if (b) {
/*  607 */           rhs = new Long(rnum.longValue());
/*      */         } else {
/*  609 */           lhs = new Long(lnum.longValue());
/*      */         } 
/*      */       } 
/*      */     } 
/*  613 */     return new Object[] { lhs, rhs };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Primitive unaryOperation(Primitive val, int kind) throws UtilEvalError {
/*  619 */     if (val == NULL) {
/*  620 */       throw new UtilEvalError("illegal use of null object or 'null' literal");
/*      */     }
/*  622 */     if (val == VOID) {
/*  623 */       throw new UtilEvalError("illegal use of undefined object or 'void' literal");
/*      */     }
/*      */     
/*  626 */     Class<byte> operandType = val.getType();
/*  627 */     Object operand = promoteToInteger(val.getValue());
/*      */     
/*  629 */     if (operand instanceof Boolean)
/*  630 */       return new Primitive(booleanUnaryOperation((Boolean)operand, kind)); 
/*  631 */     if (operand instanceof Integer) {
/*      */       
/*  633 */       int result = intUnaryOperation((Integer)operand, kind);
/*      */ 
/*      */       
/*  636 */       if (kind == 100 || kind == 101) {
/*      */         
/*  638 */         if (operandType == byte.class)
/*  639 */           return new Primitive((byte)result); 
/*  640 */         if (operandType == short.class)
/*  641 */           return new Primitive((short)result); 
/*  642 */         if (operandType == char.class) {
/*  643 */           return new Primitive((char)result);
/*      */         }
/*      */       } 
/*  646 */       return new Primitive(result);
/*      */     } 
/*  648 */     if (operand instanceof Long)
/*  649 */       return new Primitive(longUnaryOperation((Long)operand, kind)); 
/*  650 */     if (operand instanceof Float)
/*  651 */       return new Primitive(floatUnaryOperation((Float)operand, kind)); 
/*  652 */     if (operand instanceof Double) {
/*  653 */       return new Primitive(doubleUnaryOperation((Double)operand, kind));
/*      */     }
/*  655 */     throw new InterpreterError("An error occurred.  Please call technical support.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean booleanUnaryOperation(Boolean B, int kind) throws UtilEvalError {
/*  662 */     boolean operand = B.booleanValue();
/*  663 */     switch (kind) {
/*      */       
/*      */       case 86:
/*  666 */         return !operand;
/*      */     } 
/*  668 */     throw new UtilEvalError("Operator inappropriate for boolean");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int intUnaryOperation(Integer I, int kind) {
/*  674 */     int operand = I.intValue();
/*      */     
/*  676 */     switch (kind) {
/*      */       
/*      */       case 102:
/*  679 */         return operand;
/*      */       case 103:
/*  681 */         return -operand;
/*      */       case 87:
/*  683 */         return operand ^ 0xFFFFFFFF;
/*      */       case 100:
/*  685 */         return operand + 1;
/*      */       case 101:
/*  687 */         return operand - 1;
/*      */     } 
/*  689 */     throw new InterpreterError("bad integer unaryOperation");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static long longUnaryOperation(Long L, int kind) {
/*  695 */     long operand = L.longValue();
/*      */     
/*  697 */     switch (kind) {
/*      */       
/*      */       case 102:
/*  700 */         return operand;
/*      */       case 103:
/*  702 */         return -operand;
/*      */       case 87:
/*  704 */         return operand ^ 0xFFFFFFFFFFFFFFFFL;
/*      */       case 100:
/*  706 */         return operand + 1L;
/*      */       case 101:
/*  708 */         return operand - 1L;
/*      */     } 
/*  710 */     throw new InterpreterError("bad long unaryOperation");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static float floatUnaryOperation(Float F, int kind) {
/*  716 */     float operand = F.floatValue();
/*      */     
/*  718 */     switch (kind) {
/*      */       
/*      */       case 102:
/*  721 */         return operand;
/*      */       case 103:
/*  723 */         return -operand;
/*      */     } 
/*  725 */     throw new InterpreterError("bad float unaryOperation");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static double doubleUnaryOperation(Double D, int kind) {
/*  731 */     double operand = D.doubleValue();
/*      */     
/*  733 */     switch (kind) {
/*      */       
/*      */       case 102:
/*  736 */         return operand;
/*      */       case 103:
/*  738 */         return -operand;
/*      */     } 
/*  740 */     throw new InterpreterError("bad double unaryOperation");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int intValue() throws UtilEvalError {
/*  746 */     if (this.value instanceof Number) {
/*  747 */       return ((Number)this.value).intValue();
/*      */     }
/*  749 */     throw new UtilEvalError("Primitive not a number");
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean booleanValue() throws UtilEvalError {
/*  754 */     if (this.value instanceof Boolean) {
/*  755 */       return ((Boolean)this.value).booleanValue();
/*      */     }
/*  757 */     throw new UtilEvalError("Primitive not a boolean");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNumber() {
/*  765 */     return (!(this.value instanceof Boolean) && this != NULL && this != VOID);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Number numberValue() throws UtilEvalError {
/*  771 */     Object value = this.value;
/*      */ 
/*      */     
/*  774 */     if (value instanceof Character) {
/*  775 */       value = new Integer(((Character)value).charValue());
/*      */     }
/*  777 */     if (value instanceof Number) {
/*  778 */       return (Number)value;
/*      */     }
/*  780 */     throw new UtilEvalError("Primitive not a number");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  789 */     if (obj instanceof Primitive) {
/*  790 */       return ((Primitive)obj).value.equals(this.value);
/*      */     }
/*  792 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  801 */     return this.value.hashCode() * 21;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object unwrap(Object obj) {
/*  815 */     if (obj == VOID) {
/*  816 */       return null;
/*      */     }
/*      */     
/*  819 */     if (obj instanceof Primitive) {
/*  820 */       return ((Primitive)obj).getValue();
/*      */     }
/*  822 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object[] unwrap(Object[] args) {
/*  832 */     Object[] oa = new Object[args.length];
/*  833 */     for (int i = 0; i < args.length; i++)
/*  834 */       oa[i] = unwrap(args[i]); 
/*  835 */     return oa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object[] wrap(Object[] args, Class[] paramTypes) {
/*  842 */     if (args == null) {
/*  843 */       return null;
/*      */     }
/*  845 */     Object[] oa = new Object[args.length];
/*  846 */     for (int i = 0; i < args.length; i++)
/*  847 */       oa[i] = wrap(args[i], paramTypes[i]); 
/*  848 */     return oa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object wrap(Object value, Class<void> type) {
/*  862 */     if (type == void.class) {
/*  863 */       return VOID;
/*      */     }
/*  865 */     if (value == null) {
/*  866 */       return NULL;
/*      */     }
/*  868 */     if (type.isPrimitive() && isWrapperType(value.getClass())) {
/*  869 */       return new Primitive(value);
/*      */     }
/*  871 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Primitive getDefaultValue(Class<boolean> type) {
/*  880 */     if (type == null || !type.isPrimitive())
/*  881 */       return NULL; 
/*  882 */     if (type == boolean.class) {
/*  883 */       return new Primitive(false);
/*      */     }
/*      */     
/*      */     try {
/*  887 */       return (new Primitive(0)).castToType(type, 0);
/*  888 */     } catch (UtilEvalError e) {
/*  889 */       throw new InterpreterError("bad cast");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class boxType(Class primitiveType) {
/*  900 */     Class c = (Class)wrapperMap.get(primitiveType);
/*  901 */     if (c != null)
/*  902 */       return c; 
/*  903 */     throw new InterpreterError("Not a primitive type: " + primitiveType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class unboxType(Class wrapperType) {
/*  914 */     Class c = (Class)wrapperMap.get(wrapperType);
/*  915 */     if (c != null)
/*  916 */       return c; 
/*  917 */     throw new InterpreterError("Not a primitive wrapper type: " + wrapperType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Primitive castToType(Class toType, int operation) throws UtilEvalError {
/*  932 */     return castPrimitive(toType, getType(), this, false, operation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Primitive castPrimitive(Class<boolean> toType, Class<void> fromType, Primitive fromValue, boolean checkOnly, int operation) throws UtilEvalError {
/*  964 */     if (checkOnly && fromValue != null)
/*  965 */       throw new InterpreterError("bad cast param 1"); 
/*  966 */     if (!checkOnly && fromValue == null)
/*  967 */       throw new InterpreterError("bad cast param 2"); 
/*  968 */     if (fromType != null && !fromType.isPrimitive())
/*  969 */       throw new InterpreterError("bad fromType:" + fromType); 
/*  970 */     if (fromValue == NULL && fromType != null)
/*  971 */       throw new InterpreterError("inconsistent args 1"); 
/*  972 */     if (fromValue == VOID && fromType != void.class) {
/*  973 */       throw new InterpreterError("inconsistent args 2");
/*      */     }
/*      */     
/*  976 */     if (fromType == void.class) {
/*  977 */       if (checkOnly) {
/*  978 */         return Types.INVALID_CAST;
/*      */       }
/*  980 */       throw Types.castError(Reflect.normalizeClassName(toType), "void value", operation);
/*      */     } 
/*      */ 
/*      */     
/*  984 */     Object value = null;
/*  985 */     if (fromValue != null) {
/*  986 */       value = fromValue.getValue();
/*      */     }
/*  988 */     if (toType.isPrimitive()) {
/*      */ 
/*      */       
/*  991 */       if (fromType == null) {
/*  992 */         if (checkOnly) {
/*  993 */           return Types.INVALID_CAST;
/*      */         }
/*  995 */         throw Types.castError("primitive type:" + toType, "Null value", operation);
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1003 */       if (fromType == null) {
/* 1004 */         return checkOnly ? Types.VALID_CAST : NULL;
/*      */       }
/*      */       
/* 1007 */       if (checkOnly) {
/* 1008 */         return Types.INVALID_CAST;
/*      */       }
/* 1010 */       throw Types.castError("object type:" + toType, "primitive value", operation);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1015 */     if (fromType == boolean.class) {
/*      */       
/* 1017 */       if (toType != boolean.class) {
/* 1018 */         if (checkOnly) {
/* 1019 */           return Types.INVALID_CAST;
/*      */         }
/* 1021 */         throw Types.castError(toType, fromType, operation);
/*      */       } 
/* 1023 */       return checkOnly ? Types.VALID_CAST : fromValue;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1030 */     if (operation == 1 && !Types.isJavaAssignable(toType, fromType)) {
/*      */ 
/*      */       
/* 1033 */       if (checkOnly) {
/* 1034 */         return Types.INVALID_CAST;
/*      */       }
/* 1036 */       throw Types.castError(toType, fromType, operation);
/*      */     } 
/*      */     
/* 1039 */     return checkOnly ? Types.VALID_CAST : new Primitive(castWrapper(toType, value));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isWrapperType(Class type) {
/* 1045 */     return (wrapperMap.get(type) != null && !type.isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object castWrapper(Class<boolean> toType, Object value) {
/* 1059 */     if (!toType.isPrimitive())
/* 1060 */       throw new InterpreterError("invalid type in castWrapper: " + toType); 
/* 1061 */     if (value == null)
/* 1062 */       throw new InterpreterError("null value in castWrapper, guard"); 
/* 1063 */     if (value instanceof Boolean) {
/*      */       
/* 1065 */       if (toType != boolean.class) {
/* 1066 */         throw new InterpreterError("bad wrapper cast of boolean");
/*      */       }
/* 1068 */       return value;
/*      */     } 
/*      */ 
/*      */     
/* 1072 */     if (value instanceof Character) {
/* 1073 */       value = new Integer(((Character)value).charValue());
/*      */     }
/* 1075 */     if (!(value instanceof Number)) {
/* 1076 */       throw new InterpreterError("bad type in cast");
/*      */     }
/* 1078 */     Number number = (Number)value;
/*      */     
/* 1080 */     if (toType == byte.class)
/* 1081 */       return new Byte(number.byteValue()); 
/* 1082 */     if (toType == short.class)
/* 1083 */       return new Short(number.shortValue()); 
/* 1084 */     if (toType == char.class)
/* 1085 */       return new Character((char)number.intValue()); 
/* 1086 */     if (toType == int.class)
/* 1087 */       return new Integer(number.intValue()); 
/* 1088 */     if (toType == long.class)
/* 1089 */       return new Long(number.longValue()); 
/* 1090 */     if (toType == float.class)
/* 1091 */       return new Float(number.floatValue()); 
/* 1092 */     if (toType == double.class) {
/* 1093 */       return new Double(number.doubleValue());
/*      */     }
/* 1095 */     throw new InterpreterError("error in wrapper cast");
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Primitive.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */