package javolution.xml.internal.stream;

import javolution.text.CharArray;
import javolution.util.FastTable;
import javolution.xml.stream.NamespaceContext;

import java.util.Iterator;

public final class NamespacesImpl
        implements NamespaceContext {
    static final int NBR_PREDEFINED_NAMESPACES = 3;
    final CharArray _nullNsURI = new CharArray("");

    final CharArray _defaultNsPrefix = new CharArray("");

    final CharArray _xml = new CharArray("xml");

    final CharArray _xmlURI = new CharArray("http:

    final CharArray _xmlns = new CharArray("xmlns");

    final CharArray _xmlnsURI = new CharArray("http:
    CharArray[] _prefixes = new CharArray[16];
    CharArray[] _namespaces = new CharArray[this._prefixes.length];
    boolean[] _prefixesWritten = new boolean[this._prefixes.length];
    int[] _namespacesCount = new int[16];
    CharArray _defaultNamespace = this._nullNsURI;
    int _defaultNamespaceIndex;
    private int _nesting = 0;
    private CharArray[] _prefixesTmp;

    private CharArray[] _namespacesTmp;

    public NamespacesImpl() {
        this._prefixesTmp = new CharArray[this._prefixes.length];

        this._namespacesTmp = new CharArray[this._prefixes.length];
        this._prefixes[0] = this._defaultNsPrefix;
        this._namespaces[0] = this._nullNsURI;
        this._prefixes[1] = this._xml;
        this._namespaces[1] = this._xmlURI;
        this._prefixes[2] = this._xmlns;
        this._namespaces[2] = this._xmlnsURI;
        this._namespacesCount[0] = 3;
    }

    public CharArray getNamespaceURI(CharSequence prefix) {
        if (prefix == null)
            throw new IllegalArgumentException("null prefix not allowed");
        return getNamespaceURINullAllowed(prefix);
    }

    CharArray getNamespaceURINullAllowed(CharSequence prefix) {
        if (prefix == null || prefix.length() == 0)
            return this._defaultNamespace;
        int count = this._namespacesCount[this._nesting];
        for (int i = count; --i >= 0; ) {
            if (this._prefixes[i].equals(prefix))
                return this._namespaces[i];
        }
        return null;
    }

    public CharArray getPrefix(CharSequence uri) {
        if (uri == null)
            throw new IllegalArgumentException("null namespace URI not allowed");
        return this._defaultNamespace.equals(uri) ? this._defaultNsPrefix : getPrefix(uri, this._namespacesCount[this._nesting]);
    }

    CharArray getPrefix(CharSequence uri, int count) {
        for (int i = count; --i >= 0; ) {
            CharArray prefix = this._prefixes[i];
            CharArray namespace = this._namespaces[i];
            if (namespace.equals(uri)) {

                boolean isPrefixOverwritten = false;
                for (int j = i + 1; j < count; j++) {
                    if (prefix.equals(this._prefixes[j])) {
                        isPrefixOverwritten = true;
                        break;
                    }
                }
                if (!isPrefixOverwritten)
                    return prefix;
            }
        }
        return null;
    }

    public Iterator<CharArray> getPrefixes(CharSequence namespaceURI) {
        FastTable<CharArray> prefixes = new FastTable();
        for (int i = this._namespacesCount[this._nesting]; --i >= 0; ) {
            if (this._namespaces[i].equals(namespaceURI)) {
                prefixes.add(this._prefixes[i]);
            }
        }
        return prefixes.iterator();
    }

    void setPrefix(CharArray prefix, CharArray uri) {
        int index = this._namespacesCount[this._nesting];
        this._prefixes[index] = prefix;
        this._namespaces[index] = uri;
        if (prefix.length() == 0) {
            this._defaultNamespaceIndex = index;
            this._defaultNamespace = uri;
        }
        this._namespacesCount[this._nesting] = this._namespacesCount[this._nesting] + 1;
        if (this._namespacesCount[this._nesting] + 1 >= this._prefixes.length) {
            resizePrefixStack();
        }
    }

    void setPrefix(CharSequence prefix, CharSequence uri, boolean isWritten) {
        int index = this._namespacesCount[this._nesting];
        this._prefixesWritten[index] = isWritten;
        int prefixLength = prefix.length();
        CharArray prefixTmp = this._prefixesTmp[index];
        if (prefixTmp == null || (prefixTmp.array()).length < prefixLength) {
            this._prefixesTmp[index] = (new CharArray()).setArray(new char[prefixLength + 32], 0, 0);

            prefixTmp = this._prefixesTmp[index];
        }
        for (int i = 0; i < prefixLength; i++) {
            prefixTmp.array()[i] = prefix.charAt(i);
        }
        prefixTmp.setArray(prefixTmp.array(), 0, prefixLength);

        int uriLength = uri.length();
        CharArray namespaceTmp = this._namespacesTmp[index];
        if (namespaceTmp == null || (namespaceTmp.array()).length < uriLength) {
            this._namespacesTmp[index] = (new CharArray()).setArray(new char[uriLength + 32], 0, 0);

            namespaceTmp = this._namespacesTmp[index];
        }
        for (int j = 0; j < uriLength; j++) {
            namespaceTmp.array()[j] = uri.charAt(j);
        }
        namespaceTmp.setArray(namespaceTmp.array(), 0, uriLength);

        setPrefix(prefixTmp, namespaceTmp);
    }

    void pop() {
        if (this._namespacesCount[--this._nesting] <= this._defaultNamespaceIndex) {
            searchDefaultNamespace();
        }
    }

    private void searchDefaultNamespace() {
        int count = this._namespacesCount[this._nesting];
        for (int i = count; --i >= 0; ) {
            if (this._prefixes[i].length() == 0) {
                this._defaultNamespaceIndex = i;
                return;
            }
        }
        throw new Error("Cannot find default namespace");
    }

    void push() {
        this._nesting++;
        if (this._nesting >= this._namespacesCount.length) {
            resizeNamespacesCount();
        }
        this._namespacesCount[this._nesting] = this._namespacesCount[this._nesting - 1];
    }

    public void reset() {
        this._defaultNamespace = this._nullNsURI;
        this._defaultNamespaceIndex = 0;
        this._namespacesCount[0] = 3;
        this._nesting = 0;
    }

    private void resizeNamespacesCount() {
        int oldLength = this._namespacesCount.length;
        int newLength = oldLength * 2;

        int[] tmp = new int[newLength];
        System.arraycopy(this._namespacesCount, 0, tmp, 0, oldLength);
        this._namespacesCount = tmp;
    }

    private void resizePrefixStack() {
        int oldLength = this._prefixes.length;
        int newLength = oldLength * 2;

        CharArray[] tmp0 = new CharArray[newLength];
        System.arraycopy(this._prefixes, 0, tmp0, 0, oldLength);
        this._prefixes = tmp0;

        CharArray[] tmp1 = new CharArray[newLength];
        System.arraycopy(this._namespaces, 0, tmp1, 0, oldLength);
        this._namespaces = tmp1;

        boolean[] tmp2 = new boolean[newLength];
        System.arraycopy(this._prefixesWritten, 0, tmp2, 0, oldLength);
        this._prefixesWritten = tmp2;

        CharArray[] tmp3 = new CharArray[newLength];
        System.arraycopy(this._prefixesTmp, 0, tmp3, 0, oldLength);
        this._prefixesTmp = tmp3;

        CharArray[] tmp4 = new CharArray[newLength];
        System.arraycopy(this._namespacesTmp, 0, tmp4, 0, oldLength);
        this._namespacesTmp = tmp4;
    }
}

