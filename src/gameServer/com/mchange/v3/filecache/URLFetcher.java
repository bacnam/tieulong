package com.mchange.v3.filecache;

import com.mchange.v2.log.MLogger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface URLFetcher {
  InputStream openStream(URL paramURL, MLogger paramMLogger) throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/filecache/URLFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */