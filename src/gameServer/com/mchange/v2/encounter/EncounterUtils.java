package com.mchange.v2.encounter;

import com.mchange.v1.identicator.IdHashMap;
import com.mchange.v1.identicator.IdWeakHashMap;
import com.mchange.v1.identicator.Identicator;
import java.util.Map;

public final class EncounterUtils
{
public static EncounterCounter createStrong(Identicator paramIdenticator) {
return new GenericEncounterCounter((Map)new IdHashMap(paramIdenticator));
}
public static EncounterCounter createWeak(Identicator paramIdenticator) {
return new GenericEncounterCounter((Map)new IdWeakHashMap(paramIdenticator));
}

public static EncounterCounter syncWrap(final EncounterCounter inner) {
return new EncounterCounter() {
public synchronized long encounter(Object param1Object) {
return inner.encounter(param1Object);
} public synchronized long reset(Object param1Object) { return inner.reset(param1Object); } public synchronized void resetAll() {
inner.resetAll();
}
};
}
}

