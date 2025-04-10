package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.SortedSet;

@Beta
@GwtCompatible
public abstract class ContiguousSet<C extends Comparable>
extends ImmutableSortedSet<C>
{
final DiscreteDomain<C> domain;

ContiguousSet(DiscreteDomain<C> domain) {
super(Ordering.natural());
this.domain = domain;
}

public ContiguousSet<C> headSet(C toElement) {
return headSet((C)Preconditions.checkNotNull(toElement), false);
}

ContiguousSet<C> headSet(C toElement, boolean inclusive) {
return headSetImpl((C)Preconditions.checkNotNull(toElement), inclusive);
}

public ContiguousSet<C> subSet(C fromElement, C toElement) {
Preconditions.checkNotNull(fromElement);
Preconditions.checkNotNull(toElement);
Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
return subSet(fromElement, true, toElement, false);
}

ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
Preconditions.checkNotNull(fromElement);
Preconditions.checkNotNull(toElement);
Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
}

public ContiguousSet<C> tailSet(C fromElement) {
return tailSet((C)Preconditions.checkNotNull(fromElement), true);
}

ContiguousSet<C> tailSet(C fromElement, boolean inclusive) {
return tailSetImpl((C)Preconditions.checkNotNull(fromElement), inclusive);
}

public String toString() {
return range().toString();
}

abstract ContiguousSet<C> headSetImpl(C paramC, boolean paramBoolean);

abstract ContiguousSet<C> subSetImpl(C paramC1, boolean paramBoolean1, C paramC2, boolean paramBoolean2);

abstract ContiguousSet<C> tailSetImpl(C paramC, boolean paramBoolean);

public abstract ContiguousSet<C> intersection(ContiguousSet<C> paramContiguousSet);

public abstract Range<C> range();

public abstract Range<C> range(BoundType paramBoundType1, BoundType paramBoundType2);
}

