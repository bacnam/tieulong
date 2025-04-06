/*      */ package jsr166y;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import sun.misc.Unsafe;
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
/*      */ public class LinkedTransferQueue<E>
/*      */   extends AbstractQueue<E>
/*      */   implements TransferQueue<E>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -3223113410248163686L;
/*  378 */   private static final boolean MP = (Runtime.getRuntime().availableProcessors() > 1);
/*      */ 
/*      */   
/*      */   private static final int FRONT_SPINS = 128;
/*      */ 
/*      */   
/*      */   private static final int CHAINED_SPINS = 64;
/*      */ 
/*      */   
/*      */   static final int SWEEP_THRESHOLD = 32;
/*      */ 
/*      */   
/*      */   volatile transient Node head;
/*      */ 
/*      */   
/*      */   private volatile transient Node tail;
/*      */ 
/*      */   
/*      */   private volatile transient int sweepVotes;
/*      */ 
/*      */   
/*      */   private static final int NOW = 0;
/*      */ 
/*      */   
/*      */   private static final int ASYNC = 1;
/*      */ 
/*      */   
/*      */   private static final int SYNC = 2;
/*      */ 
/*      */   
/*      */   private static final int TIMED = 3;
/*      */ 
/*      */   
/*      */   static final class Node
/*      */   {
/*      */     final boolean isData;
/*      */     
/*      */     volatile Object item;
/*      */     
/*      */     volatile Node next;
/*      */     
/*      */     volatile Thread waiter;
/*      */ 
/*      */     
/*      */     final boolean casNext(Node cmp, Node val) {
/*  423 */       return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
/*      */     }
/*      */     
/*      */     final boolean casItem(Object cmp, Object val) {
/*  427 */       assert cmp == null || cmp.getClass() != Node.class;
/*  428 */       return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Node(Object item, boolean isData) {
/*  436 */       UNSAFE.putObject(this, itemOffset, item);
/*  437 */       this.isData = isData;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final void forgetNext() {
/*  445 */       UNSAFE.putObject(this, nextOffset, this);
/*      */     }
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
/*      */     final void forgetContents() {
/*  458 */       UNSAFE.putObject(this, itemOffset, this);
/*  459 */       UNSAFE.putObject(this, waiterOffset, (Object)null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean isMatched() {
/*  467 */       Object x = this.item;
/*  468 */       return (x == this || ((x == null)) == this.isData);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean isUnmatchedRequest() {
/*  475 */       return (!this.isData && this.item == null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean cannotPrecede(boolean haveData) {
/*  484 */       boolean d = this.isData;
/*      */       Object x;
/*  486 */       return (d != haveData && (x = this.item) != this && ((x != null)) == d);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean tryMatchData() {
/*  493 */       assert this.isData;
/*  494 */       Object x = this.item;
/*  495 */       if (x != null && x != this && casItem(x, null)) {
/*  496 */         LockSupport.unpark(this.waiter);
/*  497 */         return true;
/*      */       } 
/*  499 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  504 */     private static final Unsafe UNSAFE = LinkedTransferQueue.getUnsafe();
/*  505 */     private static final long nextOffset = LinkedTransferQueue.objectFieldOffset(UNSAFE, "next", Node.class);
/*      */     
/*  507 */     private static final long itemOffset = LinkedTransferQueue.objectFieldOffset(UNSAFE, "item", Node.class);
/*      */     
/*  509 */     private static final long waiterOffset = LinkedTransferQueue.objectFieldOffset(UNSAFE, "waiter", Node.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = -3375979862319811754L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean casTail(Node cmp, Node val) {
/*  526 */     return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
/*      */   }
/*      */   
/*      */   private boolean casHead(Node cmp, Node val) {
/*  530 */     return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
/*      */   }
/*      */   
/*      */   private boolean casSweepVotes(int cmp, int val) {
/*  534 */     return UNSAFE.compareAndSwapInt(this, sweepVotesOffset, cmp, val);
/*      */   }
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
/*      */   static <E> E cast(Object item) {
/*  547 */     assert item == null || item.getClass() != Node.class;
/*  548 */     return (E)item;
/*      */   }
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
/*      */   private E xfer(E e, boolean haveData, int how, long nanos) {
/*  562 */     if (haveData && e == null)
/*  563 */       throw new NullPointerException(); 
/*  564 */     Node s = null;
/*      */ 
/*      */     
/*      */     while (true) {
/*  568 */       for (Node h = this.head, p = h; p != null; ) {
/*  569 */         boolean isData = p.isData;
/*  570 */         Object item = p.item;
/*  571 */         if (item != p && ((item != null)) == isData) {
/*  572 */           if (isData == haveData)
/*      */             break; 
/*  574 */           if (p.casItem(item, e)) {
/*  575 */             for (Node q = p; q != h; ) {
/*  576 */               Node node = q.next;
/*  577 */               if (this.head == h && casHead(h, (node == null) ? q : node)) {
/*  578 */                 h.forgetNext();
/*      */                 break;
/*      */               } 
/*  581 */               if ((h = this.head) == null || (q = h.next) == null || !q.isMatched()) {
/*      */                 break;
/*      */               }
/*      */             } 
/*  585 */             LockSupport.unpark(p.waiter);
/*  586 */             this; return cast(item);
/*      */           } 
/*      */         } 
/*  589 */         Node n = p.next;
/*  590 */         p = (p != n) ? n : (h = this.head);
/*      */       } 
/*      */       
/*  593 */       if (how != 0) {
/*  594 */         if (s == null)
/*  595 */           s = new Node(e, haveData); 
/*  596 */         Node pred = tryAppend(s, haveData);
/*  597 */         if (pred == null)
/*      */           continue; 
/*  599 */         if (how != 1)
/*  600 */           return awaitMatch(s, pred, e, (how == 3), nanos); 
/*      */       }  break;
/*  602 */     }  return e;
/*      */   }
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
/*      */   private Node tryAppend(Node s, boolean haveData) {
/*  616 */     Node t = this.tail, p = t;
/*      */     while (true) {
/*  618 */       if (p == null && (p = this.head) == null) {
/*  619 */         if (casHead(null, s))
/*  620 */           return s;  continue;
/*      */       } 
/*  622 */       if (p.cannotPrecede(haveData))
/*  623 */         return null;  Node n;
/*  624 */       if ((n = p.next) != null) {
/*  625 */         Node u; p = (p != t && t != (u = this.tail)) ? (t = u) : ((p != n) ? n : null); continue;
/*      */       } 
/*  627 */       if (!p.casNext(null, s)) {
/*  628 */         p = p.next; continue;
/*      */       }  break;
/*  630 */     }  if (p != t)
/*      */     {
/*      */ 
/*      */       
/*  634 */       while ((this.tail != t || !casTail(t, s)) && (t = this.tail) != null && (s = t.next) != null && (s = s.next) != null && s != t);
/*      */     }
/*  636 */     return p;
/*      */   }
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
/*      */   private E awaitMatch(Node s, Node pred, E e, boolean timed, long nanos) {
/*  654 */     long lastTime = timed ? System.nanoTime() : 0L;
/*  655 */     Thread w = Thread.currentThread();
/*  656 */     int spins = -1;
/*  657 */     ThreadLocalRandom randomYields = null;
/*      */     
/*      */     while (true) {
/*  660 */       Object item = s.item;
/*  661 */       if (item != e) {
/*  662 */         assert item != s;
/*  663 */         s.forgetContents();
/*  664 */         this; return cast(item);
/*      */       } 
/*  666 */       if ((w.isInterrupted() || (timed && nanos <= 0L)) && s.casItem(e, s)) {
/*      */         
/*  668 */         unsplice(pred, s);
/*  669 */         return e;
/*      */       } 
/*      */       
/*  672 */       if (spins < 0) {
/*  673 */         if ((spins = spinsFor(pred, s.isData)) > 0)
/*  674 */           randomYields = ThreadLocalRandom.current();  continue;
/*      */       } 
/*  676 */       if (spins > 0) {
/*  677 */         spins--;
/*  678 */         if (randomYields.nextInt(64) == 0)
/*  679 */           Thread.yield();  continue;
/*      */       } 
/*  681 */       if (s.waiter == null) {
/*  682 */         s.waiter = w; continue;
/*      */       } 
/*  684 */       if (timed) {
/*  685 */         long now = System.nanoTime();
/*  686 */         if ((nanos -= now - lastTime) > 0L)
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*  691 */           LockSupport.parkNanos(this, nanos);
/*      */         }
/*  693 */         lastTime = now;
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */       
/*  700 */       LockSupport.park(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int spinsFor(Node pred, boolean haveData) {
/*  711 */     if (MP && pred != null) {
/*  712 */       if (pred.isData != haveData)
/*  713 */         return 192; 
/*  714 */       if (pred.isMatched())
/*  715 */         return 128; 
/*  716 */       if (pred.waiter == null)
/*  717 */         return 64; 
/*      */     } 
/*  719 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Node succ(Node p) {
/*  730 */     Node next = p.next;
/*  731 */     return (p == next) ? this.head : next;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node firstOfMode(boolean isData) {
/*  739 */     for (Node p = this.head; p != null; p = succ(p)) {
/*  740 */       if (!p.isMatched())
/*  741 */         return (p.isData == isData) ? p : null; 
/*      */     } 
/*  743 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private E firstDataItem() {
/*  751 */     for (Node p = this.head; p != null; p = succ(p)) {
/*  752 */       Object item = p.item;
/*  753 */       if (p.isData) {
/*  754 */         if (item != null && item != p) {
/*  755 */           this; return cast(item);
/*      */         } 
/*  757 */       } else if (item == null) {
/*  758 */         return null;
/*      */       } 
/*  760 */     }  return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int countOfMode(boolean data) {
/*  768 */     int count = 0;
/*  769 */     for (Node p = this.head; p != null; ) {
/*  770 */       if (!p.isMatched()) {
/*  771 */         if (p.isData != data)
/*  772 */           return 0; 
/*  773 */         if (++count == Integer.MAX_VALUE)
/*      */           break; 
/*      */       } 
/*  776 */       Node n = p.next;
/*  777 */       if (n != p) {
/*  778 */         p = n; continue;
/*      */       } 
/*  780 */       count = 0;
/*  781 */       p = this.head;
/*      */     } 
/*      */     
/*  784 */     return count;
/*      */   }
/*      */ 
/*      */   
/*      */   final class Itr
/*      */     implements Iterator<E>
/*      */   {
/*      */     private LinkedTransferQueue.Node nextNode;
/*      */     private E nextItem;
/*      */     private LinkedTransferQueue.Node lastRet;
/*      */     private LinkedTransferQueue.Node lastPred;
/*      */     
/*      */     private void advance(LinkedTransferQueue.Node prev) {
/*  797 */       this.lastPred = this.lastRet;
/*  798 */       this.lastRet = prev;
/*  799 */       LinkedTransferQueue.Node p = (prev == null) ? LinkedTransferQueue.this.head : LinkedTransferQueue.this.succ(prev);
/*  800 */       for (; p != null; p = LinkedTransferQueue.this.succ(p)) {
/*  801 */         Object item = p.item;
/*  802 */         if (p.isData) {
/*  803 */           if (item != null && item != p) {
/*  804 */             this.nextItem = LinkedTransferQueue.cast(item);
/*  805 */             this.nextNode = p;
/*      */             
/*      */             return;
/*      */           } 
/*  809 */         } else if (item == null) {
/*      */           break;
/*      */         } 
/*  812 */       }  this.nextNode = null;
/*      */     }
/*      */     
/*      */     Itr() {
/*  816 */       advance(null);
/*      */     }
/*      */     
/*      */     public final boolean hasNext() {
/*  820 */       return (this.nextNode != null);
/*      */     }
/*      */     
/*      */     public final E next() {
/*  824 */       LinkedTransferQueue.Node p = this.nextNode;
/*  825 */       if (p == null) throw new NoSuchElementException(); 
/*  826 */       E e = this.nextItem;
/*  827 */       advance(p);
/*  828 */       return e;
/*      */     }
/*      */     
/*      */     public final void remove() {
/*  832 */       LinkedTransferQueue.Node p = this.lastRet;
/*  833 */       if (p == null) throw new IllegalStateException(); 
/*  834 */       if (p.tryMatchData()) {
/*  835 */         LinkedTransferQueue.this.unsplice(this.lastPred, p);
/*      */       }
/*      */     }
/*      */   }
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
/*      */   final void unsplice(Node pred, Node s) {
/*  850 */     s.forgetContents();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  858 */     if (pred != null && pred != s && pred.next == s) {
/*  859 */       Node n = s.next;
/*  860 */       if (n == null || (n != s && pred.casNext(s, n) && pred.isMatched())) {
/*      */         
/*      */         while (true) {
/*  863 */           Node h = this.head;
/*  864 */           if (h == pred || h == s || h == null)
/*      */             return; 
/*  866 */           if (!h.isMatched())
/*      */             break; 
/*  868 */           Node hn = h.next;
/*  869 */           if (hn == null)
/*      */             return; 
/*  871 */           if (hn != h && casHead(h, hn))
/*  872 */             h.forgetNext(); 
/*      */         } 
/*  874 */         if (pred.next != pred && s.next != s) {
/*      */           while (true) {
/*  876 */             int v = this.sweepVotes;
/*  877 */             if (v < 32) {
/*  878 */               if (casSweepVotes(v, v + 1))
/*      */                 break;  continue;
/*      */             } 
/*  881 */             if (casSweepVotes(v, 0)) {
/*  882 */               sweep();
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void sweep() {
/*      */     Node s;
/*  895 */     for (Node p = this.head; p != null && (s = p.next) != null; ) {
/*  896 */       if (p == s) {
/*  897 */         p = this.head; continue;
/*  898 */       }  if (!s.isMatched()) {
/*  899 */         p = s; continue;
/*  900 */       }  Node n; if ((n = s.next) == null) {
/*      */         break;
/*      */       }
/*  903 */       p.casNext(s, n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean findAndRemove(Object e) {
/*  911 */     if (e != null) {
/*  912 */       for (Node pred = null, p = this.head; p != null; ) {
/*  913 */         Object item = p.item;
/*  914 */         if (p.isData) {
/*  915 */           if (item != null && item != p && e.equals(item) && p.tryMatchData())
/*      */           {
/*  917 */             unsplice(pred, p);
/*  918 */             return true;
/*      */           }
/*      */         
/*  921 */         } else if (item == null) {
/*      */           break;
/*  923 */         }  pred = p;
/*  924 */         if ((p = p.next) == pred) {
/*  925 */           pred = null;
/*  926 */           p = this.head;
/*      */         } 
/*      */       } 
/*      */     }
/*  930 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedTransferQueue() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedTransferQueue(Collection<? extends E> c) {
/*  950 */     this();
/*  951 */     addAll(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(E e) {
/*  961 */     xfer(e, true, 1, 0L);
/*      */   }
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
/*      */   public boolean offer(E e, long timeout, TimeUnit unit) {
/*  974 */     xfer(e, true, 1, 0L);
/*  975 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offer(E e) {
/*  987 */     xfer(e, true, 1, 0L);
/*  988 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean add(E e) {
/* 1000 */     xfer(e, true, 1, 0L);
/* 1001 */     return true;
/*      */   }
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
/*      */   public boolean tryTransfer(E e) {
/* 1015 */     return (xfer(e, true, 0, 0L) == null);
/*      */   }
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
/*      */   public void transfer(E e) throws InterruptedException {
/* 1030 */     if (xfer(e, true, 2, 0L) != null) {
/* 1031 */       Thread.interrupted();
/* 1032 */       throw new InterruptedException();
/*      */     } 
/*      */   }
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
/*      */   public boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/* 1052 */     if (xfer(e, true, 3, unit.toNanos(timeout)) == null)
/* 1053 */       return true; 
/* 1054 */     if (!Thread.interrupted())
/* 1055 */       return false; 
/* 1056 */     throw new InterruptedException();
/*      */   }
/*      */   
/*      */   public E take() throws InterruptedException {
/* 1060 */     E e = xfer((E)null, false, 2, 0L);
/* 1061 */     if (e != null)
/* 1062 */       return e; 
/* 1063 */     Thread.interrupted();
/* 1064 */     throw new InterruptedException();
/*      */   }
/*      */   
/*      */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/* 1068 */     E e = xfer((E)null, false, 3, unit.toNanos(timeout));
/* 1069 */     if (e != null || !Thread.interrupted())
/* 1070 */       return e; 
/* 1071 */     throw new InterruptedException();
/*      */   }
/*      */   
/*      */   public E poll() {
/* 1075 */     return xfer((E)null, false, 0, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int drainTo(Collection<? super E> c) {
/* 1083 */     if (c == null)
/* 1084 */       throw new NullPointerException(); 
/* 1085 */     if (c == this)
/* 1086 */       throw new IllegalArgumentException(); 
/* 1087 */     int n = 0;
/*      */     E e;
/* 1089 */     while ((e = poll()) != null) {
/* 1090 */       c.add(e);
/* 1091 */       n++;
/*      */     } 
/* 1093 */     return n;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int drainTo(Collection<? super E> c, int maxElements) {
/* 1101 */     if (c == null)
/* 1102 */       throw new NullPointerException(); 
/* 1103 */     if (c == this)
/* 1104 */       throw new IllegalArgumentException(); 
/* 1105 */     int n = 0;
/*      */     E e;
/* 1107 */     while (n < maxElements && (e = poll()) != null) {
/* 1108 */       c.add(e);
/* 1109 */       n++;
/*      */     } 
/* 1111 */     return n;
/*      */   }
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
/*      */   public Iterator<E> iterator() {
/* 1128 */     return new Itr();
/*      */   }
/*      */   
/*      */   public E peek() {
/* 1132 */     return firstDataItem();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 1141 */     for (Node p = this.head; p != null; p = succ(p)) {
/* 1142 */       if (!p.isMatched())
/* 1143 */         return !p.isData; 
/*      */     } 
/* 1145 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hasWaitingConsumer() {
/* 1149 */     return (firstOfMode(false) != null);
/*      */   }
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
/*      */   public int size() {
/* 1165 */     return countOfMode(true);
/*      */   }
/*      */   
/*      */   public int getWaitingConsumerCount() {
/* 1169 */     return countOfMode(false);
/*      */   }
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
/*      */   public boolean remove(Object o) {
/* 1184 */     return findAndRemove(o);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int remainingCapacity() {
/* 1195 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1207 */     s.defaultWriteObject();
/* 1208 */     for (E e : this) {
/* 1209 */       s.writeObject(e);
/*      */     }
/* 1211 */     s.writeObject(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1222 */     s.defaultReadObject();
/*      */     while (true) {
/* 1224 */       E item = (E)s.readObject();
/* 1225 */       if (item == null) {
/*      */         break;
/*      */       }
/* 1228 */       offer(item);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1234 */   private static final Unsafe UNSAFE = getUnsafe();
/* 1235 */   private static final long headOffset = objectFieldOffset(UNSAFE, "head", LinkedTransferQueue.class);
/*      */   
/* 1237 */   private static final long tailOffset = objectFieldOffset(UNSAFE, "tail", LinkedTransferQueue.class);
/*      */   
/* 1239 */   private static final long sweepVotesOffset = objectFieldOffset(UNSAFE, "sweepVotes", LinkedTransferQueue.class);
/*      */ 
/*      */ 
/*      */   
/*      */   static long objectFieldOffset(Unsafe UNSAFE, String field, Class<?> klazz) {
/*      */     try {
/* 1245 */       return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
/* 1246 */     } catch (NoSuchFieldException e) {
/*      */       
/* 1248 */       NoSuchFieldError error = new NoSuchFieldError(field);
/* 1249 */       error.initCause(e);
/* 1250 */       throw error;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Unsafe getUnsafe() {
/*      */     try {
/* 1263 */       return Unsafe.getUnsafe();
/* 1264 */     } catch (SecurityException se) {
/*      */       try {
/* 1266 */         return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*      */             {
/*      */               public Unsafe run() throws Exception
/*      */               {
/* 1270 */                 Field f = Unsafe.class.getDeclaredField("theUnsafe");
/*      */                 
/* 1272 */                 f.setAccessible(true);
/* 1273 */                 return (Unsafe)f.get((Object)null); }
/*      */             });
/* 1275 */       } catch (PrivilegedActionException e) {
/* 1276 */         throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsr166y/LinkedTransferQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */