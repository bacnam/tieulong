package ch.qos.logback.core;

import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.Status;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

public class OutputStreamAppender<E>
extends UnsynchronizedAppenderBase<E>
{
protected Encoder<E> encoder;
protected final ReentrantLock lock = new ReentrantLock(true);

private OutputStream outputStream;

public OutputStream getOutputStream() {
return this.outputStream;
}

public void start() {
int errors = 0;
if (this.encoder == null) {
addStatus((Status)new ErrorStatus("No encoder set for the appender named \"" + this.name + "\".", this));

errors++;
} 

if (this.outputStream == null) {
addStatus((Status)new ErrorStatus("No output stream set for the appender named \"" + this.name + "\".", this));

errors++;
} 

if (errors == 0) {
super.start();
}
}

public void setLayout(Layout<E> layout) {
addWarn("This appender no longer admits a layout as a sub-component, set an encoder instead.");
addWarn("To ensure compatibility, wrapping your layout in LayoutWrappingEncoder.");
addWarn("See also http:
LayoutWrappingEncoder<E> lwe = new LayoutWrappingEncoder();
lwe.setLayout(layout);
lwe.setContext(this.context);
this.encoder = (Encoder<E>)lwe;
}

protected void append(E eventObject) {
if (!isStarted()) {
return;
}

subAppend(eventObject);
}

public void stop() {
this.lock.lock();
try {
closeOutputStream();
super.stop();
} finally {
this.lock.unlock();
} 
}

protected void closeOutputStream() {
if (this.outputStream != null) {

try {
encoderClose();
this.outputStream.close();
this.outputStream = null;
} catch (IOException e) {
addStatus((Status)new ErrorStatus("Could not close output stream for OutputStreamAppender.", this, e));
} 
}
}

void encoderInit() {
if (this.encoder != null && this.outputStream != null) {
try {
this.encoder.init(this.outputStream);
} catch (IOException ioe) {
this.started = false;
addStatus((Status)new ErrorStatus("Failed to initialize encoder for appender named [" + this.name + "].", this, ioe));
} 
}
}

void encoderClose() {
if (this.encoder != null && this.outputStream != null) {
try {
this.encoder.close();
} catch (IOException ioe) {
this.started = false;
addStatus((Status)new ErrorStatus("Failed to write footer for appender named [" + this.name + "].", this, ioe));
} 
}
}

public void setOutputStream(OutputStream outputStream) {
this.lock.lock();

try {
closeOutputStream();

this.outputStream = outputStream;
if (this.encoder == null) {
addWarn("Encoder has not been set. Cannot invoke its init method.");

return;
} 
encoderInit();
} finally {
this.lock.unlock();
} 
}

protected void writeOut(E event) throws IOException {
this.encoder.doEncode(event);
}

protected void subAppend(E event) {
if (!isStarted()) {
return;
}

try {
if (event instanceof DeferredProcessingAware) {
((DeferredProcessingAware)event).prepareForDeferredProcessing();
}

this.lock.lock();
try {
writeOut(event);
} finally {
this.lock.unlock();
} 
} catch (IOException ioe) {

this.started = false;
addStatus((Status)new ErrorStatus("IO failure in appender", this, ioe));
} 
}

public Encoder<E> getEncoder() {
return this.encoder;
}

public void setEncoder(Encoder<E> encoder) {
this.encoder = encoder;
}
}

