package bsh.org.objectweb.asm;

public class Label
{
CodeWriter owner;
boolean resolved;
int position;
private int referenceCount;
private int[] srcAndRefPositions;
int beginStackSize;
int maxStackSize;
Edge successors;
Label next;
boolean pushed;

void put(CodeWriter owner, ByteVector out, int source, boolean wideOffset) {
if (this.resolved) {
if (wideOffset) {
out.put4(this.position - source);
} else {
out.put2(this.position - source);
}

} else if (wideOffset) {
addReference(-1 - source, out.length);
out.put4(-1);
} else {
addReference(source, out.length);
out.put2(-1);
} 
}

private void addReference(int sourcePosition, int referencePosition) {
if (this.srcAndRefPositions == null) {
this.srcAndRefPositions = new int[6];
}
if (this.referenceCount >= this.srcAndRefPositions.length) {
int[] a = new int[this.srcAndRefPositions.length + 6];
System.arraycopy(this.srcAndRefPositions, 0, a, 0, this.srcAndRefPositions.length);
this.srcAndRefPositions = a;
} 
this.srcAndRefPositions[this.referenceCount++] = sourcePosition;
this.srcAndRefPositions[this.referenceCount++] = referencePosition;
}

boolean resolve(CodeWriter owner, int position, byte[] data) {
boolean needUpdate = false;
this.resolved = true;
this.position = position;
int i = 0;
while (i < this.referenceCount) {
int source = this.srcAndRefPositions[i++];
int reference = this.srcAndRefPositions[i++];

if (source >= 0) {
int j = position - source;
if (j < -32768 || j > 32767) {

int opcode = data[reference - 1] & 0xFF;
if (opcode <= 168) {

data[reference - 1] = (byte)(opcode + 49);
} else {

data[reference - 1] = (byte)(opcode + 20);
} 
needUpdate = true;
} 
data[reference++] = (byte)(j >>> 8);
data[reference] = (byte)j; continue;
} 
int offset = position + source + 1;
data[reference++] = (byte)(offset >>> 24);
data[reference++] = (byte)(offset >>> 16);
data[reference++] = (byte)(offset >>> 8);
data[reference] = (byte)offset;
} 

return needUpdate;
}
}

