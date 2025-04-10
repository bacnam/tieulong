package com.notnoop.exceptions;

import java.io.IOException;

public class NetworkIOException
extends ApnsException
{
private static final long serialVersionUID = 3353516625486306533L;

public NetworkIOException() {}

public NetworkIOException(String message) {
super(message);
} public NetworkIOException(IOException cause) { super(cause); } public NetworkIOException(String m, IOException c) {
super(m, c);
}
}

