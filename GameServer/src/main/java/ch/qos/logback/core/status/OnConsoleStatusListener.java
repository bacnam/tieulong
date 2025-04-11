package ch.qos.logback.core.status;

import ch.qos.logback.core.Context;

import java.io.PrintStream;

public class OnConsoleStatusListener
        extends OnPrintStreamStatusListenerBase {
    public static void addNewInstanceToContext(Context context) {
        OnConsoleStatusListener onConsoleStatusListener = new OnConsoleStatusListener();
        onConsoleStatusListener.setContext(context);
        onConsoleStatusListener.start();
        context.getStatusManager().add(onConsoleStatusListener);
    }

    protected PrintStream getPrintStream() {
        return System.out;
    }
}

