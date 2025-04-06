/*    */ package org.junit.internal.requests;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.junit.runner.Description;
/*    */ import org.junit.runner.Request;
/*    */ import org.junit.runner.Runner;
/*    */ import org.junit.runner.manipulation.Sorter;
/*    */ 
/*    */ public class SortingRequest
/*    */   extends Request {
/*    */   private final Request request;
/*    */   private final Comparator<Description> comparator;
/*    */   
/*    */   public SortingRequest(Request request, Comparator<Description> comparator) {
/* 15 */     this.request = request;
/* 16 */     this.comparator = comparator;
/*    */   }
/*    */ 
/*    */   
/*    */   public Runner getRunner() {
/* 21 */     Runner runner = this.request.getRunner();
/* 22 */     (new Sorter(this.comparator)).apply(runner);
/* 23 */     return runner;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/requests/SortingRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */