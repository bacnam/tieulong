package com.mchange.v2.async;

import com.mchange.v2.io.IndentedWriter;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.util.ResourceClosedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;

public final class ThreadPoolAsynchronousRunner
        implements AsynchronousRunner {
    static final MLogger logger = MLog.getLogger(ThreadPoolAsynchronousRunner.class);

    static final int POLL_FOR_STOP_INTERVAL = 5000;

    static final int DFLT_DEADLOCK_DETECTOR_INTERVAL = 10000;

    static final int DFLT_INTERRUPT_DELAY_AFTER_APPARENT_DEADLOCK = 60000;

    static final int DFLT_MAX_INDIVIDUAL_TASK_TIME = 0;

    static final int DFLT_MAX_EMERGENCY_THREADS = 10;

    static final long PURGE_EVERY = 500L;

    int deadlock_detector_interval;
    int interrupt_delay_after_apparent_deadlock;
    int max_individual_task_time;
    int num_threads;
    boolean daemon;
    HashSet managed;
    HashSet available;
    LinkedList pendingTasks;
    Random rnd = new Random();

    Timer myTimer;

    boolean should_cancel_timer;
    TimerTask deadlockDetector = new DeadlockDetector();
    TimerTask replacedThreadInterruptor = null;

    Map stoppedThreadsToStopDates = new HashMap<Object, Object>();

    String threadLabel;

    private ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer, boolean paramBoolean2, String paramString) {
        this.num_threads = paramInt1;
        this.daemon = paramBoolean1;
        this.max_individual_task_time = paramInt2;
        this.deadlock_detector_interval = paramInt3;
        this.interrupt_delay_after_apparent_deadlock = paramInt4;
        this.myTimer = paramTimer;
        this.should_cancel_timer = paramBoolean2;

        this.threadLabel = paramString;

        recreateThreadsAndTasks();

        paramTimer.schedule(this.deadlockDetector, paramInt3, paramInt3);
    }

    private ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer, boolean paramBoolean2) {
        this(paramInt1, paramBoolean1, paramInt2, paramInt3, paramInt4, paramTimer, paramBoolean2, null);
    }

    public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer, String paramString) {
        this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, paramTimer, false, paramString);
    }

    public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer) {
        this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, paramTimer, false);
    }

    public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, String paramString) {
        this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, new Timer(true), true, paramString);
    }

    public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4) {
        this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, new Timer(true), true);
    }

    public ThreadPoolAsynchronousRunner(int paramInt, boolean paramBoolean, Timer paramTimer, String paramString) {
        this(paramInt, paramBoolean, 0, 10000, 60000, paramTimer, false, paramString);
    }

    public ThreadPoolAsynchronousRunner(int paramInt, boolean paramBoolean, Timer paramTimer) {
        this(paramInt, paramBoolean, 0, 10000, 60000, paramTimer, false);
    }

    public ThreadPoolAsynchronousRunner(int paramInt, boolean paramBoolean) {
        this(paramInt, paramBoolean, 0, 10000, 60000, new Timer(true), true);
    }

    public synchronized void postRunnable(Runnable paramRunnable) {
        try {
            this.pendingTasks.add(paramRunnable);
            notifyAll();

            if (logger.isLoggable(MLevel.FINEST)) {
                logger.log(MLevel.FINEST, this + ": Adding task to queue -- " + paramRunnable);
            }
        } catch (NullPointerException nullPointerException) {

            if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, "NullPointerException while posting Runnable -- Probably we're closed.", nullPointerException);
            }
            throw new ResourceClosedException("Attempted to use a ThreadPoolAsynchronousRunner in a closed or broken state.");
        }
    }

    public synchronized int getThreadCount() {
        return this.managed.size();
    }

    public void close(boolean paramBoolean) {
        synchronized (this) {

            if (this.managed == null)
                return;
            this.deadlockDetector.cancel();

            if (this.should_cancel_timer)
                this.myTimer.cancel();
            this.myTimer = null;
            for (PoolThread poolThread : this.managed) {

                poolThread.gentleStop();
                if (paramBoolean)
                    poolThread.interrupt();
            }
            this.managed = null;

            if (!paramBoolean) {
                for (Iterator<Runnable> iterator = this.pendingTasks.iterator(); iterator.hasNext(); ) {

                    Runnable runnable = iterator.next();
                    (new Thread(runnable)).start();
                    iterator.remove();
                }
            }
            this.available = null;
            this.pendingTasks = null;
        }
    }

    public void close() {
        close(true);
    }

    public synchronized int getActiveCount() {
        return this.managed.size() - this.available.size();
    }

    public synchronized int getIdleCount() {
        return this.available.size();
    }

    public synchronized int getPendingTaskCount() {
        return this.pendingTasks.size();
    }

    public synchronized String getStatus() {
        return getMultiLineStatusString();
    }

    public synchronized String getStackTraces() {
        return getStackTraces(0);
    }

    private String getStackTraces(int paramInt) {
        assert Thread.holdsLock(this);

        if (this.managed == null) {
            return null;
        }

        try {
            Method method = Thread.class.getMethod("getStackTrace", null);

            StringWriter stringWriter = new StringWriter(2048);
            IndentedWriter indentedWriter = new IndentedWriter(stringWriter);
            byte b;
            for (b = 0; b < paramInt; b++)
                indentedWriter.upIndent();
            for (Object object : this.managed) {

                Object[] arrayOfObject = (Object[]) method.invoke(object, null);
                printStackTraces(indentedWriter, object, arrayOfObject);
            }
            for (b = 0; b < paramInt; b++)
                indentedWriter.downIndent();
            indentedWriter.flush();
            String str = stringWriter.toString();
            indentedWriter.close();
            return str;
        } catch (NoSuchMethodException noSuchMethodException) {

            if (logger.isLoggable(MLevel.FINE))
                logger.fine(this + ": stack traces unavailable because this is a pre-Java 1.5 VM.");
            return null;
        } catch (Exception exception) {

            if (logger.isLoggable(MLevel.FINE))
                logger.log(MLevel.FINE, this + ": An Exception occurred while trying to extract PoolThread stack traces.", exception);
            return null;
        }
    }

    private String getJvmStackTraces(int paramInt) {
        try {
            Method method = Thread.class.getMethod("getAllStackTraces", null);
            Map map = (Map) method.invoke(null, null);

            StringWriter stringWriter = new StringWriter(2048);
            IndentedWriter indentedWriter = new IndentedWriter(stringWriter);
            byte b;
            for (b = 0; b < paramInt; b++)
                indentedWriter.upIndent();
            for (Map.Entry entry : map.entrySet()) {

                Object object = entry.getKey();
                Object[] arrayOfObject = (Object[]) entry.getValue();
                printStackTraces(indentedWriter, object, arrayOfObject);
            }
            for (b = 0; b < paramInt; b++)
                indentedWriter.downIndent();
            indentedWriter.flush();
            String str = stringWriter.toString();
            indentedWriter.close();
            return str;
        } catch (NoSuchMethodException noSuchMethodException) {

            if (logger.isLoggable(MLevel.FINE))
                logger.fine(this + ": JVM stack traces unavailable because this is a pre-Java 1.5 VM.");
            return null;
        } catch (Exception exception) {

            if (logger.isLoggable(MLevel.FINE))
                logger.log(MLevel.FINE, this + ": An Exception occurred while trying to extract PoolThread stack traces.", exception);
            return null;
        }
    }

    private void printStackTraces(IndentedWriter paramIndentedWriter, Object paramObject, Object[] paramArrayOfObject) throws IOException {
        paramIndentedWriter.println(paramObject);
        paramIndentedWriter.upIndent();
        byte b;
        int i;
        for (b = 0, i = paramArrayOfObject.length; b < i; b++)
            paramIndentedWriter.println(paramArrayOfObject[b]);
        paramIndentedWriter.downIndent();
    }

    public synchronized String getMultiLineStatusString() {
        return getMultiLineStatusString(0);
    }

    private String getMultiLineStatusString(int paramInt) {
        try {
            StringWriter stringWriter = new StringWriter(2048);
            IndentedWriter indentedWriter = new IndentedWriter(stringWriter);
            byte b;
            for (b = 0; b < paramInt; b++) {
                indentedWriter.upIndent();
            }
            if (this.managed == null) {

                indentedWriter.print("[");
                indentedWriter.print(this);
                indentedWriter.println(" closed.]");
            } else {

                HashSet hashSet = (HashSet) this.managed.clone();
                hashSet.removeAll(this.available);

                indentedWriter.print("Managed Threads: ");
                indentedWriter.println(this.managed.size());
                indentedWriter.print("Active Threads: ");
                indentedWriter.println(hashSet.size());
                indentedWriter.println("Active Tasks: ");
                indentedWriter.upIndent();
                for (PoolThread poolThread : hashSet) {

                    indentedWriter.println(poolThread.getCurrentTask());
                    indentedWriter.upIndent();
                    indentedWriter.print("on thread: ");
                    indentedWriter.println(poolThread.getName());
                    indentedWriter.downIndent();
                }
                indentedWriter.downIndent();
                indentedWriter.println("Pending Tasks: ");
                indentedWriter.upIndent();
                byte b1;
                int i;
                for (b1 = 0, i = this.pendingTasks.size(); b1 < i; b1++)
                    indentedWriter.println(this.pendingTasks.get(b1));
                indentedWriter.downIndent();
            }

            for (b = 0; b < paramInt; b++)
                indentedWriter.downIndent();
            indentedWriter.flush();
            String str = stringWriter.toString();
            indentedWriter.close();
            return str;
        } catch (IOException iOException) {

            if (logger.isLoggable(MLevel.WARNING))
                logger.log(MLevel.WARNING, "Huh? An IOException when working with a StringWriter?!?", iOException);
            throw new RuntimeException("Huh? An IOException when working with a StringWriter?!? " + iOException);
        }
    }

    private void appendStatusString(StringBuffer paramStringBuffer) {
        if (this.managed == null) {
            paramStringBuffer.append("[closed]");
        } else {

            HashSet hashSet = (HashSet) this.managed.clone();
            hashSet.removeAll(this.available);
            paramStringBuffer.append("[num_managed_threads: ");
            paramStringBuffer.append(this.managed.size());
            paramStringBuffer.append(", num_active: ");
            paramStringBuffer.append(hashSet.size());
            paramStringBuffer.append("; activeTasks: ");
            boolean bool = true;
            for (Iterator<PoolThread> iterator = hashSet.iterator(); iterator.hasNext(); ) {

                if (bool) {
                    bool = false;
                } else {
                    paramStringBuffer.append(", ");
                }
                PoolThread poolThread = iterator.next();
                paramStringBuffer.append(poolThread.getCurrentTask());
                paramStringBuffer.append(" (");
                paramStringBuffer.append(poolThread.getName());
                paramStringBuffer.append(')');
            }
            paramStringBuffer.append("; pendingTasks: ");
            byte b;
            int i;
            for (b = 0, i = this.pendingTasks.size(); b < i; b++) {

                if (b != 0) paramStringBuffer.append(", ");
                paramStringBuffer.append(this.pendingTasks.get(b));
            }
            paramStringBuffer.append(']');
        }
    }

    private void recreateThreadsAndTasks() {
        if (this.managed != null) {

            Date date = new Date();
            for (PoolThread poolThread : this.managed) {

                poolThread.gentleStop();
                this.stoppedThreadsToStopDates.put(poolThread, date);
                ensureReplacedThreadsProcessing();
            }
        }

        this.managed = new HashSet();
        this.available = new HashSet();
        this.pendingTasks = new LinkedList();
        for (byte b = 0; b < this.num_threads; b++) {

            PoolThread poolThread = new PoolThread(b, this.daemon);
            this.managed.add(poolThread);
            this.available.add(poolThread);
            poolThread.start();
        }
    }

    private void processReplacedThreads() {
        long l = System.currentTimeMillis();
        for (Iterator<PoolThread> iterator = this.stoppedThreadsToStopDates.keySet().iterator(); iterator.hasNext(); ) {

            PoolThread poolThread = iterator.next();
            if (!poolThread.isAlive()) {
                iterator.remove();
            } else {

                Date date = (Date) this.stoppedThreadsToStopDates.get(poolThread);
                if (l - date.getTime() > this.interrupt_delay_after_apparent_deadlock) {

                    if (logger.isLoggable(MLevel.WARNING)) {
                        logger.log(MLevel.WARNING, "Task " + poolThread.getCurrentTask() + " (in deadlocked PoolThread) failed to complete in maximum time " + this.interrupt_delay_after_apparent_deadlock + "ms. Trying interrupt().");
                    }

                    poolThread.interrupt();
                    iterator.remove();
                }
            }

            if (this.stoppedThreadsToStopDates.isEmpty()) {
                stopReplacedThreadsProcessing();
            }
        }
    }

    private void ensureReplacedThreadsProcessing() {
        if (this.replacedThreadInterruptor == null) {

            if (logger.isLoggable(MLevel.FINE)) {
                logger.fine("Apparently some threads have been replaced. Replacement thread processing enabled.");
            }
            this.replacedThreadInterruptor = new ReplacedThreadInterruptor();
            int i = this.interrupt_delay_after_apparent_deadlock / 4;
            this.myTimer.schedule(this.replacedThreadInterruptor, i, i);
        }
    }

    private void stopReplacedThreadsProcessing() {
        if (this.replacedThreadInterruptor != null) {

            this.replacedThreadInterruptor.cancel();
            this.replacedThreadInterruptor = null;

            if (logger.isLoggable(MLevel.FINE)) {
                logger.fine("Apparently all replaced threads have either completed their tasks or been interrupted(). Replacement thread processing cancelled.");
            }
        }
    }

    private void shuttingDown(PoolThread paramPoolThread) {
        if (this.managed != null && this.managed.contains(paramPoolThread)) {

            this.managed.remove(paramPoolThread);
            this.available.remove(paramPoolThread);
            PoolThread poolThread = new PoolThread(paramPoolThread.getIndex(), this.daemon);
            this.managed.add(poolThread);
            this.available.add(poolThread);
            poolThread.start();
        }
    }

    private void runInEmergencyThread(Runnable paramRunnable) {
        Thread thread = new Thread(paramRunnable);
        thread.start();
        if (this.max_individual_task_time > 0) {

            MaxIndividualTaskTimeEnforcer maxIndividualTaskTimeEnforcer = new MaxIndividualTaskTimeEnforcer(thread, thread + " [One-off emergency thread!!!]", paramRunnable.toString());
            this.myTimer.schedule(maxIndividualTaskTimeEnforcer, this.max_individual_task_time);
        }
    }

    class PoolThread
            extends Thread {
        Runnable currentTask;

        boolean should_stop;

        int index;

        TimerTask maxIndividualTaskTimeEnforcer = null;

        PoolThread(int param1Int, boolean param1Boolean) {
            setName(((ThreadPoolAsynchronousRunner.this.threadLabel == null) ? getClass().getName() : ThreadPoolAsynchronousRunner.this.threadLabel) + "-#" + param1Int);
            setDaemon(param1Boolean);
            this.index = param1Int;
        }

        public int getIndex() {
            return this.index;
        }

        void gentleStop() {
            this.should_stop = true;
        }

        Runnable getCurrentTask() {
            return this.currentTask;
        }

        private void setMaxIndividualTaskTimeEnforcer() {
            this.maxIndividualTaskTimeEnforcer = new ThreadPoolAsynchronousRunner.MaxIndividualTaskTimeEnforcer(this);
            ThreadPoolAsynchronousRunner.this.myTimer.schedule(this.maxIndividualTaskTimeEnforcer, ThreadPoolAsynchronousRunner.this.max_individual_task_time);
        }

        private void cancelMaxIndividualTaskTimeEnforcer() {
            this.maxIndividualTaskTimeEnforcer.cancel();
            this.maxIndividualTaskTimeEnforcer = null;
        }

        private void purgeTimer() {
            ThreadPoolAsynchronousRunner.this.myTimer.purge();
            if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINER)) {
                ThreadPoolAsynchronousRunner.logger.log(MLevel.FINER, getClass().getName() + " -- PURGING TIMER");
            }
        }

        public void run() {

        }
    }

    class DeadlockDetector
            extends TimerTask {
        LinkedList last = null;
        LinkedList current = null;

        public void run() {
            boolean bool = false;
            synchronized (ThreadPoolAsynchronousRunner.this) {

                if (ThreadPoolAsynchronousRunner.this.pendingTasks.size() == 0) {

                    this.last = null;
                    if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINEST)) {
                        ThreadPoolAsynchronousRunner.logger.log(MLevel.FINEST, this + " -- Running DeadlockDetector[Exiting. No pending tasks.]");
                    }
                    return;
                }
                this.current = (LinkedList) ThreadPoolAsynchronousRunner.this.pendingTasks.clone();
                if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINEST)) {
                    ThreadPoolAsynchronousRunner.logger.log(MLevel.FINEST, this + " -- Running DeadlockDetector[last->" + this.last + ",current->" + this.current + ']');
                }
                if (this.current.equals(this.last)) {

                    if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.WARNING)) {

                        ThreadPoolAsynchronousRunner.logger.warning(this + " -- APPARENT DEADLOCK!!! Creating emergency threads for unassigned pending tasks!");
                        StringWriter stringWriter = new StringWriter(4096);
                        PrintWriter printWriter = new PrintWriter(stringWriter);

                        printWriter.print(this);
                        printWriter.println(" -- APPARENT DEADLOCK!!! Complete Status: ");
                        printWriter.print(ThreadPoolAsynchronousRunner.this.getMultiLineStatusString(1));
                        printWriter.println("Pool thread stack traces:");
                        String str = ThreadPoolAsynchronousRunner.this.getStackTraces(1);
                        if (str == null) {
                            printWriter.println("\t[Stack traces of deadlocked task threads not available.]");
                        } else {
                            printWriter.print(str);
                        }
                        printWriter.flush();
                        ThreadPoolAsynchronousRunner.logger.warning(stringWriter.toString());
                        printWriter.close();
                    }
                    if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINEST)) {

                        StringWriter stringWriter = new StringWriter(4096);
                        PrintWriter printWriter = new PrintWriter(stringWriter);
                        printWriter.print(this);
                        printWriter.println(" -- APPARENT DEADLOCK extra info, full JVM thread dump: ");
                        String str = ThreadPoolAsynchronousRunner.this.getJvmStackTraces(1);
                        if (str == null) {
                            printWriter.println("\t[Full JVM thread dump not available.]");
                        } else {
                            printWriter.print(str);
                        }
                        printWriter.flush();
                        ThreadPoolAsynchronousRunner.logger.finest(stringWriter.toString());
                        printWriter.close();
                    }
                    ThreadPoolAsynchronousRunner.this.recreateThreadsAndTasks();
                    bool = true;
                }
            }
            if (bool) {

                ThreadPerTaskAsynchronousRunner threadPerTaskAsynchronousRunner = new ThreadPerTaskAsynchronousRunner(10, ThreadPoolAsynchronousRunner.this.max_individual_task_time);
                for (Iterator<Runnable> iterator = this.current.iterator(); iterator.hasNext(); )
                    threadPerTaskAsynchronousRunner.postRunnable(iterator.next());
                threadPerTaskAsynchronousRunner.close(false);
                this.last = null;
            } else {

                this.last = this.current;
            }

            this.current = null;
        }
    }

    class MaxIndividualTaskTimeEnforcer
            extends TimerTask {
        ThreadPoolAsynchronousRunner.PoolThread pt;
        Thread interruptMe;
        String threadStr;
        String fixedTaskStr;

        MaxIndividualTaskTimeEnforcer(ThreadPoolAsynchronousRunner.PoolThread param1PoolThread) {
            this.pt = param1PoolThread;
            this.interruptMe = param1PoolThread;
            this.threadStr = param1PoolThread.toString();
            this.fixedTaskStr = null;
        }

        MaxIndividualTaskTimeEnforcer(Thread param1Thread, String param1String1, String param1String2) {
            this.pt = null;
            this.interruptMe = param1Thread;
            this.threadStr = param1String1;
            this.fixedTaskStr = param1String2;
        }

        public void run() {
            String str;
            if (this.fixedTaskStr != null) {
                str = this.fixedTaskStr;
            } else if (this.pt != null) {

                synchronized (ThreadPoolAsynchronousRunner.this) {
                    str = String.valueOf(this.pt.getCurrentTask());
                }
            } else {
                str = "Unknown task?!";
            }
            if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.WARNING)) {
                ThreadPoolAsynchronousRunner.logger.warning("A task has exceeded the maximum allowable task time. Will interrupt() thread [" + this.threadStr + "], with current task: " + str);
            }

            this.interruptMe.interrupt();

            if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.WARNING)) {
                ThreadPoolAsynchronousRunner.logger.warning("Thread [" + this.threadStr + "] interrupted.");
            }
        }
    }

    class ReplacedThreadInterruptor
            extends TimerTask {
        public void run() {
            synchronized (ThreadPoolAsynchronousRunner.this) {
                ThreadPoolAsynchronousRunner.this.processReplacedThreads();
            }
        }
    }
}

