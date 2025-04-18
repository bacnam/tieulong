package com.mchange.io.impl;

import com.mchange.io.StringMemoryFile;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LazyStringMemoryFileImpl
extends LazyReadOnlyMemoryFileImpl
implements StringMemoryFile
{
private static final String DEFAULT_ENCODING;

static {
String str = System.getProperty("file.encoding");
DEFAULT_ENCODING = (str == null) ? "8859_1" : str;
}

String encoding = null;
String string = null;

public LazyStringMemoryFileImpl(File paramFile) {
super(paramFile);
}
public LazyStringMemoryFileImpl(String paramString) {
super(paramString);
}

public synchronized String asString(String paramString) throws IOException, UnsupportedEncodingException {
update();
if (this.encoding != paramString)
this.string = new String(this.bytes, paramString); 
return this.string;
}

public String asString() throws IOException {
try {
return asString(DEFAULT_ENCODING);
} catch (UnsupportedEncodingException unsupportedEncodingException) {
throw new InternalError("Default Encoding is not supported?!");
} 
}

void refreshBytes() throws IOException {
super.refreshBytes();
this.encoding = this.string = null;
}
}

