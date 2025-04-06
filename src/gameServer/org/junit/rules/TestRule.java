package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public interface TestRule {
  Statement apply(Statement paramStatement, Description paramDescription);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/rules/TestRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */