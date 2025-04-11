package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

@GwtCompatible
final class BstRangeOps {
    public static <K, N extends BstNode<K, N>> int totalInRange(BstAggregate<? super N> aggregate, GeneralRange<K> range, @Nullable N root) {
        Preconditions.checkNotNull(aggregate);
        Preconditions.checkNotNull(range);
        if (root == null || range.isEmpty()) {
            return 0;
        }
        int total = aggregate.treeValue(root);
        if (range.hasLowerBound()) {
            total -= totalBeyondRangeToSide(aggregate, range, BstSide.LEFT, root);
        }
        if (range.hasUpperBound()) {
            total -= totalBeyondRangeToSide(aggregate, range, BstSide.RIGHT, root);
        }
        return total;
    }

    private static <K, N extends BstNode<K, N>> int totalBeyondRangeToSide(BstAggregate<? super N> aggregate, GeneralRange<K> range, BstSide side, @Nullable N root) {
        int accum = 0;
        while (root != null) {
            if (beyond(range, (K) root.getKey(), side)) {
                accum += aggregate.entryValue(root);
                accum += aggregate.treeValue((N) root.childOrNull(side));
                root = (N) root.childOrNull(side.other());
                continue;
            }
            root = (N) root.childOrNull(side);
        }

        return accum;
    }

    @Nullable
    public static <K, N extends BstNode<K, N>> N minusRange(GeneralRange<K> range, BstBalancePolicy<N> balancePolicy, BstNodeFactory<N> nodeFactory, @Nullable N root) {
        Preconditions.checkNotNull(range);
        Preconditions.checkNotNull(balancePolicy);
        Preconditions.checkNotNull(nodeFactory);
        N higher = range.hasUpperBound() ? subTreeBeyondRangeToSide(range, balancePolicy, nodeFactory, BstSide.RIGHT, root) : null;

        N lower = range.hasLowerBound() ? subTreeBeyondRangeToSide(range, balancePolicy, nodeFactory, BstSide.LEFT, root) : null;

        return balancePolicy.combine(nodeFactory, lower, higher);
    }

    @Nullable
    private static <K, N extends BstNode<K, N>> N subTreeBeyondRangeToSide(GeneralRange<K> range, BstBalancePolicy<N> balancePolicy, BstNodeFactory<N> nodeFactory, BstSide side, @Nullable N root) {
        if (root == null) {
            return null;
        }
        if (beyond(range, (K) root.getKey(), side)) {
            N left = (N) root.childOrNull(BstSide.LEFT);
            N right = (N) root.childOrNull(BstSide.RIGHT);
            switch (side) {
                case LEFT:
                    right = subTreeBeyondRangeToSide(range, balancePolicy, nodeFactory, BstSide.LEFT, right);

                    return balancePolicy.balance(nodeFactory, root, left, right);
                case RIGHT:
                    left = subTreeBeyondRangeToSide(range, balancePolicy, nodeFactory, BstSide.RIGHT, left);
                    return balancePolicy.balance(nodeFactory, root, left, right);
            }
            throw new AssertionError();
        }
        return subTreeBeyondRangeToSide(range, balancePolicy, nodeFactory, side, (N) root.childOrNull(side));
    }

    @Nullable
    public static <K, N extends BstNode<K, N>, P extends BstPath<N, P>> P furthestPath(GeneralRange<K> range, BstSide side, BstPathFactory<N, P> pathFactory, @Nullable N root) {
        Preconditions.checkNotNull(range);
        Preconditions.checkNotNull(pathFactory);
        Preconditions.checkNotNull(side);
        if (root == null) {
            return null;
        }
        P path = pathFactory.initialPath(root);
        return furthestPath(range, side, pathFactory, path);
    }

    private static <K, N extends BstNode<K, N>, P extends BstPath<N, P>> P furthestPath(GeneralRange<K> range, BstSide side, BstPathFactory<N, P> pathFactory, P currentPath) {
        N tip = (N) currentPath.getTip();
        K tipKey = (K) tip.getKey();
        if (beyond(range, tipKey, side)) {
            if (tip.hasChild(side.other())) {
                currentPath = pathFactory.extension(currentPath, side.other());
                return furthestPath(range, side, pathFactory, currentPath);
            }
            return null;
        }
        if (tip.hasChild(side)) {
            P alphaPath = pathFactory.extension(currentPath, side);
            alphaPath = furthestPath(range, side, pathFactory, alphaPath);
            if (alphaPath != null) {
                return alphaPath;
            }
        }
        return beyond(range, tipKey, side.other()) ? null : currentPath;
    }

    public static <K> boolean beyond(GeneralRange<K> range, K key, BstSide side) {
        Preconditions.checkNotNull(range);
        Preconditions.checkNotNull(key);
        switch (side) {
            case LEFT:
                return range.tooLow(key);
            case RIGHT:
                return range.tooHigh(key);
        }
        throw new AssertionError();
    }
}

