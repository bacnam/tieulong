package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E>
extends ImmutableMultiset<E>
{
private final transient ImmutableMap<E, Integer> map;
private final transient int size;

RegularImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
this.map = map;
this.size = size;
}

boolean isPartialView() {
return this.map.isPartialView();
}

public int count(@Nullable Object element) {
Integer value = this.map.get(element);
return (value == null) ? 0 : value.intValue();
}

public int size() {
return this.size;
}

public boolean contains(@Nullable Object element) {
return this.map.containsKey(element);
}

public ImmutableSet<E> elementSet() {
return this.map.keySet();
}

UnmodifiableIterator<Multiset.Entry<E>> entryIterator() {
final Iterator<Map.Entry<E, Integer>> mapIterator = this.map.entrySet().iterator();

return (UnmodifiableIterator)new UnmodifiableIterator<Multiset.Entry<Multiset.Entry<E>>>()
{
public boolean hasNext() {
return mapIterator.hasNext();
}

public Multiset.Entry<E> next() {
Map.Entry<E, Integer> mapEntry = mapIterator.next();
return Multisets.immutableEntry(mapEntry.getKey(), ((Integer)mapEntry.getValue()).intValue());
}
};
}

public int hashCode() {
return this.map.hashCode();
}

int distinctElements() {
return this.map.size();
}
}

