package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.annotation.Immutable;
import org.apache.http.io.EofSensor;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
@Immutable
public class LoggingSessionInputBuffer
implements SessionInputBuffer, EofSensor
{
private final SessionInputBuffer in;
private final EofSensor eofSensor;
private final Wire wire;
private final String charset;

public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire, String charset) {
this.in = in;
this.eofSensor = (in instanceof EofSensor) ? (EofSensor)in : null;
this.wire = wire;
this.charset = (charset != null) ? charset : Consts.ASCII.name();
}

public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire) {
this(in, wire, null);
}

public boolean isDataAvailable(int timeout) throws IOException {
return this.in.isDataAvailable(timeout);
}

public int read(byte[] b, int off, int len) throws IOException {
int l = this.in.read(b, off, len);
if (this.wire.enabled() && l > 0) {
this.wire.input(b, off, l);
}
return l;
}

public int read() throws IOException {
int l = this.in.read();
if (this.wire.enabled() && l != -1) {
this.wire.input(l);
}
return l;
}

public int read(byte[] b) throws IOException {
int l = this.in.read(b);
if (this.wire.enabled() && l > 0) {
this.wire.input(b, 0, l);
}
return l;
}

public String readLine() throws IOException {
String s = this.in.readLine();
if (this.wire.enabled() && s != null) {
String tmp = s + "\r\n";
this.wire.input(tmp.getBytes(this.charset));
} 
return s;
}

public int readLine(CharArrayBuffer buffer) throws IOException {
int l = this.in.readLine(buffer);
if (this.wire.enabled() && l >= 0) {
int pos = buffer.length() - l;
String s = new String(buffer.buffer(), pos, l);
String tmp = s + "\r\n";
this.wire.input(tmp.getBytes(this.charset));
} 
return l;
}

public HttpTransportMetrics getMetrics() {
return this.in.getMetrics();
}

public boolean isEof() {
if (this.eofSensor != null) {
return this.eofSensor.isEof();
}
return false;
}
}

