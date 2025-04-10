package business.gmcmd.cmd;

import business.player.Player;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Command
{
private String name;
private String comment;
private Method cmd;
private Object excuter;

public Command(business.gmcmd.annotation.Command annotation, Method m, Object excuter) {
this.name = annotation.command().trim();
if (this.name.isEmpty()) {
this.name = m.getName().toLowerCase();
}
this.comment = annotation.comment();
this.cmd = m;
this.excuter = excuter;
}

public String run(Player player, String[] args) throws Exception {
Object[] params = parseParams(player, (Object[])args);

if (this.cmd.getReturnType() == null) {
this.cmd.invoke(this.excuter, params);
return "command[" + this.name + "] run success";
} 
Object rtn = this.cmd.invoke(this.excuter, params);
if (rtn == null) {
return "command[" + this.name + "] run success";
}
return rtn.toString();
}

private Object[] parseParams(Player player, Object[] args) throws Exception {
Class[] type = this.cmd.getParameterTypes();
int parameCount = this.cmd.getParameterCount(); byte b; int j; Class[] arrayOfClass1;
for (j = (arrayOfClass1 = type).length, b = 0; b < j; ) { Class<?> clazz = arrayOfClass1[b];
if (clazz == Player.class) {
parameCount--;
break;
} 
b++; }

if (args.length != parameCount) {
throw new Exception("[" + this.name + "] needs " + type.length + " params but provide " + args.length + " params");
}

Object[] newArgs = new Object[type.length];
if (parameCount < type.length) {
int index = 0;
for (int k = 0; k < type.length; k++) {
if (type[k] == Player.class) {
newArgs[k] = player;
} else {

newArgs[k] = args[index];
index++;
} 
} 
} 
Object[] params = new Object[this.cmd.getParameterCount()];
for (int i = 0; i < type.length; i++) {
params[i] = parseParam(type[i], newArgs[i]);
}

return params;
}

private Object parseParam(Class<?> clazz, Object object) throws Exception {
if (clazz == Player.class) {
return object;
}
String param = (String)object;
if (List.class.isAssignableFrom(clazz)) {
List<Object> list;
if (clazz.isAssignableFrom(List.class)) {
list = new ArrayList();
} else {
list = (List<Object>)clazz.newInstance();
} 
Type gt = clazz.getGenericSuperclass();
ParameterizedType pt = (ParameterizedType)gt;
Class<?> ptClass = (Class)pt.getActualTypeArguments()[0];
String[] subparams = param.split(";"); byte b; int i; String[] arrayOfString1;
for (i = (arrayOfString1 = subparams).length, b = 0; b < i; ) { String p = arrayOfString1[b];
list.add(parseParam(ptClass, p)); b++; }

return list;
}  if (clazz.isArray()) {
String[] subparams = param.split(";");
Object[] array = new Object[subparams.length];
Class<Object[]> ptClass = (Class)clazz.getComponentType();
for (int i = 0; i < subparams.length; i++) {
array[i] = parseParam(ptClass, subparams[i]);
}
return array;
}  if (clazz == int.class || clazz == Integer.class)
return Integer.valueOf(Integer.parseInt(param)); 
if (clazz == long.class || clazz == Long.class)
return Long.valueOf(Long.parseLong(param)); 
if (clazz == float.class || clazz == Float.class)
return Float.valueOf(param); 
if (clazz == double.class || clazz == Double.class)
return Double.valueOf(param); 
if (clazz == boolean.class || clazz == Boolean.class)
return Boolean.valueOf(param); 
if (clazz == String.class)
return param; 
if (clazz.isEnum()) {
return Enum.valueOf(clazz, param);
}
throw new Exception("can not instance " + clazz.getSimpleName() + " define for command:" + this.name);
}

public String toString() {
return String.valueOf(this.name) + " " + getParamString() + " " + this.comment;
}

private String getParamString() {
StringBuilder stringBuilder = new StringBuilder(); byte b; int i; Parameter[] arrayOfParameter;
for (i = (arrayOfParameter = this.cmd.getParameters()).length, b = 0; b < i; ) { Parameter parameter = arrayOfParameter[b];
if (parameter.getType() != Player.class)
{

stringBuilder.append(parameter.getType().getSimpleName().toLowerCase()).append(" "); }  b++; }

return stringBuilder.toString();
}

public String getName() {
return this.name;
}
}

