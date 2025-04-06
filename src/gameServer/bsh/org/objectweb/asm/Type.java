/*     */ package bsh.org.objectweb.asm;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Type
/*     */ {
/*     */   public static final int VOID = 0;
/*     */   public static final int BOOLEAN = 1;
/*     */   public static final int CHAR = 2;
/*     */   public static final int BYTE = 3;
/*     */   public static final int SHORT = 4;
/*     */   public static final int INT = 5;
/*     */   public static final int FLOAT = 6;
/*     */   public static final int LONG = 7;
/*     */   public static final int DOUBLE = 8;
/*     */   public static final int ARRAY = 9;
/*     */   public static final int OBJECT = 10;
/* 106 */   public static final Type VOID_TYPE = new Type(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static final Type BOOLEAN_TYPE = new Type(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static final Type CHAR_TYPE = new Type(2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public static final Type BYTE_TYPE = new Type(3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public static final Type SHORT_TYPE = new Type(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public static final Type INT_TYPE = new Type(5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public static final Type FLOAT_TYPE = new Type(6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static final Type LONG_TYPE = new Type(7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final Type DOUBLE_TYPE = new Type(8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int sort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char[] buf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int off;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int len;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Type(int sort) {
/* 197 */     this.sort = sort;
/* 198 */     this.len = 1;
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
/*     */   private Type(int sort, char[] buf, int off, int len) {
/* 216 */     this.sort = sort;
/* 217 */     this.buf = buf;
/* 218 */     this.off = off;
/* 219 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(String typeDescriptor) {
/* 230 */     return getType(typeDescriptor.toCharArray(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(Class<int> c) {
/* 241 */     if (c.isPrimitive()) {
/* 242 */       if (c == int.class)
/* 243 */         return INT_TYPE; 
/* 244 */       if (c == void.class)
/* 245 */         return VOID_TYPE; 
/* 246 */       if (c == boolean.class)
/* 247 */         return BOOLEAN_TYPE; 
/* 248 */       if (c == byte.class)
/* 249 */         return BYTE_TYPE; 
/* 250 */       if (c == char.class)
/* 251 */         return CHAR_TYPE; 
/* 252 */       if (c == short.class)
/* 253 */         return SHORT_TYPE; 
/* 254 */       if (c == double.class)
/* 255 */         return DOUBLE_TYPE; 
/* 256 */       if (c == float.class) {
/* 257 */         return FLOAT_TYPE;
/*     */       }
/* 259 */       return LONG_TYPE;
/*     */     } 
/*     */     
/* 262 */     return getType(getDescriptor(c));
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
/*     */   public static Type[] getArgumentTypes(String methodDescriptor) {
/* 276 */     char[] buf = methodDescriptor.toCharArray();
/* 277 */     int off = 1;
/* 278 */     int size = 0;
/*     */     while (true) {
/* 280 */       char car = buf[off++];
/* 281 */       if (car == ')')
/*     */         break; 
/* 283 */       if (car == 'L') {
/* 284 */         while (buf[off++] != ';');
/*     */         
/* 286 */         size++; continue;
/* 287 */       }  if (car != '[') {
/* 288 */         size++;
/*     */       }
/*     */     } 
/* 291 */     Type[] args = new Type[size];
/* 292 */     off = 1;
/* 293 */     size = 0;
/* 294 */     while (buf[off] != ')') {
/* 295 */       args[size] = getType(buf, off);
/* 296 */       off += (args[size]).len;
/* 297 */       size++;
/*     */     } 
/* 299 */     return args;
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
/*     */   public static Type[] getArgumentTypes(Method method) {
/* 312 */     Class[] classes = method.getParameterTypes();
/* 313 */     Type[] types = new Type[classes.length];
/* 314 */     for (int i = classes.length - 1; i >= 0; i--) {
/* 315 */       types[i] = getType(classes[i]);
/*     */     }
/* 317 */     return types;
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
/*     */   public static Type getReturnType(String methodDescriptor) {
/* 330 */     char[] buf = methodDescriptor.toCharArray();
/* 331 */     return getType(buf, methodDescriptor.indexOf(')') + 1);
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
/*     */   public static Type getReturnType(Method method) {
/* 344 */     return getType(method.getReturnType());
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
/*     */   private static Type getType(char[] buf, int off) {
/* 357 */     switch (buf[off]) { case 'V':
/* 358 */         return VOID_TYPE;
/* 359 */       case 'Z': return BOOLEAN_TYPE;
/* 360 */       case 'C': return CHAR_TYPE;
/* 361 */       case 'B': return BYTE_TYPE;
/* 362 */       case 'S': return SHORT_TYPE;
/* 363 */       case 'I': return INT_TYPE;
/* 364 */       case 'F': return FLOAT_TYPE;
/* 365 */       case 'J': return LONG_TYPE;
/* 366 */       case 'D': return DOUBLE_TYPE;
/*     */       case '[':
/* 368 */         len = 1;
/* 369 */         while (buf[off + len] == '[') {
/* 370 */           len++;
/*     */         }
/* 372 */         if (buf[off + len] == 'L') {
/* 373 */           len++;
/* 374 */           while (buf[off + len] != ';') {
/* 375 */             len++;
/*     */           }
/*     */         } 
/* 378 */         return new Type(9, buf, off, len + 1); }
/*     */ 
/*     */     
/* 381 */     int len = 1;
/* 382 */     while (buf[off + len] != ';') {
/* 383 */       len++;
/*     */     }
/* 385 */     return new Type(10, buf, off, len + 1);
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
/*     */   public int getSort() {
/* 403 */     return this.sort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimensions() {
/* 414 */     int i = 1;
/* 415 */     while (this.buf[this.off + i] == '[') {
/* 416 */       i++;
/*     */     }
/* 418 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getElementType() {
/* 429 */     return getType(this.buf, this.off + getDimensions());
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
/*     */   public String getClassName() {
/* 441 */     return (new String(this.buf, this.off + 1, this.len - 2)).replace('/', '.');
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
/*     */   public String getInternalName() {
/* 453 */     return new String(this.buf, this.off + 1, this.len - 2);
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
/*     */   public String getDescriptor() {
/* 467 */     StringBuffer buf = new StringBuffer();
/* 468 */     getDescriptor(buf);
/* 469 */     return buf.toString();
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
/*     */   public static String getMethodDescriptor(Type returnType, Type[] argumentTypes) {
/* 486 */     StringBuffer buf = new StringBuffer();
/* 487 */     buf.append('(');
/* 488 */     for (int i = 0; i < argumentTypes.length; i++) {
/* 489 */       argumentTypes[i].getDescriptor(buf);
/*     */     }
/* 491 */     buf.append(')');
/* 492 */     returnType.getDescriptor(buf);
/* 493 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getDescriptor(StringBuffer buf) {
/* 504 */     switch (this.sort) { case 0:
/* 505 */         buf.append('V'); return;
/* 506 */       case 1: buf.append('Z'); return;
/* 507 */       case 2: buf.append('C'); return;
/* 508 */       case 3: buf.append('B'); return;
/* 509 */       case 4: buf.append('S'); return;
/* 510 */       case 5: buf.append('I'); return;
/* 511 */       case 6: buf.append('F'); return;
/* 512 */       case 7: buf.append('J'); return;
/* 513 */       case 8: buf.append('D');
/*     */         return; }
/*     */     
/* 516 */     buf.append(this.buf, this.off, this.len);
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
/*     */   public static String getInternalName(Class c) {
/* 534 */     return c.getName().replace('.', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDescriptor(Class c) {
/* 545 */     StringBuffer buf = new StringBuffer();
/* 546 */     getDescriptor(buf, c);
/* 547 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMethodDescriptor(Method m) {
/* 558 */     Class[] parameters = m.getParameterTypes();
/* 559 */     StringBuffer buf = new StringBuffer();
/* 560 */     buf.append('(');
/* 561 */     for (int i = 0; i < parameters.length; i++) {
/* 562 */       getDescriptor(buf, parameters[i]);
/*     */     }
/* 564 */     buf.append(')');
/* 565 */     getDescriptor(buf, m.getReturnType());
/* 566 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getDescriptor(StringBuffer buf, Class<int> c) {
/* 577 */     Class<int> d = c;
/*     */     while (true) {
/* 579 */       if (d.isPrimitive()) {
/*     */         char car;
/* 581 */         if (d == int.class) {
/* 582 */           car = 'I';
/* 583 */         } else if (d == void.class) {
/* 584 */           car = 'V';
/* 585 */         } else if (d == boolean.class) {
/* 586 */           car = 'Z';
/* 587 */         } else if (d == byte.class) {
/* 588 */           car = 'B';
/* 589 */         } else if (d == char.class) {
/* 590 */           car = 'C';
/* 591 */         } else if (d == short.class) {
/* 592 */           car = 'S';
/* 593 */         } else if (d == double.class) {
/* 594 */           car = 'D';
/* 595 */         } else if (d == float.class) {
/* 596 */           car = 'F';
/*     */         } else {
/* 598 */           car = 'J';
/*     */         } 
/* 600 */         buf.append(car); return;
/*     */       } 
/* 602 */       if (d.isArray()) {
/* 603 */         buf.append('[');
/* 604 */         d = (Class)d.getComponentType(); continue;
/*     */       }  break;
/* 606 */     }  buf.append('L');
/* 607 */     String name = d.getName();
/* 608 */     int len = name.length();
/* 609 */     for (int i = 0; i < len; i++) {
/* 610 */       char car = name.charAt(i);
/* 611 */       buf.append((car == '.') ? 47 : car);
/*     */     } 
/* 613 */     buf.append(';');
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
/*     */   public int getSize() {
/* 631 */     return (this.sort == 7 || this.sort == 8) ? 2 : 1;
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
/*     */   public int getOpcode(int opcode) {
/* 646 */     if (opcode == 46 || opcode == 79) {
/* 647 */       switch (this.sort) {
/*     */         case 0:
/* 649 */           return opcode + 5;
/*     */         case 1:
/*     */         case 3:
/* 652 */           return opcode + 6;
/*     */         case 2:
/* 654 */           return opcode + 7;
/*     */         case 4:
/* 656 */           return opcode + 8;
/*     */         case 5:
/* 658 */           return opcode;
/*     */         case 6:
/* 660 */           return opcode + 2;
/*     */         case 7:
/* 662 */           return opcode + 1;
/*     */         case 8:
/* 664 */           return opcode + 3;
/*     */       } 
/*     */ 
/*     */       
/* 668 */       return opcode + 4;
/*     */     } 
/*     */     
/* 671 */     switch (this.sort) {
/*     */       case 0:
/* 673 */         return opcode + 5;
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 679 */         return opcode;
/*     */       case 6:
/* 681 */         return opcode + 2;
/*     */       case 7:
/* 683 */         return opcode + 1;
/*     */       case 8:
/* 685 */         return opcode + 3;
/*     */     } 
/*     */ 
/*     */     
/* 689 */     return opcode + 4;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/org/objectweb/asm/Type.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */