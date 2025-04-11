package org.apache.http.client.protocol;

import org.apache.http.*;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.entity.DeflateInputStream;
import org.apache.http.client.entity.InputStreamFactory;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

@Immutable
public class ResponseContentEncoding
        implements HttpResponseInterceptor {
    public static final String UNCOMPRESSED = "http.client.response.uncompressed";

    private static final InputStreamFactory GZIP = new InputStreamFactory() {
        public InputStream create(InputStream instream) throws IOException {
            return new GZIPInputStream(instream);
        }
    };

    private static final InputStreamFactory DEFLATE = new InputStreamFactory() {
        public InputStream create(InputStream instream) throws IOException {
            return (InputStream) new DeflateInputStream(instream);
        }
    };

    private final Lookup<InputStreamFactory> decoderRegistry;

    public ResponseContentEncoding(Lookup<InputStreamFactory> decoderRegistry) {
        this.decoderRegistry = (decoderRegistry != null) ? decoderRegistry : (Lookup<InputStreamFactory>) RegistryBuilder.create().register("gzip", GZIP).register("x-gzip", GZIP).register("deflate", DEFLATE).build();
    }

    public ResponseContentEncoding() {
        this(null);
    }

    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        HttpEntity entity = response.getEntity();

        HttpClientContext clientContext = HttpClientContext.adapt(context);
        RequestConfig requestConfig = clientContext.getRequestConfig();

        if (requestConfig.isDecompressionEnabled() && entity != null && entity.getContentLength() != 0L) {
            Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
                HeaderElement[] codecs = ceheader.getElements();
                for (HeaderElement codec : codecs) {
                    String codecname = codec.getName().toLowerCase(Locale.ROOT);
                    InputStreamFactory decoderFactory = (InputStreamFactory) this.decoderRegistry.lookup(codecname);
                    if (decoderFactory != null) {
                        response.setEntity((HttpEntity) new DecompressingEntity(response.getEntity(), decoderFactory));
                        response.removeHeaders("Content-Length");
                        response.removeHeaders("Content-Encoding");
                        response.removeHeaders("Content-MD5");
                    } else if (!"identity".equals(codecname)) {
                        throw new HttpException("Unsupported Content-Coding: " + codec.getName());
                    }
                }
            }
        }
    }
}

