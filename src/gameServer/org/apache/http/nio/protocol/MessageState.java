package org.apache.http.nio.protocol;

enum MessageState
{
READY, INIT, ACK_EXPECTED, BODY_STREAM, COMPLETED;
}

