package org.apache.mina.core.session;

import java.net.SocketAddress;
import java.util.Set;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

public interface IoSession {
  long getId();
  
  IoService getService();
  
  IoHandler getHandler();
  
  IoSessionConfig getConfig();
  
  IoFilterChain getFilterChain();
  
  WriteRequestQueue getWriteRequestQueue();
  
  TransportMetadata getTransportMetadata();
  
  ReadFuture read();
  
  WriteFuture write(Object paramObject);
  
  WriteFuture write(Object paramObject, SocketAddress paramSocketAddress);
  
  CloseFuture close(boolean paramBoolean);
  
  @Deprecated
  CloseFuture close();
  
  @Deprecated
  Object getAttachment();
  
  @Deprecated
  Object setAttachment(Object paramObject);
  
  Object getAttribute(Object paramObject);
  
  Object getAttribute(Object paramObject1, Object paramObject2);
  
  Object setAttribute(Object paramObject1, Object paramObject2);
  
  Object setAttribute(Object paramObject);
  
  Object setAttributeIfAbsent(Object paramObject1, Object paramObject2);
  
  Object setAttributeIfAbsent(Object paramObject);
  
  Object removeAttribute(Object paramObject);
  
  boolean removeAttribute(Object paramObject1, Object paramObject2);
  
  boolean replaceAttribute(Object paramObject1, Object paramObject2, Object paramObject3);
  
  boolean containsAttribute(Object paramObject);
  
  Set<Object> getAttributeKeys();
  
  boolean isConnected();
  
  boolean isClosing();
  
  boolean isSecured();
  
  CloseFuture getCloseFuture();
  
  SocketAddress getRemoteAddress();
  
  SocketAddress getLocalAddress();
  
  SocketAddress getServiceAddress();
  
  void setCurrentWriteRequest(WriteRequest paramWriteRequest);
  
  void suspendRead();
  
  void suspendWrite();
  
  void resumeRead();
  
  void resumeWrite();
  
  boolean isReadSuspended();
  
  boolean isWriteSuspended();
  
  void updateThroughput(long paramLong, boolean paramBoolean);
  
  long getReadBytes();
  
  long getWrittenBytes();
  
  long getReadMessages();
  
  long getWrittenMessages();
  
  double getReadBytesThroughput();
  
  double getWrittenBytesThroughput();
  
  double getReadMessagesThroughput();
  
  double getWrittenMessagesThroughput();
  
  int getScheduledWriteMessages();
  
  long getScheduledWriteBytes();
  
  Object getCurrentWriteMessage();
  
  WriteRequest getCurrentWriteRequest();
  
  long getCreationTime();
  
  long getLastIoTime();
  
  long getLastReadTime();
  
  long getLastWriteTime();
  
  boolean isIdle(IdleStatus paramIdleStatus);
  
  boolean isReaderIdle();
  
  boolean isWriterIdle();
  
  boolean isBothIdle();
  
  int getIdleCount(IdleStatus paramIdleStatus);
  
  int getReaderIdleCount();
  
  int getWriterIdleCount();
  
  int getBothIdleCount();
  
  long getLastIdleTime(IdleStatus paramIdleStatus);
  
  long getLastReaderIdleTime();
  
  long getLastWriterIdleTime();
  
  long getLastBothIdleTime();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IoSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */