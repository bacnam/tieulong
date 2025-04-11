package com.zhonglian.server.logger.scribe;

import org.apache.thrift.TEnum;

public enum ResultCode
        implements TEnum {
    OK(0), TRY_LATER(1);

    private final int value;

    ResultCode(int value) {
        this.value = value;
    }

    public static ResultCode findByValue(int value) {
        switch (value) {
            case 0:
                return OK;
            case 1:
                return TRY_LATER;
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}

