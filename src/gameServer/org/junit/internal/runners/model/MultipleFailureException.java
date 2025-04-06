/*    */ package org.junit.internal.runners.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.junit.runners.model.MultipleFailureException;
/*    */ 
/*    */ @Deprecated
/*    */ public class MultipleFailureException
/*    */   extends MultipleFailureException {
/*    */   public MultipleFailureException(List<Throwable> errors) {
/* 10 */     super(errors);
/*    */   }
/*    */   
/*    */   private static final long serialVersionUID = 1L;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/runners/model/MultipleFailureException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */