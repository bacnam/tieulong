package com.jolbox.bonecp;

import com.google.common.base.Objects;

public class UsernamePassword {
    private String username;
    private String password;

    public UsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean equals(Object obj) {
        if (obj instanceof UsernamePassword) {
            UsernamePassword that = (UsernamePassword) obj;
            return (Objects.equal(this.username, that.getUsername()) && Objects.equal(this.password, that.getPassword()));
        }

        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.username, this.password});
    }
}

