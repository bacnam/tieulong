package com.mchange.v2.beans.swing;

import com.mchange.v2.beans.BeansUtils;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;

class PropertyComponentBindingUtility
{
static final Object[] EMPTY_ARGS = new Object[0];

HostBindingInterface hbi;

Object bean;
PropertyDescriptor pd = null;
EventSetDescriptor propChangeEsd = null;
Method addMethod = null;
Method removeMethod = null;
Method propGetter = null;
Method propSetter = null;
PropertyEditor propEditor = null;

Object nullReplacement = null;

PropertyComponentBindingUtility(final HostBindingInterface hbi, Object paramObject, final String propName, boolean paramBoolean) throws IntrospectionException {
this.hbi = hbi;
this.bean = paramObject;

BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass());

PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); int i;
for (byte b = 0; b < i; b++) {

PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
if (propName.equals(propertyDescriptor.getName())) {

this.pd = propertyDescriptor;
break;
} 
} 
if (this.pd == null) {
throw new IntrospectionException("Cannot find property on bean Object with name '" + propName + "'.");
}
EventSetDescriptor[] arrayOfEventSetDescriptor = beanInfo.getEventSetDescriptors(); int j;
for (i = 0, j = arrayOfEventSetDescriptor.length; i < j; i++) {

EventSetDescriptor eventSetDescriptor = arrayOfEventSetDescriptor[i];
if ("propertyChange".equals(eventSetDescriptor.getName())) {

this.propChangeEsd = eventSetDescriptor;
break;
} 
} 
if (this.propChangeEsd == null) {
throw new IntrospectionException("Cannot find PropertyChangeEvent on bean Object with name '" + propName + "'.");
}
this.propEditor = BeansUtils.findPropertyEditor(this.pd);
if (paramBoolean && this.propEditor == null) {
throw new IntrospectionException("Could not find an appropriate PropertyEditor for property: " + propName);
}

this.propGetter = this.pd.getReadMethod();
this.propSetter = this.pd.getWriteMethod();

if (this.propGetter == null || this.propSetter == null) {
throw new IntrospectionException("The specified property '" + propName + "' must be both readdable and writable, but it is not!");
}
Class<?> clazz = this.pd.getPropertyType();
if (clazz.isPrimitive()) {

if (clazz == boolean.class)
this.nullReplacement = Boolean.FALSE; 
if (clazz == byte.class) {
this.nullReplacement = new Byte((byte)0);
} else if (clazz == char.class) {
this.nullReplacement = new Character(false);
} else if (clazz == short.class) {
this.nullReplacement = new Short((short)0);
} else if (clazz == int.class) {
this.nullReplacement = new Integer(0);
} else if (clazz == long.class) {
this.nullReplacement = new Long(0L);
} else if (clazz == float.class) {
this.nullReplacement = new Float(0.0F);
} else if (clazz == double.class) {
this.nullReplacement = new Double(0.0D);
} else {
throw new InternalError("What kind of primitive is " + clazz.getName() + "???");
} 
} 
this.addMethod = this.propChangeEsd.getAddListenerMethod();
this.removeMethod = this.propChangeEsd.getAddListenerMethod();

PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
{
public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent)
{
String str = param1PropertyChangeEvent.getPropertyName();
if (str.equals(propName)) {
hbi.syncToValue(PropertyComponentBindingUtility.this.propEditor, param1PropertyChangeEvent.getNewValue());
}
}
};
try {
this.addMethod.invoke(paramObject, new Object[] { propertyChangeListener });
} catch (Exception exception) {

exception.printStackTrace();
throw new IntrospectionException("The introspected PropertyChangeEvent adding method failed with an Exception.");
} 

hbi.addUserModificationListeners();
}

public void userModification() {
Object object = null;
try {
object = this.propGetter.invoke(this.bean, EMPTY_ARGS);
} catch (Exception exception) {
exception.printStackTrace();
} 

try {
Object object1 = this.hbi.fetchUserModification(this.propEditor, object);
if (object1 == null)
object1 = this.nullReplacement; 
this.propSetter.invoke(this.bean, new Object[] { object1 });
}
catch (Exception exception) {

if (!(exception instanceof java.beans.PropertyVetoException))
exception.printStackTrace(); 
syncComponentToValue(true);
} 
}

public void resync() {
syncComponentToValue(false);
}

private void syncComponentToValue(final boolean alert_error) {
try {
final Object reversionValue = this.propGetter.invoke(this.bean, EMPTY_ARGS);
Runnable runnable = new Runnable()
{
public void run()
{
if (alert_error)
PropertyComponentBindingUtility.this.hbi.alertErroneousInput(); 
PropertyComponentBindingUtility.this.hbi.syncToValue(PropertyComponentBindingUtility.this.propEditor, reversionValue);
}
};
SwingUtilities.invokeLater(runnable);
}
catch (Exception exception) {

exception.printStackTrace();
} 
}
}

