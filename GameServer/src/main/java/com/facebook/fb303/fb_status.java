package com.facebook.fb303;

import org.apache.thrift.TEnum;

public enum fb_status
        implements TEnum {
    DEAD(0),
    STARTING(1),
    ALIVE(2),
    STOPPING(3),
    STOPPED(4),
    WARNING(5);

    private final int value;

    fb_status(int value) {
        this.value = value;
    }

    public static fb_status findByValue(int value) {
        switch (value) {
            case 0:
                return DEAD;
            case 1:
                return STARTING;
            case 2:
                return ALIVE;
            case 3:
                return STOPPING;
            case 4:
                return STOPPED;
            case 5:
                return WARNING;
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}

