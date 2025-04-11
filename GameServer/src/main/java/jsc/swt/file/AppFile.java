package jsc.swt.file;

import java.io.File;

public interface AppFile {
    boolean read(File paramFile);

    void setFile(File paramFile);

    boolean write(File paramFile);
}

