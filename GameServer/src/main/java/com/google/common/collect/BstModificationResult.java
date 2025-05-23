package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

@GwtCompatible
final class BstModificationResult<N extends BstNode<?, N>>
{
@Nullable
private final N originalTarget;
@Nullable
private final N changedTarget;
private final ModificationType type;

enum ModificationType
{
IDENTITY, REBUILDING_CHANGE, REBALANCING_CHANGE;
}

static <N extends BstNode<?, N>> BstModificationResult<N> identity(@Nullable N target) {
return new BstModificationResult<N>(target, target, ModificationType.IDENTITY);
}

static <N extends BstNode<?, N>> BstModificationResult<N> rebuildingChange(@Nullable N originalTarget, @Nullable N changedTarget) {
return new BstModificationResult<N>(originalTarget, changedTarget, ModificationType.REBUILDING_CHANGE);
}

static <N extends BstNode<?, N>> BstModificationResult<N> rebalancingChange(@Nullable N originalTarget, @Nullable N changedTarget) {
return new BstModificationResult<N>(originalTarget, changedTarget, ModificationType.REBALANCING_CHANGE);
}

private BstModificationResult(@Nullable N originalTarget, @Nullable N changedTarget, ModificationType type) {
this.originalTarget = originalTarget;
this.changedTarget = changedTarget;
this.type = (ModificationType)Preconditions.checkNotNull(type);
}

@Nullable
N getOriginalTarget() {
return this.originalTarget;
}

@Nullable
N getChangedTarget() {
return this.changedTarget;
}

ModificationType getType() {
return this.type;
}
}

