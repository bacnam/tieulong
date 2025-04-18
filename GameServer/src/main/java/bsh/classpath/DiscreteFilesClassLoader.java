package bsh.classpath;

import bsh.BshClassManager;
import java.util.HashMap;

public class DiscreteFilesClassLoader
extends BshClassLoader
{
ClassSourceMap map;

public static class ClassSourceMap
extends HashMap
{
public void put(String name, BshClassPath.ClassSource source) {
put((K)name, (V)source);
}
public BshClassPath.ClassSource get(String name) {
return (BshClassPath.ClassSource)get(name);
}
}

public DiscreteFilesClassLoader(BshClassManager classManager, ClassSourceMap map) {
super(classManager);
this.map = map;
}

public Class findClass(String name) throws ClassNotFoundException {
BshClassPath.ClassSource source = this.map.get(name);

if (source != null) {

byte[] code = source.getCode(name);
return defineClass(name, code, 0, code.length);
} 

return super.findClass(name);
}

public String toString() {
return super.toString() + "for files: " + this.map;
}
}

