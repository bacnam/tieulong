package javolution.io;

import java.io.IOException;
import java.io.Reader;
import javolution.lang.MathLib;
import javolution.text.CharArray;
import javolution.text.Text;
import javolution.text.TextBuilder;

public final class CharSequenceReader
extends Reader
{
private CharSequence _input;
private int _index;

public CharSequenceReader setInput(CharSequence charSequence) {
if (this._input != null)
throw new IllegalStateException("Reader not closed or reset"); 
this._input = charSequence;
return this;
}

public boolean ready() throws IOException {
if (this._input == null)
throw new IOException("Reader closed"); 
return true;
}

public void close() {
if (this._input != null) {
reset();
}
}

public int read() throws IOException {
if (this._input == null)
throw new IOException("Reader closed"); 
return (this._index < this._input.length()) ? this._input.charAt(this._index++) : -1;
}

public int read(char[] cbuf, int off, int len) throws IOException {
if (this._input == null)
throw new IOException("Reader closed"); 
int inputLength = this._input.length();
if (this._index >= inputLength)
return -1; 
int count = MathLib.min(inputLength - this._index, len);
Object csq = this._input;
if (csq instanceof String) {
String str = (String)csq;
str.getChars(this._index, this._index + count, cbuf, off);
} else if (csq instanceof Text) {
Text txt = (Text)csq;
txt.getChars(this._index, this._index + count, cbuf, off);
} else if (csq instanceof TextBuilder) {
TextBuilder tb = (TextBuilder)csq;
tb.getChars(this._index, this._index + count, cbuf, off);
} else if (csq instanceof CharArray) {
CharArray ca = (CharArray)csq;
System.arraycopy(ca.array(), this._index + ca.offset(), cbuf, off, count);
} else {
for (int i = off, n = off + count, j = this._index; i < n;) {
cbuf[i++] = this._input.charAt(j++);
}
} 
this._index += count;
return count;
}

public void read(Appendable dest) throws IOException {
if (this._input == null)
throw new IOException("Reader closed"); 
dest.append(this._input);
}

public void reset() {
this._index = 0;
this._input = null;
}
}

