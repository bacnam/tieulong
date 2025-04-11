package javolution.xml;

import javolution.util.FastMap;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamReader;
import javolution.xml.stream.XMLStreamWriter;

public class XMLBinding
        implements XMLSerializable {
    static final XMLBinding DEFAULT = new XMLBinding();
    private static final long serialVersionUID = 6611041662550083919L;
    private final FastMap<Class<?>, QName> _classToAlias = new FastMap();

    private final FastMap<QName, Class<?>> _aliasToClass = new FastMap();
    private QName _classAttribute = QName.valueOf("class");

    public void setAlias(Class<?> cls, QName qName) {
        this._classToAlias.put(cls, qName);
        this._aliasToClass.put(qName, cls);
    }

    public final void setAlias(Class<?> cls, String alias) {
        setAlias(cls, QName.valueOf(alias));
    }

    public void setClassAttribute(QName classAttribute) {
        this._classAttribute = classAttribute;
    }

    public final void setClassAttribute(String name) {
        setClassAttribute((name == null) ? null : QName.valueOf(name));
    }

    protected XMLFormat<?> getFormat(Class<?> forClass) throws XMLStreamException {
        return XMLContext.getFormat(forClass);
    }

    protected Class<?> readClass(XMLStreamReader reader, boolean useAttributes) throws XMLStreamException {
        try {
            QName classQName;
            if (useAttributes) {
                if (this._classAttribute == null) {
                    throw new XMLStreamException("Binding has no class attribute defined, cannot retrieve class");
                }
                classQName = QName.valueOf((CharSequence) reader.getAttributeValue(this._classAttribute.getNamespaceURI(), this._classAttribute.getLocalName()));

                if (classQName == null) {
                    throw new XMLStreamException("Cannot retrieve class (class attribute not found)");
                }
            } else {
                classQName = QName.valueOf((CharSequence) reader.getNamespaceURI(), (CharSequence) reader.getLocalName());
            }

            Class<?> cls = (Class) this._aliasToClass.get(classQName);
            if (cls != null) {
                return cls;
            }

            cls = (Class) this._aliasToClass.get(QName.valueOf(classQName.getLocalName()));
            if (cls != null) {
                return cls;
            }

            cls = Class.forName(classQName.getLocalName().toString());
            if (cls == null) {
                throw new XMLStreamException("Class " + classQName.getLocalName() + " not found (see javolution.lang.Reflection to support additional class loader)");
            }

            this._aliasToClass.put(classQName, cls);
            return cls;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void writeClass(Class<?> cls, XMLStreamWriter writer, boolean useAttributes) throws XMLStreamException {
        QName qName = (QName) this._classToAlias.get(cls);
        String name = (qName != null) ? qName.toString() : cls.getName();
        if (useAttributes) {
            if (this._classAttribute == null)
                return;
            if (this._classAttribute.getNamespaceURI() == null) {
                writer.writeAttribute(this._classAttribute.getLocalName(), name);
            } else {
                writer.writeAttribute(this._classAttribute.getNamespaceURI(), this._classAttribute.getLocalName(), name);
            }

        } else if (qName != null) {
            if (qName.getNamespaceURI() == null) {
                writer.writeStartElement(qName.getLocalName());
            } else {
                writer.writeStartElement(qName.getNamespaceURI(), qName.getLocalName());
            }
        } else {

            writer.writeStartElement(name);
        }
    }

    public void reset() {
        this._classAttribute = QName.valueOf("class");
        this._aliasToClass.clear();
        this._classToAlias.clear();
    }
}

