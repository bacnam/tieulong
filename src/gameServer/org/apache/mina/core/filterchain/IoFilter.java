package org.apache.mina.core.filterchain;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public interface IoFilter {
  void init() throws Exception;
  
  void destroy() throws Exception;
  
  void onPreAdd(IoFilterChain paramIoFilterChain, String paramString, NextFilter paramNextFilter) throws Exception;
  
  void onPostAdd(IoFilterChain paramIoFilterChain, String paramString, NextFilter paramNextFilter) throws Exception;
  
  void onPreRemove(IoFilterChain paramIoFilterChain, String paramString, NextFilter paramNextFilter) throws Exception;
  
  void onPostRemove(IoFilterChain paramIoFilterChain, String paramString, NextFilter paramNextFilter) throws Exception;
  
  void sessionCreated(NextFilter paramNextFilter, IoSession paramIoSession) throws Exception;
  
  void sessionOpened(NextFilter paramNextFilter, IoSession paramIoSession) throws Exception;
  
  void sessionClosed(NextFilter paramNextFilter, IoSession paramIoSession) throws Exception;
  
  void sessionIdle(NextFilter paramNextFilter, IoSession paramIoSession, IdleStatus paramIdleStatus) throws Exception;
  
  void exceptionCaught(NextFilter paramNextFilter, IoSession paramIoSession, Throwable paramThrowable) throws Exception;
  
  void inputClosed(NextFilter paramNextFilter, IoSession paramIoSession) throws Exception;
  
  void messageReceived(NextFilter paramNextFilter, IoSession paramIoSession, Object paramObject) throws Exception;
  
  void messageSent(NextFilter paramNextFilter, IoSession paramIoSession, WriteRequest paramWriteRequest) throws Exception;
  
  void filterClose(NextFilter paramNextFilter, IoSession paramIoSession) throws Exception;
  
  void filterWrite(NextFilter paramNextFilter, IoSession paramIoSession, WriteRequest paramWriteRequest) throws Exception;
  
  public static interface NextFilter {
    void sessionCreated(IoSession param1IoSession);
    
    void sessionOpened(IoSession param1IoSession);
    
    void sessionClosed(IoSession param1IoSession);
    
    void sessionIdle(IoSession param1IoSession, IdleStatus param1IdleStatus);
    
    void exceptionCaught(IoSession param1IoSession, Throwable param1Throwable);
    
    void inputClosed(IoSession param1IoSession);
    
    void messageReceived(IoSession param1IoSession, Object param1Object);
    
    void messageSent(IoSession param1IoSession, WriteRequest param1WriteRequest);
    
    void filterWrite(IoSession param1IoSession, WriteRequest param1WriteRequest);
    
    void filterClose(IoSession param1IoSession);
  }
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/filterchain/IoFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */