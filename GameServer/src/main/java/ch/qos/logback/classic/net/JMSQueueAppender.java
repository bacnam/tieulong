package ch.qos.logback.classic.net;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.JMSAppenderBase;
import ch.qos.logback.core.spi.PreSerializationTransformer;

import javax.jms.*;
import javax.naming.Context;
import java.io.Serializable;

public class JMSQueueAppender
        extends JMSAppenderBase<ILoggingEvent> {
    static int SUCCESSIVE_FAILURE_LIMIT = 3;

    String queueBindingName;

    String qcfBindingName;
    QueueConnection queueConnection;
    QueueSession queueSession;
    QueueSender queueSender;
    int successiveFailureCount = 0;

    private PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();

    public String getQueueConnectionFactoryBindingName() {
        return this.qcfBindingName;
    }

    public void setQueueConnectionFactoryBindingName(String qcfBindingName) {
        this.qcfBindingName = qcfBindingName;
    }

    public String getQueueBindingName() {
        return this.queueBindingName;
    }

    public void setQueueBindingName(String queueBindingName) {
        this.queueBindingName = queueBindingName;
    }

    public void start() {
        try {
            Context jndi = buildJNDIContext();

            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) lookup(jndi, this.qcfBindingName);

            if (this.userName != null) {
                this.queueConnection = queueConnectionFactory.createQueueConnection(this.userName, this.password);
            } else {

                this.queueConnection = queueConnectionFactory.createQueueConnection();
            }

            this.queueSession = this.queueConnection.createQueueSession(false, 1);

            Queue queue = (Queue) lookup(jndi, this.queueBindingName);

            this.queueSender = this.queueSession.createSender(queue);

            this.queueConnection.start();

            jndi.close();
        } catch (Exception e) {
            addError("Error while activating options for appender named [" + this.name + "].", e);
        }

        if (this.queueConnection != null && this.queueSession != null && this.queueSender != null) {
            super.start();
        }
    }

    public synchronized void stop() {
        if (!this.started) {
            return;
        }

        this.started = false;

        try {
            if (this.queueSession != null) {
                this.queueSession.close();
            }
            if (this.queueConnection != null) {
                this.queueConnection.close();
            }
        } catch (Exception e) {
            addError("Error while closing JMSAppender [" + this.name + "].", e);
        }

        this.queueSender = null;
        this.queueSession = null;
        this.queueConnection = null;
    }

    public void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }

        try {
            ObjectMessage msg = this.queueSession.createObjectMessage();
            Serializable so = this.pst.transform(event);
            msg.setObject(so);
            this.queueSender.send((Message) msg);
            this.successiveFailureCount = 0;
        } catch (Exception e) {
            this.successiveFailureCount++;
            if (this.successiveFailureCount > SUCCESSIVE_FAILURE_LIMIT) {
                stop();
            }
            addError("Could not send message in JMSQueueAppender [" + this.name + "].", e);
        }
    }

    protected QueueConnection getQueueConnection() {
        return this.queueConnection;
    }

    protected QueueSession getQueueSession() {
        return this.queueSession;
    }

    protected QueueSender getQueueSender() {
        return this.queueSender;
    }
}

