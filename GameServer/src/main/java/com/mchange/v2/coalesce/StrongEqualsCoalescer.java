package com.mchange.v2.coalesce;

import java.util.HashMap;

final class StrongEqualsCoalescer
extends AbstractStrongCoalescer
implements Coalescer
{
StrongEqualsCoalescer() {
super(new HashMap<Object, Object>());
}
}

