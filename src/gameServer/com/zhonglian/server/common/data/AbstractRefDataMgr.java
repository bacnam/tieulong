package com.zhonglian.server.common.data;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import com.zhonglian.server.common.data.ref.RefBase;
import com.zhonglian.server.common.data.ref.RefFactor;
import com.zhonglian.server.common.data.ref.RefGeneral;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.CommFile;
import com.zhonglian.server.common.utils.CommTime;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRefDataMgr
{
private static AbstractRefDataMgr instance = null;

protected Map<Class<? extends RefBase>, RefContainer<?>> refs;

private List<String> dulError;

private boolean assertPassed;

protected AbstractRefDataMgr() {
this.refs = new ConcurrentHashMap<>();
this.dulError = new ArrayList<>();
this.assertPassed = false;
instance = this;
} public static <T extends RefBase> T get(Class<T> clazz, Object key) {
RefBase refBase;
T ret = null;
if (instance.refs.containsKey(clazz)) {
refBase = ((RefContainer<RefBase>)instance.refs.get(clazz)).get(key);
}
if (refBase == null)
CommLog.warn("BaseRefDataMgr get clazz:{}, key:{} failed!!", clazz.getSimpleName(), key.toString()); 
return (T)refBase;
} public static AbstractRefDataMgr getInstance() {
return instance;
} public static <T extends RefBase> T getOrLast(Class<T> clazz, Object key) {
RefBase refBase;
T ret = null;
if (instance.refs.containsKey(clazz)) {
refBase = ((RefContainer<RefBase>)instance.refs.get(clazz)).get(key);
}
if (refBase == null)
return getAll(clazz).last(); 
return (T)refBase;
}

public static <T extends RefBase> RefContainer<T> getAll(Class<T> clazz) {
if (!instance.refs.containsKey(clazz)) {
instance.refs.put(clazz, new RefContainer());
}
return (RefContainer<T>)instance.refs.get(clazz);
}

public static <T extends RefBase> int size(Class<T> clazz) {
if (!instance.refs.containsKey(clazz)) {
instance.refs.put(clazz, new RefContainer());
}
return ((RefContainer)instance.refs.get(clazz)).size();
}

public static int getFactor(String name) {
try {
RefFactor ref = get(RefFactor.class, name);
if (ref != null) {
return (int)ref.Value;
}
} catch (Exception e) {
return 0;
} 
return 0;
}

public static int getFactor(String name, int defaultValue) {
try {
RefFactor ref = get(RefFactor.class, name);
if (ref != null) {
return (int)ref.Value;
}
} catch (Exception e) {
return defaultValue;
} 
return defaultValue;
}

public static double getFactor(String name, double defaultValue) {
try {
RefFactor ref = get(RefFactor.class, name);
if (ref != null) {
return ref.Value;
}
} catch (Exception e) {
return defaultValue;
} 
return defaultValue;
}

public static String getGeneral(String name, String defaultValue) {
try {
RefGeneral ref = get(RefGeneral.class, name);
if (ref != null) {
return ref.Value;
}
} catch (Exception e) {
return defaultValue;
} 
return defaultValue;
}

public static List<String> getGeneral(String name) {
List<String> list = new ArrayList<>();

RefGeneral ref = get(RefGeneral.class, name);
try {
if (ref != null) {
String[] lists = ref.Value.split(";");
list = Arrays.asList(lists);
return list;
} 
} catch (Exception e) {
if (ref == null) {
CommLog.error("getGeneral name:{}, null", name);
} else {
CommLog.error("getGeneral name:{}, value:{} parse List failed", name, ref.Value);
} 
return list;
} 
return list;
}

public void reload() {
CommLog.info("=======开始加载配表");
this.dulError.clear();

checkDirects();
renameRefFilesToLowcase();

load(RefBase.class);
onCustomLoad();

this.assertPassed = assertRefs();
CommLog.info("=======配表加载结束");
}

public boolean assertRefs() {
long begin = CommTime.nowMS();
CommLog.info("进行配表检测...");
boolean rtn = true;
for (Map.Entry<Class<? extends RefBase>, RefContainer<?>> pair : this.refs.entrySet()) {
for (RefBase ref : ((RefContainer)pair.getValue()).values()) {
try {
if (!ref.Assert()) {
CommLog.error("[{}] 配表自校验发现问题:{}", ref.getClass().getSimpleName(), CommClass.getClassPropertyInfos(ref));
rtn = false;
} 
} catch (Throwable e) {
CommLog.error("[{}] 配表自校验发现问题:{},异常:", new Object[] { ref.getClass().getSimpleName(), CommClass.getClassPropertyInfos(ref), e });
rtn = false;
} 
} 
try {
RefBase base = ((Class<RefBase>)pair.getKey()).newInstance();
if (!base.AssertAll(pair.getValue())) {
CommLog.error("[{}] 配表全局校验发现问题", ((Class)pair.getKey()).getSimpleName());
rtn = false;
} 
} catch (Throwable e) {
CommLog.error("[{}] 配表全局校验发现问题, 异常:", ((Class)pair.getKey()).getSimpleName(), e);
rtn = false;
} 
} 
try {
if (!assertAll()) {
CommLog.error("配表总检不通过");
rtn = false;
} 
} catch (Throwable e) {
CommLog.error("refdata自校验不通过", e);
rtn = false;
} 
CommLog.info("配表检测完毕，用时:{}ms", Long.valueOf(CommTime.nowMS() - begin));
return rtn;
}

protected abstract boolean assertAll();

public boolean isAssertPassed() {
return this.assertPassed;
}

public <T extends RefBase> void load(Class<T> clazz) {
List<Class<?>> refdatas = CommClass.getAllClassByInterface(clazz, clazz.getPackage().getName());
for (Class<?> cs : refdatas) {
RefBase refdata = null;
try {
refdata = CommClass.forName(cs.getName()).newInstance();
} catch (Exception e) {
CommLog.error("onAutoLoad occured error:{}", e.getMessage());
} 
if (refdata == null) {
continue;
}
RefContainer<?> data = this.refs.get(cs);

if (data == null) {
data = new RefContainer();
this.refs.put(refdata.getClass(), data);
} 

loadByClazz((Class)refdata.getClass(), data);
} 
}

protected abstract void onCustomLoad();

public abstract String getRefPath();

public boolean checkDirects() {
File path = new File(getRefPath());
if (!path.exists() || !path.isDirectory()) {
CommLog.error("【【【Refdata文件夹地址(" + path.getAbsolutePath() + ")配置错误！！！】】】");
System.exit(-1);
} 
return true;
}

public boolean renameRefFilesToLowcase() {
File path = new File(getRefPath());
File[] files = path.listFiles(); byte b; int i; File[] arrayOfFile1;
for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) { File refFile = arrayOfFile1[b];
if (refFile.isFile())
{

if (refFile.getName().toLowerCase().endsWith(".txt")) {

File lowcaseFile = new File(refFile.getParentFile(), refFile.getName().toLowerCase());
if (!lowcaseFile.exists())
{

refFile.renameTo(lowcaseFile); } 
}  }  b++; }
return true;
}

public <T extends RefBase> void loadByClazz(Class<? extends RefBase> clazz, RefContainer<T> data) {
String name = clazz.getSimpleName();
String path = String.format("%s%c%s.txt", new Object[] { getRefPath(), Character.valueOf(File.separatorChar), name.substring(3).toLowerCase() });
Map<String, Map<String, String>> table = CommFile.GetTable(path, 2, 3);

Field keysField = null; byte b; int i; Field[] arrayOfField;
for (i = (arrayOfField = clazz.getFields()).length, b = 0; b < i; ) { Field f = arrayOfField[b];
RefField annotation = f.<RefField>getAnnotation(RefField.class);
if (annotation != null && annotation.iskey())
if (keysField != null) {
CommLog.error("[{}]配置了多个键值({},{})", new Object[] { clazz.getSimpleName(), f.getName(), keysField.getName() });
System.exit(1);
} else {
keysField = f;
}  
b++; }

if (keysField == null) {
CommLog.error("[{}]未配置键值", clazz.getSimpleName());
System.exit(1);
} 

for (Map<String, String> lineValue : table.values()) {
String keysrtvalue = lineValue.get(keysField.getName());
if (keysrtvalue == null) {
CommLog.error("[{}]txt配表上缺少键值[{}]的值配置", clazz.getName(), keysField.getName());
System.exit(1);
} 
Object keyvalue = parseCreateObj(keysField.getType(), keysrtvalue, clazz.getSimpleName(), keysField.getName());
if (keyvalue == null) {
CommLog.error("[{}]txt配表上缺少键值[{}]值[{}]解析失败", new Object[] { clazz.getName(), keysField.getName(), keysrtvalue });
continue;
} 
RefBase refBase = (RefBase)data.get(keyvalue);
if (refBase == null) {
try {
refBase = clazz.newInstance();
} catch (Exception e) {
CommLog.error("[{}]创建实例失败", e);
System.exit(1);
} 
data.put(keyvalue, (T)refBase);
} 
try {
loadLineValue(refBase, lineValue);
} catch (Exception e) {
CommLog.error("[{}]解析配置时发生错误", clazz.getName(), e);
} 
} 
}

public <T extends RefBase> void loadLineValue(T obj, Map<String, String> lineValue) throws Exception {
String parsingTable = obj.getClass().getSimpleName();
Field[] fields = obj.getClass().getDeclaredFields(); byte b; int i; Field[] arrayOfField1;
for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) { Field field = arrayOfField1[b];
int modifiers = field.getModifiers();
if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {

RefField annotation = field.<RefField>getAnnotation(RefField.class);
if (annotation == null || annotation.isfield()) {
Object fieldValue;

String varName = field.getName();
String varValue = lineValue.get(varName);
Class<?> typeClass = field.getType();

if (List.class.isAssignableFrom(typeClass)) {
List<Object> listValue = null;
if (typeClass.isAssignableFrom(List.class)) {
listValue = new ArrayList();
} else {
listValue = (List<Object>)typeClass.newInstance();
} 

if (varValue != null && !varValue.equals("")) {
Type gt = field.getGenericType();
ParameterizedType pt = (ParameterizedType)gt;
Class<?> clazz = (Class)pt.getActualTypeArguments()[0];
String[] valueList = varValue.split(";");
int length = valueList.length;
for (int index = 0; index < length; index++) {
listValue.add(parseCreateObj(clazz, valueList[index], parsingTable, varName));
}
} 
fieldValue = listValue;
}
else if (typeClass.isArray()) {
Class<Object[]> typeListClass = (Class)field.getType();

Class<Object> subTypeClass = (Class)typeClass.getComponentType();

Object[] listValue = (Object[])Array.newInstance(subTypeClass, 0);
if (varValue != null && !varValue.equals("")) {
String[] valueList = varValue.split(";");
int length = valueList.length;

listValue = (Object[])Array.newInstance(subTypeClass, length);

for (int index = 0; index < length; index++) {
listValue[index] = parseCreateObj(typeListClass.getComponentType(), valueList[index], parsingTable, varName);
}
} 

fieldValue = listValue;
}
else if (varValue != null) {
fieldValue = parseCreateObj(typeClass, varValue, parsingTable, varName);
} else {
fieldValue = null;
} 

if (fieldValue != null)
field.set(obj, fieldValue); 
} 
} 
b++; }

}
private Object parseCreateObj(Class<?> field, String value, String tbName, String fieldName) {
try {
if (field == int.class || field == Integer.class) {

int index = value.indexOf(".");
if (-1 != index) {
value = value.substring(0, index);
}

if (value.equals("")) {
return Integer.valueOf(0);
}
int ret = Integer.valueOf(value).intValue();
return Integer.valueOf(ret);
}  if (field == long.class || field == Long.class) {

if (value.equals("")) {
return Long.valueOf(0L);
}
return Long.valueOf(value);
}  if (field == float.class || field == Float.class) {

if (value.equals("")) {
return Float.valueOf(0.0F);
}
return Float.valueOf(value);
}  if (field == double.class || field == Double.class) {

if (value.equals("")) {
return Float.valueOf(0.0F);
}
return Double.valueOf(value);
}  if (field == boolean.class || field == Boolean.class)
try {
int v = Integer.valueOf(value).intValue();
return (v != 0) ? Boolean.valueOf(true) : Boolean.valueOf(false);
} catch (Exception e) {
return Boolean.valueOf(value);
}  
if (field == NumberRange.class)
return NumberRange.parse(value); 
if (field == String.class)
return value; 
if (field.isEnum()) {
if (value.isEmpty()) {
value = "None";
}
return Enum.valueOf(field, value);
}

} catch (Exception e) {
log("BaseRefDataMgr 解析值失败 [%s] field:[%s] type:[%s] value:[%s] ", new Object[] { tbName, fieldName, field.getSimpleName(), value });
} 
return null;
}

private void log(String format, Object... param) {
String err = String.format(format, param);
if (!this.dulError.contains(err)) {
CommLog.error(err);
this.dulError.add(err);
} 
}
}

