package com.mchange.v2.async.test;

import com.mchange.v2.async.RoundRobinAsynchronousRunner;
import com.mchange.v2.lang.ThreadUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InterruptTaskThread {
    static Set interruptedThreads = Collections.synchronizedSet(new HashSet());

    public static void main(String[] paramArrayOfString) {
        try {
            RoundRobinAsynchronousRunner roundRobinAsynchronousRunner = new RoundRobinAsynchronousRunner(5, false);
            (new Interrupter()).start();
            for (byte b = 0; b < 'Ϩ'; b++) {

                try {
                    roundRobinAsynchronousRunner.postRunnable(new DumbTask());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                Thread.sleep(50L);
            }
            System.out.println("Interrupted Threads: " + interruptedThreads.size());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static class Interrupter
            extends Thread {
        public void run() {
            try {
                while (true) {
                    Thread[] arrayOfThread = new Thread[1000];
                    ThreadUtils.enumerateAll(arrayOfThread);
                    for (byte b = 0; arrayOfThread[b] != null; b++) {

                        if (arrayOfThread[b].getName().indexOf("RunnableQueue.TaskThread") >= 0) {

                            arrayOfThread[b].interrupt();
                            System.out.println("INTERRUPTED!");
                            InterruptTaskThread.interruptedThreads.add(arrayOfThread[b]);
                            break;
                        }
                    }
                    Thread.sleep(1000L);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }
    }

    static class DumbTask implements Runnable {
        static int count = 0;

        static synchronized int number() {
            return count++;
        }

        public void run() {
            try {
                Thread.sleep(200L);
                System.out.println("DumbTask complete! " + number());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

