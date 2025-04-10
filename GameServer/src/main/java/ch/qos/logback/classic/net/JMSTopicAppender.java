package ch.qos.logback.classic.net;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.JMSAppenderBase;
import ch.qos.logback.core.spi.PreSerializationTransformer;
import java.io.Serializable;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;

public class JMSTopicAppender
extends JMSAppenderBase<ILoggingEvent>
{
static int SUCCESSIVE_FAILURE_LIMIT = 3;

String topicBindingName;

String tcfBindingName;
TopicConnection topicConnection;
TopicSession topicSession;
TopicPublisher topicPublisher;
int successiveFailureCount = 0;

private PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();

public void setTopicConnectionFactoryBindingName(String tcfBindingName) {
this.tcfBindingName = tcfBindingName;
}

public String getTopicConnectionFactoryBindingName() {
return this.tcfBindingName;
}

public void setTopicBindingName(String topicBindingName) {
this.topicBindingName = topicBindingName;
}

public String getTopicBindingName() {
return this.topicBindingName;
}

public void start() {
try {
Context jndi = buildJNDIContext();

TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(jndi, this.tcfBindingName);

if (this.userName != null) {
this.topicConnection = topicConnectionFactory.createTopicConnection(this.userName, this.password);
} else {

this.topicConnection = topicConnectionFactory.createTopicConnection();
} 

this.topicSession = this.topicConnection.createTopicSession(false, 1);

Topic topic = (Topic)lookup(jndi, this.topicBindingName);

this.topicPublisher = this.topicSession.createPublisher(topic);

this.topicConnection.start();

jndi.close();
} catch (Exception e) {
addError("Error while activating options for appender named [" + this.name + "].", e);
} 

if (this.topicConnection != null && this.topicSession != null && this.topicPublisher != null)
{
super.start();
}
}

public synchronized void stop() {
if (!this.started) {
return;
}

this.started = false;

try {
if (this.topicSession != null) {
this.topicSession.close();
}
if (this.topicConnection != null) {
this.topicConnection.close();
}
} catch (Exception e) {
addError("Error while closing JMSAppender [" + this.name + "].", e);
} 

this.topicPublisher = null;
this.topicSession = null;
this.topicConnection = null;
}

public void append(ILoggingEvent event) {
if (!isStarted()) {
return;
}

try {
ObjectMessage msg = this.topicSession.createObjectMessage();
Serializable so = this.pst.transform(event);
msg.setObject(so);
this.topicPublisher.publish((Message)msg);
this.successiveFailureCount = 0;
} catch (Exception e) {
this.successiveFailureCount++;
if (this.successiveFailureCount > SUCCESSIVE_FAILURE_LIMIT) {
stop();
}
addError("Could not publish message in JMSTopicAppender [" + this.name + "].", e);
} 
}

protected TopicConnection getTopicConnection() {
return this.topicConnection;
}

protected TopicSession getTopicSession() {
return this.topicSession;
}

protected TopicPublisher getTopicPublisher() {
return this.topicPublisher;
}
}

