package com.mchange.v2.log.jdk14logging;

import com.mchange.v2.log.MLevel;
import java.util.logging.Level;

final class Jdk14LoggingUtils
{
public static MLevel mlevelFromLevel(Level paramLevel) {
if (paramLevel == Level.ALL)
return MLevel.ALL; 
if (paramLevel == Level.CONFIG)
return MLevel.CONFIG; 
if (paramLevel == Level.FINE)
return MLevel.FINE; 
if (paramLevel == Level.FINER)
return MLevel.FINER; 
if (paramLevel == Level.FINEST)
return MLevel.FINEST; 
if (paramLevel == Level.INFO)
return MLevel.INFO; 
if (paramLevel == Level.OFF)
return MLevel.OFF; 
if (paramLevel == Level.SEVERE)
return MLevel.SEVERE; 
if (paramLevel == Level.WARNING) {
return MLevel.WARNING;
}
throw new IllegalArgumentException("Unexpected Jdk14 logging level: " + paramLevel);
}
}

