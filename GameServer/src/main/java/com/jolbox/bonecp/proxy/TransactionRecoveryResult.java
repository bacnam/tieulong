package com.jolbox.bonecp.proxy;

import java.util.HashMap;
import java.util.Map;

public class TransactionRecoveryResult {
    private Object result;
    private Map<Object, Object> replaceTarget = new HashMap<Object, Object>();

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Map<Object, Object> getReplaceTarget() {
        return this.replaceTarget;
    }
}

