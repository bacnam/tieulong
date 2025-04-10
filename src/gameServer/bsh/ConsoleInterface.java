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

