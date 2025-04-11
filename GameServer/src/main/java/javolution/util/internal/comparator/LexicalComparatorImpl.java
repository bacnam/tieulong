package javolution.util.internal.comparator;

import javolution.lang.MathLib;
import javolution.util.function.Equality;

import java.io.Serializable;

public class LexicalComparatorImpl
        implements Equality<CharSequence>, Serializable {
    private static final long serialVersionUID = 7904852144917623728L;

    public int hashCodeOf(CharSequence csq) {
        if (csq == null)
            return 0;
        if (csq instanceof String)
            return csq.hashCode();
        int h = 0;
        for (int i = 0, n = csq.length(); i < n; ) {
            h = 31 * h + csq.charAt(i++);
        }
        return h;
    }

    public boolean areEqual(CharSequence csq1, CharSequence csq2) {
        if (csq1 == csq2)
            return true;
        if (csq1 == null || csq2 == null)
            return false;
        if (csq1 instanceof String) {
            if (csq2 instanceof String)
                return csq1.equals(csq2);
            return ((String) csq1).contentEquals(csq2);
        }
        if (csq2 instanceof String) return ((String) csq2).contentEquals(csq1);

        int n = csq1.length();
        if (csq2.length() != n)
            return false;
        for (int i = 0; i < n; ) {
            if (csq1.charAt(i) != csq2.charAt(i++))
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
            return ((String) left).compareTo((String) right);
        int i = 0;
        int n = MathLib.min(left.length(), right.length());
        while (n-- != 0) {
            char c1 = left.charAt(i);
            char c2 = right.charAt(i++);
            if (c1 != c2)
                return c1 - c2;
        }
        return left.length() - right.length();
    }
}

