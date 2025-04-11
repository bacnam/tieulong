package javolution.xml;

import javolution.text.CharArray;
import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.util.FastTable;
import javolution.util.Index;
import javolution.util.function.Equalities;
import javolution.xml.stream.XMLStreamException;

public class XMLReferenceResolver {
    private FastMap<Object, Index> _objectToId = new FastMap(Equalities.IDENTITY);

    private FastTable<Object> _idToObject = new FastTable();

    private int _counter;

    private String _idName = "id";

    private String _idURI = null;

    private String _refName = "ref";

    private String _refURI = null;

    private TextBuilder _tmp;

    public XMLReferenceResolver() {
        this._tmp = new TextBuilder();
    }

    public void setIdentifierAttribute(String name) {
        setIdentifierAttribute(name, null);
    }

    public void setIdentifierAttribute(String localName, String uri) {
        this._idName = localName;
        this._idURI = uri;
    }

    public void setReferenceAttribute(String name) {
        setReferenceAttribute(name, null);
    }

    public void setReferenceAttribute(String localName, String uri) {
        this._refName = localName;
        this._refURI = uri;
    }

    public boolean writeReference(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
        Index id = (Index) this._objectToId.get(obj);
        if (id == null) {
            id = Index.valueOf(this._counter++);
            this._objectToId.put(obj, id);
            this._tmp.clear().append(id.intValue());
            if (this._idURI == null) {
                xml.getStreamWriter().writeAttribute(this._idName, (CharSequence) this._tmp);
            } else {
                xml.getStreamWriter().writeAttribute(this._idURI, this._idName, (CharSequence) this._tmp);
            }
            return false;
        }
        this._tmp.clear().append(id.intValue());
        if (this._refURI == null) {
            xml._writer.writeAttribute(this._refName, (CharSequence) this._tmp);
        } else {
            xml._writer.writeAttribute(this._refURI, this._refName, (CharSequence) this._tmp);
        }
        return true;
    }

    public Object readReference(XMLFormat.InputElement xml) throws XMLStreamException {
        CharArray value = xml._reader.getAttributeValue(this._refURI, this._refName);
        if (value == null)
            return null;
        int ref = value.toInt();
        if (ref >= this._idToObject.size())
            throw new XMLStreamException("Reference: " + value + " not found");
        return this._idToObject.get(ref);
    }

    public void createReference(Object obj, XMLFormat.InputElement xml) throws XMLStreamException {
        CharArray value = xml._reader.getAttributeValue(this._idURI, this._idName);
        if (value == null)
            return;
        int i = value.toInt();
        if (this._idToObject.size() != i) {
            throw new XMLStreamException("Identifier discontinuity detected (expected " + this._idToObject.size() + " found " + i + ")");
        }
        this._idToObject.add(obj);
    }

    public void reset() {
        this._idName = "id";
        this._idURI = null;
        this._refName = "ref";
        this._refURI = null;
        this._idToObject.clear();
        this._objectToId.clear();
        this._counter = 0;
    }
}

