package com.mchange.v2.util;

import com.mchange.v1.util.WrapperIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PatternReplacementMap {
    List mappings = new LinkedList();

    public synchronized void addMapping(Pattern paramPattern, String paramString) {
        this.mappings.add(new Mapping(paramPattern, paramString));
    }

    public synchronized void removeMapping(Pattern paramPattern) {
        byte b;
        int i;
        for (b = 0, i = this.mappings.size(); b < i; b++) {
            if (((Mapping) this.mappings.get(b)).getPattern().equals(paramPattern))
                this.mappings.remove(b);
        }
    }

    public synchronized Iterator patterns() {
        return (Iterator) new WrapperIterator(this.mappings.iterator(), true) {
            protected Object transformObject(Object param1Object) {
                PatternReplacementMap.Mapping mapping = (PatternReplacementMap.Mapping) param1Object;
                return mapping.getPattern();
            }
        };
    }

    public synchronized int size() {
        return this.mappings.size();
    }

    public synchronized String attemptReplace(String paramString) {
        String str = null;
        for (Mapping mapping : this.mappings) {

            Matcher matcher = mapping.getPattern().matcher(paramString);
            if (matcher.matches()) {

                str = matcher.replaceAll(mapping.getReplacement());
                break;
            }
        }
        return str;
    }

    private static final class Mapping {
        Pattern pattern;
        String replacement;

        public Mapping(Pattern param1Pattern, String param1String) {
            this.pattern = param1Pattern;
            this.replacement = param1String;
        }

        public Pattern getPattern() {
            return this.pattern;
        }

        public String getReplacement() {
            return this.replacement;
        }
    }
}

