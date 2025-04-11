package org.apache.http.nio.reactor;

public enum IOReactorStatus {
    INACTIVE,

    ACTIVE,

    SHUTDOWN_REQUEST,

    SHUTTING_DOWN,

    SHUT_DOWN;
}

