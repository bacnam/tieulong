package ch.qos.logback.core.encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class EventObjectInputStream<E>
extends InputStream
{
NonClosableInputStream ncis;
List<E> buffer = new ArrayList<E>();

int index = 0;

EventObjectInputStream(InputStream is) throws IOException {
this.ncis = new NonClosableInputStream(is);
}

public int read() throws IOException {
throw new UnsupportedOperationException("Only the readEvent method is supported.");
}

public int available() throws IOException {
return this.ncis.available();
}

public E readEvent() throws IOException {
E event = getFromBuffer();
if (event != null) {
return event;
}

internalReset();
int count = readHeader();
if (count == -1) {
return null;
}
readPayload(count);
readFooter(count);
return getFromBuffer();
}

private void internalReset() {
this.index = 0;
this.buffer.clear();
}

E getFromBuffer() {
if (this.index >= this.buffer.size()) {
return null;
}
return this.buffer.get(this.index++);
}

int readHeader() throws IOException {
byte[] headerBA = new byte[16];

int bytesRead = this.ncis.read(headerBA);
if (bytesRead == -1) {
return -1;
}

int offset = 0;
int startPebble = ByteArrayUtil.readInt(headerBA, offset);
if (startPebble != 1853421169) {
throw new IllegalStateException("Does not look like data created by ObjectStreamEncoder");
}

offset += 4;
int count = ByteArrayUtil.readInt(headerBA, offset);
offset += 4;
int endPointer = ByteArrayUtil.readInt(headerBA, offset);
offset += 4;
int checksum = ByteArrayUtil.readInt(headerBA, offset);
if (checksum != (0x6E78F671 ^ count)) {
throw new IllegalStateException("Invalid checksum");
}
return count;
}

E readEvents(ObjectInputStream ois) throws IOException {
E e = null;
try {
e = (E)ois.readObject();
this.buffer.add(e);
} catch (ClassNotFoundException e1) {

e1.printStackTrace();
} 
return e;
}

void readFooter(int count) throws IOException {
byte[] headerBA = new byte[8];
this.ncis.read(headerBA);

int offset = 0;
int stopPebble = ByteArrayUtil.readInt(headerBA, offset);
if (stopPebble != 640373619) {
throw new IllegalStateException("Looks like a corrupt stream");
}

offset += 4;
int checksum = ByteArrayUtil.readInt(headerBA, offset);
if (checksum != (0x262B5373 ^ count)) {
throw new IllegalStateException("Invalid checksum");
}
}

void readPayload(int count) throws IOException {
List<E> eventList = new ArrayList<E>(count);
ObjectInputStream ois = new ObjectInputStream(this.ncis);
for (int i = 0; i < count; i++) {
E e = readEvents(ois);
eventList.add(e);
} 
ois.close();
}

public void close() throws IOException {
this.ncis.realClose();
}
}

