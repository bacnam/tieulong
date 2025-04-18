package org.slf4j.impl;

import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

public class StaticMarkerBinder
implements MarkerFactoryBinder
{
public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();

final IMarkerFactory markerFactory = (IMarkerFactory)new BasicMarkerFactory();

public IMarkerFactory getMarkerFactory() {
return this.markerFactory;
}

public String getMarkerFactoryClassStr() {
return BasicMarkerFactory.class.getName();
}
}

