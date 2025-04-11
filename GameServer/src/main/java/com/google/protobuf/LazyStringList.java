package com.google.protobuf;

import java.util.List;

public interface LazyStringList extends List<String> {

    ByteString getByteString(int index);

    void add(ByteString element);

    List<?> getUnderlyingElements();
}
