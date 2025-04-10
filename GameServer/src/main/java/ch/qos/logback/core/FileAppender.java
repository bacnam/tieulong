package ch.qos.logback.core;

import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileAppender<E>
extends OutputStreamAppender<E>
{
protected boolean append = true;
protected String fileName = null;

private boolean prudent = false;

public void setFile(String file) {
if (file == null) {
this.fileName = file;
}
else {

this.fileName = file.trim();
} 
}

public boolean isAppend() {
return this.append;
}

public final String rawFileProperty() {
return this.fileName;
}

public String getFile() {
return this.fileName;
}

public void start() {
int errors = 0;
if (getFile() != null) {
addInfo("File property is set to [" + this.fileName + "]");

if (this.prudent && 
!isAppend()) {
setAppend(true);
addWarn("Setting \"Append\" property to true on account of \"Prudent\" mode");
} 

try {
openFile(getFile());
} catch (IOException e) {
errors++;
addError("openFile(" + this.fileName + "," + this.append + ") call failed.", e);
} 
} else {
errors++;
addError("\"File\" property not set for appender named [" + this.name + "].");
} 
if (errors == 0) {
super.start();
}
}

public void openFile(String file_name) throws IOException {
this.lock.lock();
try {
File file = new File(file_name);
boolean result = FileUtil.createMissingParentDirectories(file);
if (!result) {
addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
}

ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(file, this.append);

resilientFos.setContext(this.context);
setOutputStream((OutputStream)resilientFos);
} finally {
this.lock.unlock();
} 
}

public boolean isPrudent() {
return this.prudent;
}

public void setPrudent(boolean prudent) {
this.prudent = prudent;
}

public void setAppend(boolean append) {
this.append = append;
}

private void safeWrite(E event) throws IOException {
ResilientFileOutputStream resilientFOS = (ResilientFileOutputStream)getOutputStream();
FileChannel fileChannel = resilientFOS.getChannel();
if (fileChannel == null) {
return;
}

boolean interrupted = Thread.interrupted();

FileLock fileLock = null;
try {
fileLock = fileChannel.lock();
long position = fileChannel.position();
long size = fileChannel.size();
if (size != position) {
fileChannel.position(size);
}
super.writeOut(event);
} catch (IOException e) {

resilientFOS.postIOFailure(e);
} finally {

if (fileLock != null && fileLock.isValid()) {
fileLock.release();
}

if (interrupted) {
Thread.currentThread().interrupt();
}
} 
}

protected void writeOut(E event) throws IOException {
if (this.prudent) {
safeWrite(event);
} else {
super.writeOut(event);
} 
}
}

