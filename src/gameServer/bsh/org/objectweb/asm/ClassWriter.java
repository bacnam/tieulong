package bsh.org.objectweb.asm;

public class ClassWriter
implements ClassVisitor
{
static final int CLASS = 7;
static final int FIELD = 9;
static final int METH = 10;
static final int IMETH = 11;
static final int STR = 8;
static final int INT = 3;
static final int FLOAT = 4;
static final int LONG = 5;
static final int DOUBLE = 6;
static final int NAME_TYPE = 12;
static final int UTF8 = 1;
private short index;
private ByteVector pool;
private Item[] table;
private int threshold;
private int access;
private int name;
private int superName;
private int interfaceCount;
private int[] interfaces;
private Item sourceFile;
private int fieldCount;
private ByteVector fields;
private boolean computeMaxs;
CodeWriter firstMethod;
CodeWriter lastMethod;
private int innerClassesCount;
private ByteVector innerClasses;
Item key;
Item key2;
Item key3;
static final int NOARG_INSN = 0;
static final int SBYTE_INSN = 1;
static final int SHORT_INSN = 2;
static final int VAR_INSN = 3;
static final int IMPLVAR_INSN = 4;
static final int TYPE_INSN = 5;
static final int FIELDORMETH_INSN = 6;
static final int ITFMETH_INSN = 7;
static final int LABEL_INSN = 8;
static final int LABELW_INSN = 9;
static final int LDC_INSN = 10;
static final int LDCW_INSN = 11;
static final int IINC_INSN = 12;
static final int TABL_INSN = 13;
static final int LOOK_INSN = 14;
static final int MANA_INSN = 15;
static final int WIDE_INSN = 16;
static byte[] TYPE;

static {
byte[] b = new byte[220];
String s = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHAFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";

for (int i = 0; i < b.length; i++) {
b[i] = (byte)(s.charAt(i) - 65);
}
TYPE = b;
}

public ClassWriter(boolean computeMaxs) {
this.index = 1;
this.pool = new ByteVector();
this.table = new Item[64];
this.threshold = (int)(0.75D * this.table.length);
this.key = new Item();
this.key2 = new Item();
this.key3 = new Item();
this.computeMaxs = computeMaxs;
}

public void visit(int access, String name, String superName, String[] interfaces, String sourceFile) {
this.access = access;
this.name = (newClass(name)).index;
this.superName = (superName == null) ? 0 : (newClass(superName)).index;
if (interfaces != null && interfaces.length > 0) {
this.interfaceCount = interfaces.length;
this.interfaces = new int[this.interfaceCount];
for (int i = 0; i < this.interfaceCount; i++) {
this.interfaces[i] = (newClass(interfaces[i])).index;
}
} 
if (sourceFile != null) {
newUTF8("SourceFile");
this.sourceFile = newUTF8(sourceFile);
} 
if ((access & 0x20000) != 0) {
newUTF8("Deprecated");
}
}

public void visitInnerClass(String name, String outerName, String innerName, int access) {
if (this.innerClasses == null) {
newUTF8("InnerClasses");
this.innerClasses = new ByteVector();
} 
this.innerClassesCount++;
this.innerClasses.put2((name == null) ? 0 : (newClass(name)).index);
this.innerClasses.put2((outerName == null) ? 0 : (newClass(outerName)).index);
this.innerClasses.put2((innerName == null) ? 0 : (newUTF8(innerName)).index);
this.innerClasses.put2(access);
}

public void visitField(int access, String name, String desc, Object value) {
this.fieldCount++;
if (this.fields == null) {
this.fields = new ByteVector();
}
this.fields.put2(access).put2((newUTF8(name)).index).put2((newUTF8(desc)).index);
int attributeCount = 0;
if (value != null) {
attributeCount++;
}
if ((access & 0x10000) != 0) {
attributeCount++;
}
if ((access & 0x20000) != 0) {
attributeCount++;
}
this.fields.put2(attributeCount);
if (value != null) {
this.fields.put2((newUTF8("ConstantValue")).index);
this.fields.put4(2).put2((newCst(value)).index);
} 
if ((access & 0x10000) != 0) {
this.fields.put2((newUTF8("Synthetic")).index).put4(0);
}
if ((access & 0x20000) != 0) {
this.fields.put2((newUTF8("Deprecated")).index).put4(0);
}
}

public CodeVisitor visitMethod(int access, String name, String desc, String[] exceptions) {
CodeWriter cw = new CodeWriter(this, this.computeMaxs);
cw.init(access, name, desc, exceptions);
return cw;
}

public void visitEnd() {}

public byte[] toByteArray() {
int size = 24 + 2 * this.interfaceCount;
if (this.fields != null) {
size += this.fields.length;
}
int nbMethods = 0;
CodeWriter cb = this.firstMethod;
while (cb != null) {
nbMethods++;
size += cb.getSize();
cb = cb.next;
} 
size += this.pool.length;
int attributeCount = 0;
if (this.sourceFile != null) {
attributeCount++;
size += 8;
} 
if ((this.access & 0x20000) != 0) {
attributeCount++;
size += 6;
} 
if (this.innerClasses != null) {
attributeCount++;
size += 8 + this.innerClasses.length;
} 

ByteVector out = new ByteVector(size);
out.put4(-889275714).put2(3).put2(45);
out.put2(this.index).putByteArray(this.pool.data, 0, this.pool.length);
out.put2(this.access).put2(this.name).put2(this.superName);
out.put2(this.interfaceCount);
for (int i = 0; i < this.interfaceCount; i++) {
out.put2(this.interfaces[i]);
}
out.put2(this.fieldCount);
if (this.fields != null) {
out.putByteArray(this.fields.data, 0, this.fields.length);
}
out.put2(nbMethods);
cb = this.firstMethod;
while (cb != null) {
cb.put(out);
cb = cb.next;
} 
out.put2(attributeCount);
if (this.sourceFile != null) {
out.put2((newUTF8("SourceFile")).index).put4(2).put2(this.sourceFile.index);
}
if ((this.access & 0x20000) != 0) {
out.put2((newUTF8("Deprecated")).index).put4(0);
}
if (this.innerClasses != null) {
out.put2((newUTF8("InnerClasses")).index);
out.put4(this.innerClasses.length + 2).put2(this.innerClassesCount);
out.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
} 
return out.data;
}

Item newCst(Object cst) {
if (cst instanceof Integer) {
int val = ((Integer)cst).intValue();
return newInteger(val);
}  if (cst instanceof Float) {
float val = ((Float)cst).floatValue();
return newFloat(val);
}  if (cst instanceof Long) {
long val = ((Long)cst).longValue();
return newLong(val);
}  if (cst instanceof Double) {
double val = ((Double)cst).doubleValue();
return newDouble(val);
}  if (cst instanceof String) {
return newString((String)cst);
}
throw new IllegalArgumentException("value " + cst);
}

Item newUTF8(String value) {
this.key.set(1, value, null, null);
Item result = get(this.key);
if (result == null) {
this.pool.put1(1).putUTF(value);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key);
put(result);
} 
return result;
}

Item newClass(String value) {
this.key2.set(7, value, null, null);
Item result = get(this.key2);
if (result == null) {
this.pool.put12(7, (newUTF8(value)).index);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key2);
put(result);
} 
return result;
}

Item newField(String owner, String name, String desc) {
this.key3.set(9, owner, name, desc);
Item result = get(this.key3);
if (result == null) {
put122(9, (newClass(owner)).index, (newNameType(name, desc)).index);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key3);
put(result);
} 
return result;
}

Item newMethod(String owner, String name, String desc) {
this.key3.set(10, owner, name, desc);
Item result = get(this.key3);
if (result == null) {
put122(10, (newClass(owner)).index, (newNameType(name, desc)).index);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key3);
put(result);
} 
return result;
}

Item newItfMethod(String ownerItf, String name, String desc) {
this.key3.set(11, ownerItf, name, desc);
Item result = get(this.key3);
if (result == null) {
put122(11, (newClass(ownerItf)).index, (newNameType(name, desc)).index);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key3);
put(result);
} 
return result;
}

private Item newInteger(int value) {
this.key.set(value);
Item result = get(this.key);
if (result == null) {
this.pool.put1(3).put4(value);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key);
put(result);
} 
return result;
}

private Item newFloat(float value) {
this.key.set(value);
Item result = get(this.key);
if (result == null) {
this.pool.put1(4).put4(Float.floatToIntBits(value));
this.index = (short)(this.index + 1); result = new Item(this.index, this.key);
put(result);
} 
return result;
}

private Item newLong(long value) {
this.key.set(value);
Item result = get(this.key);
if (result == null) {
this.pool.put1(5).put8(value);
result = new Item(this.index, this.key);
put(result);
this.index = (short)(this.index + 2);
} 
return result;
}

private Item newDouble(double value) {
this.key.set(value);
Item result = get(this.key);
if (result == null) {
this.pool.put1(6).put8(Double.doubleToLongBits(value));
result = new Item(this.index, this.key);
put(result);
this.index = (short)(this.index + 2);
} 
return result;
}

private Item newString(String value) {
this.key2.set(8, value, null, null);
Item result = get(this.key2);
if (result == null) {
this.pool.put12(8, (newUTF8(value)).index);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key2);
put(result);
} 
return result;
}

private Item newNameType(String name, String desc) {
this.key2.set(12, name, desc, null);
Item result = get(this.key2);
if (result == null) {
put122(12, (newUTF8(name)).index, (newUTF8(desc)).index);
this.index = (short)(this.index + 1); result = new Item(this.index, this.key2);
put(result);
} 
return result;
}

private Item get(Item key) {
Item[] tab = this.table;
int hashCode = key.hashCode;
int index = (hashCode & Integer.MAX_VALUE) % tab.length;
for (Item i = tab[index]; i != null; i = i.next) {
if (i.hashCode == hashCode && key.isEqualTo(i)) {
return i;
}
} 
return null;
}

private void put(Item i) {
if (this.index > this.threshold) {
int oldCapacity = this.table.length;
Item[] oldMap = this.table;
int newCapacity = oldCapacity * 2 + 1;
Item[] newMap = new Item[newCapacity];
this.threshold = (int)(newCapacity * 0.75D);
this.table = newMap;
for (int j = oldCapacity; j-- > 0;) {
for (Item old = oldMap[j]; old != null; ) {
Item e = old;
old = old.next;
int k = (e.hashCode & Integer.MAX_VALUE) % newCapacity;
e.next = newMap[k];
newMap[k] = e;
} 
} 
} 
int index = (i.hashCode & Integer.MAX_VALUE) % this.table.length;
i.next = this.table[index];
this.table[index] = i;
}

private void put122(int b, int s1, int s2) {
this.pool.put12(b, s1).put2(s2);
}
}

