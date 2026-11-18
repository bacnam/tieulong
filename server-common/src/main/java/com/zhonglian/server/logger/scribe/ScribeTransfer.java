package com.zhonglian.server.logger.scribe;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;

public class ScribeTransfer {
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
                this.transport = new TFramedTransport(this.tSocket);
                TBinaryProtocol protocol = new TBinaryProtocol(this.transport, false, false);
                this.client = new Scribe.Client(protocol, protocol);
            }
        } catch (Exception var4) {
            System.err.println(String.format("Scribe connnect to %s :%d failed ", this.host, this.port));
            var4.printStackTrace();
        }
    }

    public void append(String sFormatedLog) {
        synchronized (this) {
            try {
                if (this.tSocket == null) {
                    return;
                }

                if (!this.tSocket.isOpen()) {
                    System.out.println("Scribe connnect closed try again.");
                    this.start();
                }

                if (!sFormatedLog.isEmpty()) {
                    String[] msg = sFormatedLog.split("\\|", 2);
                    LogEntry entry = new LogEntry(msg[0], msg[1]);
                    this.logEntries.add(entry);
                    this.client.Log(this.logEntries);
                    return;
                }
            } catch (TException var11) {
                var11.printStackTrace();

                try {
                    System.out.println("TException Scribe connnect closed try again.");
                    this.start();
                    this.client.Log(this.logEntries);
                } catch (Exception var10) {
                    var11.printStackTrace();
                }

                return;
            } finally {
                if (this.logEntries != null) {
                    this.logEntries.clear();
                }
            }
        }
    }

    public void stop() {
        if (this.transport != null) {
            this.transport.close();
        }
    }
}
