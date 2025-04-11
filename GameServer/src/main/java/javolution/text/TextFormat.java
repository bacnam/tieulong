package javolution.text;

import javolution.lang.Parallelizable;

import java.io.IOException;

@Parallelizable
public abstract class TextFormat<T> {
    public abstract T parse(CharSequence paramCharSequence, Cursor paramCursor);

    public abstract Appendable format(T paramT, Appendable paramAppendable) throws IOException;

    public T parse(CharSequence csq) throws IllegalArgumentException {
        Cursor cursor = new Cursor();
        T obj = parse(csq, cursor);
        if (!cursor.atEnd(csq)) {
            throw new IllegalArgumentException("Extraneous character(s) \"" + cursor.tail(csq) + "\"");
        }
        return obj;
    }

    public TextBuilder format(T obj, TextBuilder dest) {
        try {
            format(obj, dest);
            return dest;
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public String format(T obj) {
        return format(obj, new TextBuilder()).toString();
    }
}

