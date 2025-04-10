package com.zhonglian.server.common.data;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.ref.RefBase;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RefAssert
{
public static boolean listSize(List<?> lista, List<?> listb, List... lists) {
if (lista == null) {
CommLog.error("[RefAssert.listSize] list(1) 为空");
return false;
} 
if (listb == null) {
CommLog.error("[RefAssert.listSize] list(2) 为空");
return false;
} 
if (lista.size() != listb.size()) {
CommLog.error("[RefAssert.listSize] list(1) 长度:{}, list(2)长度:{}", Integer.valueOf(lista.size()), Integer.valueOf(listb.size()));
return false;
} 

int size = lista.size();
for (int i = 0; i < lists.length; i++) {
List<?> list = lists[i];
if (list == null) {
CommLog.error("[RefAssert.listSize] list({}) 为空", Integer.valueOf(i + 3));
return false;
} 
if (list.size() != size) {
CommLog.error("[RefAssert.listSize] list({}) 长度:{}, 其他list长度:{}", new Object[] { Integer.valueOf(i + 3), Integer.valueOf(list.size()), Integer.valueOf(size) });
return false;
} 
} 
return true;
}

public static boolean inList(Object value, Object option1, Object... options) {
if (value.getClass().isArray() || List.class.isAssignableFrom(value.getClass())) {
for (Object v : value) {
if (!v.equals(option1) && !contained(options, v)) {
return false;
}
} 
} else {
return !(!value.equals(option1) && !contained(options, value));
} 
return false;
}

public static boolean inRef(Object value, Class<? extends RefBase> clazz, String fieldName, Object... option) {
Field field = null;
try {
field = clazz.getField(fieldName);
} catch (Exception e) {
CommLog.error("校验配表数值失败：[{}]表没有字段[{}]", clazz.getSimpleName(), fieldName);
return false;
} 
RefField refField = field.<RefField>getAnnotation(RefField.class);
boolean iskey = (refField != null && refField.iskey());
if (value.getClass().isArray() || List.class.isAssignableFrom(value.getClass())) {
List<Object> list = iskey ? new ArrayList() : getRefValues(clazz, field);
for (Object v : value) {
if (contained(option, v)) {
continue;
}
if (iskey && AbstractRefDataMgr.get(clazz, v) == null) {
CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { v, clazz.getSimpleName(), fieldName });
return false;
}  if (!iskey && !list.contains(v)) {
CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { v, clazz.getSimpleName(), fieldName });
return false;
} 
} 
} else {
if (contained(option, value)) {
return true;
}
if (iskey && AbstractRefDataMgr.get(clazz, value) == null) {
CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { value, clazz.getSimpleName(), fieldName });
return false;
}  if (!iskey && !getRefValues(clazz, field).contains(value)) {
CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { value, clazz.getSimpleName(), fieldName });
return false;
} 
} 
return true;
}

public static boolean inRef(Object value, Class<? extends RefBase> clazz, Object... option) {
return inRef(value, clazz, "id", option);
}

private static List<Object> getRefValues(Class<? extends RefBase> clazz, Field field) {
RefContainer<?> container = AbstractRefDataMgr.getAll(clazz);
List<Object> list = new ArrayList();
for (Object ref : container.values()) {
try {
list.add(field.get(ref));
} catch (Exception e) {
CommLog.error("校验配表数值失败：[{}]表没有字段[{}]", clazz.getSimpleName(), field.getName());
} 
} 
return list; } private static boolean contained(Object[] array, Object v) {
byte b;
int i;
Object[] arrayOfObject;
for (i = (arrayOfObject = array).length, b = 0; b < i; ) { Object a = arrayOfObject[b];
if (v.equals(a))
return true; 
b++; }

return false;
}
}

