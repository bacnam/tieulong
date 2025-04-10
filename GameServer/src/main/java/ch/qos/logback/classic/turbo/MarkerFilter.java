package ch.qos.logback.classic.turbo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class MarkerFilter
extends MatchingFilter
{
Marker markerToMatch;

public void start() {
if (this.markerToMatch != null) {
super.start();
} else {
addError("The marker property must be set for [" + getName() + "]");
} 
}

public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
if (!isStarted()) {
return FilterReply.NEUTRAL;
}

if (marker == null) {
return this.onMismatch;
}

if (marker.contains(this.markerToMatch)) {
return this.onMatch;
}
return this.onMismatch;
}

public void setMarker(String markerStr) {
if (markerStr != null)
this.markerToMatch = MarkerFactory.getMarker(markerStr); 
}
}

