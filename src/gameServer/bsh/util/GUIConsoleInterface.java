package bsh.util;

import bsh.ConsoleInterface;
import java.awt.Color;

public interface GUIConsoleInterface extends ConsoleInterface {
  void print(Object paramObject, Color paramColor);
  
  void setNameCompletion(NameCompletion paramNameCompletion);
  
  void setWaitFeedback(boolean paramBoolean);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/GUIConsoleInterface.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */