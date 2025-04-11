package org.apache.http.client.entity;

import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;

public class DeflateDecompressingEntity
        extends DecompressingEntity {
    public DeflateDecompressingEntity(HttpEntity entity) {
        super(entity, new InputStreamFactory() {
            public InputStream create(InputStream instream) throws IOException {
                return new DeflateInputStream(instream);
            }
        });
    }
}

