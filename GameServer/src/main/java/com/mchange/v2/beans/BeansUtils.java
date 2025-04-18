package com.mchange.v2.beans;

import com.mchange.v2.lang.Coerce;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class BeansUtils
{
static final MLogger logger = MLog.getLogger(BeansUtils.class);

static final Object[] EMPTY_ARGS = new Object[0];

public static PropertyEditor findPropertyEditor(PropertyDescriptor paramPropertyDescriptor) {
PropertyEditor propertyEditor = null;
Class<?> clazz = null;

try {
clazz = paramPropertyDescriptor.getPropertyEditorClass();
if (clazz != null) {
propertyEditor = (PropertyEditor)clazz.newInstance();
}
} catch (Exception exception) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Bad property editor class " + clazz.getName() + " registered for property " + paramPropertyDescriptor.getName(), exception);
}
} 
if (propertyEditor == null)
propertyEditor = PropertyEditorManager.findEditor(paramPropertyDescriptor.getPropertyType()); 
return propertyEditor;
}

public static boolean equalsByAccessibleProperties(Object paramObject1, Object paramObject2) throws IntrospectionException {
return equalsByAccessibleProperties(paramObject1, paramObject2, Collections.EMPTY_SET);
}

public static boolean equalsByAccessibleProperties(Object paramObject1, Object paramObject2, Collection paramCollection) throws IntrospectionException {
HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
extractAccessiblePropertiesToMap(hashMap1, paramObject1, paramCollection);
extractAccessiblePropertiesToMap(hashMap2, paramObject2, paramCollection);

return hashMap1.equals(hashMap2);
}

public static boolean equalsByAccessiblePropertiesVerbose(Object paramObject1, Object paramObject2, Collection paramCollection) throws IntrospectionException {
HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
extractAccessiblePropertiesToMap(hashMap1, paramObject1, paramCollection);
extractAccessiblePropertiesToMap(hashMap2, paramObject2, paramCollection);

boolean bool = true;

if (hashMap1.size() != hashMap2.size()) {

System.err.println("Unequal sizes --> Map0: " + hashMap1.size() + "; m1: " + hashMap2.size());
Set set1 = hashMap1.keySet();
set1.removeAll(hashMap2.keySet());

Set set2 = hashMap2.keySet();
set2.removeAll(hashMap1.keySet());

if (set1.size() > 0) {

System.err.println("Map0 extras:");
for (Iterator<E> iterator = set1.iterator(); iterator.hasNext();)
System.err.println('\t' + iterator.next().toString()); 
} 
if (set2.size() > 0) {

System.err.println("Map1 extras:");
for (Iterator<E> iterator = set2.iterator(); iterator.hasNext();)
System.err.println('\t' + iterator.next().toString()); 
} 
bool = false;
} 
for (String str : hashMap1.keySet()) {

Object object1 = hashMap1.get(str);
Object object2 = hashMap2.get(str);
if ((object1 == null && object2 != null) || (object1 != null && !object1.equals(object2))) {

System.err.println('\t' + str + ": " + object1 + " != " + object2);
bool = false;
} 
} 

return bool;
}

public static void overwriteAccessibleProperties(Object paramObject1, Object paramObject2) throws IntrospectionException {
overwriteAccessibleProperties(paramObject1, paramObject2, Collections.EMPTY_SET);
}

public static void overwriteAccessibleProperties(Object paramObject1, Object paramObject2, Collection paramCollection) throws IntrospectionException {
try {
BeanInfo beanInfo = Introspector.getBeanInfo(paramObject1.getClass(), Object.class);
PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {

PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
if (!paramCollection.contains(propertyDescriptor.getName())) {

Method method1 = propertyDescriptor.getReadMethod();
Method method2 = propertyDescriptor.getWriteMethod();

if (method1 == null || method2 == null) {

if (propertyDescriptor instanceof java.beans.IndexedPropertyDescriptor)
{

if (logger.isLoggable(MLevel.WARNING)) {
logger.warning("BeansUtils.overwriteAccessibleProperties() does not support indexed properties that do not provide single-valued array getters and setters! [The indexed methods provide no means of modifying the size of the array in the destination bean if it does not match the source.]");
}
}

if (logger.isLoggable(MLevel.INFO)) {
logger.info("Property inaccessible for overwriting: " + propertyDescriptor.getName());
}
} else {

Object object = method1.invoke(paramObject1, EMPTY_ARGS);
method2.invoke(paramObject2, new Object[] { object });
} 
} 
} 
} catch (IntrospectionException introspectionException) {
throw introspectionException;
} catch (Exception exception) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "Converting exception to throwable IntrospectionException");
}
throw new IntrospectionException(exception.getMessage());
} 
}

public static void overwriteAccessiblePropertiesFromMap(Map paramMap, Object paramObject, boolean paramBoolean) throws IntrospectionException {
overwriteAccessiblePropertiesFromMap(paramMap, paramObject, paramBoolean, Collections.EMPTY_SET);
}

public static void overwriteAccessiblePropertiesFromMap(Map paramMap, Object paramObject, boolean paramBoolean, Collection paramCollection) throws IntrospectionException {
overwriteAccessiblePropertiesFromMap(paramMap, paramObject, paramBoolean, paramCollection, false, MLevel.WARNING, MLevel.WARNING, true);
}

public static void overwriteAccessiblePropertiesFromMap(Map paramMap, Object paramObject, boolean paramBoolean1, Collection paramCollection, boolean paramBoolean2, MLevel paramMLevel1, MLevel paramMLevel2, boolean paramBoolean3) throws IntrospectionException {
if (paramMLevel1 == null)
paramMLevel1 = MLevel.WARNING; 
if (paramMLevel2 == null) {
paramMLevel2 = MLevel.WARNING;
}
Set set = paramMap.keySet();

String str = null;
BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass(), Object.class);
PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b;
int i;
for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {

PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
str = propertyDescriptor.getName();

if (!set.contains(str)) {
continue;
}
if (paramCollection != null && paramCollection.contains(str)) {
continue;
}

Object object = paramMap.get(str);
if (object == null)
{
if (paramBoolean1) {
continue;
}
}
Method method = propertyDescriptor.getWriteMethod();
boolean bool = false;

Class<?> clazz = propertyDescriptor.getPropertyType();

if (method == null) {

if (propertyDescriptor instanceof java.beans.IndexedPropertyDescriptor)
{
if (logger.isLoggable(MLevel.FINER)) {
logger.finer("BeansUtils.overwriteAccessiblePropertiesFromMap() does not support indexed properties that do not provide single-valued array getters and setters! [The indexed methods provide no means of modifying the size of the array in the destination bean if it does not match the source.]");
}
}

if (logger.isLoggable(paramMLevel1))
{
String str1 = "Property inaccessible for overwriting: " + str;
logger.log(paramMLevel1, str1);
if (paramBoolean3)
{
bool = true;
throw new IntrospectionException(str1);

}

}

}
else if (paramBoolean2 && object != null && object.getClass() == String.class && (clazz = propertyDescriptor.getPropertyType()) != String.class && Coerce.canCoerce(clazz)) {

try {

Object object1 = Coerce.toObject((String)object, clazz);

method.invoke(paramObject, new Object[] { object1 });
}
catch (IllegalArgumentException illegalArgumentException) {

String str1 = "Failed to coerce property: " + str + " [propVal: " + object + "; propType: " + clazz + "]";

if (logger.isLoggable(paramMLevel2))
logger.log(paramMLevel2, str1, illegalArgumentException); 
if (paramBoolean3)
{
bool = true;
throw new IntrospectionException(str1);
}

} catch (Exception exception) {

String str1 = "Failed to set property: " + str + " [propVal: " + object + "; propType: " + clazz + "]";

if (logger.isLoggable(paramMLevel1))
logger.log(paramMLevel1, str1, exception); 
if (paramBoolean3) {

bool = true;
throw new IntrospectionException(str1);
} 
} 
} else {

try {

method.invoke(paramObject, new Object[] { object });
}
catch (Exception exception) {

String str1 = "Failed to set property: " + str + " [propVal: " + object + "; propType: " + clazz + "]";

if (logger.isLoggable(paramMLevel1))
logger.log(paramMLevel1, str1, exception); 
if (paramBoolean3) {

bool = true;
throw new IntrospectionException(str1);
} 
} 
} 
continue;
} 
}

public static void appendPropNamesAndValues(StringBuffer paramStringBuffer, Object paramObject, Collection paramCollection) throws IntrospectionException {
TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
extractAccessiblePropertiesToMap(treeMap, paramObject, paramCollection);
boolean bool = true;
for (String str : treeMap.keySet()) {

Object object = treeMap.get(str);
if (bool) {
bool = false;
} else {
paramStringBuffer.append(", ");
}  paramStringBuffer.append(str);
paramStringBuffer.append(" -> ");
paramStringBuffer.append(object);
} 
}

public static void extractAccessiblePropertiesToMap(Map paramMap, Object paramObject) throws IntrospectionException {
extractAccessiblePropertiesToMap(paramMap, paramObject, Collections.EMPTY_SET);
}

public static void extractAccessiblePropertiesToMap(Map<String, Object> paramMap, Object paramObject, Collection paramCollection) throws IntrospectionException {
String str = null;

try {
BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass(), Object.class);
PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {

PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
str = propertyDescriptor.getName();
if (!paramCollection.contains(str)) {

Method method = propertyDescriptor.getReadMethod();
Object object = method.invoke(paramObject, EMPTY_ARGS);
paramMap.put(str, object);
} 
} 
} catch (IntrospectionException introspectionException) {

if (logger.isLoggable(MLevel.WARNING))
logger.warning("Problem occurred while overwriting property: " + str); 
if (logger.isLoggable(MLevel.FINE)) {
logger.logp(MLevel.FINE, BeansUtils.class.getName(), "extractAccessiblePropertiesToMap( Map fillMe, Object bean, Collection ignoreProps )", ((str != null) ? ("Problem occurred while overwriting property: " + str) : "") + " throwing...", introspectionException);
}

throw introspectionException;
}
catch (Exception exception) {

if (logger.isLoggable(MLevel.FINE)) {
logger.logp(MLevel.FINE, BeansUtils.class.getName(), "extractAccessiblePropertiesToMap( Map fillMe, Object bean, Collection ignoreProps )", "Caught unexpected Exception; Converting to IntrospectionException.", exception);
}

throw new IntrospectionException(exception.toString() + ((str == null) ? "" : (" [" + str + ']')));
} 
}

private static void overwriteProperty(String paramString, Object paramObject1, Method paramMethod, Object paramObject2) throws Exception {
if (paramMethod.getDeclaringClass().isAssignableFrom(paramObject2.getClass())) {
paramMethod.invoke(paramObject2, new Object[] { paramObject1 });
} else {

BeanInfo beanInfo = Introspector.getBeanInfo(paramObject2.getClass(), Object.class);
PropertyDescriptor propertyDescriptor = null;

PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
if (paramString.equals(arrayOfPropertyDescriptor[b].getName())) {

propertyDescriptor = arrayOfPropertyDescriptor[b];
break;
} 
} 
Method method = propertyDescriptor.getWriteMethod();
method.invoke(paramObject2, new Object[] { paramObject1 });
} 
}

public static void overwriteSpecificAccessibleProperties(Object paramObject1, Object paramObject2, Collection<?> paramCollection) throws IntrospectionException {
try {
HashSet hashSet = new HashSet(paramCollection);

BeanInfo beanInfo = Introspector.getBeanInfo(paramObject1.getClass(), Object.class);
PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {

PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
String str = propertyDescriptor.getName();
if (hashSet.remove(str)) {

Method method1 = propertyDescriptor.getReadMethod();
Method method2 = propertyDescriptor.getWriteMethod();

if (method1 == null || method2 == null) {

if (propertyDescriptor instanceof java.beans.IndexedPropertyDescriptor)
{

if (logger.isLoggable(MLevel.WARNING)) {
logger.warning("BeansUtils.overwriteAccessibleProperties() does not support indexed properties that do not provide single-valued array getters and setters! [The indexed methods provide no means of modifying the size of the array in the destination bean if it does not match the source.]");
}
}

if (logger.isLoggable(MLevel.INFO)) {
logger.info("Property inaccessible for overwriting: " + propertyDescriptor.getName());
}
} else {

Object object = method1.invoke(paramObject1, EMPTY_ARGS);
overwriteProperty(str, object, method2, paramObject2);
} 
} 
} 
if (logger.isLoggable(MLevel.WARNING))
{
for (Iterator<String> iterator = hashSet.iterator(); iterator.hasNext();) {
logger.warning("failed to find expected property: " + iterator.next());
}
}
}
catch (IntrospectionException introspectionException) {
throw introspectionException;
} catch (Exception exception) {

if (logger.isLoggable(MLevel.FINE)) {
logger.logp(MLevel.FINE, BeansUtils.class.getName(), "overwriteSpecificAccessibleProperties( Object sourceBean, Object destBean, Collection props )", "Caught unexpected Exception; Converting to IntrospectionException.", exception);
}

throw new IntrospectionException(exception.getMessage());
} 
}

public static void debugShowPropertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
System.err.println("PropertyChangeEvent: [ propertyName -> " + paramPropertyChangeEvent.getPropertyName() + ", oldValue -> " + paramPropertyChangeEvent.getOldValue() + ", newValue -> " + paramPropertyChangeEvent.getNewValue() + " ]");
}
}

