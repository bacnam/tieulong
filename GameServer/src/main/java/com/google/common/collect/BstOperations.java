package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible
final class BstOperations
{
@Nullable
public static <K, N extends BstNode<K, N>> N seek(Comparator<? super K> comparator, @Nullable N tree, K key) {
Preconditions.checkNotNull(comparator);
if (tree == null) {
return null;
}
int cmp = comparator.compare(key, (K)tree.getKey());
if (cmp == 0) {
return tree;
}
BstSide side = (cmp < 0) ? BstSide.LEFT : BstSide.RIGHT;
return seek(comparator, (N)tree.childOrNull(side), key);
}

public static <K, N extends BstNode<K, N>> BstMutationResult<K, N> mutate(Comparator<? super K> comparator, BstMutationRule<K, N> mutationRule, @Nullable N tree, K key) {
Preconditions.checkNotNull(comparator);
Preconditions.checkNotNull(mutationRule);
Preconditions.checkNotNull(key);

if (tree != null) {
int cmp = comparator.compare(key, (K)tree.getKey());
if (cmp != 0) {
BstSide side = (cmp < 0) ? BstSide.LEFT : BstSide.RIGHT;
BstMutationResult<K, N> mutation = mutate(comparator, mutationRule, (N)tree.childOrNull(side), key);

return mutation.lift(tree, side, mutationRule.getNodeFactory(), mutationRule.getBalancePolicy());
} 
} 

return modify(tree, key, mutationRule);
}

public static <K, N extends BstNode<K, N>> BstMutationResult<K, N> mutate(BstInOrderPath<N> path, BstMutationRule<K, N> mutationRule) {
Preconditions.checkNotNull(path);
Preconditions.checkNotNull(mutationRule);
BstBalancePolicy<N> balancePolicy = mutationRule.getBalancePolicy();
BstNodeFactory<N> nodeFactory = mutationRule.getNodeFactory();
BstModifier<K, N> modifier = mutationRule.getModifier();

N target = path.getTip();
K key = (K)target.getKey();
BstMutationResult<K, N> result = modify(target, key, mutationRule);
while (path.hasPrefix()) {
BstInOrderPath<N> prefix = path.getPrefix();
result = result.lift(prefix.getTip(), path.getSideOfExtension(), nodeFactory, balancePolicy);
path = prefix;
} 
return result;
}

private static <K, N extends BstNode<K, N>> BstMutationResult<K, N> modify(@Nullable N tree, K key, BstMutationRule<K, N> mutationRule) {
N changedRoot;
BstBalancePolicy<N> rebalancePolicy = mutationRule.getBalancePolicy();
BstNodeFactory<N> nodeFactory = mutationRule.getNodeFactory();
BstModifier<K, N> modifier = mutationRule.getModifier();

N originalRoot = tree;

N originalTarget = (tree == null) ? null : nodeFactory.createLeaf(tree);
BstModificationResult<N> modResult = modifier.modify(key, originalTarget);
N originalLeft = null;
N originalRight = null;
if (tree != null) {
originalLeft = (N)tree.childOrNull(BstSide.LEFT);
originalRight = (N)tree.childOrNull(BstSide.RIGHT);
} 
switch (modResult.getType()) {
case IDENTITY:
changedRoot = tree;

return BstMutationResult.mutationResult(key, originalRoot, changedRoot, modResult);case REBUILDING_CHANGE: if (modResult.getChangedTarget() != null) { changedRoot = nodeFactory.createNode(modResult.getChangedTarget(), originalLeft, originalRight); } else if (tree == null) { changedRoot = null; } else { throw new AssertionError("Modification result is a REBUILDING_CHANGE, but rebalancing required"); }  return BstMutationResult.mutationResult(key, originalRoot, changedRoot, modResult);case REBALANCING_CHANGE: if (modResult.getChangedTarget() != null) { changedRoot = rebalancePolicy.balance(nodeFactory, modResult.getChangedTarget(), originalLeft, originalRight); } else if (tree != null) { changedRoot = rebalancePolicy.combine(nodeFactory, originalLeft, originalRight); } else { changedRoot = null; }  return BstMutationResult.mutationResult(key, originalRoot, changedRoot, modResult);
} 
throw new AssertionError();
}

public static <K, N extends BstNode<K, N>> BstMutationResult<K, N> extractMin(N root, BstNodeFactory<N> nodeFactory, BstBalancePolicy<N> balancePolicy) {
Preconditions.checkNotNull(root);
Preconditions.checkNotNull(nodeFactory);
Preconditions.checkNotNull(balancePolicy);
if (root.hasChild(BstSide.LEFT)) {
BstMutationResult<K, N> subResult = extractMin((N)root.getChild(BstSide.LEFT), nodeFactory, balancePolicy);

return subResult.lift(root, BstSide.LEFT, nodeFactory, balancePolicy);
} 
return BstMutationResult.mutationResult((K)root.getKey(), root, (N)root.childOrNull(BstSide.RIGHT), (BstModificationResult)BstModificationResult.rebalancingChange((BstNode)root, null));
}

public static <K, N extends BstNode<K, N>> BstMutationResult<K, N> extractMax(N root, BstNodeFactory<N> nodeFactory, BstBalancePolicy<N> balancePolicy) {
Preconditions.checkNotNull(root);
Preconditions.checkNotNull(nodeFactory);
Preconditions.checkNotNull(balancePolicy);
if (root.hasChild(BstSide.RIGHT)) {
BstMutationResult<K, N> subResult = extractMax((N)root.getChild(BstSide.RIGHT), nodeFactory, balancePolicy);

return subResult.lift(root, BstSide.RIGHT, nodeFactory, balancePolicy);
} 
return BstMutationResult.mutationResult((K)root.getKey(), root, (N)root.childOrNull(BstSide.LEFT), (BstModificationResult)BstModificationResult.rebalancingChange((BstNode)root, null));
}

public static <N extends BstNode<?, N>> N insertMin(@Nullable N root, N entry, BstNodeFactory<N> nodeFactory, BstBalancePolicy<N> balancePolicy) {
Preconditions.checkNotNull(entry);
Preconditions.checkNotNull(nodeFactory);
Preconditions.checkNotNull(balancePolicy);
if (root == null) {
return nodeFactory.createLeaf(entry);
}
return balancePolicy.balance(nodeFactory, root, insertMin((N)root.childOrNull(BstSide.LEFT), entry, nodeFactory, balancePolicy), (N)root.childOrNull(BstSide.RIGHT));
}

public static <N extends BstNode<?, N>> N insertMax(@Nullable N root, N entry, BstNodeFactory<N> nodeFactory, BstBalancePolicy<N> balancePolicy) {
Preconditions.checkNotNull(entry);
Preconditions.checkNotNull(nodeFactory);
Preconditions.checkNotNull(balancePolicy);
if (root == null) {
return nodeFactory.createLeaf(entry);
}
return balancePolicy.balance(nodeFactory, root, (N)root.childOrNull(BstSide.LEFT), insertMax((N)root.childOrNull(BstSide.RIGHT), entry, nodeFactory, balancePolicy));
}
}

