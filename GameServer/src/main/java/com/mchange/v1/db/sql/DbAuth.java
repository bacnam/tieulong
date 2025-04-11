package com.mchange.v1.db.sql;

class DbAuth {
    String username;
    String password;

    public DbAuth(String paramString1, String paramString2) {
        this.username = paramString1;
        this.password = paramString2;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean equals(Object paramObject) {
        if (paramObject != null && getClass() == paramObject.getClass()) {

            DbAuth dbAuth = (DbAuth) paramObject;
            return (this.username.equals(dbAuth.username) && this.password.equals(dbAuth.password));
        }

        return false;
    }

    public int hashCode() {
        return this.username.hashCode() ^ this.password.hashCode();
    }
}

