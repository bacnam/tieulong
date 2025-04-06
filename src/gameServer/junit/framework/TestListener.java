package junit.framework;

public interface TestListener {
  void addError(Test paramTest, Throwable paramThrowable);
  
  void addFailure(Test paramTest, AssertionFailedError paramAssertionFailedError);
  
  void endTest(Test paramTest);
  
  void startTest(Test paramTest);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/junit/framework/TestListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */