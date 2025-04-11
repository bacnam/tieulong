package com.mysql.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class NetworkResources {
    private final Socket mysqlConnection;
    private final InputStream mysqlInput;
    private final OutputStream mysqlOutput;

    protected NetworkResources(Socket mysqlConnection, InputStream mysqlInput, OutputStream mysqlOutput) {
        this.mysqlConnection = mysqlConnection;
        this.mysqlInput = mysqlInput;
        this.mysqlOutput = mysqlOutput;
    }

    protected final void forceClose() {
        try {
            try {
                if (this.mysqlInput != null) {
                    this.mysqlInput.close();
                }
            } finally {
                if (this.mysqlConnection != null && !this.mysqlConnection.isClosed() && !this.mysqlConnection.isInputShutdown()) {
                    try {
                        this.mysqlConnection.shutdownInput();
                    } catch (UnsupportedOperationException ex) {
                    }
                }
            }

        } catch (IOException ioEx) {
        }

        try {
            try {
                if (this.mysqlOutput != null) {
                    this.mysqlOutput.close();
                }
            } finally {
                if (this.mysqlConnection != null && !this.mysqlConnection.isClosed() && !this.mysqlConnection.isOutputShutdown()) {
                    try {
                        this.mysqlConnection.shutdownOutput();
                    } catch (UnsupportedOperationException ex) {
                    }
                }
            }

        } catch (IOException ioEx) {
        }

        try {
            if (this.mysqlConnection != null) {
                this.mysqlConnection.close();
            }
        } catch (IOException ioEx) {
        }
    }
}

