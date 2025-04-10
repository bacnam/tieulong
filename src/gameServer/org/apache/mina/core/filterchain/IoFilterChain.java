package org.apache.mina.core.filterchain;

import java.util.List;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public interface IoFilterChain {
  IoSession getSession();

  Entry getEntry(String paramString);

  Entry getEntry(IoFilter paramIoFilter);

  Entry getEntry(Class<? extends IoFilter> paramClass);

  IoFilter get(String paramString);

  IoFilter get(Class<? extends IoFilter> paramClass);

  IoFilter.NextFilter getNextFilter(String paramString);

  IoFilter.NextFilter getNextFilter(IoFilter paramIoFilter);

  IoFilter.NextFilter getNextFilter(Class<? extends IoFilter> paramClass);

  List<Entry> getAll();

  List<Entry> getAllReversed();

  boolean contains(String paramString);

  boolean contains(IoFilter paramIoFilter);

  boolean contains(Class<? extends IoFilter> paramClass);

  void addFirst(String paramString, IoFilter paramIoFilter);

  void addLast(String paramString, IoFilter paramIoFilter);

  void addBefore(String paramString1, String paramString2, IoFilter paramIoFilter);

  void addAfter(String paramString1, String paramString2, IoFilter paramIoFilter);

  IoFilter replace(String paramString, IoFilter paramIoFilter);

  void replace(IoFilter paramIoFilter1, IoFilter paramIoFilter2);

  IoFilter replace(Class<? extends IoFilter> paramClass, IoFilter paramIoFilter);

  IoFilter remove(String paramString);

  void remove(IoFilter paramIoFilter);

  IoFilter remove(Class<? extends IoFilter> paramClass);

  void clear() throws Exception;

  void fireSessionCreated();

  void fireSessionOpened();

  void fireSessionClosed();

  void fireSessionIdle(IdleStatus paramIdleStatus);

  void fireMessageReceived(Object paramObject);

  void fireMessageSent(WriteRequest paramWriteRequest);

  void fireExceptionCaught(Throwable paramThrowable);

  void fireInputClosed();

  void fireFilterWrite(WriteRequest paramWriteRequest);

  void fireFilterClose();

  public static interface Entry {
    String getName();

    IoFilter getFilter();

    IoFilter.NextFilter getNextFilter();

    void addBefore(String param1String, IoFilter param1IoFilter);

    void addAfter(String param1String, IoFilter param1IoFilter);

    void replace(IoFilter param1IoFilter);

    void remove();
  }
}

