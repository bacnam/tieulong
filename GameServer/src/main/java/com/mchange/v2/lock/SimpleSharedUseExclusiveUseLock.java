package com.mchange.v2.lock;

public class SimpleSharedUseExclusiveUseLock
implements SharedUseExclusiveUseLock
{
private int waiting_readers = 0;
private int active_readers = 0;

private int waiting_writers = 0;

private boolean writer_active = false;

public synchronized void acquireShared() throws InterruptedException {
try {
this.waiting_readers++;
while (!okayToRead())
wait(); 
this.active_readers++;
}
finally {

this.waiting_readers--;
} 
}

public synchronized void relinquishShared() {
this.active_readers--;
notifyAll();
}

public synchronized void acquireExclusive() throws InterruptedException {
try {
this.waiting_writers++;
while (!okayToWrite())
wait(); 
this.writer_active = true;
}
finally {

this.waiting_writers--;
} 
}

public synchronized void relinquishExclusive() {
this.writer_active = false;
notifyAll();
}

private boolean okayToRead() {
return (!this.writer_active && this.waiting_writers == 0);
}
private boolean okayToWrite() {
return (this.active_readers == 0 && !this.writer_active);
}
}

