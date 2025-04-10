package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.CloseUtil;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

class RemoteReceiverStreamClient
extends ContextAwareBase
implements RemoteReceiverClient
{
private final String clientId;
private final Socket socket;
private final OutputStream outputStream;
private BlockingQueue<Serializable> queue;

public RemoteReceiverStreamClient(String id, Socket socket) {
this.clientId = "client " + id + ": ";
this.socket = socket;
this.outputStream = null;
}

RemoteReceiverStreamClient(String id, OutputStream outputStream) {
this.clientId = "client " + id + ": ";
this.socket = null;
this.outputStream = outputStream;
}

public void setQueue(BlockingQueue<Serializable> queue) {
this.queue = queue;
}

public boolean offer(Serializable event) {
if (this.queue == null) {
throw new IllegalStateException("client has no event queue");
}
return this.queue.offer(event);
}

public void close() {
if (this.socket == null)
return;  CloseUtil.closeQuietly(this.socket);
}

public void run() {
addInfo(this.clientId + "connected");

ObjectOutputStream oos = null;
try {
int counter = 0;
oos = createObjectOutputStream();
while (!Thread.currentThread().isInterrupted()) {
try {
Serializable event = this.queue.take();
oos.writeObject(event);
oos.flush();
if (++counter >= 70)
{

counter = 0;
oos.reset();
}

} catch (InterruptedException ex) {
Thread.currentThread().interrupt();
}

} 
} catch (SocketException ex) {
addInfo(this.clientId + ex);
}
catch (IOException ex) {
addError(this.clientId + ex);
}
catch (RuntimeException ex) {
addError(this.clientId + ex);
} finally {

if (oos != null) {
CloseUtil.closeQuietly(oos);
}
close();
addInfo(this.clientId + "connection closed");
} 
}

private ObjectOutputStream createObjectOutputStream() throws IOException {
if (this.socket == null) {
return new ObjectOutputStream(this.outputStream);
}
return new ObjectOutputStream(this.socket.getOutputStream());
}
}

