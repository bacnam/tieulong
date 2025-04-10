package jsc.distributions;

public class Tail
{
private final String name;

private Tail(String paramString) {
this.name = paramString;
} public String toString() {
return this.name;
}

public static final Tail LOWER = new Tail("lower tail");

public static final Tail UPPER = new Tail("upper tail");

public static final Tail TWO = new Tail("two-tail");
}

