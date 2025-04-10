package ch.qos.logback.core.encoder;

import ch.qos.logback.core.spi.ContextAwareBase;
import java.io.IOException;
import java.io.OutputStream;

public abstract class EncoderBase<E>
extends ContextAwareBase
implements Encoder<E>
{
protected boolean started;
protected OutputStream outputStream;

public void init(OutputStream os) throws IOException {
this.outputStream = os;
}

public boolean isStarted() {
return this.started;
}

public void start() {
this.started = true;
}

public void stop() {
this.started = false;
}
}

