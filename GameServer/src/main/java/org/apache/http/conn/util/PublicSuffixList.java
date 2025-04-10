package org.apache.http.conn.util;

import java.util.Collections;
import java.util.List;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class PublicSuffixList
{
private final List<String> rules;
private final List<String> exceptions;

public PublicSuffixList(List<String> rules, List<String> exceptions) {
this.rules = Collections.unmodifiableList((List<? extends String>)Args.notNull(rules, "Domain suffix rules"));
this.exceptions = Collections.unmodifiableList((List<? extends String>)Args.notNull(exceptions, "Domain suffix exceptions"));
}

public List<String> getRules() {
return this.rules;
}

public List<String> getExceptions() {
return this.exceptions;
}
}

