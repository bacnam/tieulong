/*    */ package junit.framework;
/*    */ 
/*    */ import org.junit.runner.Describable;
/*    */ import org.junit.runner.Description;
/*    */ 
/*    */ public class JUnit4TestCaseFacade implements Test, Describable {
/*    */   private final Description fDescription;
/*    */   
/*    */   JUnit4TestCaseFacade(Description description) {
/* 10 */     this.fDescription = description;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 15 */     return getDescription().toString();
/*    */   }
/*    */   
/*    */   public int countTestCases() {
/* 19 */     return 1;
/*    */   }
/*    */   
/*    */   public void run(TestResult result) {
/* 23 */     throw new RuntimeException("This test stub created only for informational purposes.");
/*    */   }
/*    */ 
/*    */   
/*    */   public Description getDescription() {
/* 28 */     return this.fDescription;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/junit/framework/JUnit4TestCaseFacade.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */