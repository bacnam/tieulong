package com.mchange.v2.io;

import com.mchange.v1.util.UIterator;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public interface FileIterator
extends UIterator
{
public static final FileIterator EMPTY_FILE_ITERATOR = new FileIterator() {
public File nextFile() {
throw new NoSuchElementException();
} public boolean hasNext() { return false; }
public Object next() { throw new NoSuchElementException(); } public void remove() {
throw new IllegalStateException();
}

public void close() {}
};

File nextFile() throws IOException;

boolean hasNext() throws IOException;

Object next() throws IOException;

void remove() throws IOException;

void close() throws IOException;
}

