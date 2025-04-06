/*     */ package bsh.org.objectweb.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassWriter
/*     */   implements ClassVisitor
/*     */ {
/*     */   static final int CLASS = 7;
/*     */   static final int FIELD = 9;
/*     */   static final int METH = 10;
/*     */   static final int IMETH = 11;
/*     */   static final int STR = 8;
/*     */   static final int INT = 3;
/*     */   static final int FLOAT = 4;
/*     */   static final int LONG = 5;
/*     */   static final int DOUBLE = 6;
/*     */   static final int NAME_TYPE = 12;
/*     */   static final int UTF8 = 1;
/*     */   private short index;
/*     */   private ByteVector pool;
/*     */   private Item[] table;
/*     */   private int threshold;
/*     */   private int access;
/*     */   private int name;
/*     */   private int superName;
/*     */   private int interfaceCount;
/*     */   private int[] interfaces;
/*     */   private Item sourceFile;
/*     */   private int fieldCount;
/*     */   private ByteVector fields;
/*     */   private boolean computeMaxs;
/*     */   CodeWriter firstMethod;
/*     */   CodeWriter lastMethod;
/*     */   private int innerClassesCount;
/*     */   private ByteVector innerClasses;
/*     */   Item key;
/*     */   Item key2;
/*     */   Item key3;
/*     */   static final int NOARG_INSN = 0;
/*     */   static final int SBYTE_INSN = 1;
/*     */   static final int SHORT_INSN = 2;
/*     */   static final int VAR_INSN = 3;
/*     */   static final int IMPLVAR_INSN = 4;
/*     */   static final int TYPE_INSN = 5;
/*     */   static final int FIELDORMETH_INSN = 6;
/*     */   static final int ITFMETH_INSN = 7;
/*     */   static final int LABEL_INSN = 8;
/*     */   static final int LABELW_INSN = 9;
/*     */   static final int LDC_INSN = 10;
/*     */   static final int LDCW_INSN = 11;
/*     */   static final int IINC_INSN = 12;
/*     */   static final int TABL_INSN = 13;
/*     */   static final int LOOK_INSN = 14;
/*     */   static final int MANA_INSN = 15;
/*     */   static final int WIDE_INSN = 16;
/*     */   static byte[] TYPE;
/*     */   
/*     */   static {
/* 350 */     byte[] b = new byte[220];
/* 351 */     String s = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHAFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     for (int i = 0; i < b.length; i++) {
/* 357 */       b[i] = (byte)(s.charAt(i) - 65);
/*     */     }
/* 359 */     TYPE = b;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassWriter(boolean computeMaxs) {
/* 446 */     this.index = 1;
/* 447 */     this.pool = new ByteVector();
/* 448 */     this.table = new Item[64];
/* 449 */     this.threshold = (int)(0.75D * this.table.length);
/* 450 */     this.key = new Item();
/* 451 */     this.key2 = new Item();
/* 452 */     this.key3 = new Item();
/* 453 */     this.computeMaxs = computeMaxs;
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
/*     */   public void visit(int access, String name, String superName, String[] interfaces, String sourceFile) {
/* 467 */     this.access = access;
/* 468 */     this.name = (newClass(name)).index;
/* 469 */     this.superName = (superName == null) ? 0 : (newClass(superName)).index;
/* 470 */     if (interfaces != null && interfaces.length > 0) {
/* 471 */       this.interfaceCount = interfaces.length;
/* 472 */       this.interfaces = new int[this.interfaceCount];
/* 473 */       for (int i = 0; i < this.interfaceCount; i++) {
/* 474 */         this.interfaces[i] = (newClass(interfaces[i])).index;
/*     */       }
/*     */     } 
/* 477 */     if (sourceFile != null) {
/* 478 */       newUTF8("SourceFile");
/* 479 */       this.sourceFile = newUTF8(sourceFile);
/*     */     } 
/* 481 */     if ((access & 0x20000) != 0) {
/* 482 */       newUTF8("Deprecated");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {
/* 492 */     if (this.innerClasses == null) {
/* 493 */       newUTF8("InnerClasses");
/* 494 */       this.innerClasses = new ByteVector();
/*     */     } 
/* 496 */     this.innerClassesCount++;
/* 497 */     this.innerClasses.put2((name == null) ? 0 : (newClass(name)).index);
/* 498 */     this.innerClasses.put2((outerName == null) ? 0 : (newClass(outerName)).index);
/* 499 */     this.innerClasses.put2((innerName == null) ? 0 : (newUTF8(innerName)).index);
/* 500 */     this.innerClasses.put2(access);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitField(int access, String name, String desc, Object value) {
/* 509 */     this.fieldCount++;
/* 510 */     if (this.fields == null) {
/* 511 */       this.fields = new ByteVector();
/*     */     }
/* 513 */     this.fields.put2(access).put2((newUTF8(name)).index).put2((newUTF8(desc)).index);
/* 514 */     int attributeCount = 0;
/* 515 */     if (value != null) {
/* 516 */       attributeCount++;
/*     */     }
/* 518 */     if ((access & 0x10000) != 0) {
/* 519 */       attributeCount++;
/*     */     }
/* 521 */     if ((access & 0x20000) != 0) {
/* 522 */       attributeCount++;
/*     */     }
/* 524 */     this.fields.put2(attributeCount);
/* 525 */     if (value != null) {
/* 526 */       this.fields.put2((newUTF8("ConstantValue")).index);
/* 527 */       this.fields.put4(2).put2((newCst(value)).index);
/*     */     } 
/* 529 */     if ((access & 0x10000) != 0) {
/* 530 */       this.fields.put2((newUTF8("Synthetic")).index).put4(0);
/*     */     }
/* 532 */     if ((access & 0x20000) != 0) {
/* 533 */       this.fields.put2((newUTF8("Deprecated")).index).put4(0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeVisitor visitMethod(int access, String name, String desc, String[] exceptions) {
/* 543 */     CodeWriter cw = new CodeWriter(this, this.computeMaxs);
/* 544 */     cw.init(access, name, desc, exceptions);
/* 545 */     return cw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 563 */     int size = 24 + 2 * this.interfaceCount;
/* 564 */     if (this.fields != null) {
/* 565 */       size += this.fields.length;
/*     */     }
/* 567 */     int nbMethods = 0;
/* 568 */     CodeWriter cb = this.firstMethod;
/* 569 */     while (cb != null) {
/* 570 */       nbMethods++;
/* 571 */       size += cb.getSize();
/* 572 */       cb = cb.next;
/*     */     } 
/* 574 */     size += this.pool.length;
/* 575 */     int attributeCount = 0;
/* 576 */     if (this.sourceFile != null) {
/* 577 */       attributeCount++;
/* 578 */       size += 8;
/*     */     } 
/* 580 */     if ((this.access & 0x20000) != 0) {
/* 581 */       attributeCount++;
/* 582 */       size += 6;
/*     */     } 
/* 584 */     if (this.innerClasses != null) {
/* 585 */       attributeCount++;
/* 586 */       size += 8 + this.innerClasses.length;
/*     */     } 
/*     */ 
/*     */     
/* 590 */     ByteVector out = new ByteVector(size);
/* 591 */     out.put4(-889275714).put2(3).put2(45);
/* 592 */     out.put2(this.index).putByteArray(this.pool.data, 0, this.pool.length);
/* 593 */     out.put2(this.access).put2(this.name).put2(this.superName);
/* 594 */     out.put2(this.interfaceCount);
/* 595 */     for (int i = 0; i < this.interfaceCount; i++) {
/* 596 */       out.put2(this.interfaces[i]);
/*     */     }
/* 598 */     out.put2(this.fieldCount);
/* 599 */     if (this.fields != null) {
/* 600 */       out.putByteArray(this.fields.data, 0, this.fields.length);
/*     */     }
/* 602 */     out.put2(nbMethods);
/* 603 */     cb = this.firstMethod;
/* 604 */     while (cb != null) {
/* 605 */       cb.put(out);
/* 606 */       cb = cb.next;
/*     */     } 
/* 608 */     out.put2(attributeCount);
/* 609 */     if (this.sourceFile != null) {
/* 610 */       out.put2((newUTF8("SourceFile")).index).put4(2).put2(this.sourceFile.index);
/*     */     }
/* 612 */     if ((this.access & 0x20000) != 0) {
/* 613 */       out.put2((newUTF8("Deprecated")).index).put4(0);
/*     */     }
/* 615 */     if (this.innerClasses != null) {
/* 616 */       out.put2((newUTF8("InnerClasses")).index);
/* 617 */       out.put4(this.innerClasses.length + 2).put2(this.innerClassesCount);
/* 618 */       out.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
/*     */     } 
/* 620 */     return out.data;
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
/*     */   Item newCst(Object cst) {
/* 639 */     if (cst instanceof Integer) {
/* 640 */       int val = ((Integer)cst).intValue();
/* 641 */       return newInteger(val);
/* 642 */     }  if (cst instanceof Float) {
/* 643 */       float val = ((Float)cst).floatValue();
/* 644 */       return newFloat(val);
/* 645 */     }  if (cst instanceof Long) {
/* 646 */       long val = ((Long)cst).longValue();
/* 647 */       return newLong(val);
/* 648 */     }  if (cst instanceof Double) {
/* 649 */       double val = ((Double)cst).doubleValue();
/* 650 */       return newDouble(val);
/* 651 */     }  if (cst instanceof String) {
/* 652 */       return newString((String)cst);
/*     */     }
/* 654 */     throw new IllegalArgumentException("value " + cst);
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
/*     */   Item newUTF8(String value) {
/* 667 */     this.key.set(1, value, null, null);
/* 668 */     Item result = get(this.key);
/* 669 */     if (result == null) {
/* 670 */       this.pool.put1(1).putUTF(value);
/* 671 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key);
/* 672 */       put(result);
/*     */     } 
/* 674 */     return result;
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
/*     */   Item newClass(String value) {
/* 686 */     this.key2.set(7, value, null, null);
/* 687 */     Item result = get(this.key2);
/* 688 */     if (result == null) {
/* 689 */       this.pool.put12(7, (newUTF8(value)).index);
/* 690 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key2);
/* 691 */       put(result);
/*     */     } 
/* 693 */     return result;
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
/*     */   Item newField(String owner, String name, String desc) {
/* 711 */     this.key3.set(9, owner, name, desc);
/* 712 */     Item result = get(this.key3);
/* 713 */     if (result == null) {
/* 714 */       put122(9, (newClass(owner)).index, (newNameType(name, desc)).index);
/* 715 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key3);
/* 716 */       put(result);
/*     */     } 
/* 718 */     return result;
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
/*     */   Item newMethod(String owner, String name, String desc) {
/* 736 */     this.key3.set(10, owner, name, desc);
/* 737 */     Item result = get(this.key3);
/* 738 */     if (result == null) {
/* 739 */       put122(10, (newClass(owner)).index, (newNameType(name, desc)).index);
/* 740 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key3);
/* 741 */       put(result);
/*     */     } 
/* 743 */     return result;
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
/*     */   Item newItfMethod(String ownerItf, String name, String desc) {
/* 761 */     this.key3.set(11, ownerItf, name, desc);
/* 762 */     Item result = get(this.key3);
/* 763 */     if (result == null) {
/* 764 */       put122(11, (newClass(ownerItf)).index, (newNameType(name, desc)).index);
/* 765 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key3);
/* 766 */       put(result);
/*     */     } 
/* 768 */     return result;
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
/*     */   private Item newInteger(int value) {
/* 780 */     this.key.set(value);
/* 781 */     Item result = get(this.key);
/* 782 */     if (result == null) {
/* 783 */       this.pool.put1(3).put4(value);
/* 784 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key);
/* 785 */       put(result);
/*     */     } 
/* 787 */     return result;
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
/*     */   private Item newFloat(float value) {
/* 799 */     this.key.set(value);
/* 800 */     Item result = get(this.key);
/* 801 */     if (result == null) {
/* 802 */       this.pool.put1(4).put4(Float.floatToIntBits(value));
/* 803 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key);
/* 804 */       put(result);
/*     */     } 
/* 806 */     return result;
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
/*     */   private Item newLong(long value) {
/* 818 */     this.key.set(value);
/* 819 */     Item result = get(this.key);
/* 820 */     if (result == null) {
/* 821 */       this.pool.put1(5).put8(value);
/* 822 */       result = new Item(this.index, this.key);
/* 823 */       put(result);
/* 824 */       this.index = (short)(this.index + 2);
/*     */     } 
/* 826 */     return result;
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
/*     */   private Item newDouble(double value) {
/* 838 */     this.key.set(value);
/* 839 */     Item result = get(this.key);
/* 840 */     if (result == null) {
/* 841 */       this.pool.put1(6).put8(Double.doubleToLongBits(value));
/* 842 */       result = new Item(this.index, this.key);
/* 843 */       put(result);
/* 844 */       this.index = (short)(this.index + 2);
/*     */     } 
/* 846 */     return result;
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
/*     */   private Item newString(String value) {
/* 858 */     this.key2.set(8, value, null, null);
/* 859 */     Item result = get(this.key2);
/* 860 */     if (result == null) {
/* 861 */       this.pool.put12(8, (newUTF8(value)).index);
/* 862 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key2);
/* 863 */       put(result);
/*     */     } 
/* 865 */     return result;
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
/*     */   private Item newNameType(String name, String desc) {
/* 878 */     this.key2.set(12, name, desc, null);
/* 879 */     Item result = get(this.key2);
/* 880 */     if (result == null) {
/* 881 */       put122(12, (newUTF8(name)).index, (newUTF8(desc)).index);
/* 882 */       this.index = (short)(this.index + 1); result = new Item(this.index, this.key2);
/* 883 */       put(result);
/*     */     } 
/* 885 */     return result;
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
/*     */   private Item get(Item key) {
/* 898 */     Item[] tab = this.table;
/* 899 */     int hashCode = key.hashCode;
/* 900 */     int index = (hashCode & Integer.MAX_VALUE) % tab.length;
/* 901 */     for (Item i = tab[index]; i != null; i = i.next) {
/* 902 */       if (i.hashCode == hashCode && key.isEqualTo(i)) {
/* 903 */         return i;
/*     */       }
/*     */     } 
/* 906 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void put(Item i) {
/* 917 */     if (this.index > this.threshold) {
/* 918 */       int oldCapacity = this.table.length;
/* 919 */       Item[] oldMap = this.table;
/* 920 */       int newCapacity = oldCapacity * 2 + 1;
/* 921 */       Item[] newMap = new Item[newCapacity];
/* 922 */       this.threshold = (int)(newCapacity * 0.75D);
/* 923 */       this.table = newMap;
/* 924 */       for (int j = oldCapacity; j-- > 0;) {
/* 925 */         for (Item old = oldMap[j]; old != null; ) {
/* 926 */           Item e = old;
/* 927 */           old = old.next;
/* 928 */           int k = (e.hashCode & Integer.MAX_VALUE) % newCapacity;
/* 929 */           e.next = newMap[k];
/* 930 */           newMap[k] = e;
/*     */         } 
/*     */       } 
/*     */     } 
/* 934 */     int index = (i.hashCode & Integer.MAX_VALUE) % this.table.length;
/* 935 */     i.next = this.table[index];
/* 936 */     this.table[index] = i;
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
/*     */   private void put122(int b, int s1, int s2) {
/* 948 */     this.pool.put12(b, s1).put2(s2);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/org/objectweb/asm/ClassWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */