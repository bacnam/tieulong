package org.apache.http.client.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

@NotThreadSafe
public class EntityBuilder
{
private String text;
private byte[] binary;
private InputStream stream;
private List<NameValuePair> parameters;
private Serializable serializable;
private File file;
private ContentType contentType;
private String contentEncoding;
private boolean chunked;
private boolean gzipCompress;

public static EntityBuilder create() {
return new EntityBuilder();
}

private void clearContent() {
this.text = null;
this.binary = null;
this.stream = null;
this.parameters = null;
this.serializable = null;
this.file = null;
}

public String getText() {
return this.text;
}

public EntityBuilder setText(String text) {
clearContent();
this.text = text;
return this;
}

public byte[] getBinary() {
return this.binary;
}

public EntityBuilder setBinary(byte[] binary) {
clearContent();
this.binary = binary;
return this;
}

public InputStream getStream() {
return this.stream;
}

public EntityBuilder setStream(InputStream stream) {
clearContent();
this.stream = stream;
return this;
}

public List<NameValuePair> getParameters() {
return this.parameters;
}

public EntityBuilder setParameters(List<NameValuePair> parameters) {
clearContent();
this.parameters = parameters;
return this;
}

public EntityBuilder setParameters(NameValuePair... parameters) {
return setParameters(Arrays.asList(parameters));
}

public Serializable getSerializable() {
return this.serializable;
}

public EntityBuilder setSerializable(Serializable serializable) {
clearContent();
this.serializable = serializable;
return this;
}

public File getFile() {
return this.file;
}

public EntityBuilder setFile(File file) {
clearContent();
this.file = file;
return this;
}

public ContentType getContentType() {
return this.contentType;
}

public EntityBuilder setContentType(ContentType contentType) {
this.contentType = contentType;
return this;
}

public String getContentEncoding() {
return this.contentEncoding;
}

public EntityBuilder setContentEncoding(String contentEncoding) {
this.contentEncoding = contentEncoding;
return this;
}

public boolean isChunked() {
return this.chunked;
}

public EntityBuilder chunked() {
this.chunked = true;
return this;
}

public boolean isGzipCompress() {
return this.gzipCompress;
}

public EntityBuilder gzipCompress() {
this.gzipCompress = true;
return this;
}

private ContentType getContentOrDefault(ContentType def) {
return (this.contentType != null) ? this.contentType : def;
}

public HttpEntity build() {
BasicHttpEntity basicHttpEntity;
if (this.text != null) {
StringEntity stringEntity = new StringEntity(this.text, getContentOrDefault(ContentType.DEFAULT_TEXT));
} else if (this.binary != null) {
ByteArrayEntity byteArrayEntity = new ByteArrayEntity(this.binary, getContentOrDefault(ContentType.DEFAULT_BINARY));
} else if (this.stream != null) {
InputStreamEntity inputStreamEntity = new InputStreamEntity(this.stream, -1L, getContentOrDefault(ContentType.DEFAULT_BINARY));
} else if (this.parameters != null) {
UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(this.parameters, (this.contentType != null) ? this.contentType.getCharset() : null);
}
else if (this.serializable != null) {
SerializableEntity serializableEntity = new SerializableEntity(this.serializable);
serializableEntity.setContentType(ContentType.DEFAULT_BINARY.toString());
} else if (this.file != null) {
FileEntity fileEntity = new FileEntity(this.file, getContentOrDefault(ContentType.DEFAULT_BINARY));
} else {
basicHttpEntity = new BasicHttpEntity();
} 
if (basicHttpEntity.getContentType() != null && this.contentType != null) {
basicHttpEntity.setContentType(this.contentType.toString());
}
basicHttpEntity.setContentEncoding(this.contentEncoding);
basicHttpEntity.setChunked(this.chunked);
if (this.gzipCompress) {
return (HttpEntity)new GzipCompressingEntity((HttpEntity)basicHttpEntity);
}
return (HttpEntity)basicHttpEntity;
}
}

