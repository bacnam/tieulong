package ch.qos.logback.core.recovery;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class ResilientFileOutputStream
extends ResilientOutputStreamBase
{
private File file;
private FileOutputStream fos;

public ResilientFileOutputStream(File file, boolean append) throws FileNotFoundException {
this.file = file;
this.fos = new FileOutputStream(file, append);
this.os = new BufferedOutputStream(this.fos);
this.presumedClean = true;
}

public FileChannel getChannel() {
if (this.os == null) {
return null;
}
return this.fos.getChannel();
}

public File getFile() {
return this.file;
}

String getDescription() {
return "file [" + this.file + "]";
}

OutputStream openNewOutputStream() throws IOException {
this.fos = new FileOutputStream(this.file, true);
return new BufferedOutputStream(this.fos);
}

public String toString() {
return "c.q.l.c.recovery.ResilientFileOutputStream@" + System.identityHashCode(this);
}
}

