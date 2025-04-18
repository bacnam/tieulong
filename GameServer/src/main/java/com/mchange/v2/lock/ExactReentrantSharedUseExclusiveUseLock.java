package com.mchange.v2.lock;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExactReentrantSharedUseExclusiveUseLock
implements SharedUseExclusiveUseLock
{
Set waitingShared = new HashSet();
List activeShared = new LinkedList();

Set waitingExclusive = new HashSet();
Thread activeExclusive = null;

int exclusive_shared_reentries = 0;
int exclusive_exclusive_reentries = 0;

String name;

public ExactReentrantSharedUseExclusiveUseLock(String paramString) {
this.name = paramString;
}
public ExactReentrantSharedUseExclusiveUseLock() {
this(null);
}

void status(String paramString) {
System.err.println(this + " -- after " + paramString);
System.err.println("waitingShared: " + this.waitingShared);
System.err.println("activeShared: " + this.activeShared);
System.err.println("waitingExclusive: " + this.waitingExclusive);
System.err.println("activeExclusive: " + this.activeExclusive);
System.err.println("exclusive_shared_reentries: " + this.exclusive_shared_reentries);
System.err.println("exclusive_exclusive_reentries: " + this.exclusive_exclusive_reentries);
System.err.println(" ---- ");
System.err.println();
}

public synchronized void acquireShared() throws InterruptedException {
Thread thread = Thread.currentThread();
if (thread == this.activeExclusive) {
this.exclusive_shared_reentries++;
} else {

try {
this.waitingShared.add(thread);
while (!okayForShared())
wait(); 
this.activeShared.add(thread);
}
finally {

this.waitingShared.remove(thread);
} 
} 
}

public synchronized void relinquishShared() {
Thread thread = Thread.currentThread();
if (thread == this.activeExclusive) {

this.exclusive_shared_reentries--;
if (this.exclusive_shared_reentries < 0) {
throw new IllegalStateException(thread + " relinquished a shared lock (reentrant on exclusive) it did not hold!");

}

}
else {

boolean bool = this.activeShared.remove(thread);
if (!bool)
throw new IllegalStateException(thread + " relinquished a shared lock it did not hold!"); 
notifyAll();
} 
}

public synchronized void acquireExclusive() throws InterruptedException {
Thread thread = Thread.currentThread();
if (thread == this.activeExclusive) {
this.exclusive_exclusive_reentries++;
} else {

try {
this.waitingExclusive.add(thread);
while (!okayForExclusive(thread))
wait(); 
this.activeExclusive = thread;
}
finally {

this.waitingExclusive.remove(thread);
} 
} 
}

public synchronized void relinquishExclusive() {
Thread thread = Thread.currentThread();
if (thread != this.activeExclusive)
throw new IllegalStateException(thread + " relinquished an exclusive lock it did not hold!"); 
if (this.exclusive_exclusive_reentries > 0) {
this.exclusive_exclusive_reentries--;
} else {

if (this.exclusive_shared_reentries != 0)
throw new IllegalStateException(thread + " relinquished an exclusive lock while it had reentered but not yet relinquished shared lock acquisitions!"); 
this.activeExclusive = null;
notifyAll();
} 
}

private boolean okayForShared() {
return (this.activeExclusive == null && this.waitingExclusive.size() == 0);
}

private boolean okayForExclusive(Thread paramThread) {
int i = this.activeShared.size();
if (i == 0)
return (this.activeExclusive == null); 
if (i == 1) {
return (this.activeShared.get(0) == paramThread);
}

HashSet hashSet = new HashSet(this.activeShared);
return (hashSet.size() == 1 && hashSet.contains(paramThread));
}

public String toString() {
return super.toString() + " [name=" + this.name + ']';
}
}

