package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SerializableExtension
implements GeneratorExtension
{
Set transientProperties;
Map transientPropertyInitializers;

public SerializableExtension(Set paramSet, Map paramMap) {
this.transientProperties = paramSet;
this.transientPropertyInitializers = paramMap;
}

public SerializableExtension() {
this(Collections.EMPTY_SET, null);
}

public Collection extraGeneralImports() {
return Collections.EMPTY_SET;
}

public Collection extraSpecificImports() {
HashSet<String> hashSet = new HashSet();
hashSet.add("java.io.IOException");
hashSet.add("java.io.Serializable");
hashSet.add("java.io.ObjectOutputStream");
hashSet.add("java.io.ObjectInputStream");
return hashSet;
}

public Collection extraInterfaceNames() {
HashSet<String> hashSet = new HashSet();
hashSet.add("Serializable");
return hashSet;
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("private static final long serialVersionUID = 1;");
paramIndentedWriter.println("private static final short VERSION = 0x0001;");
paramIndentedWriter.println();
paramIndentedWriter.println("private void writeObject( ObjectOutputStream oos ) throws IOException");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("oos.writeShort( VERSION );"); byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {

Property property = paramArrayOfProperty[b];
if (!this.transientProperties.contains(property.getName())) {

Class<byte> clazz = paramArrayOfClass[b];
if (clazz != null && clazz.isPrimitive()) {

if (clazz == byte.class) {
paramIndentedWriter.println("oos.writeByte(" + property.getName() + ");");
} else if (clazz == char.class) {
paramIndentedWriter.println("oos.writeChar(" + property.getName() + ");");
} else if (clazz == short.class) {
paramIndentedWriter.println("oos.writeShort(" + property.getName() + ");");
} else if (clazz == int.class) {
paramIndentedWriter.println("oos.writeInt(" + property.getName() + ");");
} else if (clazz == boolean.class) {
paramIndentedWriter.println("oos.writeBoolean(" + property.getName() + ");");
} else if (clazz == long.class) {
paramIndentedWriter.println("oos.writeLong(" + property.getName() + ");");
} else if (clazz == float.class) {
paramIndentedWriter.println("oos.writeFloat(" + property.getName() + ");");
} else if (clazz == double.class) {
paramIndentedWriter.println("oos.writeDouble(" + property.getName() + ");");
} 
} else {
writeStoreObject(property, clazz, paramIndentedWriter);
} 
} 
}  generateExtraSerWriteStatements(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println();

paramIndentedWriter.println("private void readObject( ObjectInputStream ois ) throws IOException, ClassNotFoundException");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("short version = ois.readShort();");
paramIndentedWriter.println("switch (version)");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("case VERSION:");
paramIndentedWriter.upIndent();
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {

Property property = paramArrayOfProperty[b];
if (!this.transientProperties.contains(property.getName())) {

Class<byte> clazz = paramArrayOfClass[b];
if (clazz != null && clazz.isPrimitive()) {

if (clazz == byte.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readByte();");
} else if (clazz == char.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readChar();");
} else if (clazz == short.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readShort();");
} else if (clazz == int.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readInt();");
} else if (clazz == boolean.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readBoolean();");
} else if (clazz == long.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readLong();");
} else if (clazz == float.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readFloat();");
} else if (clazz == double.class) {
paramIndentedWriter.println("this." + property.getName() + " = ois.readDouble();");
} 
} else {
writeUnstoreObject(property, clazz, paramIndentedWriter);
} 
} else {

String str = (String)this.transientPropertyInitializers.get(property.getName());
if (str != null)
paramIndentedWriter.println("this." + property.getName() + " = " + str + ';'); 
} 
} 
generateExtraSerInitializers(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
paramIndentedWriter.println("break;");
paramIndentedWriter.downIndent();
paramIndentedWriter.println("default:");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("throw new IOException(\"Unsupported Serialized Version: \" + version);");
paramIndentedWriter.downIndent();

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}

protected void writeStoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("oos.writeObject( " + paramProperty.getName() + " );");
}

protected void writeUnstoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("this." + paramProperty.getName() + " = (" + paramProperty.getSimpleTypeName() + ") ois.readObject();");
}

protected void generateExtraSerWriteStatements(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {}

protected void generateExtraSerInitializers(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {}
}

