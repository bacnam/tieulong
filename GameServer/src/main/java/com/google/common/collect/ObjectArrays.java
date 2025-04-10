package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Collection;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class ObjectArrays
{
@GwtIncompatible("Array.newInstance(Class, int)")
public static <T> T[] newArray(Class<T> type, int length) {
return Platform.newArray(type, length);
}

public static <T> T[] newArray(T[] reference, int length) {
return Platform.newArray(reference, length);
}

@GwtIncompatible("Array.newInstance(Class, int)")
public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
T[] result = newArray(type, first.length + second.length);
Platform.unsafeArrayCopy((Object[])first, 0, (Object[])result, 0, first.length);
Platform.unsafeArrayCopy((Object[])second, 0, (Object[])result, first.length, second.length);
return result;
}

public static <T> T[] concat(@Nullable T element, T[] array) {
T[] result = newArray(array, array.length + 1);
result[0] = element;
Platform.unsafeArrayCopy((Object[])array, 0, (Object[])result, 1, array.length);
return result;
}

public static <T> T[] concat(T[] array, @Nullable T element) {
T[] result = arraysCopyOf(array, array.length + 1);
result[array.length] = element;
return result;
}

static <T> T[] arraysCopyOf(T[] original, int newLength) {
T[] copy = newArray(original, newLength);
Platform.unsafeArrayCopy((Object[])original, 0, (Object[])copy, 0, Math.min(original.length, newLength));

return copy;
}

static <T> T[] toArrayImpl(Collection<?> c, T[] array) {
int size = c.size();
if (array.length < size) {
array = newArray(array, size);
}
fillArray(c, (Object[])array);
if (array.length > size) {
array[size] = null;
}
return array;
}

static Object[] toArrayImpl(Collection<?> c) {
return fillArray(c, new Object[c.size()]);
}

private static Object[] fillArray(Iterable<?> elements, Object[] array) {
int i = 0;
for (Object element : elements) {
array[i++] = element;
}
return array;
}

static void swap(Object[] array, int i, int j) {
Object temp = array[i];
array[i] = array[j];
array[j] = temp;
}
}

