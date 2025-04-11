package com.mchange.v2.c3p0.cfg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class NamedScope {
    HashMap props;
    HashMap userNamesToOverrides;
    HashMap extensions;

    NamedScope() {
        this.props = new HashMap<Object, Object>();
        this.userNamesToOverrides = new HashMap<Object, Object>();
        this.extensions = new HashMap<Object, Object>();
    }

    NamedScope(HashMap props, HashMap userNamesToOverrides, HashMap extensions) {
        this.props = props;
        this.userNamesToOverrides = userNamesToOverrides;
        this.extensions = extensions;
    }

    static HashMap mergeExtensions(HashMap over, HashMap under) {
        HashMap out = (HashMap) under.clone();
        out.putAll(over);
        return out;
    }

    static HashMap mergeUserNamesToOverrides(HashMap over, HashMap under) {
        HashMap<String, Object> out = (HashMap) under.clone();

        HashSet<?> underUserNames = new HashSet(under.keySet());
        HashSet overUserNames = new HashSet(over.keySet());

        HashSet newUserNames = (HashSet) overUserNames.clone();
        newUserNames.removeAll(underUserNames);

        for (Iterator<String> ii = newUserNames.iterator(); ii.hasNext(); ) {

            String name = ii.next();
            out.put(name, ((HashMap) over.get(name)).clone());
        }

        HashSet mergeUserNames = (HashSet) overUserNames.clone();
        mergeUserNames.retainAll(underUserNames);

        for (Iterator<String> iterator1 = mergeUserNames.iterator(); iterator1.hasNext(); ) {

            String name = iterator1.next();
            ((HashMap) out.get(name)).putAll((HashMap) over.get(name));
        }

        return out;
    }

    NamedScope mergedOver(NamedScope underScope) {
        HashMap mergedProps = (HashMap) underScope.props.clone();
        mergedProps.putAll(this.props);

        HashMap mergedUserNamesToOverrides = mergeUserNamesToOverrides(this.userNamesToOverrides, underScope.userNamesToOverrides);

        HashMap mergedExtensions = mergeExtensions(this.extensions, underScope.extensions);

        return new NamedScope(mergedProps, mergedUserNamesToOverrides, mergedExtensions);
    }
}

