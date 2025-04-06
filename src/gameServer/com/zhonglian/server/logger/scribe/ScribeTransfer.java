/*    */ package com.zhonglian.server.logger.scribe;
/*    */ 
/*    */ import java.net.Socket;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.thrift.TException;
/*    */ import org.apache.thrift.protocol.TBinaryProtocol;
/*    */ import org.apache.thrift.protocol.TProtocol;
/*    */ import org.apache.thrift.transport.TFramedTransport;
/*    */ import org.apache.thrift.transport.TSocket;
/*    */ import org.apache.thrift.transport.TTransport;
/*    */ 
/*    */ 
/*    */ public class ScribeTransfer
/*    */ {
/*    */   private String host;
/*    */   private int port;
/*    */   private String encoding;
/*    */   private List<LogEntry> logEntries;
/*    */   private Scribe.Client client;
/*    */   private TFramedTransport transport;
/*    */   private TSocket tSocket;
/*    */   
/*    */   public String getHost() {
/* 25 */     return this.host;
/*    */   }
/*    */   
/*    */   public void setHost(String host) {
/* 29 */     this.host = host;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 33 */     return this.port;
/*    */   }
/*    */   
/*    */   public void setPort(int port) {
/* 37 */     this.port = port;
/*    */   }
/*    */   
/*    */   public String getEncoding() {
/* 41 */     return this.encoding;
/*    */   }
/*    */   
/*    */   public void setEncoding(String encoding) {
/* 45 */     this.encoding = encoding;
/*    */   }
/*    */   
/*    */   public void start() {
/*    */     try {
/* 50 */       synchronized (this) {
/* 51 */         this.logEntries = new ArrayList<>(1);
/* 52 */         this.tSocket = new TSocket(new Socket(this.host, this.port));
/* 53 */         this.transport = new TFramedTransport((TTransport)this.tSocket);
/* 54 */         TBinaryProtocol protocol = new TBinaryProtocol((TTransport)this.transport, false, false);
/* 55 */         this.client = new Scribe.Client((TProtocol)protocol, (TProtocol)protocol);
/*    */       } 
/* 57 */     } catch (Exception e) {
/* 58 */       System.err.println(String.format("Scribe connnect to %s :%d failed ", new Object[] { this.host, Integer.valueOf(this.port) }));
/* 59 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void append(String sFormatedLog) {
/* 64 */     synchronized (this) {
/*    */       
/* 66 */       try { if (this.tSocket == null) {
/*    */           return;
/*    */         }
/* 69 */         if (!this.tSocket.isOpen()) {
/* 70 */           System.out.println("Scribe connnect closed try again.");
/* 71 */           start();
/*    */         } 
/* 73 */         if (sFormatedLog.isEmpty()) {
/*    */           return;
/*    */         }
/* 76 */         String[] msg = sFormatedLog.split("\\|", 2);
/* 77 */         LogEntry entry = new LogEntry(msg[0], msg[1]);
/* 78 */         this.logEntries.add(entry); }
/*    */       
/* 80 */       catch (TException e)
/* 81 */       { e.printStackTrace();
/*    */         try {
/* 83 */           System.out.println("TException Scribe connnect closed try again.");
/* 84 */           start();
/* 85 */           this.client.Log(this.logEntries);
/* 86 */         } catch (Exception e1) {
/* 87 */           e.printStackTrace();
/*    */         }  }
/*    */       finally
/* 90 */       { if (this.logEntries != null)
/* 91 */           this.logEntries.clear();  }  if (this.logEntries != null) this.logEntries.clear();
/*    */     
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 98 */     if (this.transport != null)
/* 99 */       this.transport.close(); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/scribe/ScribeTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */