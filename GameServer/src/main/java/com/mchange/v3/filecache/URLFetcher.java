package com.mchange.v3.filecache;

import com.mchange.v2.log.MLogger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface URLFetcher {
  InputStream openStream(URL paramURL, MLogger paramMLogger) throws IOException;
}

