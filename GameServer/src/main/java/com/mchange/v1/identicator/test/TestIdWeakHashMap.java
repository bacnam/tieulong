package com.mchange.v1.identicator.test;

import com.mchange.v1.identicator.IdWeakHashMap;
import com.mchange.v1.identicator.Identicator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TestIdWeakHashMap
{
static final Identicator id = new Identicator()
{
public boolean identical(Object param1Object1, Object param1Object2) {
return (((String)param1Object1).charAt(0) == ((String)param1Object2).charAt(0));
}
public int hash(Object param1Object) {
return ((String)param1Object).charAt(0);
}
};
static final Map weak = (Map)new IdWeakHashMap(id);

public static void main(String[] paramArrayOfString) {
doAdds();
System.gc();
show();
setRemoveHi();
System.gc();
show();
}

static void setRemoveHi() {
String str = new String("bye");
weak.put(str, "");
Set set = weak.keySet();
set.remove("hi");
show();
}

static void doAdds() {
String str1 = "hi";
String str2 = new String("hello");
String str3 = new String("yoohoo");
String str4 = new String("poop");

weak.put(str1, "");
weak.put(str2, "");
weak.put(str3, "");
weak.put(str4, "");

show();
}

static void show() {
System.out.println("elements:");
for (Iterator<String> iterator = weak.keySet().iterator(); iterator.hasNext();) {
System.out.println("\t" + iterator.next());
}
System.out.println("size: " + weak.size());
}
}

