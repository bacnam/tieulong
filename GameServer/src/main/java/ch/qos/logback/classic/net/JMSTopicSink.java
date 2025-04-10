package ch.qos.logback.classic.net;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.ContextInitializer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.slf4j.LoggerFactory;

public class JMSTopicSink
implements MessageListener
{
private Logger logger = (Logger)LoggerFactory.getLogger(JMSTopicSink.class);

public static void main(String[] args) throws Exception {
if (args.length < 2) {
usage("Wrong number of arguments.");
}

String tcfBindingName = args[0];
String topicBindingName = args[1];
String username = null;
String password = null;
if (args.length == 4) {
username = args[2];
password = args[3];
} 

LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();

(new ContextInitializer(loggerContext)).autoConfig();

new JMSTopicSink(tcfBindingName, topicBindingName, username, password);

BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

System.out.println("Type \"exit\" to quit JMSTopicSink.");
while (true) {
String s = stdin.readLine();
if (s.equalsIgnoreCase("exit")) {
System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
return;
} 
} 
}

public JMSTopicSink(String tcfBindingName, String topicBindingName, String username, String password) {
try {
Properties env = new Properties();
env.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
env.put("java.naming.provider.url", "tcp:
Context ctx = new InitialContext(env);

TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(ctx, tcfBindingName);

System.out.println("Topic Cnx Factory found");
Topic topic = (Topic)ctx.lookup(topicBindingName);
System.out.println("Topic found: " + topic.getTopicName());

TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);

System.out.println("Topic Connection created");

TopicSession topicSession = topicConnection.createTopicSession(false, 1);

TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);

topicSubscriber.setMessageListener(this);

topicConnection.start();
System.out.println("Topic Connection started");
}
catch (Exception e) {
this.logger.error("Could not read JMS message.", e);
} 
}

public void onMessage(Message message) {
try {
if (message instanceof ObjectMessage) {
ObjectMessage objectMessage = (ObjectMessage)message;
ILoggingEvent event = (ILoggingEvent)objectMessage.getObject();
Logger log = (Logger)LoggerFactory.getLogger(event.getLoggerName());
log.callAppenders(event);
} else {
this.logger.warn("Received message is of type " + message.getJMSType() + ", was expecting ObjectMessage.");
}

} catch (JMSException jmse) {
this.logger.error("Exception thrown while processing incoming message.", (Throwable)jmse);
} 
}

protected Object lookup(Context ctx, String name) throws NamingException {
try {
return ctx.lookup(name);
} catch (NameNotFoundException e) {
this.logger.error("Could not find name [" + name + "].");
throw e;
} 
}

static void usage(String msg) {
System.err.println(msg);
System.err.println("Usage: java " + JMSTopicSink.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName Username Password");

System.exit(1);
}
}

