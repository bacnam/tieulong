package ch.qos.logback.core.joran.util;

import ch.qos.logback.core.joran.spi.DefaultClass;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.AggregationType;
import ch.qos.logback.core.util.PropertySetterException;

import java.beans.*;
import java.lang.reflect.Method;

public class PropertySetter
        extends ContextAwareBase {
    protected Object obj;
    protected Class<?> objClass;
    protected PropertyDescriptor[] propertyDescriptors;
    protected MethodDescriptor[] methodDescriptors;

    public PropertySetter(Object obj) {
        this.obj = obj;
        this.objClass = obj.getClass();
    }

    protected void introspect() {
        try {
            BeanInfo bi = Introspector.getBeanInfo(this.obj.getClass());
            this.propertyDescriptors = bi.getPropertyDescriptors();
            this.methodDescriptors = bi.getMethodDescriptors();
        } catch (IntrospectionException ex) {
            addError("Failed to introspect " + this.obj + ": " + ex.getMessage());
            this.propertyDescriptors = new PropertyDescriptor[0];
            this.methodDescriptors = new MethodDescriptor[0];
        }
    }

    public void setProperty(String name, String value) {
        if (value == null) {
            return;
        }

        name = Introspector.decapitalize(name);

        PropertyDescriptor prop = getPropertyDescriptor(name);

        if (prop == null) {
            addWarn("No such property [" + name + "] in " + this.objClass.getName() + ".");
        } else {
            try {
                setProperty(prop, name, value);
            } catch (PropertySetterException ex) {
                addWarn("Failed to set property [" + name + "] to value \"" + value + "\". ", (Throwable) ex);
            }
        }
    }

    public void setProperty(PropertyDescriptor prop, String name, String value) throws PropertySetterException {
        Object arg;
        Method setter = prop.getWriteMethod();

        if (setter == null) {
            throw new PropertySetterException("No setter for property [" + name + "].");
        }

        Class<?>[] paramTypes = setter.getParameterTypes();

        if (paramTypes.length != 1) {
            throw new PropertySetterException("#params for setter != 1");
        }

        try {
            arg = StringToObjectConverter.convertArg((ContextAware) this, value, paramTypes[0]);
        } catch (Throwable t) {
            throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed. ", t);
        }

        if (arg == null) {
            throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed.");
        }

        try {
            setter.invoke(this.obj, new Object[]{arg});
        } catch (Exception ex) {
            throw new PropertySetterException(ex);
        }
    }

    public AggregationType computeAggregationType(String name) {
        String cName = capitalizeFirstLetter(name);

        Method addMethod = findAdderMethod(cName);

        if (addMethod != null) {
            AggregationType type = computeRawAggregationType(addMethod);
            switch (type) {
                case NOT_FOUND:
                    return AggregationType.NOT_FOUND;
                case AS_BASIC_PROPERTY:
                    return AggregationType.AS_BASIC_PROPERTY_COLLECTION;
                case AS_COMPLEX_PROPERTY:
                    return AggregationType.AS_COMPLEX_PROPERTY_COLLECTION;
            }

        }
        Method setterMethod = findSetterMethod(name);
        if (setterMethod != null) {
            return computeRawAggregationType(setterMethod);
        }

        return AggregationType.NOT_FOUND;
    }

    private Method findAdderMethod(String name) {
        name = capitalizeFirstLetter(name);
        return getMethod("add" + name);
    }

    private Method findSetterMethod(String name) {
        String dName = Introspector.decapitalize(name);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(dName);
        if (propertyDescriptor != null) {
            return propertyDescriptor.getWriteMethod();
        }
        return null;
    }

    private Class<?> getParameterClassForMethod(Method method) {
        if (method == null) {
            return null;
        }
        Class<?>[] classArray = method.getParameterTypes();
        if (classArray.length != 1) {
            return null;
        }
        return classArray[0];
    }

    private AggregationType computeRawAggregationType(Method method) {
        Class<?> parameterClass = getParameterClassForMethod(method);
        if (parameterClass == null) {
            return AggregationType.NOT_FOUND;
        }
        if (StringToObjectConverter.canBeBuiltFromSimpleString(parameterClass)) {
            return AggregationType.AS_BASIC_PROPERTY;
        }
        return AggregationType.AS_COMPLEX_PROPERTY;
    }

    private boolean isUnequivocallyInstantiable(Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }

        try {
            Object o = clazz.newInstance();
            if (o != null) {
                return true;
            }
            return false;
        } catch (InstantiationException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public Class<?> getObjClass() {
        return this.objClass;
    }

    public void addComplexProperty(String name, Object complexProperty) {
        Method adderMethod = findAdderMethod(name);

        if (adderMethod != null) {
            Class<?>[] paramTypes = adderMethod.getParameterTypes();
            if (!isSanityCheckSuccessful(name, adderMethod, paramTypes, complexProperty)) {
                return;
            }

            invokeMethodWithSingleParameterOnThisObject(adderMethod, complexProperty);
        } else {
            addError("Could not find method [add" + name + "] in class [" + this.objClass.getName() + "].");
        }
    }

    void invokeMethodWithSingleParameterOnThisObject(Method method, Object parameter) {
        Class<?> ccc = parameter.getClass();
        try {
            method.invoke(this.obj, new Object[]{parameter});
        } catch (Exception e) {
            addError("Could not invoke method " + method.getName() + " in class " + this.obj.getClass().getName() + " with parameter of type " + ccc.getName(), e);
        }
    }

    public void addBasicProperty(String name, String strValue) {
        Object arg;
        if (strValue == null) {
            return;
        }

        name = capitalizeFirstLetter(name);
        Method adderMethod = findAdderMethod(name);

        if (adderMethod == null) {
            addError("No adder for property [" + name + "].");

            return;
        }
        Class<?>[] paramTypes = adderMethod.getParameterTypes();
        isSanityCheckSuccessful(name, adderMethod, paramTypes, strValue);

        try {
            arg = StringToObjectConverter.convertArg((ContextAware) this, strValue, paramTypes[0]);
        } catch (Throwable t) {
            addError("Conversion to type [" + paramTypes[0] + "] failed. ", t);
            return;
        }
        if (arg != null) {
            invokeMethodWithSingleParameterOnThisObject(adderMethod, strValue);
        }
    }

    public void setComplexProperty(String name, Object complexProperty) {
        String dName = Introspector.decapitalize(name);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(dName);

        if (propertyDescriptor == null) {
            addWarn("Could not find PropertyDescriptor for [" + name + "] in " + this.objClass.getName());

            return;
        }

        Method setter = propertyDescriptor.getWriteMethod();

        if (setter == null) {
            addWarn("Not setter method for property [" + name + "] in " + this.obj.getClass().getName());

            return;
        }

        Class<?>[] paramTypes = setter.getParameterTypes();

        if (!isSanityCheckSuccessful(name, setter, paramTypes, complexProperty)) {
            return;
        }
        try {
            invokeMethodWithSingleParameterOnThisObject(setter, complexProperty);
        } catch (Exception e) {
            addError("Could not set component " + this.obj + " for parent component " + this.obj, e);
        }
    }

    private boolean isSanityCheckSuccessful(String name, Method method, Class<?>[] params, Object complexProperty) {
        Class<?> ccc = complexProperty.getClass();
        if (params.length != 1) {
            addError("Wrong number of parameters in setter method for property [" + name + "] in " + this.obj.getClass().getName());

            return false;
        }

        if (!params[0].isAssignableFrom(complexProperty.getClass())) {
            addError("A \"" + ccc.getName() + "\" object is not assignable to a \"" + params[0].getName() + "\" variable.");

            addError("The class \"" + params[0].getName() + "\" was loaded by ");
            addError("[" + params[0].getClassLoader() + "] whereas object of type ");
            addError("\"" + ccc.getName() + "\" was loaded by [" + ccc.getClassLoader() + "].");

            return false;
        }

        return true;
    }

    private String capitalizeFirstLetter(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected Method getMethod(String methodName) {
        if (this.methodDescriptors == null) {
            introspect();
        }

        for (int i = 0; i < this.methodDescriptors.length; i++) {
            if (methodName.equals(this.methodDescriptors[i].getName())) {
                return this.methodDescriptors[i].getMethod();
            }
        }

        return null;
    }

    protected PropertyDescriptor getPropertyDescriptor(String name) {
        if (this.propertyDescriptors == null) {
            introspect();
        }

        for (int i = 0; i < this.propertyDescriptors.length; i++) {

            if (name.equals(this.propertyDescriptors[i].getName())) {
                return this.propertyDescriptors[i];
            }
        }

        return null;
    }

    public Object getObj() {
        return this.obj;
    }

    Method getRelevantMethod(String name, AggregationType aggregationType) {
        Method relevantMethod;
        String cName = capitalizeFirstLetter(name);

        if (aggregationType == AggregationType.AS_COMPLEX_PROPERTY_COLLECTION) {
            relevantMethod = findAdderMethod(cName);
        } else if (aggregationType == AggregationType.AS_COMPLEX_PROPERTY) {
            relevantMethod = findSetterMethod(cName);
        } else {
            throw new IllegalStateException(aggregationType + " not allowed here");
        }
        return relevantMethod;
    }

    <T extends java.lang.annotation.Annotation> T getAnnotation(String name, Class<T> annonationClass, Method relevantMethod) {
        if (relevantMethod != null) {
            return relevantMethod.getAnnotation(annonationClass);
        }
        return null;
    }

    Class<?> getDefaultClassNameByAnnonation(String name, Method relevantMethod) {
        DefaultClass defaultClassAnnon = getAnnotation(name, DefaultClass.class, relevantMethod);

        if (defaultClassAnnon != null) {
            return defaultClassAnnon.value();
        }
        return null;
    }

    Class<?> getByConcreteType(String name, Method relevantMethod) {
        Class<?> paramType = getParameterClassForMethod(relevantMethod);
        if (paramType == null) {
            return null;
        }

        boolean isUnequivocallyInstantiable = isUnequivocallyInstantiable(paramType);
        if (isUnequivocallyInstantiable) {
            return paramType;
        }
        return null;
    }

    public Class<?> getClassNameViaImplicitRules(String name, AggregationType aggregationType, DefaultNestedComponentRegistry registry) {
        Class<?> registryResult = registry.findDefaultComponentType(this.obj.getClass(), name);

        if (registryResult != null) {
            return registryResult;
        }

        Method relevantMethod = getRelevantMethod(name, aggregationType);
        if (relevantMethod == null) {
            return null;
        }
        Class<?> byAnnotation = getDefaultClassNameByAnnonation(name, relevantMethod);
        if (byAnnotation != null) {
            return byAnnotation;
        }
        return getByConcreteType(name, relevantMethod);
    }
}

