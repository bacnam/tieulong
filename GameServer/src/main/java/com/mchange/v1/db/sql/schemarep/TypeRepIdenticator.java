package com.mchange.v1.db.sql.schemarep;

import com.mchange.v1.identicator.Identicator;
import java.util.Arrays;

public class TypeRepIdenticator
implements Identicator
{
private static final TypeRepIdenticator INSTANCE = new TypeRepIdenticator();

public static TypeRepIdenticator getInstance() {
return INSTANCE;
}

public boolean identical(Object paramObject1, Object paramObject2) {
if (paramObject1 == paramObject2) {
return true;
}
TypeRep typeRep1 = (TypeRep)paramObject1;
TypeRep typeRep2 = (TypeRep)paramObject2;

return (typeRep1.getTypeCode() == typeRep2.getTypeCode() && Arrays.equals(typeRep1.getTypeSize(), typeRep2.getTypeSize()));
}

public int hash(Object paramObject) {
TypeRep typeRep = (TypeRep)paramObject;
int i = typeRep.getTypeCode();

int[] arrayOfInt = typeRep.getTypeSize();
if (arrayOfInt != null) {

int j = arrayOfInt.length;
for (byte b = 0; b < j; b++)
i ^= arrayOfInt[b]; 
i ^= j;
} 
return i;
}
}

