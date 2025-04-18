package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import com.mchange.v2.ser.IndirectPolicy;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

public class IndirectingSerializableExtension
extends SerializableExtension
{
protected String findIndirectorExpr;
protected String indirectorClassName;

public IndirectingSerializableExtension(String paramString) {
this.indirectorClassName = paramString;
this.findIndirectorExpr = "new " + paramString + "()";
}

protected IndirectingSerializableExtension() {}

public Collection extraSpecificImports() {
Collection<String> collection = super.extraSpecificImports();
collection.add(this.indirectorClassName);
collection.add("com.mchange.v2.ser.IndirectlySerialized");
collection.add("com.mchange.v2.ser.Indirector");
collection.add("com.mchange.v2.ser.SerializableUtils");
collection.add("java.io.NotSerializableException");
return collection;
}

protected IndirectPolicy indirectingPolicy(Property paramProperty, Class<?> paramClass) {
if (Serializable.class.isAssignableFrom(paramClass)) {
return IndirectPolicy.DEFINITELY_DIRECT;
}
return IndirectPolicy.INDIRECT_ON_EXCEPTION;
}

protected void writeInitializeIndirector(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {}

protected void writeExtraDeclarations(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
super.generate(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
writeExtraDeclarations(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
}

protected void writeStoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
IndirectPolicy indirectPolicy = indirectingPolicy(paramProperty, paramClass);
if (indirectPolicy == IndirectPolicy.DEFINITELY_INDIRECT) {
writeIndirectStoreObject(paramProperty, paramClass, paramIndentedWriter);
} else if (indirectPolicy == IndirectPolicy.INDIRECT_ON_EXCEPTION) {

paramIndentedWriter.println("try");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("
paramIndentedWriter.println("SerializableUtils.toByteArray(" + paramProperty.getName() + ");");
super.writeStoreObject(paramProperty, paramClass, paramIndentedWriter);
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println("catch (NotSerializableException nse)");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
writeIndirectStoreObject(paramProperty, paramClass, paramIndentedWriter);
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}
else if (indirectPolicy == IndirectPolicy.DEFINITELY_DIRECT) {
super.writeStoreObject(paramProperty, paramClass, paramIndentedWriter);
} else {
throw new InternalError("indirectingPolicy() overridden to return unknown policy: " + indirectPolicy);
} 
}

protected void writeIndirectStoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("try");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("Indirector indirector = " + this.findIndirectorExpr + ';');
writeInitializeIndirector(paramProperty, paramClass, paramIndentedWriter);
paramIndentedWriter.println("oos.writeObject( indirector.indirectForm( " + paramProperty.getName() + " ) );");

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println("catch (IOException indirectionIOException)");
paramIndentedWriter.println("{ throw indirectionIOException; }");
paramIndentedWriter.println("catch (Exception indirectionOtherException)");
paramIndentedWriter.println("{ throw new IOException(\"Problem indirectly serializing " + paramProperty.getName() + ": \" + indirectionOtherException.toString() ); }");
}

protected void writeUnstoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
IndirectPolicy indirectPolicy = indirectingPolicy(paramProperty, paramClass);
if (indirectPolicy == IndirectPolicy.DEFINITELY_INDIRECT || indirectPolicy == IndirectPolicy.INDIRECT_ON_EXCEPTION) {

paramIndentedWriter.println("
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("Object o = ois.readObject();");
paramIndentedWriter.println("if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized) o).getObject();");
paramIndentedWriter.println("this." + paramProperty.getName() + " = (" + paramProperty.getSimpleTypeName() + ") o;");
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}
else if (indirectPolicy == IndirectPolicy.DEFINITELY_DIRECT) {
super.writeUnstoreObject(paramProperty, paramClass, paramIndentedWriter);
} else {
throw new InternalError("indirectingPolicy() overridden to return unknown policy: " + indirectPolicy);
} 
}
}

