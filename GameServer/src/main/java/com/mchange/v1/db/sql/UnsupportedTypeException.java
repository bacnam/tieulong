package com.mchange.v1.db.sql;

import java.sql.SQLException;

public class UnsupportedTypeException
        extends SQLException {
    public UnsupportedTypeException(String paramString) {
        super(paramString);
    }

    public UnsupportedTypeException() {
    }
}

