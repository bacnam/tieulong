package org.apache.mina.core.buffer;

class IoBufferHexDumper
{
private static final byte[] highDigits;
private static final byte[] lowDigits;

static {
byte[] digits = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };

byte[] high = new byte[256];
byte[] low = new byte[256];

for (int i = 0; i < 256; i++) {
high[i] = digits[i >>> 4];
low[i] = digits[i & 0xF];
} 

highDigits = high;
lowDigits = low;
}

public static String getHexdump(IoBuffer in, int lengthLimit) {
int size;
if (lengthLimit == 0) {
throw new IllegalArgumentException("lengthLimit: " + lengthLimit + " (expected: 1+)");
}

boolean truncate = (in.remaining() > lengthLimit);

if (truncate) {
size = lengthLimit;
} else {
size = in.remaining();
} 

if (size == 0) {
return "empty";
}

StringBuilder out = new StringBuilder(size * 3 + 3);

int mark = in.position();

int byteValue = in.get() & 0xFF;
out.append((char)highDigits[byteValue]);
out.append((char)lowDigits[byteValue]);
size--;

for (; size > 0; size--) {
out.append(' ');
byteValue = in.get() & 0xFF;
out.append((char)highDigits[byteValue]);
out.append((char)lowDigits[byteValue]);
} 

in.position(mark);

if (truncate) {
out.append("...");
}

return out.toString();
}
}

