package com.zhonglian.server.logger.scribe;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ScribeTransfer
{
private String host;
private int port;
private String encoding;
private List<LogEntry> logEntries;
private Scribe.Client client;
private TFramedTransport transport;
private TSocket tSocket;

public String getHost() {
return this.host;
}

public void setHost(String host) {
this.host = host;
}

public int getPort() {
return this.port;
}

public void setPort(int port) {
this.port = port;
}

public String getEncoding() {
return this.encoding;
}

public void setEncoding(String encoding) {
this.encoding = encoding;
}

public void start() {
try {
synchronized (this) {
this.logEntries = new ArrayList<>(1);
this.tSocket = new TSocket(new Socket(this.host, this.port));
this.transport = new TFramedTransport((TTransport)this.tSocket);
TBinaryProtocol protocol = new TBinaryProtocol((TTransport)this.transport, false, false);
this.client = new Scribe.Client((TProtocol)protocol, (TProtocol)protocol);
} 
} catch (Exception e) {
System.err.println(String.format("Scribe connnect to %s :%d failed ", new Object[] { this.host, Integer.valueOf(this.port) }));
e.printStackTrace();
} 
}

public void append(String sFormatedLog) {
synchronized (this) {

try { if (this.tSocket == null) {
return;
}
if (!this.tSocket.isOpen()) {
System.out.println("Scribe connnect closed try again.");
start();
} 
if (sFormatedLog.isEmpty()) {
return;
}
String[] msg = sFormatedLog.split("\\|", 2);
LogEntry entry = new LogEntry(msg[0], msg[1]);
this.logEntries.add(entry); }

catch (TException e)
{ e.printStackTrace();
try {
System.out.println("TException Scribe connnect closed try again.");
start();
this.client.Log(this.logEntries);
} catch (Exception e1) {
e.printStackTrace();
}  }
finally
{ if (this.logEntries != null)
this.logEntries.clear();  }  if (this.logEntries != null) this.logEntries.clear();

} 
}

public void stop() {
if (this.transport != null)
this.transport.close(); 
}
}

