package com.zhonglian.server.logger.scribe;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import java.io.ByteArrayOutputStream;

public class ScribeFormatedAppender
extends OutputStreamAppender<ILoggingEvent>
{
private String host;
private int port;
private String encoding;
ScribeTransfer _scribeTransfer;
private ByteArrayOutputStream _byteOutStream = new ByteArrayOutputStream();

public ScribeFormatedAppender() {
setOutputStream(this._byteOutStream);
}

public String getHost() {
return this.host;
}

public void setHost(String host) {
this.host = host;
}

public int getPort() {
return this.port;
}

public void setPort(int port) {
this.port = port;
}

public String getEncoding() {
return this.encoding;
}

public void setEncoding(String encoding) {
this.encoding = encoding;
}

public void start() {
super.start();
synchronized (this) {
try {
this.encoder.init(this._byteOutStream);
this._scribeTransfer = new ScribeTransfer();
this._scribeTransfer.setHost(getHost());
this._scribeTransfer.setPort(getPort());
this._scribeTransfer.setEncoding(getEncoding());
this._scribeTransfer.start();
} catch (Exception e) {
e.printStackTrace();
} 
} 
}

public void append(ILoggingEvent event) {
try {
this.encoder.doEncode(event);
String str = this._byteOutStream.toString();
this._byteOutStream.reset();
this._scribeTransfer.append(str);
} catch (Exception e) {
e.printStackTrace();
return;
} 
}
}

