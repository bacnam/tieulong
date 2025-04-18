package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class CloneableExtension
implements GeneratorExtension
{
boolean export_public;
boolean exception_swallowing;
String mLoggerName = null;

public boolean isExportPublic() {
return this.export_public;
}
public void setExportPublic(boolean paramBoolean) {
this.export_public = paramBoolean;
}
public boolean isExceptionSwallowing() {
return this.exception_swallowing;
}
public void setExceptionSwallowing(boolean paramBoolean) {
this.exception_swallowing = paramBoolean;
}
public String getMLoggerName() {
return this.mLoggerName;
}
public void setMLoggerName(String paramString) {
this.mLoggerName = paramString;
}

public CloneableExtension(boolean paramBoolean1, boolean paramBoolean2) {
this.export_public = paramBoolean1;
this.exception_swallowing = paramBoolean2;
}

public CloneableExtension() {
this(true, false);
}
public Collection extraGeneralImports() {
return (this.mLoggerName == null) ? Collections.EMPTY_SET : Arrays.<String>asList(new String[] { "com.mchange.v2.log" });
}
public Collection extraSpecificImports() {
return Collections.EMPTY_SET;
}

public Collection extraInterfaceNames() {
HashSet<String> hashSet = new HashSet();
hashSet.add("Cloneable");
return hashSet;
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
if (this.export_public) {

paramIndentedWriter.print("public Object clone()");
if (!this.exception_swallowing) {
paramIndentedWriter.println(" throws CloneNotSupportedException");
} else {
paramIndentedWriter.println();
}  paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
if (this.exception_swallowing) {

paramIndentedWriter.println("try");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
} 
paramIndentedWriter.println("return super.clone();");
if (this.exception_swallowing) {

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println("catch (CloneNotSupportedException e)");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
if (this.mLoggerName == null) {
paramIndentedWriter.println("e.printStackTrace();");
} else {

paramIndentedWriter.println("if ( " + this.mLoggerName + ".isLoggable( MLevel.FINE ) )");
paramIndentedWriter.upIndent();
paramIndentedWriter.println(this.mLoggerName + ".log( MLevel.FINE, \"Inconsistent clone() definitions between subclass and superclass! \", e );");
paramIndentedWriter.downIndent();
} 
paramIndentedWriter.println("throw new RuntimeException(\"Inconsistent clone() definitions between subclass and superclass! \" + e);");
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
} 

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
} 
}
}

