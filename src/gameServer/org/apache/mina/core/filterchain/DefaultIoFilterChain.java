/*      */ package org.apache.mina.core.filterchain;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.apache.mina.core.buffer.IoBuffer;
/*      */ import org.apache.mina.core.future.ConnectFuture;
/*      */ import org.apache.mina.core.service.AbstractIoService;
/*      */ import org.apache.mina.core.session.AbstractIoSession;
/*      */ import org.apache.mina.core.session.AttributeKey;
/*      */ import org.apache.mina.core.session.IdleStatus;
/*      */ import org.apache.mina.core.session.IoSession;
/*      */ import org.apache.mina.core.write.WriteRequest;
/*      */ import org.apache.mina.core.write.WriteRequestQueue;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DefaultIoFilterChain
/*      */   implements IoFilterChain
/*      */ {
/*   55 */   public static final AttributeKey SESSION_CREATED_FUTURE = new AttributeKey(DefaultIoFilterChain.class, "connectFuture");
/*      */ 
/*      */ 
/*      */   
/*      */   private final AbstractIoSession session;
/*      */ 
/*      */   
/*   62 */   private final Map<String, IoFilterChain.Entry> name2entry = new ConcurrentHashMap<String, IoFilterChain.Entry>();
/*      */ 
/*      */   
/*      */   private final EntryImpl head;
/*      */ 
/*      */   
/*      */   private final EntryImpl tail;
/*      */ 
/*      */   
/*   71 */   private static final Logger LOGGER = LoggerFactory.getLogger(DefaultIoFilterChain.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DefaultIoFilterChain(AbstractIoSession session) {
/*   80 */     if (session == null) {
/*   81 */       throw new IllegalArgumentException("session");
/*      */     }
/*      */     
/*   84 */     this.session = session;
/*   85 */     this.head = new EntryImpl(null, null, "head", new HeadFilter());
/*   86 */     this.tail = new EntryImpl(this.head, null, "tail", new TailFilter());
/*   87 */     this.head.nextEntry = this.tail;
/*      */   }
/*      */   
/*      */   public IoSession getSession() {
/*   91 */     return (IoSession)this.session;
/*      */   }
/*      */   
/*      */   public IoFilterChain.Entry getEntry(String name) {
/*   95 */     IoFilterChain.Entry e = this.name2entry.get(name);
/*      */     
/*   97 */     if (e == null) {
/*   98 */       return null;
/*      */     }
/*      */     
/*  101 */     return e;
/*      */   }
/*      */   
/*      */   public IoFilterChain.Entry getEntry(IoFilter filter) {
/*  105 */     EntryImpl e = this.head.nextEntry;
/*      */     
/*  107 */     while (e != this.tail) {
/*  108 */       if (e.getFilter() == filter) {
/*  109 */         return e;
/*      */       }
/*      */       
/*  112 */       e = e.nextEntry;
/*      */     } 
/*      */     
/*  115 */     return null;
/*      */   }
/*      */   
/*      */   public IoFilterChain.Entry getEntry(Class<? extends IoFilter> filterType) {
/*  119 */     EntryImpl e = this.head.nextEntry;
/*      */     
/*  121 */     while (e != this.tail) {
/*  122 */       if (filterType.isAssignableFrom(e.getFilter().getClass())) {
/*  123 */         return e;
/*      */       }
/*      */       
/*  126 */       e = e.nextEntry;
/*      */     } 
/*      */     
/*  129 */     return null;
/*      */   }
/*      */   
/*      */   public IoFilter get(String name) {
/*  133 */     IoFilterChain.Entry e = getEntry(name);
/*      */     
/*  135 */     if (e == null) {
/*  136 */       return null;
/*      */     }
/*      */     
/*  139 */     return e.getFilter();
/*      */   }
/*      */   
/*      */   public IoFilter get(Class<? extends IoFilter> filterType) {
/*  143 */     IoFilterChain.Entry e = getEntry(filterType);
/*      */     
/*  145 */     if (e == null) {
/*  146 */       return null;
/*      */     }
/*      */     
/*  149 */     return e.getFilter();
/*      */   }
/*      */   
/*      */   public IoFilter.NextFilter getNextFilter(String name) {
/*  153 */     IoFilterChain.Entry e = getEntry(name);
/*      */     
/*  155 */     if (e == null) {
/*  156 */       return null;
/*      */     }
/*      */     
/*  159 */     return e.getNextFilter();
/*      */   }
/*      */   
/*      */   public IoFilter.NextFilter getNextFilter(IoFilter filter) {
/*  163 */     IoFilterChain.Entry e = getEntry(filter);
/*      */     
/*  165 */     if (e == null) {
/*  166 */       return null;
/*      */     }
/*      */     
/*  169 */     return e.getNextFilter();
/*      */   }
/*      */   
/*      */   public IoFilter.NextFilter getNextFilter(Class<? extends IoFilter> filterType) {
/*  173 */     IoFilterChain.Entry e = getEntry(filterType);
/*      */     
/*  175 */     if (e == null) {
/*  176 */       return null;
/*      */     }
/*      */     
/*  179 */     return e.getNextFilter();
/*      */   }
/*      */   
/*      */   public synchronized void addFirst(String name, IoFilter filter) {
/*  183 */     checkAddable(name);
/*  184 */     register(this.head, name, filter);
/*      */   }
/*      */   
/*      */   public synchronized void addLast(String name, IoFilter filter) {
/*  188 */     checkAddable(name);
/*  189 */     register(this.tail.prevEntry, name, filter);
/*      */   }
/*      */   
/*      */   public synchronized void addBefore(String baseName, String name, IoFilter filter) {
/*  193 */     EntryImpl baseEntry = checkOldName(baseName);
/*  194 */     checkAddable(name);
/*  195 */     register(baseEntry.prevEntry, name, filter);
/*      */   }
/*      */   
/*      */   public synchronized void addAfter(String baseName, String name, IoFilter filter) {
/*  199 */     EntryImpl baseEntry = checkOldName(baseName);
/*  200 */     checkAddable(name);
/*  201 */     register(baseEntry, name, filter);
/*      */   }
/*      */   
/*      */   public synchronized IoFilter remove(String name) {
/*  205 */     EntryImpl entry = checkOldName(name);
/*  206 */     deregister(entry);
/*  207 */     return entry.getFilter();
/*      */   }
/*      */   
/*      */   public synchronized void remove(IoFilter filter) {
/*  211 */     EntryImpl e = this.head.nextEntry;
/*      */     
/*  213 */     while (e != this.tail) {
/*  214 */       if (e.getFilter() == filter) {
/*  215 */         deregister(e);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  220 */       e = e.nextEntry;
/*      */     } 
/*      */     
/*  223 */     throw new IllegalArgumentException("Filter not found: " + filter.getClass().getName());
/*      */   }
/*      */   
/*      */   public synchronized IoFilter remove(Class<? extends IoFilter> filterType) {
/*  227 */     EntryImpl e = this.head.nextEntry;
/*      */     
/*  229 */     while (e != this.tail) {
/*  230 */       if (filterType.isAssignableFrom(e.getFilter().getClass())) {
/*  231 */         IoFilter oldFilter = e.getFilter();
/*  232 */         deregister(e);
/*      */         
/*  234 */         return oldFilter;
/*      */       } 
/*      */       
/*  237 */       e = e.nextEntry;
/*      */     } 
/*      */     
/*  240 */     throw new IllegalArgumentException("Filter not found: " + filterType.getName());
/*      */   }
/*      */   
/*      */   public synchronized IoFilter replace(String name, IoFilter newFilter) {
/*  244 */     EntryImpl entry = checkOldName(name);
/*  245 */     IoFilter oldFilter = entry.getFilter();
/*      */ 
/*      */     
/*      */     try {
/*  249 */       newFilter.onPreAdd(this, name, entry.getNextFilter());
/*  250 */     } catch (Exception e) {
/*  251 */       throw new IoFilterLifeCycleException("onPreAdd(): " + name + ':' + newFilter + " in " + getSession(), e);
/*      */     } 
/*      */ 
/*      */     
/*  255 */     entry.setFilter(newFilter);
/*      */ 
/*      */     
/*      */     try {
/*  259 */       newFilter.onPostAdd(this, name, entry.getNextFilter());
/*  260 */     } catch (Exception e) {
/*  261 */       entry.setFilter(oldFilter);
/*  262 */       throw new IoFilterLifeCycleException("onPostAdd(): " + name + ':' + newFilter + " in " + getSession(), e);
/*      */     } 
/*      */     
/*  265 */     return oldFilter;
/*      */   }
/*      */   
/*      */   public synchronized void replace(IoFilter oldFilter, IoFilter newFilter) {
/*  269 */     EntryImpl entry = this.head.nextEntry;
/*      */ 
/*      */     
/*  272 */     while (entry != this.tail) {
/*  273 */       if (entry.getFilter() == oldFilter) {
/*  274 */         String oldFilterName = null;
/*      */ 
/*      */         
/*  277 */         for (String name : this.name2entry.keySet()) {
/*  278 */           if (entry == this.name2entry.get(name)) {
/*  279 */             oldFilterName = name;
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/*      */         try {
/*  287 */           newFilter.onPreAdd(this, oldFilterName, entry.getNextFilter());
/*  288 */         } catch (Exception e) {
/*  289 */           throw new IoFilterLifeCycleException("onPreAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  294 */         entry.setFilter(newFilter);
/*      */ 
/*      */         
/*      */         try {
/*  298 */           newFilter.onPostAdd(this, oldFilterName, entry.getNextFilter());
/*  299 */         } catch (Exception e) {
/*  300 */           entry.setFilter(oldFilter);
/*  301 */           throw new IoFilterLifeCycleException("onPostAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
/*      */         } 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  308 */       entry = entry.nextEntry;
/*      */     } 
/*      */     
/*  311 */     throw new IllegalArgumentException("Filter not found: " + oldFilter.getClass().getName());
/*      */   }
/*      */   
/*      */   public synchronized IoFilter replace(Class<? extends IoFilter> oldFilterType, IoFilter newFilter) {
/*  315 */     EntryImpl entry = this.head.nextEntry;
/*      */     
/*  317 */     while (entry != this.tail) {
/*  318 */       if (oldFilterType.isAssignableFrom(entry.getFilter().getClass())) {
/*  319 */         IoFilter oldFilter = entry.getFilter();
/*      */         
/*  321 */         String oldFilterName = null;
/*      */ 
/*      */         
/*  324 */         for (String name : this.name2entry.keySet()) {
/*  325 */           if (entry == this.name2entry.get(name)) {
/*  326 */             oldFilterName = name;
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/*      */         try {
/*  334 */           newFilter.onPreAdd(this, oldFilterName, entry.getNextFilter());
/*  335 */         } catch (Exception e) {
/*  336 */           throw new IoFilterLifeCycleException("onPreAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
/*      */         } 
/*      */ 
/*      */         
/*  340 */         entry.setFilter(newFilter);
/*      */ 
/*      */         
/*      */         try {
/*  344 */           newFilter.onPostAdd(this, oldFilterName, entry.getNextFilter());
/*  345 */         } catch (Exception e) {
/*  346 */           entry.setFilter(oldFilter);
/*  347 */           throw new IoFilterLifeCycleException("onPostAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
/*      */         } 
/*      */ 
/*      */         
/*  351 */         return oldFilter;
/*      */       } 
/*      */       
/*  354 */       entry = entry.nextEntry;
/*      */     } 
/*      */     
/*  357 */     throw new IllegalArgumentException("Filter not found: " + oldFilterType.getName());
/*      */   }
/*      */   
/*      */   public synchronized void clear() throws Exception {
/*  361 */     List<IoFilterChain.Entry> l = new ArrayList<IoFilterChain.Entry>(this.name2entry.values());
/*      */     
/*  363 */     for (IoFilterChain.Entry entry : l) {
/*      */       try {
/*  365 */         deregister((EntryImpl)entry);
/*  366 */       } catch (Exception e) {
/*  367 */         throw new IoFilterLifeCycleException("clear(): " + entry.getName() + " in " + getSession(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void register(EntryImpl prevEntry, String name, IoFilter filter) {
/*  378 */     EntryImpl newEntry = new EntryImpl(prevEntry, prevEntry.nextEntry, name, filter);
/*      */     
/*      */     try {
/*  381 */       filter.onPreAdd(this, name, newEntry.getNextFilter());
/*  382 */     } catch (Exception e) {
/*  383 */       throw new IoFilterLifeCycleException("onPreAdd(): " + name + ':' + filter + " in " + getSession(), e);
/*      */     } 
/*      */     
/*  386 */     prevEntry.nextEntry.prevEntry = newEntry;
/*  387 */     prevEntry.nextEntry = newEntry;
/*  388 */     this.name2entry.put(name, newEntry);
/*      */     
/*      */     try {
/*  391 */       filter.onPostAdd(this, name, newEntry.getNextFilter());
/*  392 */     } catch (Exception e) {
/*  393 */       deregister0(newEntry);
/*  394 */       throw new IoFilterLifeCycleException("onPostAdd(): " + name + ':' + filter + " in " + getSession(), e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void deregister(EntryImpl entry) {
/*  399 */     IoFilter filter = entry.getFilter();
/*      */     
/*      */     try {
/*  402 */       filter.onPreRemove(this, entry.getName(), entry.getNextFilter());
/*  403 */     } catch (Exception e) {
/*  404 */       throw new IoFilterLifeCycleException("onPreRemove(): " + entry.getName() + ':' + filter + " in " + getSession(), e);
/*      */     } 
/*      */ 
/*      */     
/*  408 */     deregister0(entry);
/*      */     
/*      */     try {
/*  411 */       filter.onPostRemove(this, entry.getName(), entry.getNextFilter());
/*  412 */     } catch (Exception e) {
/*  413 */       throw new IoFilterLifeCycleException("onPostRemove(): " + entry.getName() + ':' + filter + " in " + getSession(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void deregister0(EntryImpl entry) {
/*  419 */     EntryImpl prevEntry = entry.prevEntry;
/*  420 */     EntryImpl nextEntry = entry.nextEntry;
/*  421 */     prevEntry.nextEntry = nextEntry;
/*  422 */     nextEntry.prevEntry = prevEntry;
/*      */     
/*  424 */     this.name2entry.remove(entry.name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private EntryImpl checkOldName(String baseName) {
/*  433 */     EntryImpl e = (EntryImpl)this.name2entry.get(baseName);
/*      */     
/*  435 */     if (e == null) {
/*  436 */       throw new IllegalArgumentException("Filter not found:" + baseName);
/*      */     }
/*      */     
/*  439 */     return e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkAddable(String name) {
/*  446 */     if (this.name2entry.containsKey(name)) {
/*  447 */       throw new IllegalArgumentException("Other filter is using the same name '" + name + "'");
/*      */     }
/*      */   }
/*      */   
/*      */   public void fireSessionCreated() {
/*  452 */     callNextSessionCreated(this.head, (IoSession)this.session);
/*      */   }
/*      */   
/*      */   private void callNextSessionCreated(IoFilterChain.Entry entry, IoSession session) {
/*      */     try {
/*  457 */       IoFilter filter = entry.getFilter();
/*  458 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  459 */       filter.sessionCreated(nextFilter, session);
/*  460 */     } catch (Exception e) {
/*  461 */       fireExceptionCaught(e);
/*  462 */     } catch (Error e) {
/*  463 */       fireExceptionCaught(e);
/*  464 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireSessionOpened() {
/*  469 */     callNextSessionOpened(this.head, (IoSession)this.session);
/*      */   }
/*      */   
/*      */   private void callNextSessionOpened(IoFilterChain.Entry entry, IoSession session) {
/*      */     try {
/*  474 */       IoFilter filter = entry.getFilter();
/*  475 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  476 */       filter.sessionOpened(nextFilter, session);
/*  477 */     } catch (Exception e) {
/*  478 */       fireExceptionCaught(e);
/*  479 */     } catch (Error e) {
/*  480 */       fireExceptionCaught(e);
/*  481 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void fireSessionClosed() {
/*      */     try {
/*  488 */       this.session.getCloseFuture().setClosed();
/*  489 */     } catch (Exception e) {
/*  490 */       fireExceptionCaught(e);
/*  491 */     } catch (Error e) {
/*  492 */       fireExceptionCaught(e);
/*  493 */       throw e;
/*      */     } 
/*      */ 
/*      */     
/*  497 */     callNextSessionClosed(this.head, (IoSession)this.session);
/*      */   }
/*      */   
/*      */   private void callNextSessionClosed(IoFilterChain.Entry entry, IoSession session) {
/*      */     try {
/*  502 */       IoFilter filter = entry.getFilter();
/*  503 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  504 */       filter.sessionClosed(nextFilter, session);
/*  505 */     } catch (Exception e) {
/*  506 */       fireExceptionCaught(e);
/*  507 */     } catch (Error e) {
/*  508 */       fireExceptionCaught(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireSessionIdle(IdleStatus status) {
/*  513 */     this.session.increaseIdleCount(status, System.currentTimeMillis());
/*  514 */     callNextSessionIdle(this.head, (IoSession)this.session, status);
/*      */   }
/*      */   
/*      */   private void callNextSessionIdle(IoFilterChain.Entry entry, IoSession session, IdleStatus status) {
/*      */     try {
/*  519 */       IoFilter filter = entry.getFilter();
/*  520 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  521 */       filter.sessionIdle(nextFilter, session, status);
/*  522 */     } catch (Exception e) {
/*  523 */       fireExceptionCaught(e);
/*  524 */     } catch (Error e) {
/*  525 */       fireExceptionCaught(e);
/*  526 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireMessageReceived(Object message) {
/*  531 */     if (message instanceof IoBuffer) {
/*  532 */       this.session.increaseReadBytes(((IoBuffer)message).remaining(), System.currentTimeMillis());
/*      */     }
/*      */     
/*  535 */     callNextMessageReceived(this.head, (IoSession)this.session, message);
/*      */   }
/*      */   
/*      */   private void callNextMessageReceived(IoFilterChain.Entry entry, IoSession session, Object message) {
/*      */     try {
/*  540 */       IoFilter filter = entry.getFilter();
/*  541 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  542 */       filter.messageReceived(nextFilter, session, message);
/*  543 */     } catch (Exception e) {
/*  544 */       fireExceptionCaught(e);
/*  545 */     } catch (Error e) {
/*  546 */       fireExceptionCaught(e);
/*  547 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireMessageSent(WriteRequest request) {
/*      */     try {
/*  553 */       request.getFuture().setWritten();
/*  554 */     } catch (Exception e) {
/*  555 */       fireExceptionCaught(e);
/*  556 */     } catch (Error e) {
/*  557 */       fireExceptionCaught(e);
/*  558 */       throw e;
/*      */     } 
/*      */     
/*  561 */     if (!request.isEncoded()) {
/*  562 */       callNextMessageSent(this.head, (IoSession)this.session, request);
/*      */     }
/*      */   }
/*      */   
/*      */   private void callNextMessageSent(IoFilterChain.Entry entry, IoSession session, WriteRequest writeRequest) {
/*      */     try {
/*  568 */       IoFilter filter = entry.getFilter();
/*  569 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  570 */       filter.messageSent(nextFilter, session, writeRequest);
/*  571 */     } catch (Exception e) {
/*  572 */       fireExceptionCaught(e);
/*  573 */     } catch (Error e) {
/*  574 */       fireExceptionCaught(e);
/*  575 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireExceptionCaught(Throwable cause) {
/*  580 */     callNextExceptionCaught(this.head, (IoSession)this.session, cause);
/*      */   }
/*      */ 
/*      */   
/*      */   private void callNextExceptionCaught(IoFilterChain.Entry entry, IoSession session, Throwable cause) {
/*  585 */     ConnectFuture future = (ConnectFuture)session.removeAttribute(SESSION_CREATED_FUTURE);
/*  586 */     if (future == null) {
/*      */       try {
/*  588 */         IoFilter filter = entry.getFilter();
/*  589 */         IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  590 */         filter.exceptionCaught(nextFilter, session, cause);
/*  591 */       } catch (Throwable e) {
/*  592 */         LOGGER.warn("Unexpected exception from exceptionCaught handler.", e);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  597 */       session.close(true);
/*  598 */       future.setException(cause);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireInputClosed() {
/*  603 */     IoFilterChain.Entry head = this.head;
/*  604 */     callNextInputClosed(head, (IoSession)this.session);
/*      */   }
/*      */   
/*      */   private void callNextInputClosed(IoFilterChain.Entry entry, IoSession session) {
/*      */     try {
/*  609 */       IoFilter filter = entry.getFilter();
/*  610 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  611 */       filter.inputClosed(nextFilter, session);
/*  612 */     } catch (Throwable e) {
/*  613 */       fireExceptionCaught(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireFilterWrite(WriteRequest writeRequest) {
/*  618 */     callPreviousFilterWrite(this.tail, (IoSession)this.session, writeRequest);
/*      */   }
/*      */   
/*      */   private void callPreviousFilterWrite(IoFilterChain.Entry entry, IoSession session, WriteRequest writeRequest) {
/*      */     try {
/*  623 */       IoFilter filter = entry.getFilter();
/*  624 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  625 */       filter.filterWrite(nextFilter, session, writeRequest);
/*  626 */     } catch (Exception e) {
/*  627 */       writeRequest.getFuture().setException(e);
/*  628 */       fireExceptionCaught(e);
/*  629 */     } catch (Error e) {
/*  630 */       writeRequest.getFuture().setException(e);
/*  631 */       fireExceptionCaught(e);
/*  632 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fireFilterClose() {
/*  637 */     callPreviousFilterClose(this.tail, (IoSession)this.session);
/*      */   }
/*      */   
/*      */   private void callPreviousFilterClose(IoFilterChain.Entry entry, IoSession session) {
/*      */     try {
/*  642 */       IoFilter filter = entry.getFilter();
/*  643 */       IoFilter.NextFilter nextFilter = entry.getNextFilter();
/*  644 */       filter.filterClose(nextFilter, session);
/*  645 */     } catch (Exception e) {
/*  646 */       fireExceptionCaught(e);
/*  647 */     } catch (Error e) {
/*  648 */       fireExceptionCaught(e);
/*  649 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   public List<IoFilterChain.Entry> getAll() {
/*  654 */     List<IoFilterChain.Entry> list = new ArrayList<IoFilterChain.Entry>();
/*  655 */     EntryImpl e = this.head.nextEntry;
/*      */     
/*  657 */     while (e != this.tail) {
/*  658 */       list.add(e);
/*  659 */       e = e.nextEntry;
/*      */     } 
/*      */     
/*  662 */     return list;
/*      */   }
/*      */   
/*      */   public List<IoFilterChain.Entry> getAllReversed() {
/*  666 */     List<IoFilterChain.Entry> list = new ArrayList<IoFilterChain.Entry>();
/*  667 */     EntryImpl e = this.tail.prevEntry;
/*      */     
/*  669 */     while (e != this.head) {
/*  670 */       list.add(e);
/*  671 */       e = e.prevEntry;
/*      */     } 
/*      */     
/*  674 */     return list;
/*      */   }
/*      */   
/*      */   public boolean contains(String name) {
/*  678 */     return (getEntry(name) != null);
/*      */   }
/*      */   
/*      */   public boolean contains(IoFilter filter) {
/*  682 */     return (getEntry(filter) != null);
/*      */   }
/*      */   
/*      */   public boolean contains(Class<? extends IoFilter> filterType) {
/*  686 */     return (getEntry(filterType) != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  691 */     StringBuilder buf = new StringBuilder();
/*  692 */     buf.append("{ ");
/*      */     
/*  694 */     boolean empty = true;
/*      */     
/*  696 */     EntryImpl e = this.head.nextEntry;
/*      */     
/*  698 */     while (e != this.tail) {
/*  699 */       if (!empty) {
/*  700 */         buf.append(", ");
/*      */       } else {
/*  702 */         empty = false;
/*      */       } 
/*      */       
/*  705 */       buf.append('(');
/*  706 */       buf.append(e.getName());
/*  707 */       buf.append(':');
/*  708 */       buf.append(e.getFilter());
/*  709 */       buf.append(')');
/*      */       
/*  711 */       e = e.nextEntry;
/*      */     } 
/*      */     
/*  714 */     if (empty) {
/*  715 */       buf.append("empty");
/*      */     }
/*      */     
/*  718 */     buf.append(" }");
/*      */     
/*  720 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private class HeadFilter
/*      */     extends IoFilterAdapter
/*      */   {
/*      */     public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*  728 */       AbstractIoSession s = (AbstractIoSession)session;
/*      */ 
/*      */       
/*  731 */       if (writeRequest.getMessage() instanceof IoBuffer) {
/*  732 */         IoBuffer buffer = (IoBuffer)writeRequest.getMessage();
/*      */ 
/*      */ 
/*      */         
/*  736 */         buffer.mark();
/*  737 */         int remaining = buffer.remaining();
/*      */         
/*  739 */         if (remaining > 0) {
/*  740 */           s.increaseScheduledWriteBytes(remaining);
/*      */         }
/*      */       } else {
/*  743 */         s.increaseScheduledWriteMessages();
/*      */       } 
/*      */       
/*  746 */       WriteRequestQueue writeRequestQueue = s.getWriteRequestQueue();
/*      */       
/*  748 */       if (!s.isWriteSuspended()) {
/*  749 */         if (writeRequestQueue.isEmpty(session)) {
/*      */           
/*  751 */           s.getProcessor().write((IoSession)s, writeRequest);
/*      */         } else {
/*  753 */           s.getWriteRequestQueue().offer((IoSession)s, writeRequest);
/*  754 */           s.getProcessor().flush((IoSession)s);
/*      */         } 
/*      */       } else {
/*  757 */         s.getWriteRequestQueue().offer((IoSession)s, writeRequest);
/*      */       } 
/*      */     }
/*      */     
/*      */     private HeadFilter() {}
/*      */     
/*      */     public void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  764 */       ((AbstractIoSession)session).getProcessor().remove(session);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TailFilter
/*      */     extends IoFilterAdapter {
/*      */     public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*      */       try {
/*  772 */         session.getHandler().sessionCreated(session);
/*      */       } finally {
/*      */         
/*  775 */         ConnectFuture future = (ConnectFuture)session.removeAttribute(DefaultIoFilterChain.SESSION_CREATED_FUTURE);
/*      */         
/*  777 */         if (future != null)
/*  778 */           future.setSession(session); 
/*      */       } 
/*      */     }
/*      */     
/*      */     private TailFilter() {}
/*      */     
/*      */     public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  785 */       session.getHandler().sessionOpened(session);
/*      */     }
/*      */ 
/*      */     
/*      */     public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  790 */       AbstractIoSession s = (AbstractIoSession)session;
/*      */       
/*      */       try {
/*  793 */         s.getHandler().sessionClosed(session);
/*      */       } finally {
/*      */         try {
/*  796 */           s.getWriteRequestQueue().dispose(session);
/*      */         } finally {
/*      */           try {
/*  799 */             s.getAttributeMap().dispose(session);
/*      */           } finally {
/*      */             
/*      */             try {
/*  803 */               session.getFilterChain().clear();
/*      */             } finally {
/*  805 */               if (s.getConfig().isUseReadOperation()) {
/*  806 */                 s.offerClosedReadFuture();
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/*  816 */       session.getHandler().sessionIdle(session, status);
/*      */     }
/*      */ 
/*      */     
/*      */     public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
/*  821 */       AbstractIoSession s = (AbstractIoSession)session;
/*      */       
/*      */       try {
/*  824 */         s.getHandler().exceptionCaught((IoSession)s, cause);
/*      */       } finally {
/*  826 */         if (s.getConfig().isUseReadOperation()) {
/*  827 */           s.offerFailedReadFuture(cause);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void inputClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  834 */       session.getHandler().inputClosed(session);
/*      */     }
/*      */ 
/*      */     
/*      */     public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/*  839 */       AbstractIoSession s = (AbstractIoSession)session;
/*      */       
/*  841 */       if (!(message instanceof IoBuffer)) {
/*  842 */         s.increaseReadMessages(System.currentTimeMillis());
/*  843 */       } else if (!((IoBuffer)message).hasRemaining()) {
/*  844 */         s.increaseReadMessages(System.currentTimeMillis());
/*      */       } 
/*      */ 
/*      */       
/*  848 */       if (session.getService() instanceof AbstractIoService) {
/*  849 */         ((AbstractIoService)session.getService()).getStatistics().updateThroughput(System.currentTimeMillis());
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/*  854 */         session.getHandler().messageReceived((IoSession)s, message);
/*      */       } finally {
/*  856 */         if (s.getConfig().isUseReadOperation()) {
/*  857 */           s.offerReadFuture(message);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*  864 */       ((AbstractIoSession)session).increaseWrittenMessages(writeRequest, System.currentTimeMillis());
/*      */ 
/*      */       
/*  867 */       if (session.getService() instanceof AbstractIoService) {
/*  868 */         ((AbstractIoService)session.getService()).getStatistics().updateThroughput(System.currentTimeMillis());
/*      */       }
/*      */ 
/*      */       
/*  872 */       session.getHandler().messageSent(session, writeRequest.getMessage());
/*      */     }
/*      */ 
/*      */     
/*      */     public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*  877 */       nextFilter.filterWrite(session, writeRequest);
/*      */     }
/*      */ 
/*      */     
/*      */     public void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  882 */       nextFilter.filterClose(session);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class EntryImpl
/*      */     implements IoFilterChain.Entry
/*      */   {
/*      */     private EntryImpl prevEntry;
/*      */     
/*      */     private EntryImpl nextEntry;
/*      */     private final String name;
/*      */     private IoFilter filter;
/*      */     private final IoFilter.NextFilter nextFilter;
/*      */     
/*      */     private EntryImpl(EntryImpl prevEntry, EntryImpl nextEntry, String name, IoFilter filter) {
/*  898 */       if (filter == null) {
/*  899 */         throw new IllegalArgumentException("filter");
/*      */       }
/*      */       
/*  902 */       if (name == null) {
/*  903 */         throw new IllegalArgumentException("name");
/*      */       }
/*      */       
/*  906 */       this.prevEntry = prevEntry;
/*  907 */       this.nextEntry = nextEntry;
/*  908 */       this.name = name;
/*  909 */       this.filter = filter;
/*  910 */       this.nextFilter = new IoFilter.NextFilter() {
/*      */           public void sessionCreated(IoSession session) {
/*  912 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  913 */             DefaultIoFilterChain.this.callNextSessionCreated(nextEntry, session);
/*      */           }
/*      */           
/*      */           public void sessionOpened(IoSession session) {
/*  917 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  918 */             DefaultIoFilterChain.this.callNextSessionOpened(nextEntry, session);
/*      */           }
/*      */           
/*      */           public void sessionClosed(IoSession session) {
/*  922 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  923 */             DefaultIoFilterChain.this.callNextSessionClosed(nextEntry, session);
/*      */           }
/*      */           
/*      */           public void sessionIdle(IoSession session, IdleStatus status) {
/*  927 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  928 */             DefaultIoFilterChain.this.callNextSessionIdle(nextEntry, session, status);
/*      */           }
/*      */           
/*      */           public void exceptionCaught(IoSession session, Throwable cause) {
/*  932 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  933 */             DefaultIoFilterChain.this.callNextExceptionCaught(nextEntry, session, cause);
/*      */           }
/*      */           
/*      */           public void inputClosed(IoSession session) {
/*  937 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  938 */             DefaultIoFilterChain.this.callNextInputClosed(nextEntry, session);
/*      */           }
/*      */           
/*      */           public void messageReceived(IoSession session, Object message) {
/*  942 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  943 */             DefaultIoFilterChain.this.callNextMessageReceived(nextEntry, session, message);
/*      */           }
/*      */           
/*      */           public void messageSent(IoSession session, WriteRequest writeRequest) {
/*  947 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
/*  948 */             DefaultIoFilterChain.this.callNextMessageSent(nextEntry, session, writeRequest);
/*      */           }
/*      */           
/*      */           public void filterWrite(IoSession session, WriteRequest writeRequest) {
/*  952 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.prevEntry;
/*  953 */             DefaultIoFilterChain.this.callPreviousFilterWrite(nextEntry, session, writeRequest);
/*      */           }
/*      */           
/*      */           public void filterClose(IoSession session) {
/*  957 */             IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.prevEntry;
/*  958 */             DefaultIoFilterChain.this.callPreviousFilterClose(nextEntry, session);
/*      */           }
/*      */           
/*      */           public String toString() {
/*  962 */             return DefaultIoFilterChain.EntryImpl.this.nextEntry.name;
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     public String getName() {
/*  968 */       return this.name;
/*      */     }
/*      */     
/*      */     public IoFilter getFilter() {
/*  972 */       return this.filter;
/*      */     }
/*      */     
/*      */     private void setFilter(IoFilter filter) {
/*  976 */       if (filter == null) {
/*  977 */         throw new IllegalArgumentException("filter");
/*      */       }
/*      */       
/*  980 */       this.filter = filter;
/*      */     }
/*      */     
/*      */     public IoFilter.NextFilter getNextFilter() {
/*  984 */       return this.nextFilter;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  989 */       StringBuilder sb = new StringBuilder();
/*      */ 
/*      */       
/*  992 */       sb.append("('").append(getName()).append('\'');
/*      */ 
/*      */       
/*  995 */       sb.append(", prev: '");
/*      */       
/*  997 */       if (this.prevEntry != null) {
/*  998 */         sb.append(this.prevEntry.name);
/*  999 */         sb.append(':');
/* 1000 */         sb.append(this.prevEntry.getFilter().getClass().getSimpleName());
/*      */       } else {
/* 1002 */         sb.append("null");
/*      */       } 
/*      */ 
/*      */       
/* 1006 */       sb.append("', next: '");
/*      */       
/* 1008 */       if (this.nextEntry != null) {
/* 1009 */         sb.append(this.nextEntry.name);
/* 1010 */         sb.append(':');
/* 1011 */         sb.append(this.nextEntry.getFilter().getClass().getSimpleName());
/*      */       } else {
/* 1013 */         sb.append("null");
/*      */       } 
/*      */       
/* 1016 */       sb.append("')");
/*      */       
/* 1018 */       return sb.toString();
/*      */     }
/*      */     
/*      */     public void addAfter(String name, IoFilter filter) {
/* 1022 */       DefaultIoFilterChain.this.addAfter(getName(), name, filter);
/*      */     }
/*      */     
/*      */     public void addBefore(String name, IoFilter filter) {
/* 1026 */       DefaultIoFilterChain.this.addBefore(getName(), name, filter);
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1030 */       DefaultIoFilterChain.this.remove(getName());
/*      */     }
/*      */     
/*      */     public void replace(IoFilter newFilter) {
/* 1034 */       DefaultIoFilterChain.this.replace(getName(), newFilter);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/filterchain/DefaultIoFilterChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */