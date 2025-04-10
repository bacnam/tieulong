package ch.qos.logback.core.spi;

import java.util.Collection;
import java.util.Set;

public interface ComponentTracker<C> {
  public static final int DEFAULT_TIMEOUT = 1800000;

  public static final int DEFAULT_MAX_COMPONENTS = 2147483647;

  int getComponentCount();

  C find(String paramString);

  C getOrCreate(String paramString, long paramLong);

  void removeStaleComponents(long paramLong);

  void endOfLife(String paramString);

  Collection<C> allComponents();

  Set<String> allKeys();
}

