package com.zhonglian.server.common.data.ref;

import com.zhonglian.server.common.data.RefContainer;

public abstract class RefBase {
    public abstract boolean Assert();

    public abstract boolean AssertAll(RefContainer<?> var1);

    @Override
    public String toString() {
        return super.toString();
    }

    public String[] getOptionFields() {
        return new String[0];
    }

    public boolean isFieldRequired(String filed) {
        String[] var5;
        for (String f : var5 = this.getOptionFields()) {
            if (f.equals(filed)) {
                return false;
            }
        }

        return true;
    }
}
