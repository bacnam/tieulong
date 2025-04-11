package javolution.osgi.internal;

import javolution.util.FastTable;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public final class LogServiceImpl
        extends Thread
        implements LogService {
    private final FastTable<LogEvent> eventQueue = new FastTable();

    public LogServiceImpl() {
        super("Logging-Thread");
        setDaemon(true);
        start();
        Thread hook = new Thread(new Runnable() {
            public void run() {
                synchronized (LogServiceImpl.this.eventQueue) {
                    while (true) {
                        try {
                            if (!LogServiceImpl.this.eventQueue.isEmpty()) {
                                LogServiceImpl.this.eventQueue.wait();
                                continue;
                            }
                        } catch (InterruptedException e) {
                        }
                        break;
                    }
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
    }

    public void log(int level, String message) {
        log(level, message, (Throwable) null);
    }

    public void log(int level, String message, Throwable exception) {
        LogEvent event = new LogEvent();
        event.level = level;
        event.message = message;
        event.exception = exception;
        synchronized (this.eventQueue) {
            this.eventQueue.addFirst(event);
            this.eventQueue.notify();
        }
    }

    public void log(ServiceReference sr, int level, String message) {
        throw new UnsupportedOperationException();
    }

    public void log(ServiceReference sr, int level, String message, Throwable exception) {
        throw new UnsupportedOperationException();
    }

    public void run() {
        while (true) {
            try {
                LogEvent event;
                synchronized (this.eventQueue) {
                    while (this.eventQueue.isEmpty())
                        this.eventQueue.wait();
                    event = (LogEvent) this.eventQueue.pollLast();
                    this.eventQueue.notify();
                }
                switch (event.level) {
                    case 4:
                        System.out.println("[DEBUG] " + event.message);
                        break;
                    case 3:
                        System.out.println("[INFO] " + event.message);
                        break;
                    case 2:
                        System.out.println("[WARNING] " + event.message);
                        break;
                    case 1:
                        System.out.println("[ERROR] " + event.message);
                        break;
                    default:
                        System.out.println("[UNKNOWN] " + event.message);
                        break;
                }
                if (event.exception != null) {
                    event.exception.printStackTrace(System.out);
                }
            } catch (InterruptedException error) {
                error.printStackTrace(System.err);
            }
        }
    }

    private static class LogEvent {
        Throwable exception;
        int level;
        String message;

        private LogEvent() {
        }
    }
}

