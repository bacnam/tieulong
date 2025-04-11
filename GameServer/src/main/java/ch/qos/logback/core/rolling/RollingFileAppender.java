package ch.qos.logback.core.rolling;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.FileNamePattern;

import java.io.File;
import java.io.IOException;

public class RollingFileAppender<E>
        extends FileAppender<E> {
    File currentlyActiveFile;
    TriggeringPolicy<E> triggeringPolicy;
    RollingPolicy rollingPolicy;
    private static String RFA_NO_TP_URL = "http:";
    private static String RFA_NO_RP_URL = "http:";
    private static String COLLISION_URL = "http:";

    public void start() {
        if (this.triggeringPolicy == null) {
            addWarn("No TriggeringPolicy was set for the RollingFileAppender named " + getName());

            addWarn("For more information, please visit " + RFA_NO_TP_URL);

            return;
        }

        if (!this.append) {
            addWarn("Append mode is mandatory for RollingFileAppender");
            this.append = true;
        }

        if (this.rollingPolicy == null) {
            addError("No RollingPolicy was set for the RollingFileAppender named " + getName());

            addError("For more information, please visit " + RFA_NO_RP_URL);

            return;
        }

        if (fileAndPatternCollide()) {
            addError("File property collides with fileNamePattern. Aborting.");
            addError("For more information, please visit " + COLLISION_URL);

            return;
        }
        if (isPrudent()) {
            if (rawFileProperty() != null) {
                addWarn("Setting \"File\" property to null on account of prudent mode");
                setFile((String) null);
            }
            if (this.rollingPolicy.getCompressionMode() != CompressionMode.NONE) {
                addError("Compression is not supported in prudent mode. Aborting");

                return;
            }
        }
        this.currentlyActiveFile = new File(getFile());
        addInfo("Active log file name: " + getFile());
        super.start();
    }

    private boolean fileAndPatternCollide() {
        if (this.triggeringPolicy instanceof RollingPolicyBase) {
            RollingPolicyBase base = (RollingPolicyBase) this.triggeringPolicy;
            FileNamePattern fileNamePattern = base.fileNamePattern;

            if (fileNamePattern != null && this.fileName != null) {
                String regex = fileNamePattern.toRegex();
                return this.fileName.matches(regex);
            }
        }
        return false;
    }

    public void stop() {
        if (this.rollingPolicy != null) this.rollingPolicy.stop();
        if (this.triggeringPolicy != null) this.triggeringPolicy.stop();
        super.stop();
    }

    public void setFile(String file) {
        if (file != null && (this.triggeringPolicy != null || this.rollingPolicy != null)) {
            addError("File property must be set before any triggeringPolicy or rollingPolicy properties");
        }
        super.setFile(file);
    }

    public String getFile() {
        return this.rollingPolicy.getActiveFileName();
    }

    public void rollover() {
        this.lock.lock();

        try {
            closeOutputStream();
            attemptRollover();
            attemptOpenFile();
        } finally {
            this.lock.unlock();
        }
    }

    private void attemptOpenFile() {
        try {
            this.currentlyActiveFile = new File(this.rollingPolicy.getActiveFileName());

            openFile(this.rollingPolicy.getActiveFileName());
        } catch (IOException e) {
            addError("setFile(" + this.fileName + ", false) call failed.", e);
        }
    }

    private void attemptRollover() {
        try {
            this.rollingPolicy.rollover();
        } catch (RolloverFailure rf) {
            addWarn("RolloverFailure occurred. Deferring roll-over.");

            this.append = true;
        }
    }

    protected void subAppend(E event) {
        synchronized (this.triggeringPolicy) {
            if (this.triggeringPolicy.isTriggeringEvent(this.currentlyActiveFile, event)) {
                rollover();
            }
        }

        super.subAppend(event);
    }

    public RollingPolicy getRollingPolicy() {
        return this.rollingPolicy;
    }

    public TriggeringPolicy<E> getTriggeringPolicy() {
        return this.triggeringPolicy;
    }

    public void setRollingPolicy(RollingPolicy policy) {
        this.rollingPolicy = policy;
        if (this.rollingPolicy instanceof TriggeringPolicy) {
            this.triggeringPolicy = (TriggeringPolicy<E>) policy;
        }
    }

    public void setTriggeringPolicy(TriggeringPolicy<E> policy) {
        this.triggeringPolicy = policy;
        if (policy instanceof RollingPolicy)
            this.rollingPolicy = (RollingPolicy) policy;
    }
}

