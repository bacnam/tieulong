package com.mchange.v2.naming;

import com.mchange.v2.beans.BeansUtils;
import com.mchange.v2.lang.Coerce;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.ser.SerializableUtils;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.*;

public class JavaBeanObjectFactory
        implements ObjectFactory {
    static final Object NULL_TOKEN = new Object();
    private static final MLogger logger = MLog.getLogger(JavaBeanObjectFactory.class);

    public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable) throws Exception {
        if (paramObject instanceof Reference) {

            Reference reference = (Reference) paramObject;
            HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            for (Enumeration<RefAddr> enumeration = reference.getAll(); enumeration.hasMoreElements(); ) {

                RefAddr refAddr = enumeration.nextElement();
                hashMap.put(refAddr.getType(), refAddr);
            }
            Class<?> clazz = Class.forName(reference.getClassName());
            Set set = null;
            BinaryRefAddr binaryRefAddr = (BinaryRefAddr) hashMap.remove("com.mchange.v2.naming.JavaBeanReferenceMaker.REF_PROPS_KEY");
            if (binaryRefAddr != null)
                set = (Set) SerializableUtils.fromByteArray((byte[]) binaryRefAddr.getContent());
            Map map = createPropertyMap(clazz, hashMap);
            return findBean(clazz, map, set);
        }

        return null;
    }

    private Map createPropertyMap(Class<?> paramClass, Map paramMap) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(paramClass);
        PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors();

        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        byte b;
        int i;
        for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {

            PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
            String str = propertyDescriptor.getName();
            Class<?> clazz = propertyDescriptor.getPropertyType();
            Object object = paramMap.remove(str);
            if (object != null) {
                if (object instanceof StringRefAddr) {

                    String str1 = (String) ((StringRefAddr) object).getContent();
                    if (Coerce.canCoerce(clazz)) {
                        hashMap.put(str, Coerce.toObject(str1, clazz));
                    } else {

                        PropertyEditor propertyEditor = BeansUtils.findPropertyEditor(propertyDescriptor);
                        propertyEditor.setAsText(str1);
                        hashMap.put(str, propertyEditor.getValue());
                    }

                } else if (object instanceof BinaryRefAddr) {

                    byte[] arrayOfByte = (byte[]) ((BinaryRefAddr) object).getContent();
                    if (arrayOfByte.length == 0) {
                        hashMap.put(str, NULL_TOKEN);
                    } else {
                        hashMap.put(str, SerializableUtils.fromByteArray(arrayOfByte));
                    }

                } else if (logger.isLoggable(MLevel.WARNING)) {
                    logger.warning(getClass().getName() + " -- unknown RefAddr subclass: " + object.getClass().getName());
                }
            }
        }
        for (String str : paramMap.keySet()) {

            if (logger.isLoggable(MLevel.WARNING))
                logger.warning(getClass().getName() + " -- RefAddr for unknown property: " + str);
        }
        return hashMap;
    }

    protected Object createBlankInstance(Class paramClass) throws Exception {
        return paramClass.newInstance();
    }

    protected Object findBean(Class paramClass, Map paramMap, Set paramSet) throws Exception {
        Object object = createBlankInstance(paramClass);
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors();
        byte b;
        int i;
        for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {

            PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
            String str = propertyDescriptor.getName();
            Object object1 = paramMap.get(str);
            Method method = propertyDescriptor.getWriteMethod();
            if (object1 != null) {

                if (method != null) {
                    method.invoke(object, new Object[]{(object1 == NULL_TOKEN) ? null : object1});

                } else if (logger.isLoggable(MLevel.WARNING)) {
                    logger.warning(getClass().getName() + ": Could not restore read-only property '" + str + "'.");

                }

            } else if (method != null) {

                if (paramSet == null || paramSet.contains(str)) {

                    if (logger.isLoggable(MLevel.WARNING)) {
                        logger.warning(getClass().getName() + " -- Expected writable property ''" + str + "'' left at default value");
                    }
                }
            }
        }

        return object;
    }
}

