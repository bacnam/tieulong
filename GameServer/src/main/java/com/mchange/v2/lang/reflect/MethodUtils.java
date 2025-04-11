package com.mchange.v2.lang.reflect;

import java.lang.reflect.Method;
import java.util.Comparator;

public final class MethodUtils {
    public static final Comparator METHOD_COMPARATOR = new Comparator() {
        public int compare(Object param1Object1, Object param1Object2) {
            Method method1 = (Method) param1Object1;
            Method method2 = (Method) param1Object2;
            String str1 = method1.getName();
            String str2 = method2.getName();
            int i = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (i == 0) {
                if (str1.equals(str2)) {

                    Class[] arrayOfClass1 = method1.getParameterTypes();
                    Class[] arrayOfClass2 = method2.getParameterTypes();
                    if (arrayOfClass1.length < arrayOfClass2.length) {
                        i = -1;
                    } else if (arrayOfClass1.length > arrayOfClass2.length) {
                        i = 1;
                    } else {
                        byte b;
                        int j;
                        for (b = 0, j = arrayOfClass1.length; b < j; b++) {

                            String str3 = arrayOfClass1[b].getName();
                            String str4 = arrayOfClass2[b].getName();
                            i = str3.compareTo(str4);
                            if (i != 0) {
                                break;
                            }
                        }
                    }
                } else {

                    i = str1.compareTo(str2);
                }
            }
            return i;
        }
    };
}

