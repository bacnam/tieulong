package com.google.protobuf;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class LiteralByteString extends ByteString {

    protected final byte[] bytes;
    private int hash = 0;

    LiteralByteString(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte byteAt(int index) {

        return bytes[index];
    }

    @Override
    public int size() {
        return bytes.length;
    }

    @Override
    public ByteString substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException(
                    "Beginning index: " + beginIndex + " < 0");
        }
        if (endIndex > size()) {
            throw new IndexOutOfBoundsException("End index: " + endIndex + " > " +
                    size());
        }
        int substringLength = endIndex - beginIndex;
        if (substringLength < 0) {
            throw new IndexOutOfBoundsException(
                    "Beginning index larger than ending index: " + beginIndex + ", "
                            + endIndex);
        }

        ByteString result;
        if (substringLength == 0) {
            result = ByteString.EMPTY;
        } else {
            result = new BoundedByteString(bytes, getOffsetIntoBytes() + beginIndex,
                    substringLength);
        }
        return result;
    }

    @Override
    protected void copyToInternal(byte[] target, int sourceOffset,
                                  int targetOffset, int numberToCopy) {

        System.arraycopy(bytes, sourceOffset, target, targetOffset, numberToCopy);
    }

    @Override
    public void copyTo(ByteBuffer target) {
        target.put(bytes, getOffsetIntoBytes(), size());
    }

    @Override
    public ByteBuffer asReadOnlyByteBuffer() {
        ByteBuffer byteBuffer =
                ByteBuffer.wrap(bytes, getOffsetIntoBytes(), size());
        return byteBuffer.asReadOnlyBuffer();
    }

    @Override
    public List<ByteBuffer> asReadOnlyByteBufferList() {

        List<ByteBuffer> result = new ArrayList<ByteBuffer>(1);
        result.add(asReadOnlyByteBuffer());
        return result;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(toByteArray());
    }

    @Override
    public String toString(String charsetName)
            throws UnsupportedEncodingException {
        return new String(bytes, getOffsetIntoBytes(), size(), charsetName);
    }

    @Override
    public boolean isValidUtf8() {
        int offset = getOffsetIntoBytes();
        return Utf8.isValidUtf8(bytes, offset, offset + size());
    }

    @Override
    protected int partialIsValidUtf8(int state, int offset, int length) {
        int index = getOffsetIntoBytes() + offset;
        return Utf8.partialIsValidUtf8(state, bytes, index, index + length);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ByteString)) {
            return false;
        }

        if (size() != ((ByteString) other).size()) {
            return false;
        }
        if (size() == 0) {
            return true;
        }

        if (other instanceof LiteralByteString) {
            return equalsRange((LiteralByteString) other, 0, size());
        } else if (other instanceof RopeByteString) {
            return other.equals(this);
        } else {
            throw new IllegalArgumentException(
                    "Has a new type of ByteString been created? Found "
                            + other.getClass());
        }
    }

    boolean equalsRange(LiteralByteString other, int offset, int length) {
        if (length > other.size()) {
            throw new IllegalArgumentException(
                    "Length too large: " + length + size());
        }
        if (offset + length > other.size()) {
            throw new IllegalArgumentException(
                    "Ran off end of other: " + offset + ", " + length + ", " +
                            other.size());
        }

        byte[] thisBytes = bytes;
        byte[] otherBytes = other.bytes;
        int thisLimit = getOffsetIntoBytes() + length;
        for (int thisIndex = getOffsetIntoBytes(), otherIndex =
             other.getOffsetIntoBytes() + offset;
             (thisIndex < thisLimit); ++thisIndex, ++otherIndex) {
            if (thisBytes[thisIndex] != otherBytes[otherIndex]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = hash;

        if (h == 0) {
            int size = size();
            h = partialHash(size, 0, size);
            if (h == 0) {
                h = 1;
            }
            hash = h;
        }
        return h;
    }

    @Override
    protected int peekCachedHashCode() {
        return hash;
    }

    @Override
    protected int partialHash(int h, int offset, int length) {
        byte[] thisBytes = bytes;
        for (int i = getOffsetIntoBytes() + offset, limit = i + length; i < limit;
             i++) {
            h = h * 31 + thisBytes[i];
        }
        return h;
    }

    @Override
    public InputStream newInput() {
        return new ByteArrayInputStream(bytes, getOffsetIntoBytes(),
                size());
    }

    @Override
    public CodedInputStream newCodedInput() {

        return CodedInputStream
                .newInstance(bytes, getOffsetIntoBytes(), size());
    }

    @Override
    public ByteIterator iterator() {
        return new LiteralByteIterator();
    }

    @Override
    protected int getTreeDepth() {
        return 0;
    }

    @Override
    protected boolean isBalanced() {
        return true;
    }

    protected int getOffsetIntoBytes() {
        return 0;
    }

    private class LiteralByteIterator implements ByteIterator {
        private final int limit;
        private int position;

        private LiteralByteIterator() {
            position = 0;
            limit = size();
        }

        public boolean hasNext() {
            return (position < limit);
        }

        public Byte next() {

            return nextByte();
        }

        public byte nextByte() {
            try {
                return bytes[position++];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
