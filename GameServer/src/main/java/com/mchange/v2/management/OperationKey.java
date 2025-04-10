package com.mchange.v2.management;

import java.util.Arrays;

public final class OperationKey
{
String name;
String[] signature;

public OperationKey(String paramString, String[] paramArrayOfString) {
this.name = paramString;
this.signature = paramArrayOfString;
}

public boolean equals(Object paramObject) {
if (paramObject instanceof OperationKey) {

OperationKey operationKey = (OperationKey)paramObject;
return (this.name.equals(operationKey.name) && Arrays.equals((Object[])this.signature, (Object[])operationKey.signature));
} 

return false;
}

public int hashCode() {
return this.name.hashCode() ^ Arrays.hashCode((Object[])this.signature);
}
}

