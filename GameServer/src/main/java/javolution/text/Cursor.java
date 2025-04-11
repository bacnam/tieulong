package javolution.text;

public class Cursor {
    private int index;

    public final int getIndex() {
        return this.index;
    }

    public final void setIndex(int i) {
        this.index = i;
    }

    public final boolean atEnd(CharSequence csq) {
        return (this.index >= csq.length());
    }

    public final boolean at(char c, CharSequence csq) {
        return (this.index < csq.length()) ? ((csq.charAt(this.index) == c)) : false;
    }

    public final boolean at(CharSet charSet, CharSequence csq) {
        return (this.index < csq.length()) ? charSet.contains(csq.charAt(this.index)) : false;
    }

    public final boolean at(String str, CharSequence csq) {
        int i = this.index;
        int length = csq.length();
        for (int j = 0; j < str.length(); ) {
            if (i >= length || str.charAt(j++) != csq.charAt(i++))
                return false;
        }
        return true;
    }

    public final char currentChar(CharSequence csq) {
        return csq.charAt(this.index);
    }

    public final char nextChar(CharSequence csq) {
        return csq.charAt(this.index++);
    }

    public final boolean skipAny(char c, CharSequence csq) {
        int i = this.index;
        int n = csq.length();
        for (; i < n && csq.charAt(i) == c; i++) ;
        if (i == this.index)
            return false;
        this.index = i;
        return true;
    }

    public final boolean skipAny(CharSet charSet, CharSequence csq) {
        int i = this.index;
        int n = csq.length();
        for (; i < n && charSet.contains(csq.charAt(i)); i++) ;
        if (i == this.index)
            return false;
        this.index = i;
        return true;
    }

    public final boolean skip(char c, CharSequence csq) {
        if (at(c, csq)) {
            this.index++;
            return true;
        }
        return false;
    }

    public final boolean skip(CharSet charSet, CharSequence csq) {
        if (at(charSet, csq)) {
            this.index++;
            return true;
        }
        return false;
    }

    public final boolean skip(String str, CharSequence csq) {
        if (at(str, csq)) {
            this.index += str.length();
            return true;
        }
        return false;
    }

    public final CharSequence nextToken(CharSequence csq, char c) {
        int n = csq.length();
        for (int i = this.index; i < n; i++) {
            if (csq.charAt(i) != c) {
                int j = i;
                while (++j < n && csq.charAt(j) != c) ;

                this.index = j;
                return csq.subSequence(i, j);
            }
        }
        this.index = n;
        return null;
    }

    public final CharSequence nextToken(CharSequence csq, CharSet charSet) {
        int n = csq.length();
        for (int i = this.index; i < n; i++) {
            if (!charSet.contains(csq.charAt(i))) {
                int j = i;
                while (++j < n && !charSet.contains(csq.charAt(j))) ;

                this.index = j;
                return csq.subSequence(i, j);
            }
        }
        this.index = n;
        return null;
    }

    public final CharSequence head(CharSequence csq) {
        return csq.subSequence(0, this.index);
    }

    public final CharSequence tail(CharSequence csq) {
        return csq.subSequence(this.index, csq.length());
    }

    public final Cursor increment() {
        return increment(1);
    }

    public final Cursor increment(int i) {
        this.index += i;
        return this;
    }

    public String toString() {
        return "Cursor: " + this.index;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Cursor))
            return false;
        return (this.index == ((Cursor) obj).index);
    }

    public int hashCode() {
        return this.index;
    }
}

