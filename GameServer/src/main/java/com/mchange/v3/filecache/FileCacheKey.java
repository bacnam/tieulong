package com.mchange.v3.filecache;

import java.net.URL;

public interface FileCacheKey {
  URL getURL();

  String getCacheFilePath();

  boolean equals(Object paramObject);

  int hashCode();
}

