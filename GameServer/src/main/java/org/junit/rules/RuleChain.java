package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleChain
        implements TestRule {
    private static final RuleChain EMPTY_CHAIN = new RuleChain(Collections.emptyList());

    private List<TestRule> rulesStartingWithInnerMost;

    private RuleChain(List<TestRule> rules) {
        this.rulesStartingWithInnerMost = rules;
    }

    public static RuleChain emptyRuleChain() {
        return EMPTY_CHAIN;
    }

    public static RuleChain outerRule(TestRule outerRule) {
        return emptyRuleChain().around(outerRule);
    }

    public RuleChain around(TestRule enclosedRule) {
        List<TestRule> rulesOfNewChain = new ArrayList<TestRule>();
        rulesOfNewChain.add(enclosedRule);
        rulesOfNewChain.addAll(this.rulesStartingWithInnerMost);
        return new RuleChain(rulesOfNewChain);
    }

    public Statement apply(Statement base, Description description) {
        for (TestRule each : this.rulesStartingWithInnerMost) {
            base = each.apply(base, description);
        }
        return base;
    }
}

