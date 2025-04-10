package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAwareBase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ConcurrentServerRunner<T extends Client>
extends ContextAwareBase
implements Runnable, ServerRunner<T>
{
private final Lock clientsLock = new ReentrantLock();

private final Collection<T> clients = new ArrayList<T>();

private final ServerListener<T> listener;

private final Executor executor;

private boolean running;

public ConcurrentServerRunner(ServerListener<T> listener, Executor executor) {
this.listener = listener;
this.executor = executor;
}

public boolean isRunning() {
return this.running;
}

protected void setRunning(boolean running) {
this.running = running;
}

public void stop() throws IOException {
this.listener.close();
accept(new ClientVisitor<T>() {
public void visit(T client) {
client.close();
}
});
}

public void accept(ClientVisitor<T> visitor) {
Collection<T> clients = copyClients();
for (Client client : clients) {
try {
visitor.visit((T)client);
}
catch (RuntimeException ex) {
addError(client + ": " + ex);
} 
} 
}

private Collection<T> copyClients() {
this.clientsLock.lock();
try {
Collection<T> copy = new ArrayList<T>(this.clients);
return copy;
} finally {

this.clientsLock.unlock();
} 
}

public void run() {
setRunning(true);
try {
addInfo("listening on " + this.listener);
while (!Thread.currentThread().isInterrupted()) {
T client = this.listener.acceptClient();
if (!configureClient(client)) {
addError((new StringBuilder()).append(client).append(": connection dropped").toString());
client.close();
continue;
} 
try {
this.executor.execute(new ClientWrapper(client));
}
catch (RejectedExecutionException ex) {
addError((new StringBuilder()).append(client).append(": connection dropped").toString());
client.close();
}

} 
} catch (InterruptedException ex) {

} catch (Exception ex) {
addError("listener: " + ex);
} 

setRunning(false);
addInfo("shutting down");
this.listener.close();
}

private void addClient(T client) {
this.clientsLock.lock();
try {
this.clients.add(client);
} finally {

this.clientsLock.unlock();
} 
}

private void removeClient(T client) {
this.clientsLock.lock();
try {
this.clients.remove(client);
} finally {

this.clientsLock.unlock();
} 
}

protected abstract boolean configureClient(T paramT);

private class ClientWrapper
implements Client
{
private final T delegate;

public ClientWrapper(T client) {
this.delegate = client;
}

public void run() {
ConcurrentServerRunner.this.addClient(this.delegate);
try {
this.delegate.run();
} finally {

ConcurrentServerRunner.this.removeClient(this.delegate);
} 
}

public void close() {
this.delegate.close();
}
}
}

