package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class SocketOutputBuffer
extends AbstractSessionOutputBuffer
{
public SocketOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
Args.notNull(socket, "Socket");
int n = buffersize;
if (n < 0) {
n = socket.getSendBufferSize();
}
if (n < 1024) {
n = 1024;
}
init(socket.getOutputStream(), n, params);
}
}

