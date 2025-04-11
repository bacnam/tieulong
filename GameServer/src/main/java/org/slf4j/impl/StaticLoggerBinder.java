package org.slf4j.impl;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.ILoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder
        implements LoggerFactoryBinder {
    static final String NULL_CS_URL = "http:
    private static StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    public static String REQUESTED_API_VERSION = "1.6";
    private static Object KEY = new Object();

    static {
        SINGLETON.init();
    }

    private final ContextSelectorStaticBinder contextSelectorBinder = ContextSelectorStaticBinder.getSingleton();
    private boolean initialized = false;
    private LoggerContext defaultLoggerContext = new LoggerContext();

    private StaticLoggerBinder() {
        this.defaultLoggerContext.setName("default");
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    static void reset() {
        SINGLETON = new StaticLoggerBinder();
        SINGLETON.init();
    }

    void init() {
        try {
            try {
                (new ContextInitializer(this.defaultLoggerContext)).autoConfig();
            } catch (JoranException je) {
                Util.report("Failed to auto configure default logger context", (Throwable) je);
            }

            if (!StatusUtil.contextHasStatusListener((Context) this.defaultLoggerContext)) {
                StatusPrinter.printInCaseOfErrorsOrWarnings((Context) this.defaultLoggerContext);
            }
            this.contextSelectorBinder.init(this.defaultLoggerContext, KEY);
            this.initialized = true;
        } catch (Throwable t) {

            Util.report("Failed to instantiate [" + LoggerContext.class.getName() + "]", t);
        }
    }

    public ILoggerFactory getLoggerFactory() {
        if (!this.initialized) {
            return (ILoggerFactory) this.defaultLoggerContext;
        }

        if (this.contextSelectorBinder.getContextSelector() == null) {
            throw new IllegalStateException("contextSelector cannot be null. See also http:
        }

        return (ILoggerFactory) this.contextSelectorBinder.getContextSelector().getLoggerContext();
    }

    public String getLoggerFactoryClassStr() {
        return this.contextSelectorBinder.getClass().getName();
    }
}

