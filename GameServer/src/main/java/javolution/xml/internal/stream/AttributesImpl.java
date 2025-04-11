package javolution.xml.internal.stream;

import javolution.text.CharArray;
import javolution.util.FastTable;
import javolution.xml.sax.Attributes;

public final class AttributesImpl
        implements Attributes {
    private static final CharArray CDATA = new CharArray("CDATA");
    private static final CharArray EMPTY = new CharArray();
    private final FastTable<AttributeImpl> attributes = new FastTable();
    private final NamespacesImpl namespaces;
    private int length;

    public AttributesImpl(NamespacesImpl namespaces) {
        this.namespaces = namespaces;
    }

    public void addAttribute(CharArray localName, CharArray prefix, CharArray qName, CharArray value) {
        AttributeImpl attribute;
        if (this.length >= this.attributes.size()) {
            attribute = new AttributeImpl();
            this.attributes.add(attribute);
        } else {
            attribute = (AttributeImpl) this.attributes.get(this.length);
        }
        attribute.localName = localName;
        attribute.prefix = prefix;
        attribute.qName = qName;
        attribute.value = value;
        this.length++;
    }

    public int getIndex(CharSequence qName) {
        for (int i = 0; i < this.length; i++) {
            if (qName.equals(((AttributeImpl) this.attributes.get(i)).qName))
                return i;
        }
        return -1;
    }

    public int getIndex(CharSequence uri, CharSequence localName) {
        for (int i = 0; i < this.length; i++) {
            AttributeImpl attribute = (AttributeImpl) this.attributes.get(i);
            if (localName.equals(attribute.localName)) {
                if (attribute.prefix == null) {
                    if (uri.length() == 0) {
                        return i;
                    }
                } else if (uri.equals(this.namespaces.getNamespaceURI((CharSequence) attribute.prefix))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getLength() {
        return this.length;
    }

    public CharArray getLocalName(int index) {
        if (index < 0 || index >= this.length) return null;
        return ((AttributeImpl) this.attributes.get(index)).localName;
    }

    public CharArray getPrefix(int index) {
        if (index < 0 || index >= this.length) return null;
        return ((AttributeImpl) this.attributes.get(index)).prefix;
    }

    public CharArray getQName(int index) {
        if (index < 0 || index >= this.length) return null;
        return ((AttributeImpl) this.attributes.get(index)).qName;
    }

    public CharArray getType(CharSequence qName) {
        return (getIndex(qName) >= 0) ? CDATA : null;
    }

    public CharArray getType(CharSequence uri, CharSequence localName) {
        return (getIndex(uri, localName) >= 0) ? CDATA : null;
    }

    public CharArray getType(int index) {
        if (index < 0 || index >= this.length) return null;
        return CDATA;
    }

    public CharArray getURI(int index) {
        if (index < 0 || index >= this.length) return null;
        CharArray prefix = ((AttributeImpl) this.attributes.get(index)).prefix;
        return (prefix == null) ? EMPTY : this.namespaces.getNamespaceURI((CharSequence) prefix);
    }

    public CharArray getValue(CharSequence qName) {
        int index = getIndex(qName);
        return (index >= 0) ? ((AttributeImpl) this.attributes.get(index)).value : null;
    }

    public CharArray getValue(CharSequence uri, CharSequence localName) {
        int index = getIndex(uri, localName);
        return (index >= 0) ? ((AttributeImpl) this.attributes.get(index)).value : null;
    }

    public CharArray getValue(int index) {
        if (index < 0 || index >= this.length) return null;
        return ((AttributeImpl) this.attributes.get(index)).value;
    }

    public void reset() {
        this.length = 0;
    }

    private static class AttributeImpl {
        CharArray localName;
        CharArray prefix;
        CharArray qName;
        CharArray value;

        private AttributeImpl() {
        }

        public String toString() {
            return this.qName + "=" + this.value;
        }
    }
}

