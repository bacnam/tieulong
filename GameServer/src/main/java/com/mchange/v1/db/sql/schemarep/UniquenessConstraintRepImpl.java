package com.mchange.v1.db.sql.schemarep;

import com.mchange.v1.util.SetUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UniquenessConstraintRepImpl
implements UniquenessConstraintRep
{
Set uniqueColNames;

public UniquenessConstraintRepImpl(Collection<?> paramCollection) {
this.uniqueColNames = Collections.unmodifiableSet(new HashSet(paramCollection));
}
public Set getUniqueColumnNames() {
return this.uniqueColNames;
}

public boolean equals(Object paramObject) {
return (paramObject != null && getClass() == paramObject.getClass() && SetUtils.equivalentDisregardingSort(this.uniqueColNames, ((UniquenessConstraintRepImpl)paramObject).uniqueColNames));
}

public int hashCode() {
return getClass().hashCode() ^ SetUtils.hashContentsDisregardingSort(this.uniqueColNames);
}
}

