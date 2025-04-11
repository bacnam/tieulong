package ch.qos.logback.classic.gaffer;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.transform.ImmutableASTTransformation;

import java.util.LinkedHashMap;

public enum NestingType implements GroovyObject {
    NA, SINGLE, AS_COLLECTION;

    public static final NestingType MIN_VALUE;

    public static final NestingType MAX_VALUE;

    static {
        Object object1 = $getCallSiteArray()[13].callStatic(NestingType.class, "NA", Integer.valueOf(0));
        NA = (NestingType) ShortTypeHandling.castToEnum(object1, NestingType.class);
        Object object2 = $getCallSiteArray()[14].callStatic(NestingType.class, "SINGLE", Integer.valueOf(1));
        SINGLE = (NestingType) ShortTypeHandling.castToEnum(object2, NestingType.class);
        Object object3 = $getCallSiteArray()[15].callStatic(NestingType.class, "AS_COLLECTION", Integer.valueOf(2));
        AS_COLLECTION = (NestingType) ShortTypeHandling.castToEnum(object3, NestingType.class);
        NestingType nestingType1 = NA;
        MIN_VALUE = nestingType1;
        NestingType nestingType2 = AS_COLLECTION;
        MAX_VALUE = nestingType2;
        NestingType[] arrayOfNestingType = {NA, SINGLE, AS_COLLECTION};
        $VALUES = arrayOfNestingType;
    }

    NestingType(LinkedHashMap __namedArgs) {
        MetaClass metaClass = $getStaticMetaClass();
        this.metaClass = metaClass;
        if (ScriptBytecodeAdapter.compareEqual(__namedArgs, null))
            throw (Throwable) arrayOfCallSite[0].callConstructor(IllegalArgumentException.class, "One of the enum constants for enum ch.qos.logback.classic.gaffer.NestingType was initialized with null. Please use a non-null value or define your own constructor.");
        arrayOfCallSite[1].callStatic(ImmutableASTTransformation.class, this, __namedArgs);
    }
}

