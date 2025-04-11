package bsh;

public class ClassIdentifier {
    Class clas;

    public ClassIdentifier(Class clas) {
        this.clas = clas;
    }

    public Class getTargetClass() {
        return this.clas;
    }

    public String toString() {
        return "Class Identifier: " + this.clas.getName();
    }
}

