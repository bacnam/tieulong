package bsh.util;

import bsh.NameSource;
import bsh.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class NameCompletionTable
        extends ArrayList
        implements NameCompletion {
    NameCompletionTable table;
    List sources;

    public void add(NameCompletionTable table) {
        if (this.table != null) {
            throw new RuntimeException("Unimplemented usage error");
        }
        this.table = table;
    }

    public void add(NameSource source) {
        if (this.sources == null) {
            this.sources = new ArrayList();
        }
        this.sources.add(source);
    }

    protected void getMatchingNames(String part, List<String> found) {
        int i;
        for (i = 0; i < size(); i++) {
            String name = (String) get(i);
            if (name.startsWith(part)) {
                found.add(name);
            }
        }

        if (this.table != null) {
            this.table.getMatchingNames(part, found);
        }

        if (this.sources != null)
            for (i = 0; i < this.sources.size(); i++) {

                NameSource src = this.sources.get(i);
                String[] names = src.getAllNames();
                for (int j = 0; j < names.length; j++) {
                    if (names[j].startsWith(part)) {
                        found.add(names[j]);
                    }
                }
            }
    }

    public String[] completeName(String part) {
        List<String> found = new ArrayList();
        getMatchingNames(part, found);

        if (found.size() == 0) {
            return new String[0];
        }

        String maxCommon = found.get(0);
        for (int i = 1; i < found.size() && maxCommon.length() > 0; i++) {
            maxCommon = StringUtil.maxCommonPrefix(maxCommon, found.get(i));

            if (maxCommon.equals(part)) {
                break;
            }
        }

        if (maxCommon.length() > part.length()) {
            return new String[]{maxCommon};
        }
        return found.<String>toArray(new String[0]);
    }
}

