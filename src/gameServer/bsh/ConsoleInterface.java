package bsh;

import java.io.PrintStream;
import java.io.Reader;

public interface ConsoleInterface {
  Reader getIn();
  
  PrintStream getOut();
  
  PrintStream getErr();
  
  void println(Object paramObject);
  
  void print(Object paramObject);
  
  void error(Object paramObject);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ConsoleInterface.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */