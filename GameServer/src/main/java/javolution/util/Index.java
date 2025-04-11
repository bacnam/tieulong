package javolution.util;

import javolution.lang.Configurable;
import javolution.lang.MathLib;
import javolution.lang.Realtime;
import javolution.lang.ValueType;
import javolution.text.*;

import java.io.IOException;
import java.io.ObjectStreamException;

@Realtime
@DefaultTextFormat(Index.Decimal.class)
public final class Index
        extends Number
        implements Comparable<Index>, ValueType<Index> {
    public static final Configurable<Integer> UNIQUE = new Configurable<Integer>() {
        protected Integer getDefault() {
            return Integer.valueOf(1024);
        }

        protected Integer initialized(Integer value) {
            return Integer.valueOf(MathLib.min(value.intValue(), 65536));
        }

        protected Integer reconfigured(Integer oldCount, Integer newCount) {
            throw new UnsupportedOperationException("Unicity reconfiguration not supported.");
        }
    };
    public static final Index ZERO = new Index(0);
    private static final long serialVersionUID = 1536L;
    private static final Index[] INSTANCES = new Index[((Integer) UNIQUE.get()).intValue()];

    static {
        INSTANCES[0] = ZERO;
        for (int i = 1; i < INSTANCES.length; i++) {
            INSTANCES[i] = new Index(i);
        }
    }

    private final int value;

    private Index(int value) {
        this.value = value;
    }

    public static Index valueOf(int value) {
        return (value < INSTANCES.length) ? INSTANCES[value] : new Index(value);
    }

    public int compareTo(Index that) {
        return this.value - that.value;
    }

    public int compareTo(int value) {
        return this.value - value;
    }

    public Index copy() {
        return (this.value < INSTANCES.length) ? this : new Index(this.value);
    }

    public double doubleValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        return (this.value < INSTANCES.length) ? ((this == obj)) : ((obj instanceof Index) ? ((((Index) obj).value == this.value)) : false);
    }

    public float floatValue() {
        return this.value;
    }

    public int hashCode() {
        return this.value;
    }

    public int intValue() {
        return this.value;
    }

    public boolean isZero() {
        return (this == ZERO);
    }

    public long longValue() {
        return this.value;
    }

    public Index next() {
        return valueOf(this.value + 1);
    }

    public Index previous() {
        return valueOf(this.value - 1);
    }

    protected final Object readResolve() throws ObjectStreamException {
        return valueOf(this.value);
    }

    public String toString() {
        return TextContext.getFormat(Index.class).format(this);
    }

    public Index value() {
        return this;
    }

    public static class Decimal
            extends TextFormat<Index> {
        public Appendable format(Index obj, Appendable dest) throws IOException {
            return TypeFormat.format(obj.intValue(), dest);
        }

        public Index parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
            return Index.valueOf(TypeFormat.parseInt(csq, cursor));
        }
    }
}

