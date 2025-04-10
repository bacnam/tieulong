package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@GwtCompatible
final class BstMutationRule<K, N extends BstNode<K, N>>
{
private final BstModifier<K, N> modifier;
private final BstBalancePolicy<N> balancePolicy;
private final BstNodeFactory<N> nodeFactory;

public static <K, N extends BstNode<K, N>> BstMutationRule<K, N> createRule(BstModifier<K, N> modifier, BstBalancePolicy<N> balancePolicy, BstNodeFactory<N> nodeFactory) {
return new BstMutationRule<K, N>(modifier, balancePolicy, nodeFactory);
}

private BstMutationRule(BstModifier<K, N> modifier, BstBalancePolicy<N> balancePolicy, BstNodeFactory<N> nodeFactory) {
this.balancePolicy = (BstBalancePolicy<N>)Preconditions.checkNotNull(balancePolicy);
this.nodeFactory = (BstNodeFactory<N>)Preconditions.checkNotNull(nodeFactory);
this.modifier = (BstModifier<K, N>)Preconditions.checkNotNull(modifier);
}

public BstModifier<K, N> getModifier() {
return this.modifier;
}

public BstBalancePolicy<N> getBalancePolicy() {
return this.balancePolicy;
}

public BstNodeFactory<N> getNodeFactory() {
return this.nodeFactory;
}
}

