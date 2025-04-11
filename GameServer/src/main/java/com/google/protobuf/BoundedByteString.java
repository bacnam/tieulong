package com.google.protobuf;

import java.util.NoSuchElementException;

class BoundedByteString extends LiteralByteString {

    private final int bytesOffset;
    private final int bytesLength;

    BoundedByteString(byte[] bytes, int offset, int length) {
        super(bytes);
        if (offset < 0) {
            throw new IllegalArgumentException("Offset too small: " + offset);
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length too small: " + offset);
        }
        if ((long) offset + length > bytes.length) {
            throw new IllegalArgumentException(
                    "Offset+Length too large: " + offset + "+" + length);
        }

        this.bytesOffset = offset;
        this.bytesLength = length;
    }

    @Override
    public byte byteAt(int index) {

        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index too small: " + index);
        }
        if (index >= size()) {
            throw new ArrayIndexOutOfBoundsException(
                    "Index too large: " + index + ", " + size());
        }

        return bytes[bytesOffset + index];
    }

    @Override
    public int size() {
        return bytesLength;
    }

    @Override
    protected int getOffsetIntoBytes() {
        return bytesOffset;
    }

    @Override
    protected void copyToInternal(byte[] target, int sourceOffset,
                                  int targetOffset, int numberToCopy) {
        System.arraycopy(bytes, getOffsetIntoBytes() + sourceOffset, target,
                targetOffset, numberToCopy);
    }

    @Override
    public ByteIterator iterator() {
        return new BoundedByteIterator();
    }

    private class BoundedByteIterator implements ByteIterator {

        private final int limit;
        private int position;

        private BoundedByteIterator() {
            position = getOffsetIntoBytes();
            limit = position + size();
        }

        public boolean hasNext() {
            return (position < limit);
        }

        public Byte next() {

            return nextByte();
        }

        public byte nextByte() {
            if (position >= limit) {
                throw new NoSuchElementException();
            }
            return bytes[position++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
