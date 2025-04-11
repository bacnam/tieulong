package javolution.util.internal.comparator;

import javolution.util.function.Equality;

import java.io.Serializable;

public class LexicalCaseInsensitiveComparatorImpl
        implements Equality<CharSequence>, Serializable {
    private static final long serialVersionUID = -1046672327934410697L;

    private static char up(char c) {
        return Character.toUpperCase(c);
    }

    public int hashCodeOf(CharSequence csq) {
        if (csq == null)
            return 0;
        int h = 0;
        for (int i = 0, n = csq.length(); i < n; ) {
            h = 31 * h + up(csq.charAt(i++));
        }
        return h;
    }

    public boolean areEqual(CharSequence csq1, CharSequence csq2) {
        if (csq1 == csq2)
            return true;
        if (csq1 == null || csq2 == null)
            return false;
        if (csq1 instanceof String && csq2 instanceof String)
            return ((String) csq1).equalsIgnoreCase((String) csq2);
        int n = csq1.length();
        if (csq2.length() != n)
            return false;
        for (int i = 0; i < n; ) {
            if (up(csq1.charAt(i)) != up(csq2.charAt(i++)))
                return false;
        }
        return true;
    }

    public int compare(CharSequence left, CharSequence right) {
        if (left == null)
            return -1;
        if (right == null)
            return 1;
        if (left instanceof String && right instanceof String)
            return ((String) left).compareToIgnoreCase((String) right);
        int i = 0;
        int n = Math.min(left.length(), right.length());
        while (n-- != 0) {
            char c1 = up(left.charAt(i));
            char c2 = up(right.charAt(i++));
            if (c1 != c2)
                return c1 - c2;
        }
        return left.length() - right.length();
    }
}

