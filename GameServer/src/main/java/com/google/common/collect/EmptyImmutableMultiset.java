package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class EmptyImmutableMultiset
extends ImmutableMultiset<Object>
{
static final EmptyImmutableMultiset INSTANCE = new EmptyImmutableMultiset();
private static final long serialVersionUID = 0L;

public int count(@Nullable Object element) {
return 0;
}

public ImmutableSet<Object> elementSet() {
return ImmutableSet.of();
}

public int size() {
return 0;
}

UnmodifiableIterator<Multiset.Entry<Object>> entryIterator() {
return Iterators.emptyIterator();
}

int distinctElements() {
return 0;
}

boolean isPartialView() {
return false;
}

ImmutableSet<Multiset.Entry<Object>> createEntrySet() {
return ImmutableSet.of();
}
}

