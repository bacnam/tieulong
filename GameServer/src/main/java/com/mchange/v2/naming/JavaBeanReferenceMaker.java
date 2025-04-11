package com.mchange.v2.naming;

import com.mchange.v2.beans.BeansUtils;
import com.mchange.v2.lang.Coerce;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.ser.IndirectPolicy;
import com.mchange.v2.ser.SerializableUtils;

import javax.naming.BinaryRefAddr;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.*;

public class JavaBeanReferenceMaker
        implements ReferenceMaker {
    static final String REF_PROPS_KEY = "com.mchange.v2.naming.JavaBeanReferenceMaker.REF_PROPS_KEY";
    static final Object[] EMPTY_ARGS = new Object[0];
    static final byte[] NULL_TOKEN_BYTES = new byte[0];
    private static final MLogger logger = MLog.getLogger(JavaBeanReferenceMaker.class);
    String factoryClassName = "com.mchange.v2.naming.JavaBeanObjectFactory";
    String defaultFactoryClassLocation = null;

    Set referenceProperties = new HashSet();

    ReferenceIndirector indirector = new ReferenceIndirector();

    public Hashtable getEnvironmentProperties() {
        return this.indirector.getEnvironmentProperties();
    }

    public void setEnvironmentProperties(Hashtable paramHashtable) {
        this.indirector.setEnvironmentProperties(paramHashtable);
    }

    public String getFactoryClassName() {
        return this.factoryClassName;
    }

    public void setFactoryClassName(String paramString) {
        this.factoryClassName = paramString;
    }

    public String getDefaultFactoryClassLocation() {
        return this.defaultFactoryClassLocation;
    }

    public void setDefaultFactoryClassLocation(String paramString) {
        this.defaultFactoryClassLocation = paramString;
    }

    public void addReferenceProperty(String paramString) {
        this.referenceProperties.add(paramString);
    }

    public void removeReferenceProperty(String paramString) {
        this.referenceProperties.remove(paramString);
    }

    public Reference createReference(Object paramObject) throws NamingException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass());
            PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors();
            ArrayList<BinaryRefAddr> arrayList = new ArrayList();
            String str = this.defaultFactoryClassLocation;

            boolean bool = (this.referenceProperties.size() > 0) ? true : false;

            if (bool)
                arrayList.add(new BinaryRefAddr("com.mchange.v2.naming.JavaBeanReferenceMaker.REF_PROPS_KEY", SerializableUtils.toByteArray(this.referenceProperties)));
            byte b;
            int i;
            for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {

                PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
                String str1 = propertyDescriptor.getName();

                if (!bool || this.referenceProperties.contains(str1)) {

                    Class<?> clazz = propertyDescriptor.getPropertyType();
                    Method method1 = propertyDescriptor.getReadMethod();
                    Method method2 = propertyDescriptor.getWriteMethod();
                    if (method1 != null && method2 != null) {

                        Object object = method1.invoke(paramObject, EMPTY_ARGS);

                        if (str1.equals("factoryClassLocation")) {

                            if (String.class != clazz) {
                                throw new NamingException(getClass().getName() + " requires a factoryClassLocation property to be a string, " + clazz.getName() + " is not valid.");
                            }
                            str = (String) object;
                        }

                        if (object == null) {

                            BinaryRefAddr binaryRefAddr = new BinaryRefAddr(str1, NULL_TOKEN_BYTES);
                            arrayList.add(binaryRefAddr);
                        } else if (Coerce.canCoerce(clazz)) {

                            StringRefAddr stringRefAddr = new StringRefAddr(str1, String.valueOf(object));
                            arrayList.add(stringRefAddr);
                        } else {
                            BinaryRefAddr binaryRefAddr;

                            StringRefAddr stringRefAddr = null;
                            PropertyEditor propertyEditor = BeansUtils.findPropertyEditor(propertyDescriptor);
                            if (propertyEditor != null) {

                                propertyEditor.setValue(object);
                                String str2 = propertyEditor.getAsText();
                                if (str2 != null)
                                    stringRefAddr = new StringRefAddr(str1, str2);
                            }
                            if (stringRefAddr == null) {
                                binaryRefAddr = new BinaryRefAddr(str1, SerializableUtils.toByteArray(object, this.indirector, IndirectPolicy.INDIRECT_ON_EXCEPTION));
                            }

                            arrayList.add(binaryRefAddr);

                        }

                    } else if (logger.isLoggable(MLevel.WARNING)) {
                        logger.warning(getClass().getName() + ": Skipping " + str1 + " because it is " + ((method2 == null) ? "read-only." : "write-only."));
                    }
                }
            }

            Reference reference = new Reference(paramObject.getClass().getName(), this.factoryClassName, str);
            for (Iterator<BinaryRefAddr> iterator = arrayList.iterator(); iterator.hasNext(); )
                reference.add(iterator.next());
            return reference;
        } catch (Exception exception) {

            if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, "Exception trying to create Reference.", exception);
            }
            throw new NamingException("Could not create reference from bean: " + exception.toString());
        }
    }
}

