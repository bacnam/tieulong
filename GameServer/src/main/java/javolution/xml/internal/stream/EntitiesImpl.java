package javolution.xml.internal.stream;

import javolution.text.CharArray;
import javolution.util.FastTable;
import javolution.util.function.Function;
import javolution.xml.stream.XMLStreamException;

import java.util.Map;

public final class EntitiesImpl {
    private int _maxLength = 1;

    private Map<String, String> _entitiesMapping;

    private CharArray _tmp;

    EntitiesImpl() {
        this._tmp = new CharArray();
    }

    public int getMaxLength() {
        return this._maxLength;
    }

    public int replaceEntity(char[] buffer, int start, int length) throws XMLStreamException {
        if (buffer[start + 1] == '#') {
            char c = buffer[start + 2];
            int base = (c == 'x') ? 16 : 10;
            int j = (c == 'x') ? 3 : 2;
            int charValue = 0;
            for (; j < length - 1; j++) {
                c = buffer[start + j];
                charValue *= base;
                charValue += (c <= '9') ? (c - 48) : ((c <= 'Z') ? (c - 65) : (c - 97));
            }

            buffer[start] = (char) charValue;
            return 1;
        }

        if (buffer[start + 1] == 'l' && buffer[start + 2] == 't' && buffer[start + 3] == ';') {

            buffer[start] = '<';
            return 1;
        }

        if (buffer[start + 1] == 'g' && buffer[start + 2] == 't' && buffer[start + 3] == ';') {

            buffer[start] = '>';
            return 1;
        }

        if (buffer[start + 1] == 'a' && buffer[start + 2] == 'p' && buffer[start + 3] == 'o' && buffer[start + 4] == 's' && buffer[start + 5] == ';') {

            buffer[start] = '\'';
            return 1;
        }

        if (buffer[start + 1] == 'q' && buffer[start + 2] == 'u' && buffer[start + 3] == 'o' && buffer[start + 4] == 't' && buffer[start + 5] == ';') {

            buffer[start] = '"';
            return 1;
        }

        if (buffer[start + 1] == 'a' && buffer[start + 2] == 'm' && buffer[start + 3] == 'p' && buffer[start + 4] == ';') {

            buffer[start] = '&';
            return 1;
        }

        this._tmp.setArray(buffer, start + 1, length - 2);
        CharSequence replacementText = (this._entitiesMapping != null) ? this._entitiesMapping.get(this._tmp) : null;

        if (replacementText == null)
            throw new XMLStreamException("Entity " + this._tmp + " not recognized");
        int replacementTextLength = replacementText.length();
        for (int i = 0; i < replacementTextLength; i++) {
            buffer[start + i] = replacementText.charAt(i);
        }
        return replacementTextLength;
    }

    public Map<String, String> getEntitiesMapping() {
        return this._entitiesMapping;
    }

    public void setEntitiesMapping(Map<String, String> entityToReplacementText) {
        FastTable<String> values = new FastTable();
        values.addAll(entityToReplacementText.values());
        this._maxLength = ((Integer) values.mapped(new Function<CharSequence, Integer>() {
            public Integer apply(CharSequence csq) {
                return Integer.valueOf(csq.length());
            }
        }).max()).intValue();
        this._entitiesMapping = entityToReplacementText;
    }

    public void reset() {
        this._maxLength = 1;
        this._entitiesMapping = null;
    }
}

