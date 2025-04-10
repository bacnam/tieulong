package org.junit.experimental.theories;

public abstract class PotentialAssignment
{
public static class CouldNotGenerateValueException
extends Exception
{
private static final long serialVersionUID = 1L;

public CouldNotGenerateValueException() {}

public CouldNotGenerateValueException(Throwable e) {
super(e);
}
}

public static PotentialAssignment forValue(final String name, final Object value) {
return new PotentialAssignment()
{
public Object getValue() {
return value;
}

public String toString() {
return String.format("[%s]", new Object[] { this.val$value });
}

public String getDescription() {
String str;
if (value == null) {
str = "null";
} else {
try {
str = String.format("\"%s\"", new Object[] { this.val$value });
} catch (Throwable e) {
str = String.format("[toString() threw %s: %s]", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
} 
} 

return String.format("%s <from %s>", new Object[] { str, this.val$name });
}
};
}

public abstract Object getValue() throws CouldNotGenerateValueException;

public abstract String getDescription() throws CouldNotGenerateValueException;
}

