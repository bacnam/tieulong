package com.mchange.v2.lang;

public final class ThreadGroupUtils
{
public static ThreadGroup rootThreadGroup() {
ThreadGroup threadGroup1 = Thread.currentThread().getThreadGroup();
ThreadGroup threadGroup2 = threadGroup1.getParent();
while (threadGroup2 != null) {

threadGroup1 = threadGroup2;
threadGroup2 = threadGroup1.getParent();
} 
return threadGroup1;
}
}

