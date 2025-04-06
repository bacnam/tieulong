package com.mchange.v3.filecache;

import java.net.URL;

public interface FileCacheKey {
  URL getURL();
  
  String getCacheFilePath();
  
  boolean equals(Object paramObject);
  
  int hashCode();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/filecache/FileCacheKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */