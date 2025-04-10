package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.joran.action.Action;
import java.util.List;

public interface RuleStore {
  void addRule(ElementSelector paramElementSelector, String paramString) throws ClassNotFoundException;

  void addRule(ElementSelector paramElementSelector, Action paramAction);

  List<Action> matchActions(ElementPath paramElementPath);
}

