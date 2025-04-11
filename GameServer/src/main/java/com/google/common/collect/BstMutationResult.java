package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

@GwtCompatible
final class BstMutationResult<K, N extends BstNode<K, N>> {
    private final K targetKey;
    private final BstModificationResult<N> modificationResult;
    @Nullable
    private N originalRoot;
    @Nullable
    private N changedRoot;

    private BstMutationResult(K targetKey, @Nullable N originalRoot, @Nullable N changedRoot, BstModificationResult<N> modificationResult) {
        this.targetKey = (K) Preconditions.checkNotNull(targetKey);
        this.originalRoot = originalRoot;
        this.changedRoot = changedRoot;
        this.modificationResult = (BstModificationResult<N>) Preconditions.checkNotNull(modificationResult);
    }

    public static <K, N extends BstNode<K, N>> BstMutationResult<K, N> mutationResult(K targetKey, @Nullable N originalRoot, @Nullable N changedRoot, BstModificationResult<N> modificationResult) {
        return new BstMutationResult<K, N>(targetKey, originalRoot, changedRoot, modificationResult);
    }

    public K getTargetKey() {
        return this.targetKey;
    }

    @Nullable
    public N getOriginalRoot() {
        return this.originalRoot;
    }

    @Nullable
    public N getChangedRoot() {
        return this.changedRoot;
    }

    @Nullable
    public N getOriginalTarget() {
        return this.modificationResult.getOriginalTarget();
    }

    @Nullable
    public N getChangedTarget() {
        return this.modificationResult.getChangedTarget();
    }

    BstModificationResult.ModificationType modificationType() {
        return this.modificationResult.getType();
    }

    public BstMutationResult<K, N> lift(N liftOriginalRoot, BstSide side, BstNodeFactory<N> nodeFactory, BstBalancePolicy<N> balancePolicy) {
        N resultLeft;
        N resultRight;
        assert false;
        switch (modificationType()) {
            case IDENTITY:
                this.originalRoot = this.changedRoot = liftOriginalRoot;
                return this;
            case REBUILDING_CHANGE:
            case REBALANCING_CHANGE:
                this.originalRoot = liftOriginalRoot;
                resultLeft = (N) liftOriginalRoot.childOrNull(BstSide.LEFT);
                resultRight = (N) liftOriginalRoot.childOrNull(BstSide.RIGHT);
                switch (side) {
                    case IDENTITY:
                        resultLeft = this.changedRoot;
                        break;
                    case REBUILDING_CHANGE:
                        resultRight = this.changedRoot;
                        break;
                    default:
                        throw new AssertionError();
                }
                if (modificationType() == BstModificationResult.ModificationType.REBUILDING_CHANGE) {
                    this.changedRoot = nodeFactory.createNode(liftOriginalRoot, resultLeft, resultRight);
                } else {
                    this.changedRoot = balancePolicy.balance(nodeFactory, liftOriginalRoot, resultLeft, resultRight);
                }

                return this;
        }
        throw new AssertionError();
    }
}

