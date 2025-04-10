package ch.qos.logback.core.encoder;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.OutputStream;

public class EchoEncoder<E>
extends EncoderBase<E>
{
String fileHeader;
String fileFooter;

public void doEncode(E event) throws IOException {
String val = (new StringBuilder()).append(event).append(CoreConstants.LINE_SEPARATOR).toString();
this.outputStream.write(val.getBytes());

this.outputStream.flush();
}

public void close() throws IOException {
if (this.fileFooter == null) {
return;
}
this.outputStream.write(this.fileFooter.getBytes());
}

public void init(OutputStream os) throws IOException {
super.init(os);
if (this.fileHeader == null) {
return;
}
this.outputStream.write(this.fileHeader.getBytes());
}
}

