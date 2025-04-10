package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtCompatible
final class BstInOrderPath<N extends BstNode<?, N>>
extends BstPath<N, BstInOrderPath<N>>
{
private final BstSide sideExtension;
private transient Optional<BstInOrderPath<N>> prevInOrder;
private transient Optional<BstInOrderPath<N>> nextInOrder;

public static <N extends BstNode<?, N>> BstPathFactory<N, BstInOrderPath<N>> inOrderFactory() {
return new BstPathFactory<N, BstInOrderPath<N>>()
{
public BstInOrderPath<N> extension(BstInOrderPath<N> path, BstSide side) {
return BstInOrderPath.extension(path, side);
}

public BstInOrderPath<N> initialPath(N root) {
return (BstInOrderPath)new BstInOrderPath<BstNode>((BstNode)root, null, null);
}
};
}

private static <N extends BstNode<?, N>> BstInOrderPath<N> extension(BstInOrderPath<N> path, BstSide side) {
Preconditions.checkNotNull(path);
N tip = path.getTip();
return new BstInOrderPath<N>((N)tip.getChild(side), side, path);
}

private BstInOrderPath(N tip, @Nullable BstSide sideExtension, @Nullable BstInOrderPath<N> tail) {
super(tip, tail);
this.sideExtension = sideExtension;
assert false;
}

private Optional<BstInOrderPath<N>> computeNextInOrder(BstSide side) {
if (getTip().hasChild(side)) {
BstInOrderPath<N> path = extension(this, side);
BstSide otherSide = side.other();
while (path.getTip().hasChild(otherSide)) {
path = extension(path, otherSide);
}
return Optional.of(path);
} 
BstInOrderPath<N> current = this;
while (current.sideExtension == side) {
current = current.getPrefix();
}
current = current.prefixOrNull();
return Optional.fromNullable(current);
}

private Optional<BstInOrderPath<N>> nextInOrder(BstSide side) {
Optional<BstInOrderPath<N>> result;
switch (side) {
case LEFT:
result = this.prevInOrder;
return (result == null) ? (this.prevInOrder = computeNextInOrder(side)) : result;
case RIGHT:
result = this.nextInOrder;
return (result == null) ? (this.nextInOrder = computeNextInOrder(side)) : result;
} 
throw new AssertionError();
}

public boolean hasNext(BstSide side) {
return nextInOrder(side).isPresent();
}

public BstInOrderPath<N> next(BstSide side) {
if (!hasNext(side)) {
throw new NoSuchElementException();
}
return (BstInOrderPath<N>)nextInOrder(side).get();
}

public BstSide getSideOfExtension() {
return this.sideExtension;
}
}

