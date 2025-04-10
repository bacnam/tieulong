package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible
class BstNode<K, N extends BstNode<K, N>>
{
private final K key;
@Nullable
private final N left;
@Nullable
private final N right;

BstNode(K key, @Nullable N left, @Nullable N right) {
this.key = (K)Preconditions.checkNotNull(key);
this.left = left;
this.right = right;
}

public final K getKey() {
return this.key;
}

@Nullable
public final N childOrNull(BstSide side) {
switch (side) {
case LEFT:
return this.left;
case RIGHT:
return this.right;
} 
throw new AssertionError();
}

public final boolean hasChild(BstSide side) {
return (childOrNull(side) != null);
}

public final N getChild(BstSide side) {
N child = childOrNull(side);
Preconditions.checkState((child != null));
return child;
}

protected final boolean orderingInvariantHolds(Comparator<? super K> comparator) {
int i;
Preconditions.checkNotNull(comparator);
boolean result = true;
if (hasChild(BstSide.LEFT)) {
i = result & ((comparator.compare((K)getChild(BstSide.LEFT).getKey(), this.key) < 0) ? 1 : 0);
}
if (hasChild(BstSide.RIGHT)) {
i &= (comparator.compare((K)getChild(BstSide.RIGHT).getKey(), this.key) > 0) ? 1 : 0;
}
return i;
}
}

