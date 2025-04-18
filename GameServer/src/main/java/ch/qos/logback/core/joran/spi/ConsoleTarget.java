package ch.qos.logback.core.joran.spi;

import java.io.IOException;
import java.io.OutputStream;

public enum ConsoleTarget
{
SystemOut("System.out", new OutputStream()
{
public void write(int b) throws IOException {
System.out.write(b);
}

public void write(byte[] b) throws IOException {
System.out.write(b);
}

public void write(byte[] b, int off, int len) throws IOException {
System.out.write(b, off, len);
}

public void flush() throws IOException {
System.out.flush();
}
}),

SystemErr("System.err", new OutputStream()
{
public void write(int b) throws IOException {
System.err.write(b);
}

public void write(byte[] b) throws IOException {
System.err.write(b);
}

public void write(byte[] b, int off, int len) throws IOException {
System.err.write(b, off, len);
}

public void flush() throws IOException {
System.err.flush();
}
});

public static ConsoleTarget findByName(String name) {
for (ConsoleTarget target : values()) {
if (target.name.equalsIgnoreCase(name)) {
return target;
}
} 
return null;
}

private final String name;
private final OutputStream stream;

ConsoleTarget(String name, OutputStream stream) {
this.name = name;
this.stream = stream;
}

public String getName() {
return this.name;
}

public OutputStream getStream() {
return this.stream;
}
}

