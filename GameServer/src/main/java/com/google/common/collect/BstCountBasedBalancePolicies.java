package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

@GwtCompatible
final class BstCountBasedBalancePolicies
{
private static final int SINGLE_ROTATE_RATIO = 4;
private static final int SECOND_ROTATE_RATIO = 2;

public static <N extends BstNode<?, N>> BstBalancePolicy<N> noRebalancePolicy(final BstAggregate<N> countAggregate) {
Preconditions.checkNotNull(countAggregate);
return new BstBalancePolicy<N>()
{
public N balance(BstNodeFactory<N> nodeFactory, N source, @Nullable N left, @Nullable N right)
{
return (N)((BstNodeFactory)Preconditions.checkNotNull(nodeFactory)).createNode(source, left, right);
}

@Nullable
public N combine(BstNodeFactory<N> nodeFactory, @Nullable N left, @Nullable N right) {
if (left == null)
return right; 
if (right == null)
return left; 
if (countAggregate.treeValue(left) > countAggregate.treeValue(right)) {
return nodeFactory.createNode(left, (N)left.childOrNull(BstSide.LEFT), combine(nodeFactory, (N)left.childOrNull(BstSide.RIGHT), right));
}

return nodeFactory.createNode(right, combine(nodeFactory, left, (N)right.childOrNull(BstSide.LEFT)), (N)right.childOrNull(BstSide.RIGHT));
}
};
}

public static <K, N extends BstNode<K, N>> BstBalancePolicy<N> singleRebalancePolicy(final BstAggregate<N> countAggregate) {
Preconditions.checkNotNull(countAggregate);
return new BstBalancePolicy<N>()
{
public N balance(BstNodeFactory<N> nodeFactory, N source, @Nullable N left, @Nullable N right)
{
int countL = countAggregate.treeValue(left);
int countR = countAggregate.treeValue(right);
if (countL + countR > 1) {
if (countR >= 4 * countL)
return rotateL(nodeFactory, source, left, right); 
if (countL >= 4 * countR) {
return rotateR(nodeFactory, source, left, right);
}
} 
return nodeFactory.createNode(source, left, right);
}

private N rotateL(BstNodeFactory<N> nodeFactory, N source, @Nullable N left, N right) {
Preconditions.checkNotNull(right);
N rl = (N)right.childOrNull(BstSide.LEFT);
N rr = (N)right.childOrNull(BstSide.RIGHT);
if (countAggregate.treeValue(rl) >= 2 * countAggregate.treeValue(rr)) {
right = singleR(nodeFactory, right, rl, rr);
}
return singleL(nodeFactory, source, left, right);
}

private N rotateR(BstNodeFactory<N> nodeFactory, N source, N left, @Nullable N right) {
Preconditions.checkNotNull(left);
N lr = (N)left.childOrNull(BstSide.RIGHT);
N ll = (N)left.childOrNull(BstSide.LEFT);
if (countAggregate.treeValue(lr) >= 2 * countAggregate.treeValue(ll)) {
left = singleL(nodeFactory, left, ll, lr);
}
return singleR(nodeFactory, source, left, right);
}

private N singleL(BstNodeFactory<N> nodeFactory, N source, @Nullable N left, N right) {
Preconditions.checkNotNull(right);
return nodeFactory.createNode(right, nodeFactory.createNode(source, left, (N)right.childOrNull(BstSide.LEFT)), (N)right.childOrNull(BstSide.RIGHT));
}

private N singleR(BstNodeFactory<N> nodeFactory, N source, N left, @Nullable N right) {
Preconditions.checkNotNull(left);
return nodeFactory.createNode(left, (N)left.childOrNull(BstSide.LEFT), nodeFactory.createNode(source, (N)left.childOrNull(BstSide.RIGHT), right));
}

@Nullable
public N combine(BstNodeFactory<N> nodeFactory, @Nullable N left, @Nullable N right) {
N newRootSource;
if (left == null)
return right; 
if (right == null) {
return left;
}

if (countAggregate.treeValue(left) > countAggregate.treeValue(right)) {
BstMutationResult<K, N> extractLeftMax = (BstMutationResult)BstOperations.extractMax((BstNode)left, (BstNodeFactory)nodeFactory, this);
newRootSource = extractLeftMax.getOriginalTarget();
left = extractLeftMax.getChangedRoot();
} else {
BstMutationResult<K, N> extractRightMin = (BstMutationResult)BstOperations.extractMin((BstNode)right, (BstNodeFactory)nodeFactory, this);
newRootSource = extractRightMin.getOriginalTarget();
right = extractRightMin.getChangedRoot();
} 
return nodeFactory.createNode(newRootSource, left, right);
}
};
}

public static <K, N extends BstNode<K, N>> BstBalancePolicy<N> fullRebalancePolicy(final BstAggregate<N> countAggregate) {
Preconditions.checkNotNull(countAggregate);
final BstBalancePolicy<N> singleBalancePolicy = singleRebalancePolicy(countAggregate);

return new BstBalancePolicy<N>()
{
public N balance(BstNodeFactory<N> nodeFactory, N source, @Nullable N left, @Nullable N right)
{
if (left == null)
return (N)BstOperations.insertMin((BstNode)right, (BstNode)source, (BstNodeFactory)nodeFactory, singleBalancePolicy); 
if (right == null) {
return (N)BstOperations.insertMax((BstNode)left, (BstNode)source, (BstNodeFactory)nodeFactory, singleBalancePolicy);
}
int countL = countAggregate.treeValue(left);
int countR = countAggregate.treeValue(right);
if (4 * countL <= countR) {
N resultLeft = balance(nodeFactory, source, left, (N)right.childOrNull(BstSide.LEFT));
return (N)singleBalancePolicy.balance(nodeFactory, right, resultLeft, right.childOrNull(BstSide.RIGHT));
} 
if (4 * countR <= countL) {
N resultRight = balance(nodeFactory, source, (N)left.childOrNull(BstSide.RIGHT), right);
return (N)singleBalancePolicy.balance(nodeFactory, left, left.childOrNull(BstSide.LEFT), resultRight);
} 

return nodeFactory.createNode(source, left, right);
}

@Nullable
public N combine(BstNodeFactory<N> nodeFactory, @Nullable N left, @Nullable N right) {
if (left == null)
return right; 
if (right == null) {
return left;
}
int countL = countAggregate.treeValue(left);
int countR = countAggregate.treeValue(right);
if (4 * countL <= countR) {
N resultLeft = combine(nodeFactory, left, (N)right.childOrNull(BstSide.LEFT));
return (N)singleBalancePolicy.balance(nodeFactory, right, resultLeft, right.childOrNull(BstSide.RIGHT));
} 
if (4 * countR <= countL) {
N resultRight = combine(nodeFactory, (N)left.childOrNull(BstSide.RIGHT), right);
return (N)singleBalancePolicy.balance(nodeFactory, left, left.childOrNull(BstSide.LEFT), resultRight);
} 

return (N)singleBalancePolicy.combine(nodeFactory, left, right);
}
};
}
}

