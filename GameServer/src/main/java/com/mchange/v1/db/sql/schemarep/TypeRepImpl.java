package com.mchange.v1.db.sql.schemarep;

public final class TypeRepImpl
implements TypeRep
{
int type_code;
int[] typeSize;

public TypeRepImpl(int paramInt, int[] paramArrayOfint) {
this.type_code = paramInt;
this.typeSize = paramArrayOfint;
}

public int getTypeCode() {
return this.type_code;
}
public int[] getTypeSize() {
return this.typeSize;
}

public boolean equals(Object paramObject) {
if (this == paramObject)
return true; 
if (paramObject instanceof TypeRep) {
return TypeRepIdenticator.getInstance().identical(this, paramObject);
}
return false;
}

public int hashCode() {
return TypeRepIdenticator.getInstance().hash(this);
}
}

