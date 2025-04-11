package com.mchange.v1.util;

import java.util.Map;

public final class MapUtils {
    public static boolean equivalentDisregardingSort(Map paramMap1, Map paramMap2) {
        if (paramMap1.size() != paramMap2.size()) {
            return false;
        }
        for (Object object : paramMap1.keySet()) {

            if (!paramMap1.get(object).equals(paramMap2.get(object)))
                return false;
        }
        return true;
    }

    public static int hashContentsDisregardingSort(Map paramMap) {
        int i = 0;
        for (Object object : paramMap.keySet()) {

            Object object1 = paramMap.get(object);
            i ^= object.hashCode() ^ object1.hashCode();
        }
        return i;
    }
}

