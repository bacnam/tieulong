package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.spi.ContextAwareBase;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationWatchList
extends ContextAwareBase
{
URL mainURL;
List<File> fileWatchList = new ArrayList<File>();
List<Long> lastModifiedList = new ArrayList<Long>();

public void clear() {
this.mainURL = null;
this.lastModifiedList.clear();
this.fileWatchList.clear();
}

public void setMainURL(URL mainURL) {
this.mainURL = mainURL;
if (mainURL != null)
addAsFileToWatch(mainURL); 
}

private void addAsFileToWatch(URL url) {
File file = convertToFile(url);
if (file != null) {
this.fileWatchList.add(file);
this.lastModifiedList.add(Long.valueOf(file.lastModified()));
} 
}

public void addToWatchList(URL url) {
addAsFileToWatch(url);
}

public URL getMainURL() {
return this.mainURL;
}

public List<File> getCopyOfFileWatchList() {
return new ArrayList<File>(this.fileWatchList);
}

public boolean changeDetected() {
int len = this.fileWatchList.size();
for (int i = 0; i < len; i++) {
long lastModified = ((Long)this.lastModifiedList.get(i)).longValue();
File file = this.fileWatchList.get(i);
if (lastModified != file.lastModified()) {
return true;
}
} 
return false;
}

File convertToFile(URL url) {
String protocol = url.getProtocol();
if ("file".equals(protocol)) {
return new File(URLDecoder.decode(url.getFile()));
}
addInfo("URL [" + url + "] is not of type file");
return null;
}
}

