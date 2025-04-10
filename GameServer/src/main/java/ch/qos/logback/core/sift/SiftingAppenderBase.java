package ch.qos.logback.core.sift;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.util.Duration;

public abstract class SiftingAppenderBase<E>
extends AppenderBase<E>
{
protected AppenderTracker<E> appenderTracker;
AppenderFactory<E> appenderFactory;
Duration timeout = new Duration(1800000L);
int maxAppenderCount = Integer.MAX_VALUE;

Discriminator<E> discriminator;

public Duration getTimeout() {
return this.timeout;
}

public void setTimeout(Duration timeout) {
this.timeout = timeout;
}

public int getMaxAppenderCount() {
return this.maxAppenderCount;
}

public void setMaxAppenderCount(int maxAppenderCount) {
this.maxAppenderCount = maxAppenderCount;
}

public void setAppenderFactory(AppenderFactory<E> appenderFactory) {
this.appenderFactory = appenderFactory;
}

public void start() {
int errors = 0;
if (this.discriminator == null) {
addError("Missing discriminator. Aborting");
errors++;
} 
if (!this.discriminator.isStarted()) {
addError("Discriminator has not started successfully. Aborting");
errors++;
} 
if (this.appenderFactory == null) {
addError("AppenderFactory has not been set. Aborting");
errors++;
} else {
this.appenderTracker = new AppenderTracker<E>(this.context, this.appenderFactory);
this.appenderTracker.setMaxComponents(this.maxAppenderCount);
this.appenderTracker.setTimeout(this.timeout.getMilliseconds());
} 
if (errors == 0) {
super.start();
}
}

public void stop() {
for (Appender<E> appender : (Iterable<Appender<E>>)this.appenderTracker.allComponents()) {
appender.stop();
}
}

protected abstract long getTimestamp(E paramE);

protected void append(E event) {
if (!isStarted()) {
return;
}
String discriminatingValue = this.discriminator.getDiscriminatingValue(event);
long timestamp = getTimestamp(event);

Appender<E> appender = (Appender<E>)this.appenderTracker.getOrCreate(discriminatingValue, timestamp);

if (eventMarksEndOfLife(event)) {
this.appenderTracker.endOfLife(discriminatingValue);
}
this.appenderTracker.removeStaleComponents(timestamp);
appender.doAppend(event);
}

protected abstract boolean eventMarksEndOfLife(E paramE);

public Discriminator<E> getDiscriminator() {
return this.discriminator;
}

public void setDiscriminator(Discriminator<E> discriminator) {
this.discriminator = discriminator;
}

public AppenderTracker<E> getAppenderTracker() {
return this.appenderTracker;
}

public String getDiscriminatorKey() {
if (this.discriminator != null) {
return this.discriminator.getKey();
}
return null;
}
}

