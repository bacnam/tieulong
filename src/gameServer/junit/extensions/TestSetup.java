/*    */ package junit.extensions;
/*    */ 
/*    */ import junit.framework.Protectable;
/*    */ import junit.framework.Test;
/*    */ import junit.framework.TestResult;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestSetup
/*    */   extends TestDecorator
/*    */ {
/*    */   public TestSetup(Test test) {
/* 15 */     super(test);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(final TestResult result) {
/* 20 */     Protectable p = new Protectable() {
/*    */         public void protect() throws Exception {
/* 22 */           TestSetup.this.setUp();
/* 23 */           TestSetup.this.basicRun(result);
/* 24 */           TestSetup.this.tearDown();
/*    */         }
/*    */       };
/* 27 */     result.runProtected(this, p);
/*    */   }
/*    */   
/*    */   protected void setUp() throws Exception {}
/*    */   
/*    */   protected void tearDown() throws Exception {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/junit/extensions/TestSetup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */