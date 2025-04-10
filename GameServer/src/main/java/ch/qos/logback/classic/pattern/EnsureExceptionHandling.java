package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.PostCompileProcessor;

public class EnsureExceptionHandling
implements PostCompileProcessor<ILoggingEvent>
{
public void process(Converter<ILoggingEvent> head) {
if (head == null)
{
throw new IllegalArgumentException("cannot process empty chain");
}
if (!chainHandlesThrowable(head)) {
Converter<ILoggingEvent> tail = ConverterUtil.findTail(head);
ExtendedThrowableProxyConverter extendedThrowableProxyConverter = new ExtendedThrowableProxyConverter();
tail.setNext((Converter)extendedThrowableProxyConverter);
} 
}

public boolean chainHandlesThrowable(Converter head) {
Converter c = head;
while (c != null) {
if (c instanceof ThrowableHandlingConverter) {
return true;
}
c = c.getNext();
} 
return false;
}
}

