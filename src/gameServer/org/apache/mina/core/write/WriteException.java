/*     */ package org.apache.mina.core.write;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import org.apache.mina.util.MapBackedSet;
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
/*     */ public class WriteException
/*     */   extends IOException
/*     */ {
/*     */   private static final long serialVersionUID = -4174407422754524197L;
/*     */   private final List<WriteRequest> requests;
/*     */   
/*     */   public WriteException(WriteRequest request) {
/*  48 */     this.requests = asRequestList(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteException(WriteRequest request, String s) {
/*  55 */     super(s);
/*  56 */     this.requests = asRequestList(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteException(WriteRequest request, String message, Throwable cause) {
/*  63 */     super(message);
/*  64 */     initCause(cause);
/*  65 */     this.requests = asRequestList(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteException(WriteRequest request, Throwable cause) {
/*  72 */     initCause(cause);
/*  73 */     this.requests = asRequestList(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteException(Collection<WriteRequest> requests) {
/*  81 */     this.requests = asRequestList(requests);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteException(Collection<WriteRequest> requests, String s) {
/*  88 */     super(s);
/*  89 */     this.requests = asRequestList(requests);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteException(Collection<WriteRequest> requests, String message, Throwable cause) {
/*  96 */     super(message);
/*  97 */     initCause(cause);
/*  98 */     this.requests = asRequestList(requests);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteException(Collection<WriteRequest> requests, Throwable cause) {
/* 105 */     initCause(cause);
/* 106 */     this.requests = asRequestList(requests);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WriteRequest> getRequests() {
/* 113 */     return this.requests;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteRequest getRequest() {
/* 120 */     return this.requests.get(0);
/*     */   }
/*     */   
/*     */   private static List<WriteRequest> asRequestList(Collection<WriteRequest> requests) {
/* 124 */     if (requests == null) {
/* 125 */       throw new IllegalArgumentException("requests");
/*     */     }
/* 127 */     if (requests.isEmpty()) {
/* 128 */       throw new IllegalArgumentException("requests is empty.");
/*     */     }
/*     */ 
/*     */     
/* 132 */     MapBackedSet<WriteRequest> mapBackedSet = new MapBackedSet(new LinkedHashMap<Object, Object>());
/* 133 */     for (WriteRequest r : requests) {
/* 134 */       mapBackedSet.add(r.getOriginalRequest());
/*     */     }
/*     */     
/* 137 */     return Collections.unmodifiableList(new ArrayList<WriteRequest>((Collection<? extends WriteRequest>)mapBackedSet));
/*     */   }
/*     */   
/*     */   private static List<WriteRequest> asRequestList(WriteRequest request) {
/* 141 */     if (request == null) {
/* 142 */       throw new IllegalArgumentException("request");
/*     */     }
/*     */     
/* 145 */     List<WriteRequest> requests = new ArrayList<WriteRequest>(1);
/* 146 */     requests.add(request.getOriginalRequest());
/* 147 */     return Collections.unmodifiableList(requests);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/WriteException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */