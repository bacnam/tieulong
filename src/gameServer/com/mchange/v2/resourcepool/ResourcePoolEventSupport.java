/*     */ package com.mchange.v2.resourcepool;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ public class ResourcePoolEventSupport
/*     */ {
/*     */   ResourcePool source;
/*  43 */   Set mlisteners = new HashSet();
/*     */   
/*     */   public ResourcePoolEventSupport(ResourcePool source) {
/*  46 */     this.source = source;
/*     */   }
/*     */   public synchronized void addResourcePoolListener(ResourcePoolListener mlistener) {
/*  49 */     this.mlisteners.add(mlistener);
/*     */   }
/*     */   public synchronized void removeResourcePoolListener(ResourcePoolListener mlistener) {
/*  52 */     this.mlisteners.remove(mlistener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void fireResourceAcquired(Object resc, int pool_size, int available_size, int removed_but_unreturned_size) {
/*  59 */     if (!this.mlisteners.isEmpty()) {
/*     */       
/*  61 */       ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, false, pool_size, available_size, removed_but_unreturned_size);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  67 */       for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {
/*     */         
/*  69 */         ResourcePoolListener rpl = i.next();
/*  70 */         rpl.resourceAcquired(evt);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void fireResourceCheckedIn(Object resc, int pool_size, int available_size, int removed_but_unreturned_size) {
/*  80 */     if (!this.mlisteners.isEmpty()) {
/*     */       
/*  82 */       ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, false, pool_size, available_size, removed_but_unreturned_size);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  88 */       for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {
/*     */         
/*  90 */         ResourcePoolListener rpl = i.next();
/*  91 */         rpl.resourceCheckedIn(evt);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void fireResourceCheckedOut(Object resc, int pool_size, int available_size, int removed_but_unreturned_size) {
/* 101 */     if (!this.mlisteners.isEmpty()) {
/*     */       
/* 103 */       ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, true, pool_size, available_size, removed_but_unreturned_size);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {
/*     */         
/* 111 */         ResourcePoolListener rpl = i.next();
/* 112 */         rpl.resourceCheckedOut(evt);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void fireResourceRemoved(Object resc, boolean checked_out_resource, int pool_size, int available_size, int removed_but_unreturned_size) {
/* 123 */     if (!this.mlisteners.isEmpty()) {
/*     */       
/* 125 */       ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, checked_out_resource, pool_size, available_size, removed_but_unreturned_size);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 131 */       for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {
/*     */         
/* 133 */         ResourcePoolListener rpl = i.next();
/* 134 */         rpl.resourceRemoved(evt);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/ResourcePoolEventSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */