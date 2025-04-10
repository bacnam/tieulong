package jsc.tests;

import jsc.distributions.Tail;

public class H1
{
private final String name;

private H1(String paramString) {
this.name = paramString;
} public String toString() {
return this.name;
}

public static final H1 GREATER_THAN = new H1("greater than");

public static final H1 LESS_THAN = new H1("less than");

public static final H1 NOT_EQUAL = new H1("not equal to");

public static Tail toTail(H1 paramH1) {
if (paramH1 == NOT_EQUAL) return Tail.TWO; 
if (paramH1 == GREATER_THAN) return Tail.UPPER; 
return Tail.LOWER;
}
}

