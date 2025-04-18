package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ExplicitPropsConstructorGeneratorExtension
implements GeneratorExtension
{
static final MLogger logger = MLog.getLogger(ExplicitPropsConstructorGeneratorExtension.class);

String[] propNames;

boolean skips_silently = false;

int ctor_modifiers;

public String[] getPropNames() {
return (String[])this.propNames.clone();
}
public void setPropNames(String[] paramArrayOfString) {
this.propNames = (String[])paramArrayOfString.clone();
}
public boolean isSkipsSilently() {
return this.skips_silently;
}
public void setsSkipsSilently(boolean paramBoolean) {
this.skips_silently = paramBoolean;
}
public ExplicitPropsConstructorGeneratorExtension() { this.ctor_modifiers = 1; } public ExplicitPropsConstructorGeneratorExtension(String[] paramArrayOfString) { this.ctor_modifiers = 1;
this.propNames = paramArrayOfString; }
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
HashMap<Object, Object> hashMap = new HashMap<Object, Object>(); int i;
for (byte b = 0; b < i; b++) {
hashMap.put(paramArrayOfProperty[b].getName(), paramArrayOfProperty[b]);
}
ArrayList<Property> arrayList = new ArrayList(this.propNames.length); int j;
for (i = 0, j = this.propNames.length; i < j; i++) {

Property property = (Property)hashMap.get(this.propNames[i]);
if (property == null) {
logger.warning("Could not include property '" + this.propNames[i] + "' in explicit-props-constructor generated for bean class '" + paramClassInfo.getClassName() + "' because the property is not defined for the bean. Skipping.");
} else {

arrayList.add(property);
} 
} 
if (arrayList.size() > 0) {

Property[] arrayOfProperty = arrayList.<Property>toArray(new Property[arrayList.size()]);

paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
paramIndentedWriter.print(paramClassInfo.getClassName() + "( ");
BeangenUtils.writeArgList(arrayOfProperty, true, paramIndentedWriter);
paramIndentedWriter.println(" )");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
int k;
for (j = 0, k = arrayOfProperty.length; j < k; j++) {

paramIndentedWriter.print("this." + arrayOfProperty[j].getName() + " = ");
String str = arrayOfProperty[j].getDefensiveCopyExpression();
if (str == null)
str = arrayOfProperty[j].getName(); 
paramIndentedWriter.println(str + ';');
} 

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
} 
}
}

