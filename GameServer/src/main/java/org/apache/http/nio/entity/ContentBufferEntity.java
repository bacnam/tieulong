package org.apache.http.nio.entity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.nio.util.ContentInputBuffer;
import org.apache.http.util.Args;

@NotThreadSafe
public class ContentBufferEntity
extends BasicHttpEntity
{
private final HttpEntity wrappedEntity;

public ContentBufferEntity(HttpEntity entity, ContentInputBuffer buffer) {
Args.notNull(entity, "HTTP entity");
this.wrappedEntity = entity;
setContent(new ContentInputStream(buffer));
}

public boolean isChunked() {
return this.wrappedEntity.isChunked();
}

public long getContentLength() {
return this.wrappedEntity.getContentLength();
}

public Header getContentType() {
return this.wrappedEntity.getContentType();
}

public Header getContentEncoding() {
return this.wrappedEntity.getContentEncoding();
}
}

