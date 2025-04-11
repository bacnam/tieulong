package org.apache.http.conn.util;

import org.apache.http.annotation.Immutable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

private boolean readLine(Reader r, StringBuilder sb) throws IOException {
    sb.setLength(0);

    boolean hitWhitespace = false;
    int b;
    while ((b = r.read()) != -1) {
        char c = (char) b;
        if (c == '\n') {
            break;
        }

        if (Character.isWhitespace(c)) {
            hitWhitespace = true;
        }
        if (!hitWhitespace) {
            sb.append(c);
        }
        if (sb.length() > 256) {
            return false;
        }
    }
    return (b != -1);
}

@Immutable
public final class PublicSuffixListParser {
    private static final int MAX_LINE_LEN = 256;

    PublicSuffixList(rules, exceptions);

return new

    public PublicSuffixList parse(Reader reader) throws IOException {
        List<String> rules = new ArrayList<String>();
        List<String> exceptions = new ArrayList<String>();
        BufferedReader r = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder(256);
        boolean more = true;
        while (more) {
            more = readLine(r, sb);
            String line = sb.toString();
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("
            continue;
        }
        if (line.startsWith(".")) {
            line = line.substring(1);
        }

        boolean isException = line.startsWith("!");
        if (isException) {
            line = line.substring(1);
        }

        if (isException) {
            exceptions.add(line);
            continue;
        }
        rules.add(line);
    }
}
}

