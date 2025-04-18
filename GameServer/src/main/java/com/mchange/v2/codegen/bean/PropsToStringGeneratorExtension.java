package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class PropsToStringGeneratorExtension
implements GeneratorExtension
{
private Collection excludePropNames = null;

public void setExcludePropertyNames(Collection paramCollection) {
this.excludePropNames = paramCollection;
}
public Collection getExcludePropertyNames() {
return this.excludePropNames;
}
public Collection extraGeneralImports() {
return Collections.EMPTY_SET;
}
public Collection extraSpecificImports() {
return Collections.EMPTY_SET;
}
public Collection extraInterfaceNames() {
return Collections.EMPTY_SET;
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("public String toString()");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("StringBuffer sb = new StringBuffer();");
paramIndentedWriter.println("sb.append( super.toString() );");
paramIndentedWriter.println("sb.append(\" [ \");"); byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {

Property property = paramArrayOfProperty[b];

if (this.excludePropNames == null || !this.excludePropNames.contains(property.getName())) {

paramIndentedWriter.println("sb.append( \"" + property.getName() + " -> \"" + " + " + property.getName() + " );");
if (b != i - 1)
paramIndentedWriter.println("sb.append( \", \");"); 
} 
} 
paramIndentedWriter.println();
paramIndentedWriter.println("String extraToStringInfo = this.extraToStringInfo();");
paramIndentedWriter.println("if (extraToStringInfo != null)");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("sb.append( extraToStringInfo );");
paramIndentedWriter.downIndent();

paramIndentedWriter.println("sb.append(\" ]\");");
paramIndentedWriter.println("return sb.toString();");
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println();
paramIndentedWriter.println("protected String extraToStringInfo()");
paramIndentedWriter.println("{ return null; }");
}
}

