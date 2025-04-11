package com.mchange.v2.management;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import java.util.Comparator;

public class ManagementUtils {
    public static final Comparator PARAM_INFO_COMPARATOR = new Comparator() {
        public int compare(Object param1Object1, Object param1Object2) {
            MBeanParameterInfo mBeanParameterInfo1 = (MBeanParameterInfo) param1Object1;
            MBeanParameterInfo mBeanParameterInfo2 = (MBeanParameterInfo) param1Object2;
            int i = mBeanParameterInfo1.getType().compareTo(mBeanParameterInfo2.getType());
            if (i == 0) {

                i = mBeanParameterInfo1.getName().compareTo(mBeanParameterInfo2.getName());
                if (i == 0) {

                    String str1 = mBeanParameterInfo1.getDescription();
                    String str2 = mBeanParameterInfo2.getDescription();
                    if (str1 == null && str2 == null) {
                        i = 0;
                    } else if (str1 == null) {
                        i = -1;
                    } else if (str2 == null) {
                        i = 1;
                    } else {
                        i = str1.compareTo(str2);
                    }
                }
            }
            return i;
        }
    };

    public static final Comparator OP_INFO_COMPARATOR = new Comparator() {
        public int compare(Object param1Object1, Object param1Object2) {
            MBeanOperationInfo mBeanOperationInfo1 = (MBeanOperationInfo) param1Object1;
            MBeanOperationInfo mBeanOperationInfo2 = (MBeanOperationInfo) param1Object2;
            String str1 = mBeanOperationInfo1.getName();
            String str2 = mBeanOperationInfo2.getName();
            int i = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (i == 0) {
                if (str1.equals(str2)) {

                    MBeanParameterInfo[] arrayOfMBeanParameterInfo1 = mBeanOperationInfo1.getSignature();
                    MBeanParameterInfo[] arrayOfMBeanParameterInfo2 = mBeanOperationInfo2.getSignature();
                    if (arrayOfMBeanParameterInfo1.length < arrayOfMBeanParameterInfo2.length) {
                        i = -1;
                    } else if (arrayOfMBeanParameterInfo1.length > arrayOfMBeanParameterInfo2.length) {
                        i = 1;
                    } else {
                        byte b;
                        int j;
                        for (b = 0, j = arrayOfMBeanParameterInfo1.length; b < j; b++) {

                            i = ManagementUtils.PARAM_INFO_COMPARATOR.compare(arrayOfMBeanParameterInfo1[b], arrayOfMBeanParameterInfo2[b]);
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

