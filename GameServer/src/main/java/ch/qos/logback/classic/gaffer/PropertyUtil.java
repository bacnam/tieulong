package ch.qos.logback.classic.gaffer;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;

import java.beans.Introspector;
import java.lang.ref.SoftReference;

import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;

public class PropertyUtil implements GroovyObject {
    public PropertyUtil() {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        MetaClass metaClass = $getStaticMetaClass();
        this.metaClass = metaClass;
    }

    public static boolean hasAdderMethod(Object obj, String name) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        String addMethod = null;
        if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            GStringImpl gStringImpl1 = new GStringImpl(new Object[]{upperCaseFirstLetter(name)}, new String[]{"add", ""});
            addMethod = ShortTypeHandling.castToString(gStringImpl1);
            return DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(arrayOfCallSite[2].callGetProperty(obj), obj, addMethod));
        }
        GStringImpl gStringImpl = new GStringImpl(new Object[]{arrayOfCallSite[0].callStatic(PropertyUtil.class, name)});
        //new String[]{"add", ""});
        addMethod = ShortTypeHandling.castToString(gStringImpl);
        return DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(arrayOfCallSite[2].callGetProperty(obj), obj, addMethod));
    }

    public static NestingType nestingType(Object obj, String name) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Object decapitalizedName = arrayOfCallSite[3].call(Introspector.class, name);
        if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[4].call(obj, decapitalizedName))) {
            return (NestingType) ShortTypeHandling.castToEnum(arrayOfCallSite[5].callGetProperty(NestingType.class), NestingType.class);
        }
        if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[6].callStatic(PropertyUtil.class, obj, name))) {
            return (NestingType) ShortTypeHandling.castToEnum(arrayOfCallSite[7].callGetProperty(NestingType.class), NestingType.class);
        }
        return (NestingType) ShortTypeHandling.castToEnum(arrayOfCallSite[8].callGetProperty(NestingType.class), NestingType.class);
    }

    public static void attach(NestingType nestingType, Object component, Object subComponent, String name) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        NestingType nestingType1 = nestingType;
        if (ScriptBytecodeAdapter.isCase(nestingType1, arrayOfCallSite[9].callGetProperty(NestingType.class))) {
            Object object1 = arrayOfCallSite[10].call(Introspector.class, name);
            name = ShortTypeHandling.castToString(object1);
            Object object2 = subComponent;
            ScriptBytecodeAdapter.setProperty(object2, null, component, ShortTypeHandling.castToString(new GStringImpl(new Object[]{name}, new String[]{"", ""})));
        } else if (ScriptBytecodeAdapter.isCase(nestingType1, arrayOfCallSite[11].callGetProperty(NestingType.class))) {
            String firstUpperName = ShortTypeHandling.castToString(arrayOfCallSite[12].call(PropertyUtil.class, name));
            ScriptBytecodeAdapter.invokeMethodN(PropertyUtil.class, component, ShortTypeHandling.castToString(new GStringImpl(new Object[]{firstUpperName}, new String[]{"add", ""})), new Object[]{subComponent});
        }
    }

    public static String transformFirstLetter(String s, Closure closure) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        if (BytecodeInterface8.isOrigInt() && BytecodeInterface8.isOrigZ() && !__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if ((ScriptBytecodeAdapter.compareEqual(s, null) || ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[14].call(s), Integer.valueOf(0))))
                return s;
        } else if ((ScriptBytecodeAdapter.compareEqual(s, null) || ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[13].call(s), Integer.valueOf(0)))) {
            return s;
        }

        String firstLetter = ShortTypeHandling.castToString(arrayOfCallSite[15].callConstructor(String.class, arrayOfCallSite[16].call(s, Integer.valueOf(0))));

        String modifiedFistLetter = ShortTypeHandling.castToString(arrayOfCallSite[17].call(closure, firstLetter));

        if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[18].call(s), Integer.valueOf(1))) {
            return modifiedFistLetter;
        }
        return ShortTypeHandling.castToString(arrayOfCallSite[19].call(modifiedFistLetter, arrayOfCallSite[20].call(s, Integer.valueOf(1))));
    }

    public static String upperCaseFirstLetter(String s) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return ShortTypeHandling.castToString(arrayOfCallSite[21].callStatic(PropertyUtil.class, s, new _upperCaseFirstLetter_closure1(PropertyUtil.class, PropertyUtil.class)));
    }

    class _upperCaseFirstLetter_closure1 extends Closure implements GeneratedClosure {
        public Object doCall(String it) {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            return arrayOfCallSite[0].call(it);
        }

        public _upperCaseFirstLetter_closure1(Object _outerInstance, Object _thisObject) {
            super(_outerInstance, _thisObject);
        }

        public Object call(String it) {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            return (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) ? doCall(it) : arrayOfCallSite[1].callCurrent((GroovyObject) this, it);
        }
    }

}

