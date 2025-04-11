package org.junit.internal.runners.statements;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.List;

public class RunBefores
        extends Statement {
    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> befores;

    public RunBefores(Statement next, List<FrameworkMethod> befores, Object target) {
        this.next = next;
        this.befores = befores;
        this.target = target;
    }

    public void evaluate() throws Throwable {
        for (FrameworkMethod before : this.befores) {
            before.invokeExplosively(this.target, new Object[0]);
        }
        this.next.evaluate();
    }
}

