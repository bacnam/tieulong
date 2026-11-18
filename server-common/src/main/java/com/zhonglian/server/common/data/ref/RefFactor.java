package com.zhonglian.server.common.data.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefFactor extends RefBase {
    @RefField(
        iskey = true
    )
    public String id;
    public double Value;
    public String Remarks;

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
