package javolution.util.internal.comparator;

public class LexicalFastComparatorImpl
        extends LexicalComparatorImpl {
    private static final long serialVersionUID = -1449702752185594025L;

    public int hashCodeOf(CharSequence csq) {
        if (csq == null)
            return 0;
        int n = csq.length();
        if (n == 0) {
            return 0;
        }
        return csq.charAt(0) + csq.charAt(n - 1) * 31 + csq.charAt(n >> 1) * 1009 + csq.charAt(n >> 2) * 27583 + csq.charAt(n - 1 - (n >> 2)) * 73408859;
    }
}

