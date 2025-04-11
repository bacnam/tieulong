package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.pattern.parser.Node;
import ch.qos.logback.core.pattern.parser.Parser;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;

import java.util.HashMap;
import java.util.Map;

public abstract class PatternLayoutBase<E>
        extends LayoutBase<E> {
    protected PostCompileProcessor<E> postCompileProcessor;
    protected boolean outputPatternAsHeader = false;
    Converter<E> head;
    String pattern;
    Map<String, String> instanceConverterMap = new HashMap<String, String>();

    public abstract Map<String, String> getDefaultConverterMap();

    public Map<String, String> getEffectiveConverterMap() {
        Map<String, String> effectiveMap = new HashMap<String, String>();

        Map<String, String> defaultMap = getDefaultConverterMap();
        if (defaultMap != null) {
            effectiveMap.putAll(defaultMap);
        }

        Context context = getContext();
        if (context != null) {

            Map<String, String> contextMap = (Map<String, String>) context.getObject("PATTERN_RULE_REGISTRY");

            if (contextMap != null) {
                effectiveMap.putAll(contextMap);
            }
        }

        effectiveMap.putAll(this.instanceConverterMap);
        return effectiveMap;
    }

    public void start() {
        if (this.pattern == null || this.pattern.length() == 0) {
            addError("Empty or null pattern.");
            return;
        }
        try {
            Parser<E> p = new Parser(this.pattern);
            if (getContext() != null) {
                p.setContext(getContext());
            }
            Node t = p.parse();
            this.head = p.compile(t, getEffectiveConverterMap());
            if (this.postCompileProcessor != null) {
                this.postCompileProcessor.process(this.head);
            }
            ConverterUtil.setContextForConverters(getContext(), this.head);
            ConverterUtil.startConverters(this.head);
            super.start();
        } catch (ScanException sce) {
            StatusManager sm = getContext().getStatusManager();
            sm.add((Status) new ErrorStatus("Failed to parse pattern \"" + getPattern() + "\".", this, (Throwable) sce));
        }
    }

    public void setPostCompileProcessor(PostCompileProcessor<E> postCompileProcessor) {
        this.postCompileProcessor = postCompileProcessor;
    }

    protected void setContextForConverters(Converter<E> head) {
        ConverterUtil.setContextForConverters(getContext(), head);
    }

    protected String writeLoopOnConverters(E event) {
        StringBuilder buf = new StringBuilder(128);
        Converter<E> c = this.head;
        while (c != null) {
            c.write(buf, event);
            c = c.getNext();
        }
        return buf.toString();
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String toString() {
        return getClass().getName() + "(\"" + getPattern() + "\")";
    }

    public Map<String, String> getInstanceConverterMap() {
        return this.instanceConverterMap;
    }

    protected String getPresentationHeaderPrefix() {
        return "";
    }

    public boolean isOutputPatternAsHeader() {
        return this.outputPatternAsHeader;
    }

    public void setOutputPatternAsHeader(boolean outputPatternAsHeader) {
        this.outputPatternAsHeader = outputPatternAsHeader;
    }

    public String getPresentationHeader() {
        if (this.outputPatternAsHeader) {
            return getPresentationHeaderPrefix() + this.pattern;
        }
        return super.getPresentationHeader();
    }
}

