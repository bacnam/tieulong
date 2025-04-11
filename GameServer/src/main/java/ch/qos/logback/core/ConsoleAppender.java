package ch.qos.logback.core;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.WarnStatus;
import ch.qos.logback.core.util.EnvUtil;
import ch.qos.logback.core.util.OptionHelper;

import java.io.OutputStream;
import java.util.Arrays;

public class ConsoleAppender<E>
        extends OutputStreamAppender<E> {
    private static final String WindowsAnsiOutputStream_CLASS_NAME = "org.fusesource.jansi.WindowsAnsiOutputStream";
    protected ConsoleTarget target = ConsoleTarget.SystemOut;
    protected boolean withJansi = false;

    public String getTarget() {
        return this.target.getName();
    }

    public void setTarget(String value) {
        ConsoleTarget t = ConsoleTarget.findByName(value.trim());
        if (t == null) {
            targetWarn(value);
        } else {
            this.target = t;
        }
    }

    private void targetWarn(String val) {
        WarnStatus warnStatus = new WarnStatus("[" + val + "] should be one of " + Arrays.toString((Object[]) ConsoleTarget.values()), this);

        warnStatus.add((Status) new WarnStatus("Using previously set target, System.out by default.", this));

        addStatus((Status) warnStatus);
    }

    public void start() {
        OutputStream targetStream = this.target.getStream();

        if (EnvUtil.isWindows() && this.withJansi) {
            targetStream = getTargetStreamForWindows(targetStream);
        }
        setOutputStream(targetStream);
        super.start();
    }

    private OutputStream getTargetStreamForWindows(OutputStream targetStream) {
        try {
            addInfo("Enabling JANSI WindowsAnsiOutputStream for the console.");
            Object windowsAnsiOutputStream = OptionHelper.instantiateByClassNameAndParameter("org.fusesource.jansi.WindowsAnsiOutputStream", Object.class, this.context, OutputStream.class, targetStream);

            return (OutputStream) windowsAnsiOutputStream;
        } catch (Exception e) {
            addWarn("Failed to create WindowsAnsiOutputStream. Falling back on the default stream.", e);

            return targetStream;
        }
    }

    public boolean isWithJansi() {
        return this.withJansi;
    }

    public void setWithJansi(boolean withJansi) {
        this.withJansi = withJansi;
    }
}

