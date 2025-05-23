package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import com.mchange.v2.naming.JavaBeanObjectFactory;
import com.mchange.v2.naming.JavaBeanReferenceMaker;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class PropertyReferenceableExtension
implements GeneratorExtension
{
boolean explicit_reference_properties = false;
String factoryClassName = JavaBeanObjectFactory.class.getName();

String javaBeanReferenceMakerClassName = JavaBeanReferenceMaker.class.getName();

public void setUseExplicitReferenceProperties(boolean paramBoolean) {
this.explicit_reference_properties = paramBoolean;
}
public boolean getUseExplicitReferenceProperties() {
return this.explicit_reference_properties;
}
public void setFactoryClassName(String paramString) {
this.factoryClassName = paramString;
}
public String getFactoryClassName() {
return this.factoryClassName;
}

public Collection extraGeneralImports() {
return new HashSet();
}

public Collection extraSpecificImports() {
HashSet<String> hashSet = new HashSet();
hashSet.add("javax.naming.Reference");
hashSet.add("javax.naming.Referenceable");
hashSet.add("javax.naming.NamingException");
hashSet.add("com.mchange.v2.naming.JavaBeanObjectFactory");
hashSet.add("com.mchange.v2.naming.JavaBeanReferenceMaker");
hashSet.add("com.mchange.v2.naming.ReferenceMaker");
return hashSet;
}

public Collection extraInterfaceNames() {
HashSet<String> hashSet = new HashSet();
hashSet.add("Referenceable");
return hashSet;
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("final static JavaBeanReferenceMaker referenceMaker = new " + this.javaBeanReferenceMakerClassName + "();");
paramIndentedWriter.println();
paramIndentedWriter.println("static");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("referenceMaker.setFactoryClassName( \"" + this.factoryClassName + "\" );");
if (this.explicit_reference_properties) {
byte b; int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
paramIndentedWriter.println("referenceMaker.addReferenceProperty(\"" + paramArrayOfProperty[b].getName() + "\");");
}
} 
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println();
paramIndentedWriter.println("public Reference getReference() throws NamingException");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("return referenceMaker.createReference( this );");

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}
}

