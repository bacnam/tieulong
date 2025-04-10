package com.mchange.v2.util;

import java.util.Comparator;

public final class ComparatorUtils
{
public static Comparator reverse(final Comparator c) {
return new Comparator()
{
public int compare(Object param1Object1, Object param1Object2) {
return -c.compare((T)param1Object1, (T)param1Object2);
}
};
}
}

