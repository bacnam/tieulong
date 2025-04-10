package javolution.util.internal.collection;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public final class ReadWriteLockImpl
implements ReadWriteLock, Serializable
{
private static final long serialVersionUID = 1536L;

public final class ReadLock
implements Lock, Serializable
{
private static final long serialVersionUID = 1536L;

public void lock() {
try {
lockInterruptibly();
} catch (InterruptedException e) {}
}

public void lockInterruptibly() throws InterruptedException {
synchronized (ReadWriteLockImpl.this) {
if (ReadWriteLockImpl.this.writerThread == Thread.currentThread())
return;  while (ReadWriteLockImpl.this.writerThread != null || ReadWriteLockImpl.this.waitingWriters != 0) {
ReadWriteLockImpl.this.wait();
}
ReadWriteLockImpl.this.givenLocks++;
} 
}

public Condition newCondition() {
throw new UnsupportedOperationException();
}

public boolean tryLock() {
throw new UnsupportedOperationException();
}

public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
throw new UnsupportedOperationException();
}

public void unlock() {
synchronized (ReadWriteLockImpl.this) {
if (ReadWriteLockImpl.this.writerThread == Thread.currentThread())
return;  assert ReadWriteLockImpl.this.givenLocks > 0;
ReadWriteLockImpl.this.givenLocks--;
ReadWriteLockImpl.this.notifyAll();
} 
}
}

public final class WriteLock
implements Lock, Serializable
{
private static final long serialVersionUID = 1536L;

public void lock() {
try {
lockInterruptibly();
} catch (InterruptedException e) {}
}

public void lockInterruptibly() throws InterruptedException {
synchronized (ReadWriteLockImpl.this) {
ReadWriteLockImpl.this.waitingWriters++;
while (ReadWriteLockImpl.this.givenLocks != 0) {
ReadWriteLockImpl.this.wait();
}
ReadWriteLockImpl.this.waitingWriters--;
ReadWriteLockImpl.this.writerThread = Thread.currentThread();
} 
}

public Condition newCondition() {
throw new UnsupportedOperationException();
}

public boolean tryLock() {
throw new UnsupportedOperationException();
}

public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
throw new UnsupportedOperationException();
}

public void unlock() {
synchronized (ReadWriteLockImpl.this) {
ReadWriteLockImpl.this.writerThread = null;
ReadWriteLockImpl.this.notifyAll();
} 
}
}

public final ReadLock readLock = new ReadLock();
public final WriteLock writeLock = new WriteLock();

private transient int givenLocks;
private transient int waitingWriters;
private transient Thread writerThread;

public ReadLock readLock() {
return this.readLock;
}

public WriteLock writeLock() {
return this.writeLock;
}
}

