package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class PropertyMapConstructorGeneratorExtension
implements GeneratorExtension
{
int ctor_modifiers = 1;

public Collection extraGeneralImports() {
return Collections.EMPTY_SET;
}

public Collection extraSpecificImports() {
HashSet<String> hashSet = new HashSet();
hashSet.add("java.util.Map");
return hashSet;
}

public Collection extraInterfaceNames() {
return Collections.EMPTY_SET;
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
paramIndentedWriter.print(' ' + paramClassInfo.getClassName() + "( Map map )");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("Object raw;"); byte b; int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {

Property property = paramArrayOfProperty[b];
String str = property.getName();
Class<boolean> clazz = paramArrayOfClass[b];
paramIndentedWriter.println("raw = map.get( \"" + str + "\" );");
paramIndentedWriter.println("if (raw != null)");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.print("this." + str + " = ");
if (clazz == boolean.class) {
paramIndentedWriter.println("((Boolean) raw ).booleanValue();");
} else if (clazz == byte.class) {
paramIndentedWriter.println("((Byte) raw ).byteValue();");
} else if (clazz == char.class) {
paramIndentedWriter.println("((Character) raw ).charValue();");
} else if (clazz == short.class) {
paramIndentedWriter.println("((Short) raw ).shortValue();");
} else if (clazz == int.class) {
paramIndentedWriter.println("((Integer) raw ).intValue();");
} else if (clazz == long.class) {
paramIndentedWriter.println("((Long) raw ).longValue();");
} else if (clazz == float.class) {
paramIndentedWriter.println("((Float) raw ).floatValue();");
} else if (clazz == double.class) {
paramIndentedWriter.println("((Double) raw ).doubleValue();");
}  paramIndentedWriter.println("raw = null;");

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
} 

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}
}

