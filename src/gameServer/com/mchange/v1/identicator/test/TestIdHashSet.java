package com.mchange.v1.identicator.test;

import com.mchange.v1.identicator.IdHashSet;
import com.mchange.v1.identicator.Identicator;

public class TestIdHashSet
{
public static void main(String[] paramArrayOfString) {
Identicator identicator = new Identicator()
{
public boolean identical(Object param1Object1, Object param1Object2) {
return (((String)param1Object1).charAt(0) == ((String)param1Object2).charAt(0));
}
public int hash(Object param1Object) {
return ((String)param1Object).charAt(0);
}
};
IdHashSet<String> idHashSet = new IdHashSet(identicator);
System.out.println(idHashSet.add("hello"));
System.out.println(idHashSet.add("world"));
System.out.println(idHashSet.add("hi"));
System.out.println(idHashSet.size());
Object[] arrayOfObject = idHashSet.toArray();
for (byte b = 0; b < arrayOfObject.length; b++)
System.out.println(arrayOfObject[b]); 
}
}

