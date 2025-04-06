/*      */ package bsh.org.objectweb.asm;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CodeWriter
/*      */   implements CodeVisitor
/*      */ {
/*      */   static final boolean CHECK = false;
/*      */   CodeWriter next;
/*      */   private ClassWriter cw;
/*      */   private Item name;
/*      */   private Item desc;
/*      */   private int access;
/*      */   private int maxStack;
/*      */   private int maxLocals;
/*   87 */   private ByteVector code = new ByteVector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int catchCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector catchTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int exceptionCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] exceptions;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int localVarCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector localVar;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lineNumberCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector lineNumber;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean resize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean computeMaxs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int stackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxStackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label currentBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label blockStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int[] SIZE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Edge head;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Edge tail;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Edge pool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  253 */     int[] b = new int[202];
/*  254 */     String s = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
/*      */ 
/*      */ 
/*      */     
/*  258 */     for (int i = 0; i < b.length; i++) {
/*  259 */       b[i] = s.charAt(i) - 69;
/*      */     }
/*  261 */     SIZE = b;
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
/*      */ 
/*      */ 
/*      */   
/*      */   protected CodeWriter(ClassWriter cw, boolean computeMaxs) {
/*  491 */     if (cw.firstMethod == null) {
/*  492 */       cw.firstMethod = this;
/*  493 */       cw.lastMethod = this;
/*      */     } else {
/*  495 */       cw.lastMethod.next = this;
/*  496 */       cw.lastMethod = this;
/*      */     } 
/*  498 */     this.cw = cw;
/*  499 */     this.computeMaxs = computeMaxs;
/*  500 */     if (computeMaxs) {
/*      */       
/*  502 */       this.currentBlock = new Label();
/*  503 */       this.currentBlock.pushed = true;
/*  504 */       this.blockStack = this.currentBlock;
/*      */     } 
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
/*      */   protected void init(int access, String name, String desc, String[] exceptions) {
/*  524 */     this.access = access;
/*  525 */     this.name = this.cw.newUTF8(name);
/*  526 */     this.desc = this.cw.newUTF8(desc);
/*  527 */     if (exceptions != null && exceptions.length > 0) {
/*  528 */       this.exceptionCount = exceptions.length;
/*  529 */       this.exceptions = new int[this.exceptionCount];
/*  530 */       for (int i = 0; i < this.exceptionCount; i++) {
/*  531 */         this.exceptions[i] = (this.cw.newClass(exceptions[i])).index;
/*      */       }
/*      */     } 
/*  534 */     if (this.computeMaxs) {
/*      */       
/*  536 */       int size = getArgumentsAndReturnSizes(desc) >> 2;
/*  537 */       if ((access & 0x8) != 0) {
/*  538 */         size--;
/*      */       }
/*  540 */       if (size > this.maxLocals) {
/*  541 */         this.maxLocals = size;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInsn(int opcode) {
/*  551 */     if (this.computeMaxs) {
/*      */       
/*  553 */       int size = this.stackSize + SIZE[opcode];
/*  554 */       if (size > this.maxStackSize) {
/*  555 */         this.maxStackSize = size;
/*      */       }
/*  557 */       this.stackSize = size;
/*      */       
/*  559 */       if ((opcode >= 172 && opcode <= 177) || opcode == 191)
/*      */       {
/*      */         
/*  562 */         if (this.currentBlock != null) {
/*  563 */           this.currentBlock.maxStackSize = this.maxStackSize;
/*  564 */           this.currentBlock = null;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  569 */     this.code.put1(opcode);
/*      */   }
/*      */   
/*      */   public void visitIntInsn(int opcode, int operand) {
/*  573 */     if (this.computeMaxs && opcode != 188) {
/*      */ 
/*      */       
/*  576 */       int size = this.stackSize + 1;
/*  577 */       if (size > this.maxStackSize) {
/*  578 */         this.maxStackSize = size;
/*      */       }
/*  580 */       this.stackSize = size;
/*      */     } 
/*      */     
/*  583 */     if (opcode == 17) {
/*  584 */       this.code.put12(opcode, operand);
/*      */     } else {
/*  586 */       this.code.put11(opcode, operand);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitVarInsn(int opcode, int var) {
/*  591 */     if (this.computeMaxs) {
/*      */       int n;
/*  593 */       if (opcode == 169) {
/*      */         
/*  595 */         if (this.currentBlock != null) {
/*  596 */           this.currentBlock.maxStackSize = this.maxStackSize;
/*  597 */           this.currentBlock = null;
/*      */         } 
/*      */       } else {
/*  600 */         int size = this.stackSize + SIZE[opcode];
/*  601 */         if (size > this.maxStackSize) {
/*  602 */           this.maxStackSize = size;
/*      */         }
/*  604 */         this.stackSize = size;
/*      */       } 
/*      */ 
/*      */       
/*  608 */       if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57) {
/*      */ 
/*      */         
/*  611 */         n = var + 2;
/*      */       } else {
/*  613 */         n = var + 1;
/*      */       } 
/*  615 */       if (n > this.maxLocals) {
/*  616 */         this.maxLocals = n;
/*      */       }
/*      */     } 
/*      */     
/*  620 */     if (var < 4 && opcode != 169) {
/*      */       int opt;
/*  622 */       if (opcode < 54) {
/*  623 */         opt = 26 + (opcode - 21 << 2) + var;
/*      */       } else {
/*  625 */         opt = 59 + (opcode - 54 << 2) + var;
/*      */       } 
/*  627 */       this.code.put1(opt);
/*  628 */     } else if (var >= 256) {
/*  629 */       this.code.put1(196).put12(opcode, var);
/*      */     } else {
/*  631 */       this.code.put11(opcode, var);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeInsn(int opcode, String desc) {
/*  636 */     if (this.computeMaxs && opcode == 187) {
/*      */ 
/*      */       
/*  639 */       int size = this.stackSize + 1;
/*  640 */       if (size > this.maxStackSize) {
/*  641 */         this.maxStackSize = size;
/*      */       }
/*  643 */       this.stackSize = size;
/*      */     } 
/*      */     
/*  646 */     this.code.put12(opcode, (this.cw.newClass(desc)).index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/*  655 */     if (this.computeMaxs) {
/*      */       int size;
/*      */       
/*  658 */       char c = desc.charAt(0);
/*  659 */       switch (opcode) {
/*      */         case 178:
/*  661 */           size = this.stackSize + ((c == 'D' || c == 'J') ? 2 : 1);
/*      */           break;
/*      */         case 179:
/*  664 */           size = this.stackSize + ((c == 'D' || c == 'J') ? -2 : -1);
/*      */           break;
/*      */         case 180:
/*  667 */           size = this.stackSize + ((c == 'D' || c == 'J') ? 1 : 0);
/*      */           break;
/*      */         
/*      */         default:
/*  671 */           size = this.stackSize + ((c == 'D' || c == 'J') ? -3 : -2);
/*      */           break;
/*      */       } 
/*      */       
/*  675 */       if (size > this.maxStackSize) {
/*  676 */         this.maxStackSize = size;
/*      */       }
/*  678 */       this.stackSize = size;
/*      */     } 
/*      */     
/*  681 */     this.code.put12(opcode, (this.cw.newField(owner, name, desc)).index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/*      */     Item i;
/*  691 */     if (opcode == 185) {
/*  692 */       i = this.cw.newItfMethod(owner, name, desc);
/*      */     } else {
/*  694 */       i = this.cw.newMethod(owner, name, desc);
/*      */     } 
/*  696 */     int argSize = i.intVal;
/*  697 */     if (this.computeMaxs) {
/*      */       int size;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  703 */       if (argSize == 0) {
/*      */         
/*  705 */         argSize = getArgumentsAndReturnSizes(desc);
/*      */         
/*  707 */         i.intVal = argSize;
/*      */       } 
/*      */       
/*  710 */       if (opcode == 184) {
/*  711 */         size = this.stackSize - (argSize >> 2) + (argSize & 0x3) + 1;
/*      */       } else {
/*  713 */         size = this.stackSize - (argSize >> 2) + (argSize & 0x3);
/*      */       } 
/*      */       
/*  716 */       if (size > this.maxStackSize) {
/*  717 */         this.maxStackSize = size;
/*      */       }
/*  719 */       this.stackSize = size;
/*      */     } 
/*      */     
/*  722 */     if (opcode == 185) {
/*  723 */       if (!this.computeMaxs && 
/*  724 */         argSize == 0) {
/*  725 */         argSize = getArgumentsAndReturnSizes(desc);
/*  726 */         i.intVal = argSize;
/*      */       } 
/*      */       
/*  729 */       this.code.put12(185, i.index).put11(argSize >> 2, 0);
/*      */     } else {
/*  731 */       this.code.put12(opcode, i.index);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitJumpInsn(int opcode, Label label) {
/*  743 */     if (this.computeMaxs) {
/*  744 */       if (opcode == 167) {
/*      */         
/*  746 */         if (this.currentBlock != null) {
/*  747 */           this.currentBlock.maxStackSize = this.maxStackSize;
/*  748 */           addSuccessor(this.stackSize, label);
/*  749 */           this.currentBlock = null;
/*      */         } 
/*  751 */       } else if (opcode == 168) {
/*  752 */         if (this.currentBlock != null) {
/*  753 */           addSuccessor(this.stackSize + 1, label);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  758 */         this.stackSize += SIZE[opcode];
/*  759 */         if (this.currentBlock != null) {
/*  760 */           addSuccessor(this.stackSize, label);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  765 */     if (label.resolved && label.position - this.code.length < -32768) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  771 */       if (opcode == 167) {
/*  772 */         this.code.put1(200);
/*  773 */       } else if (opcode == 168) {
/*  774 */         this.code.put1(201);
/*      */       } else {
/*  776 */         this.code.put1((opcode <= 166) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1));
/*  777 */         this.code.put2(8);
/*  778 */         this.code.put1(200);
/*      */       } 
/*  780 */       label.put(this, this.code, this.code.length - 1, true);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  785 */       this.code.put1(opcode);
/*  786 */       label.put(this, this.code, this.code.length - 1, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLabel(Label label) {
/*  798 */     if (this.computeMaxs) {
/*  799 */       if (this.currentBlock != null) {
/*      */         
/*  801 */         this.currentBlock.maxStackSize = this.maxStackSize;
/*  802 */         addSuccessor(this.stackSize, label);
/*      */       } 
/*      */ 
/*      */       
/*  806 */       this.currentBlock = label;
/*  807 */       this.stackSize = 0;
/*  808 */       this.maxStackSize = 0;
/*      */     } 
/*      */     
/*  811 */     this.resize |= label.resolve(this, this.code.length, this.code.data);
/*      */   }
/*      */   
/*      */   public void visitLdcInsn(Object cst) {
/*  815 */     Item i = this.cw.newCst(cst);
/*  816 */     if (this.computeMaxs) {
/*      */       int size;
/*      */       
/*  819 */       if (i.type == 5 || i.type == 6) {
/*  820 */         size = this.stackSize + 2;
/*      */       } else {
/*  822 */         size = this.stackSize + 1;
/*      */       } 
/*      */       
/*  825 */       if (size > this.maxStackSize) {
/*  826 */         this.maxStackSize = size;
/*      */       }
/*  828 */       this.stackSize = size;
/*      */     } 
/*      */     
/*  831 */     int index = i.index;
/*  832 */     if (i.type == 5 || i.type == 6) {
/*  833 */       this.code.put12(20, index);
/*  834 */     } else if (index >= 256) {
/*  835 */       this.code.put12(19, index);
/*      */     } else {
/*  837 */       this.code.put11(18, index);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitIincInsn(int var, int increment) {
/*  842 */     if (this.computeMaxs) {
/*      */       
/*  844 */       int n = var + 1;
/*  845 */       if (n > this.maxLocals) {
/*  846 */         this.maxLocals = n;
/*      */       }
/*      */     } 
/*      */     
/*  850 */     if (var > 255 || increment > 127 || increment < -128) {
/*  851 */       this.code.put1(196).put12(132, var).put2(increment);
/*      */     } else {
/*  853 */       this.code.put1(132).put11(var, increment);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
/*  863 */     if (this.computeMaxs) {
/*      */       
/*  865 */       this.stackSize--;
/*      */       
/*  867 */       if (this.currentBlock != null) {
/*  868 */         this.currentBlock.maxStackSize = this.maxStackSize;
/*  869 */         addSuccessor(this.stackSize, dflt);
/*  870 */         for (int j = 0; j < labels.length; j++) {
/*  871 */           addSuccessor(this.stackSize, labels[j]);
/*      */         }
/*  873 */         this.currentBlock = null;
/*      */       } 
/*      */     } 
/*      */     
/*  877 */     int source = this.code.length;
/*  878 */     this.code.put1(170);
/*  879 */     while (this.code.length % 4 != 0) {
/*  880 */       this.code.put1(0);
/*      */     }
/*  882 */     dflt.put(this, this.code, source, true);
/*  883 */     this.code.put4(min).put4(max);
/*  884 */     for (int i = 0; i < labels.length; i++) {
/*  885 */       labels[i].put(this, this.code, source, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/*  894 */     if (this.computeMaxs) {
/*      */       
/*  896 */       this.stackSize--;
/*      */       
/*  898 */       if (this.currentBlock != null) {
/*  899 */         this.currentBlock.maxStackSize = this.maxStackSize;
/*  900 */         addSuccessor(this.stackSize, dflt);
/*  901 */         for (int j = 0; j < labels.length; j++) {
/*  902 */           addSuccessor(this.stackSize, labels[j]);
/*      */         }
/*  904 */         this.currentBlock = null;
/*      */       } 
/*      */     } 
/*      */     
/*  908 */     int source = this.code.length;
/*  909 */     this.code.put1(171);
/*  910 */     while (this.code.length % 4 != 0) {
/*  911 */       this.code.put1(0);
/*      */     }
/*  913 */     dflt.put(this, this.code, source, true);
/*  914 */     this.code.put4(labels.length);
/*  915 */     for (int i = 0; i < labels.length; i++) {
/*  916 */       this.code.put4(keys[i]);
/*  917 */       labels[i].put(this, this.code, source, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitMultiANewArrayInsn(String desc, int dims) {
/*  922 */     if (this.computeMaxs)
/*      */     {
/*      */       
/*  925 */       this.stackSize += 1 - dims;
/*      */     }
/*      */     
/*  928 */     Item classItem = this.cw.newClass(desc);
/*  929 */     this.code.put12(197, classItem.index).put1(dims);
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
/*      */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/*  946 */     if (this.computeMaxs)
/*      */     {
/*  948 */       if (!handler.pushed) {
/*  949 */         handler.beginStackSize = 1;
/*  950 */         handler.pushed = true;
/*  951 */         handler.next = this.blockStack;
/*  952 */         this.blockStack = handler;
/*      */       } 
/*      */     }
/*  955 */     this.catchCount++;
/*  956 */     if (this.catchTable == null) {
/*  957 */       this.catchTable = new ByteVector();
/*      */     }
/*  959 */     this.catchTable.put2(start.position);
/*  960 */     this.catchTable.put2(end.position);
/*  961 */     this.catchTable.put2(handler.position);
/*  962 */     this.catchTable.put2((type != null) ? (this.cw.newClass(type)).index : 0);
/*      */   }
/*      */   
/*      */   public void visitMaxs(int maxStack, int maxLocals) {
/*  966 */     if (this.computeMaxs) {
/*      */       
/*  968 */       int max = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  976 */       Label stack = this.blockStack;
/*  977 */       while (stack != null) {
/*      */         
/*  979 */         Label l = stack;
/*  980 */         stack = stack.next;
/*      */         
/*  982 */         int start = l.beginStackSize;
/*  983 */         int blockMax = start + l.maxStackSize;
/*      */         
/*  985 */         if (blockMax > max) {
/*  986 */           max = blockMax;
/*      */         }
/*      */         
/*  989 */         Edge b = l.successors;
/*  990 */         while (b != null) {
/*  991 */           l = b.successor;
/*      */           
/*  993 */           if (!l.pushed) {
/*      */             
/*  995 */             l.beginStackSize = start + b.stackSize;
/*      */             
/*  997 */             l.pushed = true;
/*  998 */             l.next = stack;
/*  999 */             stack = l;
/*      */           } 
/* 1001 */           b = b.next;
/*      */         } 
/*      */       } 
/* 1004 */       this.maxStack = max;
/*      */       
/* 1006 */       synchronized (SIZE) {
/*      */         
/* 1008 */         if (this.tail != null) {
/* 1009 */           this.tail.poolNext = pool;
/* 1010 */           pool = this.head;
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1014 */       this.maxStack = maxStack;
/* 1015 */       this.maxLocals = maxLocals;
/*      */     } 
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
/*      */   public void visitLocalVariable(String name, String desc, Label start, Label end, int index) {
/* 1034 */     if (this.localVar == null) {
/* 1035 */       this.cw.newUTF8("LocalVariableTable");
/* 1036 */       this.localVar = new ByteVector();
/*      */     } 
/* 1038 */     this.localVarCount++;
/* 1039 */     this.localVar.put2(start.position);
/* 1040 */     this.localVar.put2(end.position - start.position);
/* 1041 */     this.localVar.put2((this.cw.newUTF8(name)).index);
/* 1042 */     this.localVar.put2((this.cw.newUTF8(desc)).index);
/* 1043 */     this.localVar.put2(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLineNumber(int line, Label start) {
/* 1052 */     if (this.lineNumber == null) {
/* 1053 */       this.cw.newUTF8("LineNumberTable");
/* 1054 */       this.lineNumber = new ByteVector();
/*      */     } 
/* 1056 */     this.lineNumberCount++;
/* 1057 */     this.lineNumber.put2(start.position);
/* 1058 */     this.lineNumber.put2(line);
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
/*      */   private static int getArgumentsAndReturnSizes(String desc) {
/* 1077 */     int n = 1;
/* 1078 */     int c = 1;
/*      */     while (true) {
/* 1080 */       char car = desc.charAt(c++);
/* 1081 */       if (car == ')') {
/* 1082 */         car = desc.charAt(c);
/* 1083 */         return n << 2 | ((car == 'V') ? 0 : ((car == 'D' || car == 'J') ? 2 : 1));
/* 1084 */       }  if (car == 'L') {
/* 1085 */         while (desc.charAt(c++) != ';');
/*      */         
/* 1087 */         n++; continue;
/* 1088 */       }  if (car == '[') {
/* 1089 */         while ((car = desc.charAt(c)) == '[') {
/* 1090 */           c++;
/*      */         }
/* 1092 */         if (car == 'D' || car == 'J')
/* 1093 */           n--;  continue;
/*      */       } 
/* 1095 */       if (car == 'D' || car == 'J') {
/* 1096 */         n += 2; continue;
/*      */       } 
/* 1098 */       n++;
/*      */     } 
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
/*      */   private void addSuccessor(int stackSize, Label successor) {
/*      */     Edge b;
/* 1113 */     synchronized (SIZE) {
/* 1114 */       if (pool == null) {
/* 1115 */         b = new Edge();
/*      */       } else {
/* 1117 */         b = pool;
/*      */         
/* 1119 */         pool = pool.poolNext;
/*      */       } 
/*      */     } 
/*      */     
/* 1123 */     if (this.tail == null) {
/* 1124 */       this.tail = b;
/*      */     }
/* 1126 */     b.poolNext = this.head;
/* 1127 */     this.head = b;
/*      */     
/* 1129 */     b.stackSize = stackSize;
/* 1130 */     b.successor = successor;
/*      */     
/* 1132 */     b.next = this.currentBlock.successors;
/* 1133 */     this.currentBlock.successors = b;
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
/*      */   final int getSize() {
/* 1147 */     if (this.resize)
/*      */     {
/* 1149 */       resizeInstructions(new int[0], new int[0], 0);
/*      */     }
/* 1151 */     int size = 8;
/* 1152 */     if (this.code.length > 0) {
/* 1153 */       this.cw.newUTF8("Code");
/* 1154 */       size += 18 + this.code.length + 8 * this.catchCount;
/* 1155 */       if (this.localVar != null) {
/* 1156 */         size += 8 + this.localVar.length;
/*      */       }
/* 1158 */       if (this.lineNumber != null) {
/* 1159 */         size += 8 + this.lineNumber.length;
/*      */       }
/*      */     } 
/* 1162 */     if (this.exceptionCount > 0) {
/* 1163 */       this.cw.newUTF8("Exceptions");
/* 1164 */       size += 8 + 2 * this.exceptionCount;
/*      */     } 
/* 1166 */     if ((this.access & 0x10000) != 0) {
/* 1167 */       this.cw.newUTF8("Synthetic");
/* 1168 */       size += 6;
/*      */     } 
/* 1170 */     if ((this.access & 0x20000) != 0) {
/* 1171 */       this.cw.newUTF8("Deprecated");
/* 1172 */       size += 6;
/*      */     } 
/* 1174 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void put(ByteVector out) {
/* 1185 */     out.put2(this.access).put2(this.name.index).put2(this.desc.index);
/* 1186 */     int attributeCount = 0;
/* 1187 */     if (this.code.length > 0) {
/* 1188 */       attributeCount++;
/*      */     }
/* 1190 */     if (this.exceptionCount > 0) {
/* 1191 */       attributeCount++;
/*      */     }
/* 1193 */     if ((this.access & 0x10000) != 0) {
/* 1194 */       attributeCount++;
/*      */     }
/* 1196 */     if ((this.access & 0x20000) != 0) {
/* 1197 */       attributeCount++;
/*      */     }
/* 1199 */     out.put2(attributeCount);
/* 1200 */     if (this.code.length > 0) {
/* 1201 */       int size = 12 + this.code.length + 8 * this.catchCount;
/* 1202 */       if (this.localVar != null) {
/* 1203 */         size += 8 + this.localVar.length;
/*      */       }
/* 1205 */       if (this.lineNumber != null) {
/* 1206 */         size += 8 + this.lineNumber.length;
/*      */       }
/* 1208 */       out.put2((this.cw.newUTF8("Code")).index).put4(size);
/* 1209 */       out.put2(this.maxStack).put2(this.maxLocals);
/* 1210 */       out.put4(this.code.length).putByteArray(this.code.data, 0, this.code.length);
/* 1211 */       out.put2(this.catchCount);
/* 1212 */       if (this.catchCount > 0) {
/* 1213 */         out.putByteArray(this.catchTable.data, 0, this.catchTable.length);
/*      */       }
/* 1215 */       attributeCount = 0;
/* 1216 */       if (this.localVar != null) {
/* 1217 */         attributeCount++;
/*      */       }
/* 1219 */       if (this.lineNumber != null) {
/* 1220 */         attributeCount++;
/*      */       }
/* 1222 */       out.put2(attributeCount);
/* 1223 */       if (this.localVar != null) {
/* 1224 */         out.put2((this.cw.newUTF8("LocalVariableTable")).index);
/* 1225 */         out.put4(this.localVar.length + 2).put2(this.localVarCount);
/* 1226 */         out.putByteArray(this.localVar.data, 0, this.localVar.length);
/*      */       } 
/* 1228 */       if (this.lineNumber != null) {
/* 1229 */         out.put2((this.cw.newUTF8("LineNumberTable")).index);
/* 1230 */         out.put4(this.lineNumber.length + 2).put2(this.lineNumberCount);
/* 1231 */         out.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
/*      */       } 
/*      */     } 
/* 1234 */     if (this.exceptionCount > 0) {
/* 1235 */       out.put2((this.cw.newUTF8("Exceptions")).index).put4(2 * this.exceptionCount + 2);
/* 1236 */       out.put2(this.exceptionCount);
/* 1237 */       for (int i = 0; i < this.exceptionCount; i++) {
/* 1238 */         out.put2(this.exceptions[i]);
/*      */       }
/*      */     } 
/* 1241 */     if ((this.access & 0x10000) != 0) {
/* 1242 */       out.put2((this.cw.newUTF8("Synthetic")).index).put4(0);
/*      */     }
/* 1244 */     if ((this.access & 0x20000) != 0) {
/* 1245 */       out.put2((this.cw.newUTF8("Deprecated")).index).put4(0);
/*      */     }
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
/*      */   protected int[] resizeInstructions(int[] indexes, int[] sizes, int len) {
/* 1291 */     byte[] b = this.code.data;
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
/* 1317 */     int[] allIndexes = new int[len];
/* 1318 */     int[] allSizes = new int[len];
/*      */ 
/*      */ 
/*      */     
/* 1322 */     System.arraycopy(indexes, 0, allIndexes, 0, len);
/* 1323 */     System.arraycopy(sizes, 0, allSizes, 0, len);
/* 1324 */     boolean[] resize = new boolean[this.code.length];
/*      */     
/* 1326 */     int state = 3;
/*      */     do {
/* 1328 */       if (state == 3) {
/* 1329 */         state = 2;
/*      */       }
/* 1331 */       int u = 0;
/* 1332 */       while (u < b.length) {
/* 1333 */         int label, newOffset, opcode = b[u] & 0xFF;
/* 1334 */         int insert = 0;
/*      */         
/* 1336 */         switch (ClassWriter.TYPE[opcode]) {
/*      */           case 0:
/*      */           case 4:
/* 1339 */             u++;
/*      */             break;
/*      */           case 8:
/* 1342 */             if (opcode > 201) {
/*      */ 
/*      */               
/* 1345 */               opcode = (opcode < 218) ? (opcode - 49) : (opcode - 20);
/* 1346 */               label = u + readUnsignedShort(b, u + 1);
/*      */             } else {
/* 1348 */               label = u + readShort(b, u + 1);
/*      */             } 
/* 1350 */             newOffset = getNewOffset(allIndexes, allSizes, u, label);
/* 1351 */             if ((newOffset < -32768 || newOffset > 32767) && 
/* 1352 */               !resize[u]) {
/* 1353 */               if (opcode == 167 || opcode == 168) {
/*      */ 
/*      */                 
/* 1356 */                 insert = 2;
/*      */ 
/*      */               
/*      */               }
/*      */               else {
/*      */ 
/*      */                 
/* 1363 */                 insert = 5;
/*      */               } 
/* 1365 */               resize[u] = true;
/*      */             } 
/*      */             
/* 1368 */             u += 3;
/*      */             break;
/*      */           case 9:
/* 1371 */             u += 5;
/*      */             break;
/*      */           case 13:
/* 1374 */             if (state == 1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1381 */               newOffset = getNewOffset(allIndexes, allSizes, 0, u);
/* 1382 */               insert = -(newOffset & 0x3);
/* 1383 */             } else if (!resize[u]) {
/*      */ 
/*      */ 
/*      */               
/* 1387 */               insert = u & 0x3;
/* 1388 */               resize[u] = true;
/*      */             } 
/*      */             
/* 1391 */             u = u + 4 - (u & 0x3);
/* 1392 */             u += 4 * (readInt(b, u + 8) - readInt(b, u + 4) + 1) + 12;
/*      */             break;
/*      */           case 14:
/* 1395 */             if (state == 1) {
/*      */               
/* 1397 */               newOffset = getNewOffset(allIndexes, allSizes, 0, u);
/* 1398 */               insert = -(newOffset & 0x3);
/* 1399 */             } else if (!resize[u]) {
/*      */               
/* 1401 */               insert = u & 0x3;
/* 1402 */               resize[u] = true;
/*      */             } 
/*      */             
/* 1405 */             u = u + 4 - (u & 0x3);
/* 1406 */             u += 8 * readInt(b, u + 4) + 8;
/*      */             break;
/*      */           case 16:
/* 1409 */             opcode = b[u + 1] & 0xFF;
/* 1410 */             if (opcode == 132) {
/* 1411 */               u += 6; break;
/*      */             } 
/* 1413 */             u += 4;
/*      */             break;
/*      */           
/*      */           case 1:
/*      */           case 3:
/*      */           case 10:
/* 1419 */             u += 2;
/*      */             break;
/*      */           case 2:
/*      */           case 5:
/*      */           case 6:
/*      */           case 11:
/*      */           case 12:
/* 1426 */             u += 3;
/*      */             break;
/*      */           case 7:
/* 1429 */             u += 5;
/*      */             break;
/*      */           
/*      */           default:
/* 1433 */             u += 4;
/*      */             break;
/*      */         } 
/* 1436 */         if (insert != 0) {
/*      */           
/* 1438 */           int[] newIndexes = new int[allIndexes.length + 1];
/* 1439 */           int[] newSizes = new int[allSizes.length + 1];
/* 1440 */           System.arraycopy(allIndexes, 0, newIndexes, 0, allIndexes.length);
/* 1441 */           System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
/* 1442 */           newIndexes[allIndexes.length] = u;
/* 1443 */           newSizes[allSizes.length] = insert;
/* 1444 */           allIndexes = newIndexes;
/* 1445 */           allSizes = newSizes;
/* 1446 */           if (insert > 0) {
/* 1447 */             state = 3;
/*      */           }
/*      */         } 
/*      */       } 
/* 1451 */       if (state >= 3)
/* 1452 */         continue;  state--;
/*      */     }
/* 1454 */     while (state != 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1460 */     ByteVector newCode = new ByteVector(this.code.length);
/*      */     
/* 1462 */     int i = 0;
/* 1463 */     while (i < this.code.length) {
/* 1464 */       int v, label, j, newOffset, source; for (int k = allIndexes.length - 1; k >= 0; k--) {
/* 1465 */         if (allIndexes[k] == i && 
/* 1466 */           k < len) {
/* 1467 */           if (sizes[k] > 0) {
/* 1468 */             newCode.putByteArray(null, 0, sizes[k]);
/*      */           } else {
/* 1470 */             newCode.length += sizes[k];
/*      */           } 
/* 1472 */           indexes[k] = newCode.length;
/*      */         } 
/*      */       } 
/*      */       
/* 1476 */       int opcode = b[i] & 0xFF;
/* 1477 */       switch (ClassWriter.TYPE[opcode]) {
/*      */         case 0:
/*      */         case 4:
/* 1480 */           newCode.put1(opcode);
/* 1481 */           i++;
/*      */           continue;
/*      */         case 8:
/* 1484 */           if (opcode > 201) {
/*      */ 
/*      */             
/* 1487 */             opcode = (opcode < 218) ? (opcode - 49) : (opcode - 20);
/* 1488 */             label = i + readUnsignedShort(b, i + 1);
/*      */           } else {
/* 1490 */             label = i + readShort(b, i + 1);
/*      */           } 
/* 1492 */           newOffset = getNewOffset(allIndexes, allSizes, i, label);
/* 1493 */           if (newOffset < -32768 || newOffset > 32767) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1498 */             if (opcode == 167) {
/* 1499 */               newCode.put1(200);
/* 1500 */             } else if (opcode == 168) {
/* 1501 */               newCode.put1(201);
/*      */             } else {
/* 1503 */               newCode.put1((opcode <= 166) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1));
/* 1504 */               newCode.put2(8);
/* 1505 */               newCode.put1(200);
/* 1506 */               newOffset -= 3;
/*      */             } 
/* 1508 */             newCode.put4(newOffset);
/*      */           } else {
/* 1510 */             newCode.put1(opcode);
/* 1511 */             newCode.put2(newOffset);
/*      */           } 
/* 1513 */           i += 3;
/*      */           continue;
/*      */         case 9:
/* 1516 */           label = i + readInt(b, i + 1);
/* 1517 */           newOffset = getNewOffset(allIndexes, allSizes, i, label);
/* 1518 */           newCode.put1(opcode);
/* 1519 */           newCode.put4(newOffset);
/* 1520 */           i += 5;
/*      */           continue;
/*      */         
/*      */         case 13:
/* 1524 */           v = i;
/* 1525 */           i = i + 4 - (v & 0x3);
/*      */           
/* 1527 */           source = newCode.length;
/* 1528 */           newCode.put1(170);
/* 1529 */           while (newCode.length % 4 != 0) {
/* 1530 */             newCode.put1(0);
/*      */           }
/* 1532 */           label = v + readInt(b, i); i += 4;
/* 1533 */           newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 1534 */           newCode.put4(newOffset);
/* 1535 */           j = readInt(b, i); i += 4;
/* 1536 */           newCode.put4(j);
/* 1537 */           j = readInt(b, i) - j + 1; i += 4;
/* 1538 */           newCode.put4(readInt(b, i - 4));
/* 1539 */           for (; j > 0; j--) {
/* 1540 */             label = v + readInt(b, i); i += 4;
/* 1541 */             newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 1542 */             newCode.put4(newOffset);
/*      */           } 
/*      */           continue;
/*      */         
/*      */         case 14:
/* 1547 */           v = i;
/* 1548 */           i = i + 4 - (v & 0x3);
/*      */           
/* 1550 */           source = newCode.length;
/* 1551 */           newCode.put1(171);
/* 1552 */           while (newCode.length % 4 != 0) {
/* 1553 */             newCode.put1(0);
/*      */           }
/* 1555 */           label = v + readInt(b, i); i += 4;
/* 1556 */           newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 1557 */           newCode.put4(newOffset);
/* 1558 */           j = readInt(b, i); i += 4;
/* 1559 */           newCode.put4(j);
/* 1560 */           for (; j > 0; j--) {
/* 1561 */             newCode.put4(readInt(b, i)); i += 4;
/* 1562 */             label = v + readInt(b, i); i += 4;
/* 1563 */             newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 1564 */             newCode.put4(newOffset);
/*      */           } 
/*      */           continue;
/*      */         case 16:
/* 1568 */           opcode = b[i + 1] & 0xFF;
/* 1569 */           if (opcode == 132) {
/* 1570 */             newCode.putByteArray(b, i, 6);
/* 1571 */             i += 6; continue;
/*      */           } 
/* 1573 */           newCode.putByteArray(b, i, 4);
/* 1574 */           i += 4;
/*      */           continue;
/*      */         
/*      */         case 1:
/*      */         case 3:
/*      */         case 10:
/* 1580 */           newCode.putByteArray(b, i, 2);
/* 1581 */           i += 2;
/*      */           continue;
/*      */         case 2:
/*      */         case 5:
/*      */         case 6:
/*      */         case 11:
/*      */         case 12:
/* 1588 */           newCode.putByteArray(b, i, 3);
/* 1589 */           i += 3;
/*      */           continue;
/*      */         case 7:
/* 1592 */           newCode.putByteArray(b, i, 5);
/* 1593 */           i += 5;
/*      */           continue;
/*      */       } 
/*      */       
/* 1597 */       newCode.putByteArray(b, i, 4);
/* 1598 */       i += 4;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1605 */     if (this.catchTable != null) {
/* 1606 */       b = this.catchTable.data;
/* 1607 */       i = 0;
/* 1608 */       while (i < this.catchTable.length) {
/* 1609 */         writeShort(b, i, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i)));
/*      */         
/* 1611 */         writeShort(b, i + 2, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i + 2)));
/*      */         
/* 1613 */         writeShort(b, i + 4, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i + 4)));
/*      */         
/* 1615 */         i += 8;
/*      */       } 
/*      */     } 
/* 1618 */     if (this.localVar != null) {
/* 1619 */       b = this.localVar.data;
/* 1620 */       i = 0;
/* 1621 */       while (i < this.localVar.length) {
/* 1622 */         int j = readUnsignedShort(b, i);
/* 1623 */         int k = getNewOffset(allIndexes, allSizes, 0, j);
/* 1624 */         writeShort(b, i, k);
/* 1625 */         j += readUnsignedShort(b, i + 2);
/* 1626 */         k = getNewOffset(allIndexes, allSizes, 0, j) - k;
/* 1627 */         writeShort(b, i, k);
/* 1628 */         i += 10;
/*      */       } 
/*      */     } 
/* 1631 */     if (this.lineNumber != null) {
/* 1632 */       b = this.lineNumber.data;
/* 1633 */       i = 0;
/* 1634 */       while (i < this.lineNumber.length) {
/* 1635 */         writeShort(b, i, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i)));
/*      */         
/* 1637 */         i += 4;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1642 */     this.code = newCode;
/*      */ 
/*      */     
/* 1645 */     return indexes;
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
/*      */   static int readUnsignedShort(byte[] b, int index) {
/* 1657 */     return (b[index] & 0xFF) << 8 | b[index + 1] & 0xFF;
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
/*      */   static short readShort(byte[] b, int index) {
/* 1669 */     return (short)((b[index] & 0xFF) << 8 | b[index + 1] & 0xFF);
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
/*      */   static int readInt(byte[] b, int index) {
/* 1681 */     return (b[index] & 0xFF) << 24 | (b[index + 1] & 0xFF) << 16 | (b[index + 2] & 0xFF) << 8 | b[index + 3] & 0xFF;
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
/*      */   static void writeShort(byte[] b, int index, int s) {
/* 1696 */     b[index] = (byte)(s >>> 8);
/* 1697 */     b[index + 1] = (byte)s;
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
/*      */   static int getNewOffset(int[] indexes, int[] sizes, int begin, int end) {
/* 1729 */     int offset = end - begin;
/* 1730 */     for (int i = 0; i < indexes.length; i++) {
/* 1731 */       if (begin < indexes[i] && indexes[i] <= end) {
/* 1732 */         offset += sizes[i];
/* 1733 */       } else if (end < indexes[i] && indexes[i] <= begin) {
/* 1734 */         offset -= sizes[i];
/*      */       } 
/*      */     } 
/* 1737 */     return offset;
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
/*      */   protected int getCodeSize() {
/* 1750 */     return this.code.length;
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
/*      */   protected byte[] getCode() {
/* 1764 */     return this.code.data;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/org/objectweb/asm/CodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */