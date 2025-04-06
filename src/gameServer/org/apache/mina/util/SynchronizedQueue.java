/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
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
/*     */ public class SynchronizedQueue<E>
/*     */   implements Queue<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1439242290701194806L;
/*     */   private final Queue<E> q;
/*     */   
/*     */   public SynchronizedQueue(Queue<E> q) {
/*  40 */     this.q = q;
/*     */   }
/*     */   
/*     */   public synchronized boolean add(E e) {
/*  44 */     return this.q.add(e);
/*     */   }
/*     */   
/*     */   public synchronized E element() {
/*  48 */     return this.q.element();
/*     */   }
/*     */   
/*     */   public synchronized boolean offer(E e) {
/*  52 */     return this.q.offer(e);
/*     */   }
/*     */   
/*     */   public synchronized E peek() {
/*  56 */     return this.q.peek();
/*     */   }
/*     */   
/*     */   public synchronized E poll() {
/*  60 */     return this.q.poll();
/*     */   }
/*     */   
/*     */   public synchronized E remove() {
/*  64 */     return this.q.remove();
/*     */   }
/*     */   
/*     */   public synchronized boolean addAll(Collection<? extends E> c) {
/*  68 */     return this.q.addAll(c);
/*     */   }
/*     */   
/*     */   public synchronized void clear() {
/*  72 */     this.q.clear();
/*     */   }
/*     */   
/*     */   public synchronized boolean contains(Object o) {
/*  76 */     return this.q.contains(o);
/*     */   }
/*     */   
/*     */   public synchronized boolean containsAll(Collection<?> c) {
/*  80 */     return this.q.containsAll(c);
/*     */   }
/*     */   
/*     */   public synchronized boolean isEmpty() {
/*  84 */     return this.q.isEmpty();
/*     */   }
/*     */   
/*     */   public synchronized Iterator<E> iterator() {
/*  88 */     return this.q.iterator();
/*     */   }
/*     */   
/*     */   public synchronized boolean remove(Object o) {
/*  92 */     return this.q.remove(o);
/*     */   }
/*     */   
/*     */   public synchronized boolean removeAll(Collection<?> c) {
/*  96 */     return this.q.removeAll(c);
/*     */   }
/*     */   
/*     */   public synchronized boolean retainAll(Collection<?> c) {
/* 100 */     return this.q.retainAll(c);
/*     */   }
/*     */   
/*     */   public synchronized int size() {
/* 104 */     return this.q.size();
/*     */   }
/*     */   
/*     */   public synchronized Object[] toArray() {
/* 108 */     return this.q.toArray();
/*     */   }
/*     */   
/*     */   public synchronized <T> T[] toArray(T[] a) {
/* 112 */     return (T[])this.q.toArray((Object[])a);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean equals(Object obj) {
/* 117 */     return this.q.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int hashCode() {
/* 122 */     return this.q.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 127 */     return this.q.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/SynchronizedQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */