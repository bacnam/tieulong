package org.apache.http.client.entity;

import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipDecompressingEntity
        extends DecompressingEntity {
    public GzipDecompressingEntity(HttpEntity entity) {
        super(entity, new InputStreamFactory() {
            public InputStream create(InputStream instream) throws IOException {
                return new GZIPInputStream(instream);
            }
        });
    }
}

