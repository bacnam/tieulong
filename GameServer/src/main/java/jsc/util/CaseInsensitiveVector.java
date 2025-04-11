package jsc.util;

import java.util.Vector;

public class CaseInsensitiveVector
        extends Vector {
    public CaseInsensitiveVector() {
    }

    public CaseInsensitiveVector(int paramInt) {
        super(paramInt);
    }

    public CaseInsensitiveVector(int paramInt1, int paramInt2) {
        super(paramInt1, paramInt2);
    }

    public boolean containsString(String paramString) {
        return (indexOfString(paramString) >= 0);
    }

    public int indexOfString(String paramString) {
        for (byte b = 0; b < size(); b++) {

            String str = (String) elementAt(b);
            if (paramString.equalsIgnoreCase(str)) return b;
        }
        return -1;
    }
}

