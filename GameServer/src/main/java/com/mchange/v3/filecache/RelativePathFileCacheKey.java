package com.mchange.v3.filecache;

import java.net.MalformedURLException;
import java.net.URL;

public class RelativePathFileCacheKey
implements FileCacheKey
{
final URL url;
final String relPath;

public RelativePathFileCacheKey(URL paramURL, String paramString) throws MalformedURLException, IllegalArgumentException {
String str = paramString.trim();

if (paramURL == null || paramString == null)
throw new IllegalArgumentException("parentURL [" + paramURL + "] and relative path [" + paramString + "] must be non-null"); 
if (str.length() == 0)
throw new IllegalArgumentException("relative path [" + paramString + "] must not be a blank string"); 
if (!str.equals(paramString))
throw new IllegalArgumentException("relative path [" + paramString + "] must not begin or end with whitespace."); 
if (paramString.startsWith("/")) {
throw new IllegalArgumentException("Path must be relative, '" + paramString + "' begins with '/'.");
}
this.url = new URL(paramURL, paramString);
this.relPath = paramString;
}

public URL getURL() {
return this.url;
}
public String getCacheFilePath() {
return this.relPath;
}

public boolean equals(Object paramObject) {
if (paramObject instanceof RelativePathFileCacheKey) {

RelativePathFileCacheKey relativePathFileCacheKey = (RelativePathFileCacheKey)paramObject;
return (this.url.equals(relativePathFileCacheKey.url) && this.relPath.equals(relativePathFileCacheKey.relPath));
} 

return false;
}

public int hashCode() {
return this.url.hashCode() ^ this.relPath.hashCode();
}
}

