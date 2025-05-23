package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalences;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentMap;

@Beta
public final class Interners
{
public static <E> Interner<E> newStrongInterner() {
final ConcurrentMap<E, E> map = (new MapMaker()).makeMap();
return new Interner<E>() {
public E intern(E sample) {
E canonical = map.putIfAbsent(Preconditions.checkNotNull(sample), sample);
return (canonical == null) ? sample : canonical;
}
};
}

private static class CustomInterner<E>
implements Interner<E> {
private final CustomConcurrentHashMap<E, Dummy> map;

CustomInterner(GenericMapMaker<? super E, Object> mm) {
this.map = mm.strongValues().keyEquivalence(Equivalences.equals()).makeCustomMap();
}

public E intern(E sample) {
while (true) {
CustomConcurrentHashMap.ReferenceEntry<E, Dummy> entry = this.map.getEntry(sample);
if (entry != null) {
E canonical = entry.getKey();
if (canonical != null) {
return canonical;
}
} 

Dummy sneaky = this.map.putIfAbsent(sample, Dummy.VALUE);
if (sneaky == null) {
return sample;
}
} 
}

private enum Dummy
{
VALUE;
}
}

@GwtIncompatible("java.lang.ref.WeakReference")
public static <E> Interner<E> newWeakInterner() {
return new CustomInterner<E>((new MapMaker()).weakKeys());
}

public static <E> Function<E, E> asFunction(Interner<E> interner) {
return new InternerFunction<E>((Interner<E>)Preconditions.checkNotNull(interner));
}

private static class InternerFunction<E>
implements Function<E, E> {
private final Interner<E> interner;

public InternerFunction(Interner<E> interner) {
this.interner = interner;
}

public E apply(E input) {
return this.interner.intern(input);
}

public int hashCode() {
return this.interner.hashCode();
}

public boolean equals(Object other) {
if (other instanceof InternerFunction) {
InternerFunction<?> that = (InternerFunction)other;
return this.interner.equals(that.interner);
} 

return false;
}
}
}

