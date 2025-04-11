package javolution.text;

import javolution.lang.MathLib;
import javolution.lang.Realtime;
import javolution.lang.ValueType;
import javolution.util.FastMap;
import javolution.util.function.Equalities;
import javolution.xml.XMLSerializable;

import java.io.PrintStream;

@Realtime
public final class Text
        implements CharSequence, Comparable<CharSequence>, XMLSerializable, ValueType<Text> {
    private static final long serialVersionUID = 1536L;
    private static final int BLOCK_SIZE = 32;
    private static final int BLOCK_MASK = -32;
    private static final FastMap<Text, Text> INTERN = (new FastMap()).shared();

    public static final Text EMPTY = (new Text("")).intern();
    private static final Text TRUE = (new Text("true")).intern();
    private static final Text FALSE = (new Text("false")).intern();
    private final char[] _data;
    private int _count;
    private Text _head;
    private Text _tail;

    private Text(boolean isPrimitive) {
        this._data = isPrimitive ? new char[32] : null;
    }

    public Text(String str) {
        this((str.length() <= 32));
        this._count = str.length();
        if (this._data != null) {
            str.getChars(0, this._count, this._data, 0);
        } else {
            int half = this._count + 32 >> 1 & 0xFFFFFFE0;
            this._head = new Text(str.substring(0, half));
            this._tail = new Text(str.substring(half, this._count));
        }
    }

    public static Text valueOf(Object obj) {
        return (new TextBuilder()).append(obj).toText();
    }

    private static Text valueOf(String str) {
        return valueOf(str, 0, str.length());
    }

    private static Text valueOf(String str, int start, int end) {
        int length = end - start;
        if (length <= 32) {
            Text text = newPrimitive(length);
            str.getChars(start, end, text._data, 0);
            return text;
        }
        int half = length + 32 >> 1 & 0xFFFFFFE0;
        return newComposite(valueOf(str, start, start + half), valueOf(str, start + half, end));
    }

    public static Text valueOf(char[] chars) {
        return valueOf(chars, 0, chars.length);
    }

    public static Text valueOf(char[] chars, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > chars.length)
            throw new IndexOutOfBoundsException();
        if (length <= 32) {
            Text text = newPrimitive(length);
            System.arraycopy(chars, offset, text._data, 0, length);
            return text;
        }
        int half = length + 32 >> 1 & 0xFFFFFFE0;
        return newComposite(valueOf(chars, offset, half), valueOf(chars, offset + half, length - half));
    }

    static Text valueOf(TextBuilder tb, int start, int end) {
        int length = end - start;
        if (length <= 32) {
            Text text = newPrimitive(length);
            tb.getChars(start, end, text._data, 0);
            return text;
        }
        int half = length + 32 >> 1 & 0xFFFFFFE0;
        return newComposite(valueOf(tb, start, start + half), valueOf(tb, start + half, end));
    }

    public static Text valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static Text valueOf(char c) {
        Text text = newPrimitive(1);
        text._data[0] = c;
        return text;
    }

    public static Text valueOf(int i) {
        TextBuilder tb = new TextBuilder();
        return tb.append(i).toText();
    }

    public static Text valueOf(int i, int radix) {
        TextBuilder tb = new TextBuilder();
        return tb.append(i, radix).toText();
    }

    public static Text valueOf(long l) {
        TextBuilder tb = new TextBuilder();
        return tb.append(l).toText();
    }

    public static Text valueOf(long l, int radix) {
        TextBuilder tb = new TextBuilder();
        return tb.append(l, radix).toText();
    }

    public static Text valueOf(float f) {
        TextBuilder tb = new TextBuilder();
        return tb.append(f).toText();
    }

    public static Text valueOf(double d) {
        TextBuilder tb = new TextBuilder();
        return tb.append(d).toText();
    }

    public static Text valueOf(double d, int digits, boolean scientific, boolean showZero) {
        TextBuilder tb = new TextBuilder();
        return tb.append(d, digits, scientific, showZero).toText();
    }

    public static Text valueOf(char c, int length) {
        if (length < 0)
            throw new IndexOutOfBoundsException();
        if (length <= 32) {
            Text text = newPrimitive(length);
            for (int i = 0; i < length; ) {
                text._data[i++] = c;
            }
            return text;
        }
        int middle = length >> 1;
        return newComposite(valueOf(c, middle), valueOf(c, length - middle));
    }

    private static Text newPrimitive(int length) {
        Text text = new Text(true);
        text._count = length;
        return text;
    }

    private static Text newComposite(Text head, Text tail) {
        Text text = new Text(false);
        head._count += tail._count;
        text._head = head;
        text._tail = tail;
        return text;
    }

    public int length() {
        return this._count;
    }

    public Text plus(Object obj) {
        return concat(valueOf(obj));
    }

    public Text plus(String str) {
        Text merge = append(str);
        return (merge != null) ? merge : concat(valueOf(str));
    }

    private Text append(String str) {
        int length = str.length();
        if (this._data == null) {
            Text merge = this._tail.append(str);
            return (merge != null) ? newComposite(this._head, merge) : null;
        }
        if (this._count + length > 32)
            return null;
        Text text = newPrimitive(this._count + length);
        System.arraycopy(this._data, 0, text._data, 0, this._count);
        str.getChars(0, length, text._data, this._count);
        return text;
    }

    public Text concat(Text that) {
        int length = this._count + that._count;
        if (length <= 32) {
            Text text = newPrimitive(length);
            getChars(0, this._count, text._data, 0);
            that.getChars(0, that._count, text._data, this._count);
            return text;
        }

        Text head = this;
        Text tail = that;

        if (head._count << 1 < tail._count && tail._data == null) {

            if (tail._head._count > tail._tail._count) {
                tail = tail.rightRotation();
            }
            head = head.concat(tail._head);
            tail = tail._tail;
        } else if (tail._count << 1 < head._count && head._data == null) {

            if (head._tail._count > head._head._count) {
                head = head.leftRotation();
            }
            tail = head._tail.concat(tail);
            head = head._head;
        }
        return newComposite(head, tail);
    }

    private Text rightRotation() {
        Text P = this._head;
        if (P._data != null)
            return this;
        Text A = P._head;
        Text B = P._tail;
        Text C = this._tail;
        return newComposite(A, newComposite(B, C));
    }

    private Text leftRotation() {
        Text Q = this._tail;
        if (Q._data != null)
            return this;
        Text B = Q._head;
        Text C = Q._tail;
        Text A = this._head;
        return newComposite(newComposite(A, B), C);
    }

    public Text subtext(int start) {
        return subtext(start, length());
    }

    public Text insert(int index, Text txt) {
        return subtext(0, index).concat(txt).concat(subtext(index));
    }

    public Text delete(int start, int end) {
        if (start > end)
            throw new IndexOutOfBoundsException();
        return subtext(0, start).concat(subtext(end));
    }

    public Text replace(CharSequence target, CharSequence replacement) {
        int i = indexOf(target);
        return (i < 0) ? this : subtext(0, i).concat(valueOf(replacement)).concat(subtext(i + target.length()).replace(target, replacement));
    }

    public Text replace(CharSet charSet, CharSequence replacement) {
        int i = indexOfAny(charSet);
        return (i < 0) ? this : subtext(0, i).concat(valueOf(replacement)).concat(subtext(i + 1).replace(charSet, replacement));
    }

    public CharSequence subSequence(int start, int end) {
        return subtext(start, end);
    }

    public int indexOf(CharSequence csq) {
        return indexOf(csq, 0);
    }

    public int indexOf(CharSequence csq, int fromIndex) {
        int csqLength = csq.length();
        int min = Math.max(0, fromIndex);
        int max = this._count - csqLength;
        if (csqLength == 0) return (min > max) ? -1 : min;

        char c = csq.charAt(0);
        int i;
        for (i = indexOf(c, min); i >= 0 && i <= max; i = indexOf(c, ++i)) {

            boolean match = true;
            for (int j = 1; j < csqLength; j++) {
                if (charAt(i + j) != csq.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) return i;
        }
        return -1;
    }

    public int lastIndexOf(CharSequence csq) {
        return lastIndexOf(csq, this._count);
    }

    public int lastIndexOf(CharSequence csq, int fromIndex) {
        int csqLength = csq.length();
        int min = 0;
        int max = Math.min(fromIndex, this._count - csqLength);
        if (csqLength == 0) return (0 > max) ? -1 : max;

        char c = csq.charAt(0);
        int i;
        for (i = lastIndexOf(c, max); i >= 0; i = lastIndexOf(c, --i)) {
            boolean match = true;
            for (int j = 1; j < csqLength; j++) {
                if (charAt(i + j) != csq.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) return i;
        }
        return -1;
    }

    public boolean startsWith(CharSequence prefix) {
        return startsWith(prefix, 0);
    }

    public boolean endsWith(CharSequence suffix) {
        return startsWith(suffix, length() - suffix.length());
    }

    public boolean startsWith(CharSequence prefix, int index) {
        int prefixLength = prefix.length();
        if (index >= 0 && index <= length() - prefixLength) {
            for (int i = 0, j = index; i < prefixLength; ) {
                if (prefix.charAt(i++) != charAt(j++)) return false;
            }
            return true;
        }
        return false;
    }

    public Text trim() {
        int first = 0;
        int last = length() - 1;
        while (first <= last && charAt(first) <= ' ') {
            first++;
        }
        while (last >= first && charAt(last) <= ' ') {
            last--;
        }
        return subtext(first, last + 1);
    }

    public Text intern() {
        Text txt = (Text) INTERN.putIfAbsent(this, this);
        return (txt == null) ? this : txt;
    }

    public boolean contentEquals(CharSequence csq) {
        if (csq.length() != this._count)
            return false;
        for (int i = 0; i < this._count; ) {
            if (charAt(i) != csq.charAt(i++))
                return false;
        }
        return true;
    }

    public boolean contentEqualsIgnoreCase(CharSequence csq) {
        if (this._count != csq.length())
            return false;
        for (int i = 0; i < this._count; ) {
            char u1 = charAt(i);
            char u2 = csq.charAt(i++);
            if (u1 != u2) {
                u1 = Character.toUpperCase(u1);
                u2 = Character.toUpperCase(u2);
                if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {

                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Text))
            return false;
        Text that = (Text) obj;
        if (this._count != that._count)
            return false;
        for (int i = 0; i < this._count; ) {
            if (charAt(i) != that.charAt(i++))
                return false;
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        int length = length();
        for (int i = 0; i < length; ) {
            h = 31 * h + charAt(i++);
        }
        return h;
    }

    public int compareTo(CharSequence csq) {
        return Equalities.LEXICAL.compare(this, csq);
    }

    public Text toText() {
        return this;
    }

    public void printStatistics(PrintStream out) {
        int length = length();
        int leaves = getNbrOfLeaves();
        synchronized (out) {
            out.print("LENGTH: " + length());
            out.print(", MAX DEPTH: " + getDepth());
            out.print(", NBR OF BRANCHES: " + getNbrOfBranches());
            out.print(", NBR OF LEAVES: " + leaves);
            out.print(", AVG LEAVE LENGTH: " + ((length + (leaves >> 1)) / leaves));

            out.println();
        }
    }

    private int getDepth() {
        if (this._data != null)
            return 0;
        return MathLib.max(this._head.getDepth(), this._tail.getDepth()) + 1;
    }

    private int getNbrOfBranches() {
        return (this._data == null) ? (this._head.getNbrOfBranches() + this._tail.getNbrOfBranches() + 1) : 0;
    }

    private int getNbrOfLeaves() {
        return (this._data == null) ? (this._head.getNbrOfLeaves() + this._tail.getNbrOfLeaves()) : 1;
    }

    public Text toLowerCase() {
        if (this._data == null)
            return newComposite(this._head.toLowerCase(), this._tail.toLowerCase());
        Text text = newPrimitive(this._count);
        for (int i = 0; i < this._count; ) {
            text._data[i] = Character.toLowerCase(this._data[i++]);
        }
        return text;
    }

    public Text toUpperCase() {
        if (this._data == null)
            return newComposite(this._head.toUpperCase(), this._tail.toUpperCase());
        Text text = newPrimitive(this._count);
        for (int i = 0; i < this._count; ) {
            text._data[i] = Character.toUpperCase(this._data[i++]);
        }
        return text;
    }

    public char charAt(int index) {
        if (index >= this._count)
            throw new IndexOutOfBoundsException();
        return (this._data != null) ? this._data[index] : ((index < this._head._count) ? this._head.charAt(index) : this._tail.charAt(index - this._head._count));
    }

    public int indexOf(char c) {
        return indexOf(c, 0);
    }

    public int indexOf(char c, int fromIndex) {
        if (this._data != null) {
            for (int i = MathLib.max(fromIndex, 0); i < this._count; i++) {
                if (this._data[i] == c)
                    return i;
            }
            return -1;
        }
        int cesure = this._head._count;
        if (fromIndex < cesure) {
            int headIndex = this._head.indexOf(c, fromIndex);
            if (headIndex >= 0)
                return headIndex;
        }
        int tailIndex = this._tail.indexOf(c, fromIndex - cesure);
        return (tailIndex >= 0) ? (tailIndex + cesure) : -1;
    }

    public int lastIndexOf(char c, int fromIndex) {
        if (this._data != null) {
            for (int i = MathLib.min(fromIndex, this._count - 1); i >= 0; i--) {
                if (this._data[i] == c)
                    return i;
            }
            return -1;
        }
        int cesure = this._head._count;
        if (fromIndex >= cesure) {
            int tailIndex = this._tail.lastIndexOf(c, fromIndex - cesure);
            if (tailIndex >= 0)
                return tailIndex + cesure;
        }
        return this._head.lastIndexOf(c, fromIndex);
    }

    public Text subtext(int start, int end) {
        if (this._data != null) {
            if (start < 0 || start > end || end > this._count)
                throw new IndexOutOfBoundsException();
            if (start == 0 && end == this._count)
                return this;
            if (start == end)
                return EMPTY;
            int length = end - start;
            Text text = newPrimitive(length);
            System.arraycopy(this._data, start, text._data, 0, length);
            return text;
        }
        int cesure = this._head._count;
        if (end <= cesure)
            return this._head.subtext(start, end);
        if (start >= cesure)
            return this._tail.subtext(start - cesure, end - cesure);
        if (start == 0 && end == this._count) {
            return this;
        }
        return this._head.subtext(start, cesure).concat(this._tail.subtext(0, end - cesure));
    }

    public void getChars(int start, int end, char[] dest, int destPos) {
        if (this._data != null) {
            if (start < 0 || end > this._count || start > end)
                throw new IndexOutOfBoundsException();
            System.arraycopy(this._data, start, dest, destPos, end - start);
        } else {
            int cesure = this._head._count;
            if (end <= cesure) {
                this._head.getChars(start, end, dest, destPos);
            } else if (start >= cesure) {
                this._tail.getChars(start - cesure, end - cesure, dest, destPos);
            } else {
                this._head.getChars(start, cesure, dest, destPos);
                this._tail.getChars(0, end - cesure, dest, destPos + cesure - start);
            }
        }
    }

    public String toString() {
        if (this._data != null) {
            return new String(this._data, 0, this._count);
        }
        char[] data = new char[this._count];
        getChars(0, this._count, data, 0);
        return new String(data, 0, this._count);
    }

    public Text copy() {
        if (this._data != null) {
            Text text = newPrimitive(this._count);
            System.arraycopy(this._data, 0, text._data, 0, this._count);
            return text;
        }
        return newComposite(this._head.copy(), this._tail.copy());
    }

    public boolean isBlank() {
        return isBlank(0, length());
    }

    public boolean isBlank(int start, int length) {
        for (; start < length; start++) {
            if (charAt(start) > ' ')
                return false;
        }
        return true;
    }

    public Text trimStart() {
        int first = 0;
        int last = length() - 1;
        while (first <= last && charAt(first) <= ' ') {
            first++;
        }
        return subtext(first, last + 1);
    }

    public Text trimEnd() {
        int first = 0;
        int last = length() - 1;
        while (last >= first && charAt(last) <= ' ') {
            last--;
        }
        return subtext(first, last + 1);
    }

    public Text padLeft(int len) {
        return padLeft(len, ' ');
    }

    public Text padLeft(int len, char c) {
        int padSize = (len <= length()) ? 0 : (len - length());
        return insert(0, valueOf(c, padSize));
    }

    public Text padRight(int len) {
        return padRight(len, ' ');
    }

    public Text padRight(int len, char c) {
        int padSize = (len <= length()) ? 0 : (len - length());
        return concat(valueOf(c, padSize));
    }

    public int indexOfAny(CharSet charSet) {
        return indexOfAny(charSet, 0, length());
    }

    public int indexOfAny(CharSet charSet, int start) {
        return indexOfAny(charSet, start, length() - start);
    }

    public int indexOfAny(CharSet charSet, int start, int length) {
        int stop = start + length;
        for (int i = start; i < stop; i++) {
            if (charSet.contains(charAt(i)))
                return i;
        }
        return -1;
    }

    public int lastIndexOfAny(CharSet charSet) {
        return lastIndexOfAny(charSet, 0, length());
    }

    public int lastIndexOfAny(CharSet charSet, int start) {
        return lastIndexOfAny(charSet, start, length() - start);
    }

    public int lastIndexOfAny(CharSet charSet, int start, int length) {
        for (int i = start + length; --i >= start; ) {
            if (charSet.contains(charAt(i)))
                return i;
        }
        return -1;
    }

    public Text value() {
        return this;
    }
}

