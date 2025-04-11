package javolution.xml.stream;

public class XMLStreamException
        extends Exception {
    private static final long serialVersionUID = 1L;
    private Throwable _nested;
    private Location _location;

    public XMLStreamException() {
    }

    public XMLStreamException(String msg) {
        super(msg);
    }

    public XMLStreamException(Throwable nested) {
        this._nested = nested;
    }

    public XMLStreamException(String msg, Throwable nested) {
        super(msg);
        this._nested = nested;
    }

    public XMLStreamException(String msg, Location location, Throwable nested) {
        super(msg);
        this._nested = nested;
        this._location = location;
    }

    public XMLStreamException(String msg, Location location) {
        super(msg);
        this._location = location;
    }

    public Throwable getNestedException() {
        return this._nested;
    }

    public Location getLocation() {
        return this._location;
    }

    public String toString() {
        String msg = super.toString();
        if (this._location != null) {
            msg = msg + " (at line " + this._location.getLineNumber() + ", column " + this._location.getColumnNumber() + ")";
        }

        if (this._nested != null) {
            msg = msg + " caused by " + this._nested.toString();
        }
        return msg;
    }
}

