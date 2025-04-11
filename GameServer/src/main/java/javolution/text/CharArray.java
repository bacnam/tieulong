package javolution.text;

import javolution.util.function.Equalities;

public final class CharArray
        implements CharSequence, Comparable<CharSequence> {
    private static final char[] NO_CHAR = new char[0];
    private char[] _array;
    private int _offset;
    private int _length;

    public CharArray() {
        this._array = NO_CHAR;
    }

    public CharArray(int capacity) {
        this._array = new char[capacity];
    }

    public CharArray(String string) {
        this._array = string.toCharArray();
        this._length = string.length();
    }

    public char[] array() {
        return this._array;
    }

    public int length() {
        return this._length;
    }

    public int offset() {
        return this._offset;
    }

    public CharArray setArray(char[] array, int offset, int length) {
        this._array = array;
        this._offset = offset;
        this._length = length;
        return this;
    }

    public final int indexOf(CharSequence csq) {
        char c = csq.charAt(0);
        int csqLength = csq.length();
        for (int i = this._offset, end = this._offset + this._length - csqLength + 1; i < end; i++) {
            if (this._array[i] == c) {
                boolean match = true;
                for (int j = 1; j < csqLength; j++) {
                    if (this._array[i + j] != csq.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) return i - this._offset;
            }
        }
        return -1;
    }

    public final int indexOf(char c) {
        for (int i = this._offset, end = this._offset + this._length; i < end; i++) {
            if (this._array[i] == c)
                return i - this._offset;
        }
        return -1;
    }

    public String toString() {
        return new String(this._array, this._offset, this._length);
    }

    public int hashCode() {
        int h = 0;
        for (int i = 0, j = this._offset; i < this._length; i++) {
            h = 31 * h + this._array[j++];
        }
        return h;
    }

    public boolean equals(Object that) {
        if (that instanceof String)
            return equals((String) that);
        if (that instanceof CharArray)
            return equals((CharArray) that);
        if (that instanceof CharSequence) {
            return equals((CharSequence) that);
        }
        return false;
    }

    private boolean equals(CharSequence chars) {
        if (chars == null)
            return false;
        if (this._length != chars.length())
            return false;
        for (int i = this._length, j = this._offset + this._length; --i >= 0; ) {
            if (this._array[--j] != chars.charAt(i))
                return false;
        }
        return true;
    }

    public boolean equals(CharArray that) {
        if (this == that)
            return true;
        if (that == null)
            return false;
        if (this._length != that._length)
            return false;
        char[] thatArray = that._array;
        for (int i = that._offset + this._length, j = this._offset + this._length; --j >= this._offset; ) {
            if (this._array[j] != thatArray[--i])
                return false;
        }
        return true;
    }

    public boolean equals(String str) {
        if (str == null)
            return false;
        if (this._length != str.length())
            return false;
        for (int i = this._length, j = this._offset + this._length; --i >= 0; ) {
            if (this._array[--j] != str.charAt(i))
                return false;
        }
        return true;
    }

    public int compareTo(CharSequence seq) {
        return Equalities.LEXICAL.compare(this, seq);
    }

    public boolean toBoolean() {
        return TypeFormat.parseBoolean(this);
    }

    public int toInt() {
        return TypeFormat.parseInt(this);
    }

    public int toInt(int radix) {
        return TypeFormat.parseInt(this, radix);
    }

    public long toLong() {
        return TypeFormat.parseLong(this);
    }

    public long toLong(int radix) {
        return TypeFormat.parseLong(this, radix);
    }

    public float toFloat() {
        return TypeFormat.parseFloat(this);
    }

    public double toDouble() {
        return TypeFormat.parseDouble(this);
    }

    public char charAt(int index) {
        if (index < 0 || index >= this._length)
            throw new IndexOutOfBoundsException("index: " + index);
        return this._array[this._offset + index];
    }

    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end < 0 || start > end || end > length())
            throw new IndexOutOfBoundsException();
        CharArray chars = new CharArray();
        chars._array = this._array;
        this._offset += start;
        chars._length = end - start;
        return chars;
    }

    public void getChars(int start, int end, char[] dest, int destPos) {
        if (start < 0 || end < 0 || start > end || end > this._length)
            throw new IndexOutOfBoundsException();
        System.arraycopy(this._array, start + this._offset, dest, destPos, end - start);
    }
}

