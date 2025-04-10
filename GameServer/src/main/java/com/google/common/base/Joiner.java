package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible
public class Joiner
{
private final String separator;

public static Joiner on(String separator) {
return new Joiner(separator);
}

public static Joiner on(char separator) {
return new Joiner(String.valueOf(separator));
}

private Joiner(String separator) {
this.separator = Preconditions.<String>checkNotNull(separator);
}

private Joiner(Joiner prototype) {
this.separator = prototype.separator;
}

public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
Preconditions.checkNotNull(appendable);
Iterator<?> iterator = parts.iterator();
if (iterator.hasNext()) {
appendable.append(toString(iterator.next()));
while (iterator.hasNext()) {
appendable.append(this.separator);
appendable.append(toString(iterator.next()));
} 
} 
return appendable;
}

public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
return appendTo(appendable, Arrays.asList(parts));
}

public final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object... rest) throws IOException {
return appendTo(appendable, iterable(first, second, rest));
}

public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
try {
appendTo(builder, parts);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
return builder;
}

public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
return appendTo(builder, Arrays.asList(parts));
}

public final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object... rest) {
return appendTo(builder, iterable(first, second, rest));
}

public final String join(Iterable<?> parts) {
return appendTo(new StringBuilder(), parts).toString();
}

public final String join(Object[] parts) {
return join(Arrays.asList(parts));
}

public final String join(@Nullable Object first, @Nullable Object second, Object... rest) {
return join(iterable(first, second, rest));
}

@CheckReturnValue
public Joiner useForNull(final String nullText) {
Preconditions.checkNotNull(nullText);
return new Joiner(this) {
CharSequence toString(Object part) {
return (part == null) ? nullText : Joiner.this.toString(part);
}

public Joiner useForNull(String nullText) {
Preconditions.checkNotNull(nullText);
throw new UnsupportedOperationException("already specified useForNull");
}

public Joiner skipNulls() {
throw new UnsupportedOperationException("already specified useForNull");
}
};
}

@CheckReturnValue
public Joiner skipNulls() {
return new Joiner(this)
{
public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
Preconditions.checkNotNull(appendable, "appendable");
Preconditions.checkNotNull(parts, "parts");
Iterator<?> iterator = parts.iterator();
while (iterator.hasNext()) {
Object part = iterator.next();
if (part != null) {
appendable.append(Joiner.this.toString(part));
break;
} 
} 
while (iterator.hasNext()) {
Object part = iterator.next();
if (part != null) {
appendable.append(Joiner.this.separator);
appendable.append(Joiner.this.toString(part));
} 
} 
return appendable;
}

public Joiner useForNull(String nullText) {
Preconditions.checkNotNull(nullText);
throw new UnsupportedOperationException("already specified skipNulls");
}

public Joiner.MapJoiner withKeyValueSeparator(String kvs) {
Preconditions.checkNotNull(kvs);
throw new UnsupportedOperationException("can't use .skipNulls() with maps");
}
};
}

@CheckReturnValue
public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
return new MapJoiner(this, keyValueSeparator);
}

public static final class MapJoiner
{
private final Joiner joiner;

private final String keyValueSeparator;

private MapJoiner(Joiner joiner, String keyValueSeparator) {
this.joiner = joiner;
this.keyValueSeparator = Preconditions.<String>checkNotNull(keyValueSeparator);
}

public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
return appendTo(appendable, map.entrySet());
}

public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
return appendTo(builder, map.entrySet());
}

public String join(Map<?, ?> map) {
return join(map.entrySet());
}

@Beta
public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Map.Entry<?, ?>> entries) throws IOException {
Preconditions.checkNotNull(appendable);
Iterator<? extends Map.Entry<?, ?>> iterator = entries.iterator();
if (iterator.hasNext()) {
Map.Entry<?, ?> entry = iterator.next();
appendable.append(this.joiner.toString(entry.getKey()));
appendable.append(this.keyValueSeparator);
appendable.append(this.joiner.toString(entry.getValue()));
while (iterator.hasNext()) {
appendable.append(this.joiner.separator);
Map.Entry<?, ?> e = iterator.next();
appendable.append(this.joiner.toString(e.getKey()));
appendable.append(this.keyValueSeparator);
appendable.append(this.joiner.toString(e.getValue()));
} 
} 
return appendable;
}

@Beta
public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Map.Entry<?, ?>> entries) {
try {
appendTo(builder, entries);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
return builder;
}

@Beta
public String join(Iterable<? extends Map.Entry<?, ?>> entries) {
return appendTo(new StringBuilder(), entries).toString();
}

@CheckReturnValue
public MapJoiner useForNull(String nullText) {
return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
}
}

CharSequence toString(Object part) {
Preconditions.checkNotNull(part);
return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
}

private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
Preconditions.checkNotNull(rest);
return new AbstractList() {
public int size() {
return rest.length + 2;
}

public Object get(int index) {
switch (index) {
case 0:
return first;
case 1:
return second;
} 
return rest[index - 2];
}
};
}
}

