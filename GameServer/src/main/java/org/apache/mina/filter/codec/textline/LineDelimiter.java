package org.apache.mina.filter.codec.textline;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class LineDelimiter {
    public static final LineDelimiter DEFAULT;
    public static final LineDelimiter AUTO = new LineDelimiter("");
    public static final LineDelimiter CRLF = new LineDelimiter("\r\n");
    public static final LineDelimiter UNIX = new LineDelimiter("\n");
    public static final LineDelimiter WINDOWS = CRLF;
    public static final LineDelimiter MAC = new LineDelimiter("\r");
    public static final LineDelimiter NUL = new LineDelimiter("\000");

    static {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(bout, true);
        out.println();
        DEFAULT = new LineDelimiter(new String(bout.toByteArray()));
    }

    private final String value;

    public LineDelimiter(String value) {
        if (value == null) {
            throw new IllegalArgumentException("delimiter");
        }

        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof LineDelimiter)) {
            return false;
        }

        LineDelimiter that = (LineDelimiter) o;

        return this.value.equals(that.value);
    }

    public String toString() {
        if (this.value.length() == 0) {
            return "delimiter: auto";
        }
        StringBuilder buf = new StringBuilder();
        buf.append("delimiter:");

        for (int i = 0; i < this.value.length(); i++) {
            buf.append(" 0x");
            buf.append(Integer.toHexString(this.value.charAt(i)));
        }

        return buf.toString();
    }
}

