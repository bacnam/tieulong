package com.mchange.v3.filecache;

import java.io.FileNotFoundException;

public class FileNotCachedException
extends FileNotFoundException
{
FileNotCachedException(String paramString) {
super(paramString);
}

FileNotCachedException() {}
}

