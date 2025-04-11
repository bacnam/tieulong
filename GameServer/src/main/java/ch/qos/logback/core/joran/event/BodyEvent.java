package ch.qos.logback.core.joran.event;

import org.xml.sax.Locator;

public class BodyEvent
        extends SaxEvent {
    private String text;

    BodyEvent(String text, Locator locator) {
        super(null, null, null, locator);
        this.text = text;
    }

    public String getText() {
        if (this.text != null) {
            return this.text.trim();
        }
        return this.text;
    }

    public String toString() {
        return "BodyEvent(" + getText() + ")" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber();
    }

    public void append(String str) {
        this.text += str;
    }
}

