package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class LayoutBase<E>
        extends ContextAwareBase
        implements Layout<E> {
    protected boolean started;
    String fileHeader;
    String fileFooter;
    String presentationHeader;
    String presentationFooter;

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void start() {
        this.started = true;
    }

    public void stop() {
        this.started = false;
    }

    public boolean isStarted() {
        return this.started;
    }

    public String getFileHeader() {
        return this.fileHeader;
    }

    public void setFileHeader(String header) {
        this.fileHeader = header;
    }

    public String getPresentationHeader() {
        return this.presentationHeader;
    }

    public void setPresentationHeader(String header) {
        this.presentationHeader = header;
    }

    public String getPresentationFooter() {
        return this.presentationFooter;
    }

    public void setPresentationFooter(String footer) {
        this.presentationFooter = footer;
    }

    public String getFileFooter() {
        return this.fileFooter;
    }

    public void setFileFooter(String footer) {
        this.fileFooter = footer;
    }

    public String getContentType() {
        return "text/plain";
    }
}

