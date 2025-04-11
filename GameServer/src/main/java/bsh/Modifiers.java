package bsh;

import java.io.Serializable;
import java.util.Hashtable;

public class Modifiers
        implements Serializable {
    public static final int CLASS = 0;
    public static final int METHOD = 1;
    public static final int FIELD = 2;
    Hashtable modifiers;

    public void addModifier(int context, String name) {
        if (this.modifiers == null) {
            this.modifiers = new Hashtable<Object, Object>();
        }
        Object existing = this.modifiers.put(name, void.class);
        if (existing != null) {
            throw new IllegalStateException("Duplicate modifier: " + name);
        }

        int count = 0;
        if (hasModifier("private"))
            count++;
        if (hasModifier("protected"))
            count++;
        if (hasModifier("public"))
            count++;
        if (count > 1) {
            throw new IllegalStateException("public/private/protected cannot be used in combination.");
        }

        switch (context) {

            case 0:
                validateForClass();
                break;
            case 1:
                validateForMethod();
                break;
            case 2:
                validateForField();
                break;
        }
    }

    public boolean hasModifier(String name) {
        if (this.modifiers == null)
            this.modifiers = new Hashtable<Object, Object>();
        return (this.modifiers.get(name) != null);
    }

    private void validateForMethod() {
        insureNo("volatile", "Method");
        insureNo("transient", "Method");
    }

    private void validateForField() {
        insureNo("synchronized", "Variable");
        insureNo("native", "Variable");
        insureNo("abstract", "Variable");
    }

    private void validateForClass() {
        validateForMethod();
        insureNo("native", "Class");
        insureNo("synchronized", "Class");
    }

    private void insureNo(String modifier, String context) {
        if (hasModifier(modifier)) {
            throw new IllegalStateException(context + " cannot be declared '" + modifier + "'");
        }
    }

    public String toString() {
        return "Modifiers: " + this.modifiers;
    }
}
