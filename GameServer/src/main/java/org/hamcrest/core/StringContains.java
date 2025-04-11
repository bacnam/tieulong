package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class StringContains
        extends SubstringMatcher {
    public StringContains(String substring) {
        super(substring);
    }

    @Factory
    public static Matcher<String> containsString(String substring) {
        return (Matcher<String>) new StringContains(substring);
    }

    protected boolean evalSubstringOf(String s) {
        return (s.indexOf(this.substring) >= 0);
    }

    protected String relationship() {
        return "containing";
    }
}

