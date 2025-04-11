package bsh.org.objectweb.asm;

public class CodeWriter
        implements CodeVisitor {
    static final boolean CHECK = false;
    private static final int[] SIZE;
    private static Edge pool;

    static {
        int[] b = new int[202];
        String s = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";

        for (int i = 0; i < b.length; i++) {
            b[i] = s.charAt(i) - 69;
        }
        SIZE = b;
    }

    private final boolean computeMaxs;
    CodeWriter next;
    private ClassWriter cw;
    private Item name;
    private Item desc;
    private int access;
    private int maxStack;
    private int maxLocals;
    private ByteVector code = new ByteVector();
    private int catchCount;
    private ByteVector catchTable;
    private int exceptionCount;
    private int[] exceptions;
    private int localVarCount;
    private ByteVector localVar;
    private int lineNumberCount;
    private ByteVector lineNumber;
    private boolean resize;
    private int stackSize;
    private int maxStackSize;
    private Label currentBlock;
    private Label blockStack;
    private Edge head;
    private Edge tail;

    protected CodeWriter(ClassWriter cw, boolean computeMaxs) {
        if (cw.firstMethod == null) {
            cw.firstMethod = this;
            cw.lastMethod = this;
        } else {
            cw.lastMethod.next = this;
            cw.lastMethod = this;
        }
        this.cw = cw;
        this.computeMaxs = computeMaxs;
        if (computeMaxs) {

            this.currentBlock = new Label();
            this.currentBlock.pushed = true;
            this.blockStack = this.currentBlock;
        }
    }

    private static int getArgumentsAndReturnSizes(String desc) {
        int n = 1;
        int c = 1;
        while (true) {
            char car = desc.charAt(c++);
            if (car == ')') {
                car = desc.charAt(c);
                return n << 2 | ((car == 'V') ? 0 : ((car == 'D' || car == 'J') ? 2 : 1));
            }
            if (car == 'L') {
                while (desc.charAt(c++) != ';') ;

                n++;
                continue;
            }
            if (car == '[') {
                while ((car = desc.charAt(c)) == '[') {
                    c++;
                }
                if (car == 'D' || car == 'J')
                    n--;
                continue;
            }
            if (car == 'D' || car == 'J') {
                n += 2;
                continue;
            }
            n++;
        }
    }

    static int readUnsignedShort(byte[] b, int index) {
        return (b[index] & 0xFF) << 8 | b[index + 1] & 0xFF;
    }

    static short readShort(byte[] b, int index) {
        return (short) ((b[index] & 0xFF) << 8 | b[index + 1] & 0xFF);
    }

    static int readInt(byte[] b, int index) {
        return (b[index] & 0xFF) << 24 | (b[index + 1] & 0xFF) << 16 | (b[index + 2] & 0xFF) << 8 | b[index + 3] & 0xFF;
    }

    static void writeShort(byte[] b, int index, int s) {
        b[index] = (byte) (s >>> 8);
        b[index + 1] = (byte) s;
    }

    static int getNewOffset(int[] indexes, int[] sizes, int begin, int end) {
        int offset = end - begin;
        for (int i = 0; i < indexes.length; i++) {
            if (begin < indexes[i] && indexes[i] <= end) {
                offset += sizes[i];
            } else if (end < indexes[i] && indexes[i] <= begin) {
                offset -= sizes[i];
            }
        }
        return offset;
    }

    protected void init(int access, String name, String desc, String[] exceptions) {
        this.access = access;
        this.name = this.cw.newUTF8(name);
        this.desc = this.cw.newUTF8(desc);
        if (exceptions != null && exceptions.length > 0) {
            this.exceptionCount = exceptions.length;
            this.exceptions = new int[this.exceptionCount];
            for (int i = 0; i < this.exceptionCount; i++) {
                this.exceptions[i] = (this.cw.newClass(exceptions[i])).index;
            }
        }
        if (this.computeMaxs) {

            int size = getArgumentsAndReturnSizes(desc) >> 2;
            if ((access & 0x8) != 0) {
                size--;
            }
            if (size > this.maxLocals) {
                this.maxLocals = size;
            }
        }
    }

    public void visitInsn(int opcode) {
        if (this.computeMaxs) {

            int size = this.stackSize + SIZE[opcode];
            if (size > this.maxStackSize) {
                this.maxStackSize = size;
            }
            this.stackSize = size;

            if ((opcode >= 172 && opcode <= 177) || opcode == 191) {

                if (this.currentBlock != null) {
                    this.currentBlock.maxStackSize = this.maxStackSize;
                    this.currentBlock = null;
                }
            }
        }

        this.code.put1(opcode);
    }

    public void visitIntInsn(int opcode, int operand) {
        if (this.computeMaxs && opcode != 188) {

            int size = this.stackSize + 1;
            if (size > this.maxStackSize) {
                this.maxStackSize = size;
            }
            this.stackSize = size;
        }

        if (opcode == 17) {
            this.code.put12(opcode, operand);
        } else {
            this.code.put11(opcode, operand);
        }
    }

    public void visitVarInsn(int opcode, int var) {
        if (this.computeMaxs) {
            int n;
            if (opcode == 169) {

                if (this.currentBlock != null) {
                    this.currentBlock.maxStackSize = this.maxStackSize;
                    this.currentBlock = null;
                }
            } else {
                int size = this.stackSize + SIZE[opcode];
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }

            if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57) {

                n = var + 2;
            } else {
                n = var + 1;
            }
            if (n > this.maxLocals) {
                this.maxLocals = n;
            }
        }

        if (var < 4 && opcode != 169) {
            int opt;
            if (opcode < 54) {
                opt = 26 + (opcode - 21 << 2) + var;
            } else {
                opt = 59 + (opcode - 54 << 2) + var;
            }
            this.code.put1(opt);
        } else if (var >= 256) {
            this.code.put1(196).put12(opcode, var);
        } else {
            this.code.put11(opcode, var);
        }
    }

    public void visitTypeInsn(int opcode, String desc) {
        if (this.computeMaxs && opcode == 187) {

            int size = this.stackSize + 1;
            if (size > this.maxStackSize) {
                this.maxStackSize = size;
            }
            this.stackSize = size;
        }

        this.code.put12(opcode, (this.cw.newClass(desc)).index);
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        if (this.computeMaxs) {
            int size;

            char c = desc.charAt(0);
            switch (opcode) {
                case 178:
                    size = this.stackSize + ((c == 'D' || c == 'J') ? 2 : 1);
                    break;
                case 179:
                    size = this.stackSize + ((c == 'D' || c == 'J') ? -2 : -1);
                    break;
                case 180:
                    size = this.stackSize + ((c == 'D' || c == 'J') ? 1 : 0);
                    break;

                default:
                    size = this.stackSize + ((c == 'D' || c == 'J') ? -3 : -2);
                    break;
            }

            if (size > this.maxStackSize) {
                this.maxStackSize = size;
            }
            this.stackSize = size;
        }

        this.code.put12(opcode, (this.cw.newField(owner, name, desc)).index);
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        Item i;
        if (opcode == 185) {
            i = this.cw.newItfMethod(owner, name, desc);
        } else {
            i = this.cw.newMethod(owner, name, desc);
        }
        int argSize = i.intVal;
        if (this.computeMaxs) {
            int size;

            if (argSize == 0) {

                argSize = getArgumentsAndReturnSizes(desc);

                i.intVal = argSize;
            }

            if (opcode == 184) {
                size = this.stackSize - (argSize >> 2) + (argSize & 0x3) + 1;
            } else {
                size = this.stackSize - (argSize >> 2) + (argSize & 0x3);
            }

            if (size > this.maxStackSize) {
                this.maxStackSize = size;
            }
            this.stackSize = size;
        }

        if (opcode == 185) {
            if (!this.computeMaxs &&
                    argSize == 0) {
                argSize = getArgumentsAndReturnSizes(desc);
                i.intVal = argSize;
            }

            this.code.put12(185, i.index).put11(argSize >> 2, 0);
        } else {
            this.code.put12(opcode, i.index);
        }
    }

    public void visitJumpInsn(int opcode, Label label) {
        if (this.computeMaxs) {
            if (opcode == 167) {

                if (this.currentBlock != null) {
                    this.currentBlock.maxStackSize = this.maxStackSize;
                    addSuccessor(this.stackSize, label);
                    this.currentBlock = null;
                }
            } else if (opcode == 168) {
                if (this.currentBlock != null) {
                    addSuccessor(this.stackSize + 1, label);
                }
            } else {

                this.stackSize += SIZE[opcode];
                if (this.currentBlock != null) {
                    addSuccessor(this.stackSize, label);
                }
            }
        }

        if (label.resolved && label.position - this.code.length < -32768) {

            if (opcode == 167) {
                this.code.put1(200);
            } else if (opcode == 168) {
                this.code.put1(201);
            } else {
                this.code.put1((opcode <= 166) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1));
                this.code.put2(8);
                this.code.put1(200);
            }
            label.put(this, this.code, this.code.length - 1, true);

        } else {

            this.code.put1(opcode);
            label.put(this, this.code, this.code.length - 1, false);
        }
    }

    public void visitLabel(Label label) {
        if (this.computeMaxs) {
            if (this.currentBlock != null) {

                this.currentBlock.maxStackSize = this.maxStackSize;
                addSuccessor(this.stackSize, label);
            }

            this.currentBlock = label;
            this.stackSize = 0;
            this.maxStackSize = 0;
        }

        this.resize |= label.resolve(this, this.code.length, this.code.data);
    }

    public void visitLdcInsn(Object cst) {
        Item i = this.cw.newCst(cst);
        if (this.computeMaxs) {
            int size;

            if (i.type == 5 || i.type == 6) {
                size = this.stackSize + 2;
            } else {
                size = this.stackSize + 1;
            }

            if (size > this.maxStackSize) {
                this.maxStackSize = size;
            }
            this.stackSize = size;
        }

        int index = i.index;
        if (i.type == 5 || i.type == 6) {
            this.code.put12(20, index);
        } else if (index >= 256) {
            this.code.put12(19, index);
        } else {
            this.code.put11(18, index);
        }
    }

    public void visitIincInsn(int var, int increment) {
        if (this.computeMaxs) {

            int n = var + 1;
            if (n > this.maxLocals) {
                this.maxLocals = n;
            }
        }

        if (var > 255 || increment > 127 || increment < -128) {
            this.code.put1(196).put12(132, var).put2(increment);
        } else {
            this.code.put1(132).put11(var, increment);
        }
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        if (this.computeMaxs) {

            this.stackSize--;

            if (this.currentBlock != null) {
                this.currentBlock.maxStackSize = this.maxStackSize;
                addSuccessor(this.stackSize, dflt);
                for (int j = 0; j < labels.length; j++) {
                    addSuccessor(this.stackSize, labels[j]);
                }
                this.currentBlock = null;
            }
        }

        int source = this.code.length;
        this.code.put1(170);
        while (this.code.length % 4 != 0) {
            this.code.put1(0);
        }
        dflt.put(this, this.code, source, true);
        this.code.put4(min).put4(max);
        for (int i = 0; i < labels.length; i++) {
            labels[i].put(this, this.code, source, true);
        }
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        if (this.computeMaxs) {

            this.stackSize--;

            if (this.currentBlock != null) {
                this.currentBlock.maxStackSize = this.maxStackSize;
                addSuccessor(this.stackSize, dflt);
                for (int j = 0; j < labels.length; j++) {
                    addSuccessor(this.stackSize, labels[j]);
                }
                this.currentBlock = null;
            }
        }

        int source = this.code.length;
        this.code.put1(171);
        while (this.code.length % 4 != 0) {
            this.code.put1(0);
        }
        dflt.put(this, this.code, source, true);
        this.code.put4(labels.length);
        for (int i = 0; i < labels.length; i++) {
            this.code.put4(keys[i]);
            labels[i].put(this, this.code, source, true);
        }
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        if (this.computeMaxs) {

            this.stackSize += 1 - dims;
        }

        Item classItem = this.cw.newClass(desc);
        this.code.put12(197, classItem.index).put1(dims);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        if (this.computeMaxs) {
            if (!handler.pushed) {
                handler.beginStackSize = 1;
                handler.pushed = true;
                handler.next = this.blockStack;
                this.blockStack = handler;
            }
        }
        this.catchCount++;
        if (this.catchTable == null) {
            this.catchTable = new ByteVector();
        }
        this.catchTable.put2(start.position);
        this.catchTable.put2(end.position);
        this.catchTable.put2(handler.position);
        this.catchTable.put2((type != null) ? (this.cw.newClass(type)).index : 0);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        if (this.computeMaxs) {

            int max = 0;

            Label stack = this.blockStack;
            while (stack != null) {

                Label l = stack;
                stack = stack.next;

                int start = l.beginStackSize;
                int blockMax = start + l.maxStackSize;

                if (blockMax > max) {
                    max = blockMax;
                }

                Edge b = l.successors;
                while (b != null) {
                    l = b.successor;

                    if (!l.pushed) {

                        l.beginStackSize = start + b.stackSize;

                        l.pushed = true;
                        l.next = stack;
                        stack = l;
                    }
                    b = b.next;
                }
            }
            this.maxStack = max;

            synchronized (SIZE) {

                if (this.tail != null) {
                    this.tail.poolNext = pool;
                    pool = this.head;
                }
            }
        } else {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }
    }

    public void visitLocalVariable(String name, String desc, Label start, Label end, int index) {
        if (this.localVar == null) {
            this.cw.newUTF8("LocalVariableTable");
            this.localVar = new ByteVector();
        }
        this.localVarCount++;
        this.localVar.put2(start.position);
        this.localVar.put2(end.position - start.position);
        this.localVar.put2((this.cw.newUTF8(name)).index);
        this.localVar.put2((this.cw.newUTF8(desc)).index);
        this.localVar.put2(index);
    }

    public void visitLineNumber(int line, Label start) {
        if (this.lineNumber == null) {
            this.cw.newUTF8("LineNumberTable");
            this.lineNumber = new ByteVector();
        }
        this.lineNumberCount++;
        this.lineNumber.put2(start.position);
        this.lineNumber.put2(line);
    }

    private void addSuccessor(int stackSize, Label successor) {
        Edge b;
        synchronized (SIZE) {
            if (pool == null) {
                b = new Edge();
            } else {
                b = pool;

                pool = pool.poolNext;
            }
        }

        if (this.tail == null) {
            this.tail = b;
        }
        b.poolNext = this.head;
        this.head = b;

        b.stackSize = stackSize;
        b.successor = successor;

        b.next = this.currentBlock.successors;
        this.currentBlock.successors = b;
    }

    final int getSize() {
        if (this.resize) {
            resizeInstructions(new int[0], new int[0], 0);
        }
        int size = 8;
        if (this.code.length > 0) {
            this.cw.newUTF8("Code");
            size += 18 + this.code.length + 8 * this.catchCount;
            if (this.localVar != null) {
                size += 8 + this.localVar.length;
            }
            if (this.lineNumber != null) {
                size += 8 + this.lineNumber.length;
            }
        }
        if (this.exceptionCount > 0) {
            this.cw.newUTF8("Exceptions");
            size += 8 + 2 * this.exceptionCount;
        }
        if ((this.access & 0x10000) != 0) {
            this.cw.newUTF8("Synthetic");
            size += 6;
        }
        if ((this.access & 0x20000) != 0) {
            this.cw.newUTF8("Deprecated");
            size += 6;
        }
        return size;
    }

    final void put(ByteVector out) {
        out.put2(this.access).put2(this.name.index).put2(this.desc.index);
        int attributeCount = 0;
        if (this.code.length > 0) {
            attributeCount++;
        }
        if (this.exceptionCount > 0) {
            attributeCount++;
        }
        if ((this.access & 0x10000) != 0) {
            attributeCount++;
        }
        if ((this.access & 0x20000) != 0) {
            attributeCount++;
        }
        out.put2(attributeCount);
        if (this.code.length > 0) {
            int size = 12 + this.code.length + 8 * this.catchCount;
            if (this.localVar != null) {
                size += 8 + this.localVar.length;
            }
            if (this.lineNumber != null) {
                size += 8 + this.lineNumber.length;
            }
            out.put2((this.cw.newUTF8("Code")).index).put4(size);
            out.put2(this.maxStack).put2(this.maxLocals);
            out.put4(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            out.put2(this.catchCount);
            if (this.catchCount > 0) {
                out.putByteArray(this.catchTable.data, 0, this.catchTable.length);
            }
            attributeCount = 0;
            if (this.localVar != null) {
                attributeCount++;
            }
            if (this.lineNumber != null) {
                attributeCount++;
            }
            out.put2(attributeCount);
            if (this.localVar != null) {
                out.put2((this.cw.newUTF8("LocalVariableTable")).index);
                out.put4(this.localVar.length + 2).put2(this.localVarCount);
                out.putByteArray(this.localVar.data, 0, this.localVar.length);
            }
            if (this.lineNumber != null) {
                out.put2((this.cw.newUTF8("LineNumberTable")).index);
                out.put4(this.lineNumber.length + 2).put2(this.lineNumberCount);
                out.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
            }
        }
        if (this.exceptionCount > 0) {
            out.put2((this.cw.newUTF8("Exceptions")).index).put4(2 * this.exceptionCount + 2);
            out.put2(this.exceptionCount);
            for (int i = 0; i < this.exceptionCount; i++) {
                out.put2(this.exceptions[i]);
            }
        }
        if ((this.access & 0x10000) != 0) {
            out.put2((this.cw.newUTF8("Synthetic")).index).put4(0);
        }
        if ((this.access & 0x20000) != 0) {
            out.put2((this.cw.newUTF8("Deprecated")).index).put4(0);
        }
    }

    protected int[] resizeInstructions(int[] indexes, int[] sizes, int len) {
        byte[] b = this.code.data;

        int[] allIndexes = new int[len];
        int[] allSizes = new int[len];

        System.arraycopy(indexes, 0, allIndexes, 0, len);
        System.arraycopy(sizes, 0, allSizes, 0, len);
        boolean[] resize = new boolean[this.code.length];

        int state = 3;
        do {
            if (state == 3) {
                state = 2;
            }
            int u = 0;
            while (u < b.length) {
                int label, newOffset, opcode = b[u] & 0xFF;
                int insert = 0;

                switch (ClassWriter.TYPE[opcode]) {
                    case 0:
                    case 4:
                        u++;
                        break;
                    case 8:
                        if (opcode > 201) {

                            opcode = (opcode < 218) ? (opcode - 49) : (opcode - 20);
                            label = u + readUnsignedShort(b, u + 1);
                        } else {
                            label = u + readShort(b, u + 1);
                        }
                        newOffset = getNewOffset(allIndexes, allSizes, u, label);
                        if ((newOffset < -32768 || newOffset > 32767) &&
                                !resize[u]) {
                            if (opcode == 167 || opcode == 168) {

                                insert = 2;

                            } else {

                                insert = 5;
                            }
                            resize[u] = true;
                        }

                        u += 3;
                        break;
                    case 9:
                        u += 5;
                        break;
                    case 13:
                        if (state == 1) {

                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 0x3);
                        } else if (!resize[u]) {

                            insert = u & 0x3;
                            resize[u] = true;
                        }

                        u = u + 4 - (u & 0x3);
                        u += 4 * (readInt(b, u + 8) - readInt(b, u + 4) + 1) + 12;
                        break;
                    case 14:
                        if (state == 1) {

                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 0x3);
                        } else if (!resize[u]) {

                            insert = u & 0x3;
                            resize[u] = true;
                        }

                        u = u + 4 - (u & 0x3);
                        u += 8 * readInt(b, u + 4) + 8;
                        break;
                    case 16:
                        opcode = b[u + 1] & 0xFF;
                        if (opcode == 132) {
                            u += 6;
                            break;
                        }
                        u += 4;
                        break;

                    case 1:
                    case 3:
                    case 10:
                        u += 2;
                        break;
                    case 2:
                    case 5:
                    case 6:
                    case 11:
                    case 12:
                        u += 3;
                        break;
                    case 7:
                        u += 5;
                        break;

                    default:
                        u += 4;
                        break;
                }
                if (insert != 0) {

                    int[] newIndexes = new int[allIndexes.length + 1];
                    int[] newSizes = new int[allSizes.length + 1];
                    System.arraycopy(allIndexes, 0, newIndexes, 0, allIndexes.length);
                    System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
                    newIndexes[allIndexes.length] = u;
                    newSizes[allSizes.length] = insert;
                    allIndexes = newIndexes;
                    allSizes = newSizes;
                    if (insert > 0) {
                        state = 3;
                    }
                }
            }
            if (state >= 3)
                continue;
            state--;
        }
        while (state != 0);

        ByteVector newCode = new ByteVector(this.code.length);

        int i = 0;
        while (i < this.code.length) {
            int v, label, j, newOffset, source;
            for (int k = allIndexes.length - 1; k >= 0; k--) {
                if (allIndexes[k] == i &&
                        k < len) {
                    if (sizes[k] > 0) {
                        newCode.putByteArray(null, 0, sizes[k]);
                    } else {
                        newCode.length += sizes[k];
                    }
                    indexes[k] = newCode.length;
                }
            }

            int opcode = b[i] & 0xFF;
            switch (ClassWriter.TYPE[opcode]) {
                case 0:
                case 4:
                    newCode.put1(opcode);
                    i++;
                    continue;
                case 8:
                    if (opcode > 201) {

                        opcode = (opcode < 218) ? (opcode - 49) : (opcode - 20);
                        label = i + readUnsignedShort(b, i + 1);
                    } else {
                        label = i + readShort(b, i + 1);
                    }
                    newOffset = getNewOffset(allIndexes, allSizes, i, label);
                    if (newOffset < -32768 || newOffset > 32767) {

                        if (opcode == 167) {
                            newCode.put1(200);
                        } else if (opcode == 168) {
                            newCode.put1(201);
                        } else {
                            newCode.put1((opcode <= 166) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1));
                            newCode.put2(8);
                            newCode.put1(200);
                            newOffset -= 3;
                        }
                        newCode.put4(newOffset);
                    } else {
                        newCode.put1(opcode);
                        newCode.put2(newOffset);
                    }
                    i += 3;
                    continue;
                case 9:
                    label = i + readInt(b, i + 1);
                    newOffset = getNewOffset(allIndexes, allSizes, i, label);
                    newCode.put1(opcode);
                    newCode.put4(newOffset);
                    i += 5;
                    continue;

                case 13:
                    v = i;
                    i = i + 4 - (v & 0x3);

                    source = newCode.length;
                    newCode.put1(170);
                    while (newCode.length % 4 != 0) {
                        newCode.put1(0);
                    }
                    label = v + readInt(b, i);
                    i += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.put4(newOffset);
                    j = readInt(b, i);
                    i += 4;
                    newCode.put4(j);
                    j = readInt(b, i) - j + 1;
                    i += 4;
                    newCode.put4(readInt(b, i - 4));
                    for (; j > 0; j--) {
                        label = v + readInt(b, i);
                        i += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.put4(newOffset);
                    }
                    continue;

                case 14:
                    v = i;
                    i = i + 4 - (v & 0x3);

                    source = newCode.length;
                    newCode.put1(171);
                    while (newCode.length % 4 != 0) {
                        newCode.put1(0);
                    }
                    label = v + readInt(b, i);
                    i += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.put4(newOffset);
                    j = readInt(b, i);
                    i += 4;
                    newCode.put4(j);
                    for (; j > 0; j--) {
                        newCode.put4(readInt(b, i));
                        i += 4;
                        label = v + readInt(b, i);
                        i += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.put4(newOffset);
                    }
                    continue;
                case 16:
                    opcode = b[i + 1] & 0xFF;
                    if (opcode == 132) {
                        newCode.putByteArray(b, i, 6);
                        i += 6;
                        continue;
                    }
                    newCode.putByteArray(b, i, 4);
                    i += 4;
                    continue;

                case 1:
                case 3:
                case 10:
                    newCode.putByteArray(b, i, 2);
                    i += 2;
                    continue;
                case 2:
                case 5:
                case 6:
                case 11:
                case 12:
                    newCode.putByteArray(b, i, 3);
                    i += 3;
                    continue;
                case 7:
                    newCode.putByteArray(b, i, 5);
                    i += 5;
                    continue;
            }

            newCode.putByteArray(b, i, 4);
            i += 4;
        }

        if (this.catchTable != null) {
            b = this.catchTable.data;
            i = 0;
            while (i < this.catchTable.length) {
                writeShort(b, i, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i)));

                writeShort(b, i + 2, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i + 2)));

                writeShort(b, i + 4, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i + 4)));

                i += 8;
            }
        }
        if (this.localVar != null) {
            b = this.localVar.data;
            i = 0;
            while (i < this.localVar.length) {
                int j = readUnsignedShort(b, i);
                int k = getNewOffset(allIndexes, allSizes, 0, j);
                writeShort(b, i, k);
                j += readUnsignedShort(b, i + 2);
                k = getNewOffset(allIndexes, allSizes, 0, j) - k;
                writeShort(b, i, k);
                i += 10;
            }
        }
        if (this.lineNumber != null) {
            b = this.lineNumber.data;
            i = 0;
            while (i < this.lineNumber.length) {
                writeShort(b, i, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, i)));

                i += 4;
            }
        }

        this.code = newCode;

        return indexes;
    }

    protected int getCodeSize() {
        return this.code.length;
    }

    protected byte[] getCode() {
        return this.code.data;
    }
}

