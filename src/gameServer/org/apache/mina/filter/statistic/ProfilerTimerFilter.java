/*     */ package org.apache.mina.filter.statistic;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoEventType;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfilerTimerFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*     */   private volatile TimeUnit timeUnit;
/*     */   private TimerWorker messageReceivedTimerWorker;
/*     */   private boolean profileMessageReceived = false;
/*     */   private TimerWorker messageSentTimerWorker;
/*     */   private boolean profileMessageSent = false;
/*     */   private TimerWorker sessionCreatedTimerWorker;
/*     */   private boolean profileSessionCreated = false;
/*     */   private TimerWorker sessionOpenedTimerWorker;
/*     */   private boolean profileSessionOpened = false;
/*     */   private TimerWorker sessionIdleTimerWorker;
/*     */   private boolean profileSessionIdle = false;
/*     */   private TimerWorker sessionClosedTimerWorker;
/*     */   private boolean profileSessionClosed = false;
/*     */   
/*     */   public ProfilerTimerFilter() {
/* 107 */     this(TimeUnit.MILLISECONDS, new IoEventType[] { IoEventType.MESSAGE_RECEIVED, IoEventType.MESSAGE_SENT });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProfilerTimerFilter(TimeUnit timeUnit) {
/* 118 */     this(timeUnit, new IoEventType[] { IoEventType.MESSAGE_RECEIVED, IoEventType.MESSAGE_SENT });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProfilerTimerFilter(TimeUnit timeUnit, IoEventType... eventTypes) {
/* 138 */     this.timeUnit = timeUnit;
/*     */     
/* 140 */     setProfilers(eventTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setProfilers(IoEventType... eventTypes) {
/* 149 */     for (IoEventType type : eventTypes) {
/* 150 */       switch (type) {
/*     */         case SECONDS:
/* 152 */           this.messageReceivedTimerWorker = new TimerWorker();
/* 153 */           this.profileMessageReceived = true;
/*     */           break;
/*     */         
/*     */         case MICROSECONDS:
/* 157 */           this.messageSentTimerWorker = new TimerWorker();
/* 158 */           this.profileMessageSent = true;
/*     */           break;
/*     */         
/*     */         case NANOSECONDS:
/* 162 */           this.sessionCreatedTimerWorker = new TimerWorker();
/* 163 */           this.profileSessionCreated = true;
/*     */           break;
/*     */         
/*     */         case null:
/* 167 */           this.sessionOpenedTimerWorker = new TimerWorker();
/* 168 */           this.profileSessionOpened = true;
/*     */           break;
/*     */         
/*     */         case null:
/* 172 */           this.sessionIdleTimerWorker = new TimerWorker();
/* 173 */           this.profileSessionIdle = true;
/*     */           break;
/*     */         
/*     */         case null:
/* 177 */           this.sessionClosedTimerWorker = new TimerWorker();
/* 178 */           this.profileSessionClosed = true;
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeUnit(TimeUnit timeUnit) {
/* 190 */     this.timeUnit = timeUnit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void profile(IoEventType type) {
/* 199 */     switch (type) {
/*     */       case SECONDS:
/* 201 */         this.profileMessageReceived = true;
/*     */         
/* 203 */         if (this.messageReceivedTimerWorker == null) {
/* 204 */           this.messageReceivedTimerWorker = new TimerWorker();
/*     */         }
/*     */         return;
/*     */ 
/*     */       
/*     */       case MICROSECONDS:
/* 210 */         this.profileMessageSent = true;
/*     */         
/* 212 */         if (this.messageSentTimerWorker == null) {
/* 213 */           this.messageSentTimerWorker = new TimerWorker();
/*     */         }
/*     */         return;
/*     */ 
/*     */       
/*     */       case NANOSECONDS:
/* 219 */         this.profileSessionCreated = true;
/*     */         
/* 221 */         if (this.sessionCreatedTimerWorker == null) {
/* 222 */           this.sessionCreatedTimerWorker = new TimerWorker();
/*     */         }
/*     */         return;
/*     */ 
/*     */       
/*     */       case null:
/* 228 */         this.profileSessionOpened = true;
/*     */         
/* 230 */         if (this.sessionOpenedTimerWorker == null) {
/* 231 */           this.sessionOpenedTimerWorker = new TimerWorker();
/*     */         }
/*     */         return;
/*     */ 
/*     */       
/*     */       case null:
/* 237 */         this.profileSessionIdle = true;
/*     */         
/* 239 */         if (this.sessionIdleTimerWorker == null) {
/* 240 */           this.sessionIdleTimerWorker = new TimerWorker();
/*     */         }
/*     */         return;
/*     */ 
/*     */       
/*     */       case null:
/* 246 */         this.profileSessionClosed = true;
/*     */         
/* 248 */         if (this.sessionClosedTimerWorker == null) {
/* 249 */           this.sessionClosedTimerWorker = new TimerWorker();
/*     */         }
/*     */         return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopProfile(IoEventType type) {
/* 262 */     switch (type) {
/*     */       case SECONDS:
/* 264 */         this.profileMessageReceived = false;
/*     */         return;
/*     */       
/*     */       case MICROSECONDS:
/* 268 */         this.profileMessageSent = false;
/*     */         return;
/*     */       
/*     */       case NANOSECONDS:
/* 272 */         this.profileSessionCreated = false;
/*     */         return;
/*     */       
/*     */       case null:
/* 276 */         this.profileSessionOpened = false;
/*     */         return;
/*     */       
/*     */       case null:
/* 280 */         this.profileSessionIdle = false;
/*     */         return;
/*     */       
/*     */       case null:
/* 284 */         this.profileSessionClosed = false;
/*     */         return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<IoEventType> getEventsToProfile() {
/* 295 */     Set<IoEventType> set = new HashSet<IoEventType>();
/*     */     
/* 297 */     if (this.profileMessageReceived) {
/* 298 */       set.add(IoEventType.MESSAGE_RECEIVED);
/*     */     }
/*     */     
/* 301 */     if (this.profileMessageSent) {
/* 302 */       set.add(IoEventType.MESSAGE_SENT);
/*     */     }
/*     */     
/* 305 */     if (this.profileSessionCreated) {
/* 306 */       set.add(IoEventType.SESSION_CREATED);
/*     */     }
/*     */     
/* 309 */     if (this.profileSessionOpened) {
/* 310 */       set.add(IoEventType.SESSION_OPENED);
/*     */     }
/*     */     
/* 313 */     if (this.profileSessionIdle) {
/* 314 */       set.add(IoEventType.SESSION_IDLE);
/*     */     }
/*     */     
/* 317 */     if (this.profileSessionClosed) {
/* 318 */       set.add(IoEventType.SESSION_CLOSED);
/*     */     }
/*     */     
/* 321 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEventsToProfile(IoEventType... eventTypes) {
/* 330 */     setProfilers(eventTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/* 348 */     if (this.profileMessageReceived) {
/* 349 */       long start = timeNow();
/* 350 */       nextFilter.messageReceived(session, message);
/* 351 */       long end = timeNow();
/* 352 */       this.messageReceivedTimerWorker.addNewDuration(end - start);
/*     */     } else {
/* 354 */       nextFilter.messageReceived(session, message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 373 */     if (this.profileMessageSent) {
/* 374 */       long start = timeNow();
/* 375 */       nextFilter.messageSent(session, writeRequest);
/* 376 */       long end = timeNow();
/* 377 */       this.messageSentTimerWorker.addNewDuration(end - start);
/*     */     } else {
/* 379 */       nextFilter.messageSent(session, writeRequest);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 397 */     if (this.profileSessionCreated) {
/* 398 */       long start = timeNow();
/* 399 */       nextFilter.sessionCreated(session);
/* 400 */       long end = timeNow();
/* 401 */       this.sessionCreatedTimerWorker.addNewDuration(end - start);
/*     */     } else {
/* 403 */       nextFilter.sessionCreated(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 421 */     if (this.profileSessionOpened) {
/* 422 */       long start = timeNow();
/* 423 */       nextFilter.sessionOpened(session);
/* 424 */       long end = timeNow();
/* 425 */       this.sessionOpenedTimerWorker.addNewDuration(end - start);
/*     */     } else {
/* 427 */       nextFilter.sessionOpened(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/* 446 */     if (this.profileSessionIdle) {
/* 447 */       long start = timeNow();
/* 448 */       nextFilter.sessionIdle(session, status);
/* 449 */       long end = timeNow();
/* 450 */       this.sessionIdleTimerWorker.addNewDuration(end - start);
/*     */     } else {
/* 452 */       nextFilter.sessionIdle(session, status);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 470 */     if (this.profileSessionClosed) {
/* 471 */       long start = timeNow();
/* 472 */       nextFilter.sessionClosed(session);
/* 473 */       long end = timeNow();
/* 474 */       this.sessionClosedTimerWorker.addNewDuration(end - start);
/*     */     } else {
/* 476 */       nextFilter.sessionClosed(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAverageTime(IoEventType type) {
/* 489 */     switch (type) {
/*     */       case SECONDS:
/* 491 */         if (this.profileMessageReceived) {
/* 492 */           return this.messageReceivedTimerWorker.getAverage();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case MICROSECONDS:
/* 498 */         if (this.profileMessageSent) {
/* 499 */           return this.messageSentTimerWorker.getAverage();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case NANOSECONDS:
/* 505 */         if (this.profileSessionCreated) {
/* 506 */           return this.sessionCreatedTimerWorker.getAverage();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 512 */         if (this.profileSessionOpened) {
/* 513 */           return this.sessionOpenedTimerWorker.getAverage();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 519 */         if (this.profileSessionIdle) {
/* 520 */           return this.sessionIdleTimerWorker.getAverage();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 526 */         if (this.profileSessionClosed) {
/* 527 */           return this.sessionClosedTimerWorker.getAverage();
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 533 */     throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotalCalls(IoEventType type) {
/* 546 */     switch (type) {
/*     */       case SECONDS:
/* 548 */         if (this.profileMessageReceived) {
/* 549 */           return this.messageReceivedTimerWorker.getCallsNumber();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case MICROSECONDS:
/* 555 */         if (this.profileMessageSent) {
/* 556 */           return this.messageSentTimerWorker.getCallsNumber();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case NANOSECONDS:
/* 562 */         if (this.profileSessionCreated) {
/* 563 */           return this.sessionCreatedTimerWorker.getCallsNumber();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 569 */         if (this.profileSessionOpened) {
/* 570 */           return this.sessionOpenedTimerWorker.getCallsNumber();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 576 */         if (this.profileSessionIdle) {
/* 577 */           return this.sessionIdleTimerWorker.getCallsNumber();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 583 */         if (this.profileSessionClosed) {
/* 584 */           return this.sessionClosedTimerWorker.getCallsNumber();
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 590 */     throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotalTime(IoEventType type) {
/* 603 */     switch (type) {
/*     */       case SECONDS:
/* 605 */         if (this.profileMessageReceived) {
/* 606 */           return this.messageReceivedTimerWorker.getTotal();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case MICROSECONDS:
/* 612 */         if (this.profileMessageSent) {
/* 613 */           return this.messageSentTimerWorker.getTotal();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case NANOSECONDS:
/* 619 */         if (this.profileSessionCreated) {
/* 620 */           return this.sessionCreatedTimerWorker.getTotal();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 626 */         if (this.profileSessionOpened) {
/* 627 */           return this.sessionOpenedTimerWorker.getTotal();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 633 */         if (this.profileSessionIdle) {
/* 634 */           return this.sessionIdleTimerWorker.getTotal();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 640 */         if (this.profileSessionClosed) {
/* 641 */           return this.sessionClosedTimerWorker.getTotal();
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 647 */     throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMinimumTime(IoEventType type) {
/* 660 */     switch (type) {
/*     */       case SECONDS:
/* 662 */         if (this.profileMessageReceived) {
/* 663 */           return this.messageReceivedTimerWorker.getMinimum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case MICROSECONDS:
/* 669 */         if (this.profileMessageSent) {
/* 670 */           return this.messageSentTimerWorker.getMinimum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case NANOSECONDS:
/* 676 */         if (this.profileSessionCreated) {
/* 677 */           return this.sessionCreatedTimerWorker.getMinimum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 683 */         if (this.profileSessionOpened) {
/* 684 */           return this.sessionOpenedTimerWorker.getMinimum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 690 */         if (this.profileSessionIdle) {
/* 691 */           return this.sessionIdleTimerWorker.getMinimum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 697 */         if (this.profileSessionClosed) {
/* 698 */           return this.sessionClosedTimerWorker.getMinimum();
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 704 */     throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaximumTime(IoEventType type) {
/* 717 */     switch (type) {
/*     */       case SECONDS:
/* 719 */         if (this.profileMessageReceived) {
/* 720 */           return this.messageReceivedTimerWorker.getMaximum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case MICROSECONDS:
/* 726 */         if (this.profileMessageSent) {
/* 727 */           return this.messageSentTimerWorker.getMaximum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case NANOSECONDS:
/* 733 */         if (this.profileSessionCreated) {
/* 734 */           return this.sessionCreatedTimerWorker.getMaximum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 740 */         if (this.profileSessionOpened) {
/* 741 */           return this.sessionOpenedTimerWorker.getMaximum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 747 */         if (this.profileSessionIdle) {
/* 748 */           return this.sessionIdleTimerWorker.getMaximum();
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 754 */         if (this.profileSessionClosed) {
/* 755 */           return this.sessionClosedTimerWorker.getMaximum();
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 761 */     throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class TimerWorker
/*     */   {
/*     */     private final AtomicLong total;
/*     */ 
/*     */ 
/*     */     
/*     */     private final AtomicLong callsNumber;
/*     */ 
/*     */ 
/*     */     
/*     */     private final AtomicLong minimum;
/*     */ 
/*     */     
/*     */     private final AtomicLong maximum;
/*     */ 
/*     */     
/* 783 */     private final Object lock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TimerWorker() {
/* 790 */       this.total = new AtomicLong();
/* 791 */       this.callsNumber = new AtomicLong();
/* 792 */       this.minimum = new AtomicLong();
/* 793 */       this.maximum = new AtomicLong();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addNewDuration(long duration) {
/* 804 */       this.callsNumber.incrementAndGet();
/* 805 */       this.total.addAndGet(duration);
/*     */       
/* 807 */       synchronized (this.lock) {
/*     */         
/* 809 */         if (duration < this.minimum.longValue()) {
/* 810 */           this.minimum.set(duration);
/*     */         }
/*     */ 
/*     */         
/* 814 */         if (duration > this.maximum.longValue()) {
/* 815 */           this.maximum.set(duration);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getAverage() {
/* 826 */       synchronized (this.lock) {
/*     */         
/* 828 */         return (this.total.longValue() / this.callsNumber.longValue());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getCallsNumber() {
/* 838 */       return this.callsNumber.longValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getTotal() {
/* 847 */       return this.total.longValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getMinimum() {
/* 856 */       return this.minimum.longValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getMaximum() {
/* 865 */       return this.maximum.longValue();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long timeNow() {
/* 873 */     switch (this.timeUnit) {
/*     */       case SECONDS:
/* 875 */         return System.currentTimeMillis() / 1000L;
/*     */       
/*     */       case MICROSECONDS:
/* 878 */         return System.nanoTime() / 1000L;
/*     */       
/*     */       case NANOSECONDS:
/* 881 */         return System.nanoTime();
/*     */     } 
/*     */     
/* 884 */     return System.currentTimeMillis();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/statistic/ProfilerTimerFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */