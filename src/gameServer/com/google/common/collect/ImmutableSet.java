package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSet<E>
extends ImmutableCollection<E>
implements Set<E>
{
static final int MAX_TABLE_SIZE = 1073741824;
static final int CUTOFF = 536870912;

public static <E> ImmutableSet<E> of() {
return EmptyImmutableSet.INSTANCE;
}

public static <E> ImmutableSet<E> of(E element) {
return new SingletonImmutableSet<E>(element);
}

public static <E> ImmutableSet<E> of(E e1, E e2) {
return construct(new Object[] { e1, e2 });
}

public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
return construct(new Object[] { e1, e2, e3 });
}

public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
return construct(new Object[] { e1, e2, e3, e4 });
}

public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
return construct(new Object[] { e1, e2, e3, e4, e5 });
}

public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
int paramCount = 6;
Object[] elements = new Object[6 + others.length];
elements[0] = e1;
elements[1] = e2;
elements[2] = e3;
elements[3] = e4;
elements[4] = e5;
elements[5] = e6;
for (int i = 6; i < elements.length; i++) {
elements[i] = others[i - 6];
}
return construct(elements);
}

private static <E> ImmutableSet<E> construct(Object... elements) {
int tableSize = chooseTableSize(elements.length);
Object[] table = new Object[tableSize];
int mask = tableSize - 1;
ArrayList<Object> uniqueElementsList = null;
int hashCode = 0;
for (int i = 0; i < elements.length; i++) {
Object element = elements[i];
int hash = element.hashCode();
for (int j = Hashing.smear(hash);; j++) {
int index = j & mask;
Object value = table[index];
if (value == null) {
if (uniqueElementsList != null) {
uniqueElementsList.add(element);
}

table[index] = element;
hashCode += hash; break;
} 
if (value.equals(element)) {
if (uniqueElementsList == null) {

uniqueElementsList = new ArrayList(elements.length);
for (int k = 0; k < i; k++) {
Object previous = elements[k];
uniqueElementsList.add(previous);
} 
} 
break;
} 
} 
} 
Object[] uniqueElements = (uniqueElementsList == null) ? elements : uniqueElementsList.toArray();

if (uniqueElements.length == 1) {

E element = (E)uniqueElements[0];
return new SingletonImmutableSet<E>(element, hashCode);
}  if (tableSize > 2 * chooseTableSize(uniqueElements.length))
{

return construct(uniqueElements);
}
return new RegularImmutableSet<E>(uniqueElements, hashCode, table, mask);
}

static int chooseTableSize(int setSize) {
if (setSize < 536870912) {
return Integer.highestOneBit(setSize) << 2;
}

Preconditions.checkArgument((setSize < 1073741824), "collection too large");
return 1073741824;
}

@Deprecated
public static <E> ImmutableSet<E> of(E[] elements) {
return copyOf(elements);
}

public static <E> ImmutableSet<E> copyOf(E[] elements) {
switch (elements.length) {
case 0:
return of();
case 1:
return of(elements[0]);
} 
return construct((Object[])elements.clone());
}

public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
return (elements instanceof Collection) ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator());
}

public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
return copyFromCollection(Lists.newArrayList(elements));
}

public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
if (elements instanceof ImmutableSet && !(elements instanceof ImmutableSortedSet)) {

ImmutableSet<E> set = (ImmutableSet)elements;
if (!set.isPartialView()) {
return set;
}
} 
return copyFromCollection(elements);
}

private static <E> ImmutableSet<E> copyFromCollection(Collection<? extends E> collection) {
E onlyElement;
Object[] elements = collection.toArray();
switch (elements.length) {
case 0:
return of();

case 1:
onlyElement = (E)elements[0];
return of(onlyElement);
} 

return construct(elements);
}

boolean isHashCodeFast() {
return false;
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
if (object instanceof ImmutableSet && isHashCodeFast() && ((ImmutableSet)object).isHashCodeFast() && hashCode() != object.hashCode())
{

return false;
}
return Sets.equalsImpl(this, object);
}

public int hashCode() {
return Sets.hashCodeImpl(this);
}

static abstract class ArrayImmutableSet<E>
extends ImmutableSet<E>
{
final transient Object[] elements;

ArrayImmutableSet(Object[] elements) {
this.elements = elements;
}

public int size() {
return this.elements.length;
}

public boolean isEmpty() {
return false;
}

public UnmodifiableIterator<E> iterator() {
return Iterators.forArray((E[])this.elements);
}

public Object[] toArray() {
Object[] array = new Object[size()];
System.arraycopy(this.elements, 0, array, 0, size());
return array;
}

public <T> T[] toArray(T[] array) {
int size = size();
if (array.length < size) {
array = ObjectArrays.newArray(array, size);
} else if (array.length > size) {
array[size] = null;
} 
System.arraycopy(this.elements, 0, array, 0, size);
return array;
}

public boolean containsAll(Collection<?> targets) {
if (targets == this) {
return true;
}
if (!(targets instanceof ArrayImmutableSet)) {
return super.containsAll(targets);
}
if (targets.size() > size()) {
return false;
}
for (Object target : ((ArrayImmutableSet)targets).elements) {
if (!contains(target)) {
return false;
}
} 
return true;
}

boolean isPartialView() {
return false;
}

ImmutableList<E> createAsList() {
return new ImmutableAsList<E>(this.elements, this);
}
}

static abstract class TransformedImmutableSet<D, E>
extends ImmutableSet<E> {
final D[] source;
final int hashCode;

TransformedImmutableSet(D[] source, int hashCode) {
this.source = source;
this.hashCode = hashCode;
}

public int size() {
return this.source.length;
}

public boolean isEmpty() {
return false;
}

public UnmodifiableIterator<E> iterator() {
return new AbstractIndexedListIterator<E>(this.source.length) {
protected E get(int index) {
return ImmutableSet.TransformedImmutableSet.this.transform(ImmutableSet.TransformedImmutableSet.this.source[index]);
}
};
}

public Object[] toArray() {
return toArray(new Object[size()]);
}

public <T> T[] toArray(T[] array) {
int size = size();
if (array.length < size) {
array = ObjectArrays.newArray(array, size);
} else if (array.length > size) {
array[size] = null;
} 

T[] arrayOfT = array;
for (int i = 0; i < this.source.length; i++) {
arrayOfT[i] = (T)transform(this.source[i]);
}
return array;
}

public final int hashCode() {
return this.hashCode;
}

boolean isHashCodeFast() {
return true;
}

abstract E transform(D param1D);
}

private static class SerializedForm
implements Serializable
{
final Object[] elements;
private static final long serialVersionUID = 0L;

SerializedForm(Object[] elements) {
this.elements = elements;
}
Object readResolve() {
return ImmutableSet.copyOf(this.elements);
}
}

Object writeReplace() {
return new SerializedForm(toArray());
}

public static <E> Builder<E> builder() {
return new Builder<E>();
}

public abstract UnmodifiableIterator<E> iterator();

public static class Builder<E>
extends ImmutableCollection.Builder<E>
{
final ArrayList<E> contents = Lists.newArrayList();

public Builder<E> add(E element) {
this.contents.add((E)Preconditions.checkNotNull(element));
return this;
}

public Builder<E> add(E... elements) {
this.contents.ensureCapacity(this.contents.size() + elements.length);
super.add(elements);
return this;
}

public Builder<E> addAll(Iterable<? extends E> elements) {
if (elements instanceof Collection) {
Collection<?> collection = (Collection)elements;
this.contents.ensureCapacity(this.contents.size() + collection.size());
} 
super.addAll(elements);
return this;
}

public Builder<E> addAll(Iterator<? extends E> elements) {
super.addAll(elements);
return this;
}

public ImmutableSet<E> build() {
return ImmutableSet.copyOf(this.contents);
}
}
}

